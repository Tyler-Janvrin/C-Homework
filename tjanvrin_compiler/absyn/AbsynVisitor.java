package absyn;

public interface AbsynVisitor {

  public void visit( ExpList exp, int level );

  public void visit( AssignExp exp, int level );

  public void visit( IfExp exp, int level );

  public void visit( IntExp exp, int level );

  public void visit( OpExp exp, int level );

  // public void visit( ReadExp exp, int level );

  // public void visit( RepeatExp exp, int level );

  public void visit( VarExp exp, int level );

  public void visit( DecList decList, int level );

  public void visit( VarDecList varDecList, int level );
  
  public void visit( BoolExp exp, int level);
  // public void visit( WriteExp exp, int level );

}
