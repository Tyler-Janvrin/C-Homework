import absyn.*;
import java.util.ArrayList;

public class CodeGenerator implements AbsynCodeVisitor {

  ArrayList<NodeSkip> skipList = new ArrayList<NodeSkip>(0);

  int mainEntry = 0, inputEntry = 0, outputEntry = 0;
  int globalOffset = 0;
  int frameOffset = 0;
  final int ofpFO = 0; // original function pointer frame offset
  final int retFO = -1; // return address frame offset
  final int initFO = -2; // initial frame offset

  // special register names
  final int ac = 0;
  final int ac1 = 1;
  final int fp = 5;
  final int gp = 6;
  final int pc = 7;
  // they also mention fp, gp, pc, ofpFO, retFO, and initFO

  // keep track of locations in the file
  int emitLoc = 0;
  int highEmitLoc = 0;

  void emitRO(String op, int r, int s, int t, String c){
    System.out.printf("%3d: %5s %d, %d, %d", emitLoc, op, r, s, t);
    System.out.printf("\t%s\n", c);
    ++emitLoc;
    if(highEmitLoc < emitLoc){
      highEmitLoc = emitLoc;
    }
  }

  void emitRM(String op, int r, int d, int s, String c){
    System.out.printf("%3d: %5s %d, %d(%d)", emitLoc, op, r, d, s);
    System.out.printf("\t%s\n", c);
    ++emitLoc;
    if(highEmitLoc < emitLoc){
      highEmitLoc = emitLoc;
    }
  }

  void emitRM_Abs(String op, int r, int a, String c){
    System.out.printf("%3d: %5s %d, %d(%d)", emitLoc, op, r, a - (emitLoc + 1), pc);
    System.out.printf("\t%s\n", c);
    ++emitLoc;
    if(highEmitLoc < emitLoc){
      highEmitLoc = emitLoc;
    }
  }

  int emitSkip(int distance){
    int i = emitLoc;
    emitLoc += distance;
    if(highEmitLoc < emitLoc){
      highEmitLoc = emitLoc;
    }
    return i;
  }

  void emitBackup(int loc){
    if( loc > highEmitLoc){
      emitComment("BUG in emitBackup");
    }
    emitLoc = loc;
  }

  void emitRestore(){
    emitLoc = highEmitLoc;
  }

  void emitComment(String c){
    System.out.println("* " + c);
  }

  public void visit(Absyn trees){
    // generate the prelude
    emitRM("LD", gp, 0, ac ,"load gp with maxaddress");
    emitRM("LDA", fp, 0, gp, "copy gp to fp");
    emitRM("ST", ac, 0, ac, "clear location 0");
  

    // generate the io routines
    int ioSkip = emitSkip(1);
    
    emitComment("begin the input and output routines");
    // input
    inputEntry = emitSkip(0);
    emitRM("ST", ac, retFO, fp, "store return");
    emitRO("IN", 0, 0, 0, "input");
    emitRM("LD", pc, retFO, fp, "return to caller");
    emitComment("that's the input routine");
    // output
    outputEntry = emitSkip(0);
    emitRM("ST", ac, retFO, fp, "store return");
    emitRM("LD", ac, initFO, fp, "load output value");
    emitRO("OUT", 0, 0, 0, "output");
    emitRM("LD", pc, retFO, fp, "return to caller");
    emitComment("that's the output routine");
    int ioEnd = emitSkip(0);
    emitBackup(ioSkip);
    emitRM_Abs("LDA", pc, ioEnd, "skip to the end of io");
    emitRestore(); // skipped around io


    // make a request to the visit method for DecList
    // emitComment("Hello again from visit!");
    trees.accept(this, 0, false);

    // generate the finale
    emitRM("ST", fp, globalOffset + ofpFO, fp, "push ofp");
    emitRM("LDA", fp, globalOffset, fp, "push frame");
    emitRM("LDA", ac, 1, pc, "load ac with ret addr");
    emitRM_Abs("LDA", 7, mainEntry, "jump to main loc"); // need to add pointer to main
    emitRM("LD", fp, ofpFO, fp, "pop frame");
    emitRO("HALT",0,0,0, "halt");

     
    for(int i = 0; i < skipList.size(); i++){
      NodeSkip node = skipList.get(i);
      if(node.solved == false){
        System.err.println("Error! Function prototype " + node.name + "called at: row " + (node.exp.row + 1) + ", col " + (node.exp.col + 1) + " but was never defined.");
      }
    }
  }

  final static int SPACES = 4;

  /* 
  private void indent( int offset ) {
    for( int i = 0; i < offset * SPACES; i++ ) System.out.print( " " );
  }
    */

