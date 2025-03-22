package absyn;

public class ExpList extends Absyn {
  public Exp head;
  public ExpList tail;

  public ExpList( Exp head, ExpList tail ) {
    this.head = head;
    this.tail = tail;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
  public void accept( AbsynCodeVisitor visitor, int level, boolean isAddress ) {
    visitor.visit( this, level, isAddress );
  }
}
