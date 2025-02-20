import absyn.*;

public class ShowTreeVisitor implements AbsynVisitor {

  final static int SPACES = 4;

  private void indent( int level ) {
    for( int i = 0; i < level * SPACES; i++ ) System.out.print( " " );
  }

  public void visit( ExpList expList, int level ) {
    while( expList != null ) {
      expList.head.accept( this, level );
      expList = expList.tail;
    } 
  }

  public void visit (VarDecList varList, int level){
    while(varList != null){
      varList.head.accept(this, level);
      varList = varList.tail;
    }
  }

  public void visit (DecList decList, int level){
      while (decList != null) {
        decList.head.accept(this, level);
        decList = decList.tail;
      }
  }


  public void visit( AssignExp exp, int level ) {
    indent( level );
    System.out.println( "AssignExp:" );
    level++;
    exp.lhs.accept( this, level );
    exp.rhs.accept( this, level );
  }

  public void visit( IfExp exp, int level ) {
    indent( level );
    System.out.println( "IfExp: " );
    level++;
    exp.test.accept( this, level );
    exp.thenpart.accept( this, level );
    if (exp.elsepart != null )
       exp.elsepart.accept( this, level );
  }

  public void visit( CompoundExp exp, int level ){
    indent( level );
    System.out.println("CompoundExp: ");
    level++;
    exp.decs.accept(this, level);
    exp.exps.accept(this, level);
  }

  

  public void visit( IntExp exp, int level ) {
    indent( level );
    System.out.println( "IntExp: " + exp.value ); 
  }

  public void visit( OpExp exp, int level ) {
    indent( level );
    System.out.print( "OpExp:" ); 
    switch( exp.op ) {
      case OpExp.PLUS:
        System.out.println( " + " );
        break;
      case OpExp.MINUS:
        System.out.println( " - " );
        break;
      case OpExp.TIMES:
        System.out.println( " * " );
        break;
      case OpExp.DIV:
        System.out.println( " / " );
        break;
      case OpExp.EQ:
        System.out.println( " == " );
        break;
      case OpExp.NE:
        System.out.println( " != " );
        break;
      case OpExp.LT:
        System.out.println( " < " );
        break;
      case OpExp.LE:
        System.out.println( " <= " );
        break;
      case OpExp.GT:
        System.out.println( " > " );
        break;
      case OpExp.GE:
        System.out.println( " >= " );
        break;
      case OpExp.UMINUS:
        System.out.println( " - " );
        break;
      case OpExp.BITNOT:
        System.out.println( " ~ " );
        break;
      case OpExp.AND:
        System.out.println( " && " );
        break;
      case OpExp.OR:
      System.out.println( " || " );
        break;
      default:
        System.out.println( "Unrecognized operator at line " + exp.row + " and column " + exp.col);
    }
    level++;
    if (exp.left != null)
       exp.left.accept( this, level );
    if (exp.right != null)
       exp.right.accept( this, level );
  }

  public void visit(NameTy type, int level){
    indent( level );
    System.out.print( "NameTy: " ); 
    switch( type.type ) {
      case NameTy.BOOL:
        System.out.println( " bool " );
        break;
      case NameTy.INT:
        System.out.println( " int " );
        break;
      case NameTy.VOID:
        System.out.println( " void " );
        break;
      default:
        System.out.println( "Unrecognized type at line " + type.row + " and column " + type.col);
    }
  }

  /*

  public void visit( ReadExp exp, int level ) {
    indent( level );
    System.out.println( "ReadExp:" );
    exp.input.accept( this, ++level );
  }

  */

  /* 
  public void visit( RepeatExp exp, int level ) {
    indent( level );
    System.out.println( "RepeatExp:" );
    level++;
    exp.exps.accept( this, level );
    exp.test.accept( this, level ); 
  }
    */

  public void visit( VarExp exp, int level ) {
    indent( level );
    System.out.println( "VarExp: " );
    level++;
    exp.variable.accept( this, level);
  }

  public void visit (BoolExp exp, int level){
    indent( level );
    System.out.println("BoolExp: " + exp.value);
  }

  public void visit (SimpleVar variable, int level){
    indent( level );
    System.out.println("SimpleVar: " + variable.name);
  }

  public void visit(IndexVar variable, int level){
    indent( level);
    System.out.println("IndexVar: " + variable.name);
    level++;
    variable.index.accept(this, level);
  }

  public void visit( ReturnExp exp, int level ) {
    indent( level );
    System.out.println( "ReturnExp: " );
    level++;
    exp.exp.accept( this, level);
  }

  public void visit( WhileExp exp, int level ) {
    indent( level );
    System.out.println( "WhileExp: " );
    level++;
    exp.test.accept( this, level);
    exp.body.accept( this, level);
  }

  public void visit ( CallExp exp, int level){
    indent(level);
    System.out.println("CallExp: " + exp.func);
    level++;
    exp.args.accept(this, level);
  }

  public void visit (SimpleDec dec, int level){
    indent(level);
    System.out.println("SimpleDec: " + dec.name);
    level++;
    dec.type.accept(this, level);
  }

  public void visit (ArrayDec dec, int level){
    indent(level);
    System.out.println("ArrayDec: " + dec.name);
    level++;
    dec.type.accept(this, level);
    dec.size.accept(this, level);
  }

  public void visit (FunctionDec dec, int level){
    indent(level);
    System.out.println("FunctionDec: " + dec.func);
    level++;
    dec.result.accept(this, level);
    dec.params.accept(this, level);
    dec.body.accept(this, level);
  }

  


  public void visit(NilExp exp, int level){
    // do nothing! easy. 
  }

  public void visit(NilDec exp, int level){
    // do nothing! easy. 
  }

  public void visit(NilVarDec exp, int level){
    // do nothing! easy. 
  }

  /* 
  public void visit( WriteExp exp, int level ) {
    indent( level );
    System.out.println( "WriteExp:" );
    if (exp.output != null)
       exp.output.accept( this, ++level );
  }
       */

}