  public void visit( ExpList expList, int offset, boolean isAddress ) {
    while( expList != null ) {
      expList.head.accept( this, offset, false ); // we're always accepting with false. this should work
      if(isAddress){
        // we're overloading the boolean flag. in this case,
        // it's used to tell if we're in 
        // the body of a function, or a function's arguments
        if(!(expList.head instanceof NilExp)){
          offset--;
        }
      }
      expList = expList.tail;
    } 
  }

  public void visit (VarDecList varList, int offset, boolean isAddress){
    while(varList != null){
      varList.head.accept(this, offset, isAddress );
      varList = varList.tail;
    }
  }

  public void visit (DecList decList, int offset, boolean isAddress){
      while (decList != null) {
        decList.head.accept(this, offset, isAddress );
        decList = decList.tail;
      }
  }


  public void visit( AssignExp exp, int offset, boolean isAddress ) {
    exp.lhs.accept( this, offset - 1, true );
    exp.rhs.accept( this, offset - 2, false );
    emitRM("LD", ac, offset - 1, fp, "load assignment location into memory");
    emitRM("LD", ac1, offset - 2, fp, "load assignment value into memory");
    emitRM("ST", ac1, 0, ac, "store value to location of variable loaded in memory");
    emitRM("ST", ac1, offset, fp, "store value to result of equals operation");
    emitComment("done assigning value to variable");
  }

  public void visit( IfExp exp, int offset, boolean isAddress ) {
    exp.test.accept( this, offset, isAddress );

    emitRM("LD",ac, offset, fp, "load stored value of ifExp test");
    // conditional jump
    int conditionalJump = emitSkip(1);
    // code for true case
    exp.thenpart.accept( this, offset, isAddress );
    // unconditional jump
    int unconditionalJump = emitSkip(1);
    int labelOne = emitSkip(0);
    // code for false case
    if (exp.elsepart != null )
       exp.elsepart.accept( this, offset, isAddress );
    // code after if statement
    int labelTwo = emitSkip(0);

    emitBackup(conditionalJump);
    emitRM_Abs("JEQ", ac, labelOne, "jump to the false case");
    emitRestore();
    emitBackup(unconditionalJump);
    emitRM_Abs("LDA", pc, labelTwo, "jump around the false case");
    emitRestore();
  }

  public void visit( CompoundExp exp, int offset, boolean isAddress ){
    exp.decs.accept(this, frameOffset, isAddress);
    emitComment("Size of frameOffset: " + frameOffset);
    // by the time we reach this point, we'll have used up frameOffset
    exp.exps.accept(this, frameOffset, isAddress);
  }

  

  public void visit( IntExp exp, int offset, boolean isAddress ) {
    emitRM("LDC", ac, Integer.valueOf(exp.value), ac, "load the integer value into memory");
    emitRM("ST", ac, offset, fp, "store the integer value into memory");
    emitComment("done storing address of integer");
  }

  public void opExpHelper(String opCode){  
    emitRO("SUB", ac, ac, ac1, "subtract ac1 from ac, getting difference"); 
    // conditional jump
    int conditionalJump = emitSkip(1);
    // code for true case
    emitRM("LDC", ac, 1, 0, "case for true, load 1");        
    // unconditional jump
    int unconditionalJump = emitSkip(1);
    int labelOne = emitSkip(0);
    // code for false case
    emitRM("LDC", ac, 0, 0, "case for false, load 0");        
    // code after if statement
    int labelTwo = emitSkip(0);

    emitBackup(conditionalJump);
    emitRM_Abs(opCode, ac, labelOne, "jump to the false case");
    emitRestore();
    emitBackup(unconditionalJump);
    emitRM_Abs("LDA", pc, labelTwo, "jump around the false case");
    emitRestore();
  }

