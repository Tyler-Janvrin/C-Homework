package absyn;

public interface AbsynCodeVisitor {

  public void visit( ExpList exp, int level, boolean isAddress );

  public void visit( AssignExp exp, int level, boolean isAddress );

  public void visit( IfExp exp, int level, boolean isAddress );

  public void visit( IntExp exp, int level, boolean isAddress );

  public void visit( OpExp exp, int level, boolean isAddress );

  // public void visit( ReadExp exp, int level );

  // public void visit( RepeatExp exp, int level );

  public void visit( VarExp exp, int level, boolean isAddress );

  public void visit( DecList decList, int level, boolean isAddress );

  public void visit( VarDecList varDecList, int level, boolean isAddress );
  
  public void visit( BoolExp exp, int level, boolean isAddress );

  public void visit( SimpleVar exp, int level, boolean isAddress );

  public void visit( IndexVar exp, int level, boolean isAddress );

  public void visit( ReturnExp exp, int level, boolean isAddress );

  public void visit( NilExp exp, int level, boolean isAddress );

  public void visit( NilDec dec, int level, boolean isAddress );

  public void visit( NilVarDec dec, int level, boolean isAddress );

  public void visit( CompoundExp dec, int level, boolean isAddress );

  public void visit (WhileExp exp, int level, boolean isAddress );

  public void visit (SimpleDec dec, int level, boolean isAddress );
  
  public void visit (ArrayDec dec, int level, boolean isAddress );

  public void visit(NameTy name, int level, boolean isAddress );

  public void visit(FunctionDec dec, int level, boolean isAddress );

  public void visit(CallExp exp, int level, boolean isAddress );


  // public void visit( WriteExp exp, int level );

}
