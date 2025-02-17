package absyn;

public class DecList extends Absyn {
  public Exp head;
  public ExpList tail;

  public DecList( Exp head, ExpList tail ) {
    this.head = head;
    this.tail = tail;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
