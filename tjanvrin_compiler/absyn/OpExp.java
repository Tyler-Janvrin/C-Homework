package absyn;

public class OpExp extends Exp {
  public final static int LE     = 0;
  public final static int LT     = 1;
  public final static int GT     = 2;
  public final static int GE     = 3;
  public final static int EQ     = 4;
  public final static int NE     = 5;
  public final static int PLUS   = 6;
  public final static int MINUS  = 7;
  public final static int TIMES  = 8;
  public final static int DIV    = 9;
  public final static int UMINUS = 10;
  public final static int BITNOT = 11;
  public final static int OR     = 12;
  public final static int AND    = 13;  

  public Exp left;
  public int op;
  public Exp right;

  public OpExp( int row, int col, Exp left, int op, Exp right ) {
    this.row = row;
    this.col = col;
    this.left = left;
    this.op = op;
    this.right = right;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
  public void accept( AbsynCodeVisitor visitor, int level, boolean isAddress ) {
    visitor.visit( this, level, isAddress );
  }
}
