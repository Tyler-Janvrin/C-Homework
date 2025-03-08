package absyn;

public class NodeType {
    public String name;
    public Dec dtype;
    public int level;

    public NodeType(String name, Dec dtype, int level){
        this.name = name;
        this.dtype = dtype;
        this.level = level;
    }
}
