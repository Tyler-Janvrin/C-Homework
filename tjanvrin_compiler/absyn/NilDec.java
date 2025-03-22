package absyn;

public class NilDec extends Dec {

  public NilDec( int row, int col ) {
    this.row = row;
    this.col = col;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
  public void accept( AbsynCodeVisitor visitor, int level, boolean isAddress ) {
    visitor.visit( this, level, isAddress );
  }
}
