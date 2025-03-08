package absyn;

public class NameTy extends Absyn {
  public final static int BOOL    = 0;
  public final static int INT     = 1;  
  public final static int VOID    = 2;

  public int type;

  public NameTy( int row, int col, int type) {
    this.row = row;
    this.col = col;
    this.type = type;
  }

  public String TypeName(){
    if(this.type == 0) return "bool";
    if(this.type == 1) return "int";
    if(this.type == 2) return "void";
    return "error";
  }
  
  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}