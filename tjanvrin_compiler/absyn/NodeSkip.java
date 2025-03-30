package absyn;

public class NodeSkip {
    public String name;
    public int loc;
    public CallExp exp;
    public boolean solved;

    public NodeSkip(String name, int loc, CallExp exp){
        this.name = name;
        this.loc = loc;
        this.exp = exp;
        this.solved = false;
    }
}
