package absyn;

public class SimpleVar extends Var {

  public SimpleVar( int row, int col, String name ) {
    this.row = row;
    this.col = col;
    this.name = name;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
  public void accept( AbsynCodeVisitor visitor, int level, boolean isAddress ) {
    visitor.visit( this, level, isAddress );
  }
}
