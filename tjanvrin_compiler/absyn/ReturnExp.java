package absyn;

public class ReturnExp extends Exp {
  public Exp exp;

  public ReturnExp( int row, int col, Exp exp ) {
    this.row = row;
    this.col = col;
    this.exp = exp;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
  public void accept( AbsynCodeVisitor visitor, int level, boolean isAddress ) {
    visitor.visit( this, level, isAddress );
  }
}
