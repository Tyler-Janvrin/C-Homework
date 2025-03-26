import absyn.*;

public class CodeGenerator implements AbsynCodeVisitor {

  int mainEntry = 0, globalOffset = 0;
  int frameOffset = 0;
  int ofpFO = 0; // original function pointer frame offset
  int retFO = 0; // return address frame offset
  int initFO = 0; // initial frame offset

  // special register names
  final int ac = 0;
  final int ac1 = 1;
  final int gp = 6;
  final int fp = 5;
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



  }

  final static int SPACES = 4;

  private void indent( int offset ) {
    for( int i = 0; i < offset * SPACES; i++ ) System.out.print( " " );
  }

  public void visit( ExpList expList, int offset, boolean isAddress ) {
    while( expList != null ) {
      expList.head.accept( this, offset, isAddress );
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
  }

  public void visit( IfExp exp, int offset, boolean isAddress ) {
    exp.test.accept( this, offset, isAddress );
    exp.thenpart.accept( this, offset, isAddress );
    if (exp.elsepart != null )
       exp.elsepart.accept( this, offset, isAddress );
  }

  public void visit( CompoundExp exp, int offset, boolean isAddress ){
    exp.decs.accept(this, offset, isAddress);
    emitComment("Size of frameOffset: " + frameOffset);
    // by the time we reach this point, we'll have used up frameOffset
    exp.exps.accept(this, offset, isAddress);
  }

  

  public void visit( IntExp exp, int offset, boolean isAddress ) {
    
  }

  public void visit( OpExp exp, int offset, boolean isAddress ) {
    /*switch( exp.op ) {
      case OpExp.PLUS:
        break;
      case OpExp.MINUS:
        break;
      case OpExp.TIMES:
        break;
      case OpExp.DIV:
        break;
      case OpExp.EQ:
        break;
      case OpExp.NE:
        break;
      case OpExp.LT:
        break;
      case OpExp.LE:
        break;
      case OpExp.GT:
        break;
      case OpExp.GE:
        break;
      case OpExp.UMINUS:
        break;
      case OpExp.BITNOT:
        break;
      case OpExp.AND:
        break;
      case OpExp.OR:
        break;
      default:
    }
      */
    if (exp.left != null)
       exp.left.accept( this, offset, isAddress );
    if (exp.right != null)
       exp.right.accept( this, offset, isAddress );
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
    if(isAddress = true){
      offset = offset - 1;
      emitRM("LDA", ac, exp.dtype.offset, fp, "load the variable storing address");
      emitRM("LDA", ac, offset, fp, "store the variable storing address");
    }
    else{
      offset = offset - 2;
      // working here - I'm too tired to keep going
      // address is false - this isn't an address
    }
  }

  public void visit (BoolExp exp, int offset, boolean isAddress){

  }

  public void visit (SimpleVar variable, int offset, boolean isAddress){
    
  }

  public void visit(IndexVar variable, int offset, boolean isAddress){
    variable.index.accept(this, offset, isAddress );
  }

  public void visit( ReturnExp exp, int offset, boolean isAddress ) {
    exp.exp.accept( this, offset, isAddress );
  }

  public void visit( WhileExp exp, int offset, boolean isAddress ) {
    exp.test.accept( this, offset, isAddress);
    exp.body.accept( this, offset, isAddress);
  }

  public void visit ( CallExp exp, int offset, boolean isAddress){
    exp.args.accept(this, offset, isAddress);
  }

  public void visit (SimpleDec dec, int offset, boolean isAddress){
    dec.type.accept(this, offset, isAddress);
    frameOffset--;
    dec.offset = frameOffset;
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
    
    emitComment("Begin function: " + dec.func);
    emitRM("ST", ac, -1, fp, "store return address");
    emitComment("Code inside function");
    emitRM("LDC", ac, 42, 0, "load an arbitrary number to see if it works");
    frameOffset = -2;
    dec.params.accept(this, frameOffset, isAddress);
    dec.body.accept(this, frameOffset, isAddress);
    emitRM("LD", pc, -1, fp, "load the address stored when we entered function: " + dec.func);
    emitComment("Exit main");
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