  public void visit( OpExp exp, int offset, boolean isAddress ) {
    if (exp.left != null)
       exp.left.accept( this, offset - 1, isAddress );
    if (exp.right != null)
       exp.right.accept( this, offset - 2, isAddress );


    emitRM("LD",ac, offset - 1, fp, "load stored value of opExp lhs");
    emitRM("LD",ac1, offset - 2, fp, "load stored value of opExp rhs");
    switch( exp.op ) {
      case OpExp.PLUS:
        emitRO("ADD", ac, ac, ac1, "add opExp arguments");
        break;
      case OpExp.MINUS:
        emitRO("SUB", ac, ac, ac1, "subtract opExp arguments");
        break;
      case OpExp.TIMES:
        emitRO("MUL", ac, ac, ac1, "multiply opExp arguments");
        break;
      case OpExp.DIV:
        emitRO("DIV", ac, ac, ac1, "divide opExp arguments");
        break;
      case OpExp.EQ: // we'll do all of these when we get to ifs and whiles. first, I want to implement functions
        opExpHelper("JNE");
        break;
      case OpExp.NE:
        opExpHelper("JEQ");
        break;
      case OpExp.LT:
        opExpHelper("JGE");
        break;
      case OpExp.LE:
        opExpHelper("JGT");
        break;
      case OpExp.GT:
        opExpHelper("JLE");
        break;
      case OpExp.GE:
        opExpHelper("JLT");
        break;
      case OpExp.UMINUS:
        emitRM("LDC", ac, 0, 0, "load stored value of opExp rhs");
        emitRO("SUB", ac, ac, ac1, "subtract opExp arguments");
        break;
      case OpExp.BITNOT:
      emitRM("LD", ac, offset - 2, fp, "load stored value of bitnot argument");
      // conditional jump
      int conditionalJump = emitSkip(1);
      // code for true case // we flip it to false case
      emitRM("LDC", ac, 0, 0, "case for true, invert to zero");  
      // unconditional jump
      int unconditionalJump = emitSkip(1);
      int labelOne = emitSkip(0);
      // code for false case
      emitRM("LDC", ac, 1, 0, "case for false, invert to one"); 
      // code after if statement
      int labelTwo = emitSkip(0);

      emitBackup(conditionalJump);
      emitRM_Abs("JEQ", ac, labelOne, "jump to the false case");
      emitRestore();
      emitBackup(unconditionalJump);
      emitRM_Abs("LDA", pc, labelTwo, "jump around the false case");
      emitRestore();
        break;
      case OpExp.AND:
        emitRO("MUL", ac, ac, ac1, "and (multiply) opExp arguments");
        break;
      case OpExp.OR:
        emitRO("ADD", ac, ac, ac1, "or (add) opExp arguments");
        break;
      default:
    }
    emitRM("ST", ac, offset, fp, "store result of opExp in memory");
    emitComment("done an opExp");
  }

  public void visit(NameTy type, int offset, boolean isAddress){
    
    /* 
    switch( type.type ) {
      case NameTy.BOOL:
        break;
      case NameTy.INT:
        break;
      case NameTy.VOID:
        break;
      default:
    }
      */
  }

  /*

  public void visit( ReadExp exp, int offset ) {
    indent( offset );
    System.out.println( "ReadExp:" );
    exp.input.accept( this, ++offset );
  }

  */

  /* 
  public void visit( RepeatExp exp, int offset ) {
    indent( offset );
    System.out.println( "RepeatExp:" );
    offset++;
    exp.exps.accept( this, offset );
    exp.test.accept( this, offset ); 
  }
    */

  public void visit( VarExp exp, int offset, boolean isAddress ) {
    exp.variable.accept( this, offset, isAddress );

    int ptr;

    if(exp.dtype.nestLevel == 0){
      ptr = gp;
    }
    else{
      ptr = fp;
    }

    if(isAddress){
      emitRM("LDA", ac, exp.dtype.offset, ptr, "load the address for a variable");
      emitRM("ST", ac, offset, fp, "store the address for a variable");
      emitComment("done loading and storing addresses for lhs of assignment");
    }
    else{
      emitRM("LD", ac, exp.dtype.offset, ptr, "load the value of a simple variable");
      emitRM("ST", ac, offset, fp, "store the value of a simple variable");
      emitComment("done loading the value of a simple variable");
      // what was I doing...
      // working here - I'm too tired to keep going
      // address is false - this isn't an address
    }
  }

  public void visit (BoolExp exp, int offset, boolean isAddress){
    int boolValue;
    if(exp.value == true){
      boolValue = 1;
    }
    else{
      boolValue = 0;
    }
    emitRM("LDC", ac, boolValue, ac, "load the boolean value into memory");
    emitRM("ST", ac, offset, fp, "store the boolean value into memory");
    emitComment("done storing address of boolean");
  }

  public void visit (SimpleVar variable, int offset, boolean isAddress){
    // do nothing
  }

  public void visit(IndexVar variable, int offset, boolean isAddress){
    variable.index.accept(this, offset, isAddress );
  }

  public void visit( ReturnExp exp, int offset, boolean isAddress ) {
    exp.exp.accept( this, offset, isAddress );
    emitRM("LD", ac, offset, fp, "load result of return exp into memory");
    emitRM("LD", pc, retFO, fp, "load return address, leaving function by return (wish I knew the name)");


  }

