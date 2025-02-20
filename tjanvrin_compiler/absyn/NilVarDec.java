package absyn;

public class NilVarDec extends VarDec {

  public NilVarDec( int row, int col ) {
    this.row = row;
    this.col = col;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
