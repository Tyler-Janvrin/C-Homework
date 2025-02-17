package absyn;

public class VarDecList extends Absyn {
  public Exp head;
  public ExpList tail;

  public VarDecList( Exp head, ExpList tail ) {
    this.head = head;
    this.tail = tail;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