  public void visit( WhileExp exp, int offset, boolean isAddress ) {
    
    // code for while test
    int labelOne = emitSkip(0);
    exp.test.accept( this, offset, isAddress);
    emitRM("LD",ac, offset, fp, "load stored value of ifExp test");
    // conditional jump on false to end
    int conditionalJump = emitSkip(1);
    // code for while body
    exp.body.accept( this, offset, isAddress);
    // unconditional jump to while test
    int unconditionalJump = emitSkip(1);
    int labelTwo = emitSkip(0);

    emitBackup(conditionalJump);
    emitRM_Abs("JEQ", ac, labelTwo, "if false, jump outside the loop");
    emitRestore();
    emitBackup(unconditionalJump);
    emitRM_Abs("LDA", pc, labelOne, "otherwise, jump back to the start of the loop");
    emitRestore();
    
  }

  public void visit ( CallExp exp, int offset, boolean isAddress){
    exp.args.accept(this, offset + initFO, true); // we're overloading isAddress here
    emitRM("ST", fp, offset + ofpFO, fp, "push original frame pointer");
    emitRM("LDA", fp, offset, fp, "push frame");
    emitRM("LDA", ac, 1, pc, "load ac with return address");
    if(exp.func.equals("input")){
      emitRM_Abs("LDA", pc, inputEntry, "jump to input");
    }
    else if(exp.func.equals("output")){
      emitRM_Abs("LDA", pc, outputEntry, "jump to output");
    }
    else{
      // add a case here for if the body is a nilexp
      FunctionDec functionDec = (FunctionDec) exp.dtype;
      if(functionDec.body instanceof NilExp){
        int loc = emitSkip(1);
        skipList.add(new NodeSkip(functionDec.func, loc, exp));
      }
      else{
        emitRM_Abs("LDA", pc, functionDec.funaddr, "jump to function body");
      }  
    }
    emitRM("LD", fp, ofpFO, fp, "pop frame");
    emitRM("ST", ac, offset, fp, "save data to offset location");
    emitComment("done with function call");
  }

  public void visit (SimpleDec dec, int offset, boolean isAddress){
    dec.type.accept(this, offset, isAddress);
    if(dec.nestLevel == 0){
      // case for global variable
      dec.offset = globalOffset;
      globalOffset--;
    }
    else{
      // case for regular variable
      dec.offset = frameOffset;
      frameOffset--;
    }
    
  }

  public void visit (ArrayDec dec, int offset, boolean isAddress){
    dec.type.accept(this, offset, isAddress);
    dec.size.accept(this, offset, isAddress);
  }

  public void visit (FunctionDec dec, int offset, boolean isAddress){
    dec.result.accept(this, offset, isAddress);
    
    
    int funcJump = emitSkip(1);
    if(dec.func.equals("main")){
      mainEntry = emitSkip(0); // hmm
    }
    else{
      dec.funaddr = emitSkip(0); // yeah!
      // do the thing you were talking about here
      // with 
      for(int i = 0; i < skipList.size(); i++){
        NodeSkip skipNode = skipList.get(i);
        if(skipNode.name.equals(dec.func) && skipNode.solved == false){
          emitBackup(skipNode.loc);
          emitRM_Abs("LDA", pc, dec.funaddr, "jump to function body - with skip!");
          emitRestore(); // back-patch in function prototypes! cool!
          skipNode.solved = true;
        }
      }
    }
    
    emitComment("Begin function: " + dec.func);
    emitRM("ST", ac, retFO, fp, "store return address");
    emitComment("Code inside function");
    // emitRM("LDC", ac, 42, 0, "load an arbitrary number to see if it works");
    frameOffset = initFO; // -2, because a function will always have two parameters
    dec.params.accept(this, frameOffset, isAddress);
    dec.body.accept(this, frameOffset, isAddress);
    emitRM("LD", pc, retFO, fp, "load return address, leaving: " + dec.func);
    emitComment("Exit " + dec.func);
    int funcEnd = emitSkip(0); 
    emitBackup(funcJump);
    emitRM_Abs("LDA", pc, funcEnd, "jump past function: " + dec.func);
    emitRestore();
    
    
  }

  


  public void visit(NilExp exp, int offset, boolean isAddress){
    // do nothing! easy. 
  }

  public void visit(NilDec exp, int offset, boolean isAddress){
    // do nothing! easy. 
  }

  public void visit(NilVarDec exp, int offset, boolean isAddress){
    // do nothing! easy. 
  }

  /* 
  public void visit( WriteExp exp, int offset ) {
    indent( offset );
    System.out.println( "WriteExp:" );
    if (exp.output != null)
       exp.output.accept( this, ++offset );
  }
       */

}
