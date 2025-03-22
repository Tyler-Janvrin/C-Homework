package absyn;

public class SimpleDec extends VarDec {
  public String name;

  public SimpleDec( int row, int col, NameTy type, String name ) {
    this.row = row;
    this.col = col;
    this.type = type;
    this.name = name;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
  public void accept( AbsynCodeVisitor visitor, int level, boolean isAddress ) {
    visitor.visit( this, level, isAddress );
  }
}
