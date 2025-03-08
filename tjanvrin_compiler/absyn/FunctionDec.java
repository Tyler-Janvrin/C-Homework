package absyn;

public class FunctionDec extends Dec {
  public NameTy result;
  public String func;
  public VarDecList params;
  public Exp body;

  public FunctionDec( int row, int col, NameTy result, String func, VarDecList params, Exp body ) {
    this.row = row;
    this.col = col;
    this.result = result;
    this.type = result; // this is not clean, but I'm tired.
    this.func = func;
    this.params = params;
    this.body = body;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
