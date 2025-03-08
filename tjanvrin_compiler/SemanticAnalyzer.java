import absyn.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SemanticAnalyzer implements AbsynVisitor {

  final static int SPACES = 4;

  public boolean valid = true;

  public HashMap<String, ArrayList<NodeType>> table;
  
  public NodeType last_function; // keep track of the last function
  // in the file, to see if it's main.

  public SemanticAnalyzer(){
    this.table = new HashMap<String, ArrayList<NodeType>>(10);
  }

  // returns true if insert successful, false if otherwise
  public boolean insert(NodeType node){
    // has to check for collisions
    ArrayList<NodeType> list = table.get(node.name);
    if(list == null){
      list = new ArrayList<NodeType>();
      list.add(node);
      table.put(node.name, list);
      System.err.println("In the null loop");
      return true;
    }
    else if(list.size() > 0){
      NodeType head = list.get(0);
      if(head.level == node.level && head.name.equals(node.name)){
        // redefining name - not allowed!
        System.err.println("Error: row: " + (node.dtype.row + 1) + " column: " + (node.dtype.col + 1) + " variable declared earlier within same scope");
        return false;
      }
      else if(head.level < node.level && head.name.equals(node.name)){
        // otherwise, add the node to the list
        list.add(0, node);
        System.err.println("In the main body loop");
        return true;
      }
      else{ // head level is greater than node level...
        System.err.println("Error: row: " + (node.dtype.row + 1) + " column: " + (node.dtype.col + 1) + " trying to add a node to the list from a lower level.");
        return false;
      }
    }
    else{
      list = new ArrayList<NodeType>();
      list.add(node);
      table.put(node.name, list);
      System.err.println("Special case for when the list is empty?");
      return true;
    }
  }

  public NodeType lookup (String s){
    // this will perform a lookup to check if
    // a node with a given name exists in the hashmap

    return null;
  }

  public void delete(int layer){
    // this will pop off the top layer
    // don't need do deal with indices - we'll always just remove the top.
    Iterator<HashMap.Entry<String, ArrayList<NodeType>>> iter = table.entrySet().iterator();
    while(iter.hasNext()){
      HashMap.Entry<String, ArrayList<NodeType>> entry = iter.next();
      ArrayList<NodeType> list = entry.getValue();
      while(list.size() > 0 && list.get(0).level == layer){
        list.remove(0);
      }
    }
  }

  public void display(int level){
    // not finished...
    // System.err.println("Starting display");
    Iterator<HashMap.Entry<String, ArrayList<NodeType>>> iter = table.entrySet().iterator();
    while(iter.hasNext()){
      HashMap.Entry<String, ArrayList<NodeType>> entry = iter.next();
      ArrayList<NodeType> list = entry.getValue();
      for(int i = 0; i < list.size(); i++){
        NodeType node = list.get(i);
        // System.err.println(node.dtype.type.toString());

        if(node.dtype instanceof SimpleDec && node.level == level){
          indent(level);
          System.out.println(node.name + ": " + node.dtype.type.TypeName()+ " " + node.level);
        }
        else if(node.dtype instanceof ArrayDec && node.level == level){
          
        }
        else if(node.dtype instanceof FunctionDec && node.level == level){
          indent(level);
          System.out.print(node.name + ": " + "(");
          System.out.println(") -> " + node.dtype.type.TypeName() + " " + node.level);
        }
        else{
          // those are the only ones we care about...
        }
      }
    }
    // System.err.println("Ending display.");

  }



  private void indent( int level ) {
    for( int i = 0; i < level * SPACES; i++ ) System.out.print( " " );
  }

  /* I think varlist, declist, and explist can remain the same. */
  public void visit( ExpList expList, int level ) {
    while( expList != null ) {
      expList.head.accept( this, level );
      expList = expList.tail;
    } 
  }

  public void visit (VarDecList varList, int level){
    while(varList != null){
      varList.head.accept(this, level);
      varList = varList.tail;
    }

  }

  public void visit (DecList decList, int level){
    indent(level);
    System.out.println("Entering the global scope:");
           
      while (decList != null) {
        decList.head.accept(this, level);
        decList = decList.tail;
      }

    display(level);
    delete(level);
    indent(level);

    System.out.println("Leaving the global scope");

  }


  public void visit( AssignExp exp, int level ) {
    //indent( level );
    //System.out.println( "AssignExp:" );
    //level++;
    exp.lhs.accept( this, level );
    exp.rhs.accept( this, level );
  }

  public void visit( IfExp exp, int level ) {
    //indent( level );
    //System.out.println( "IfExp: " );
    //level++;
    exp.test.accept( this, level );
    exp.thenpart.accept( this, level );
    if (exp.elsepart != null )
       exp.elsepart.accept( this, level );
  }

  public void visit( CompoundExp exp, int level ){
    //indent( level );
    //System.out.println("CompoundExp: ");
    level++;
    indent(level);
    System.out.println("Entering a new block:");

    
    exp.decs.accept(this, level);
    exp.exps.accept(this, level);

    display(level);
    delete(level);
    indent(level);
    System.out.println("Leaving the block");
  }

  

  public void visit( IntExp exp, int level ) {
    //indent( level );
    //System.out.println( "IntExp: " + exp.value ); 
  }

  public void visit( OpExp exp, int level ) {
    //indent( level );
    /* 
    System.out.print( "OpExp:" ); 
    switch( exp.op ) {
      case OpExp.PLUS:
        System.out.println( " + " );
        break;
      case OpExp.MINUS:
        System.out.println( " - " );
        break;
      case OpExp.TIMES:
        System.out.println( " * " );
        break;
      case OpExp.DIV:
        System.out.println( " / " );
        break;
      case OpExp.EQ:
        System.out.println( " == " );
        break;
      case OpExp.NE:
        System.out.println( " != " );
        break;
      case OpExp.LT:
        System.out.println( " < " );
        break;
      case OpExp.LE:
        System.out.println( " <= " );
        break;
      case OpExp.GT:
        System.out.println( " > " );
        break;
      case OpExp.GE:
        System.out.println( " >= " );
        break;
      case OpExp.UMINUS:
        System.out.println( " - " );
        break;
      case OpExp.BITNOT:
        System.out.println( " ~ " );
        break;
      case OpExp.AND:
        System.out.println( " && " );
        break;
      case OpExp.OR:
      System.out.println( " || " );
        break;
      default:
        System.out.println( "Unrecognized operator at line " + exp.row + " and column " + exp.col);
   
  
  }
        */ 
    // level++;
    if (exp.left != null)
       exp.left.accept( this, level );
    if (exp.right != null)
       exp.right.accept( this, level );
  }

  public void visit(NameTy type, int level){
    //indent( level );
    /* 
    System.out.print( "NameTy: " ); 
    switch( type.type ) {
      case NameTy.BOOL:
        System.out.println( " bool " );
        break;
      case NameTy.INT:
        System.out.println( " int " );
        break;
      case NameTy.VOID:
        System.out.println( " void " );
        break;
      default:
        System.out.println( "Unrecognized type at line " + type.row + " and column " + type.col);
    }
    */
  }

  public void visit( VarExp exp, int level ) {
    //indent( level );
    // System.out.println( "VarExp: " );
    // level++;
    exp.variable.accept( this, level);
  }

  public void visit (BoolExp exp, int level){
    // indent( level );
    //System.out.println("BoolExp: " + exp.value);
  }

  public void visit (SimpleVar variable, int level){
    // indent( level );
    //System.out.println("SimpleVar: " + variable.name);
  }

  public void visit(IndexVar variable, int level){
    // indent( level);
    //System.out.println("IndexVar: " + variable.name);
    // level++;
    variable.index.accept(this, level);
  }

  public void visit( ReturnExp exp, int level ) {
    // indent( level );
    //System.out.println( "ReturnExp: " );
    // level++;
    exp.exp.accept( this, level);
  }

  public void visit( WhileExp exp, int level ) {
    // indent( level );
    //System.out.println( "WhileExp: " );
    // level++;
    exp.test.accept( this, level);
    exp.body.accept( this, level);
  }

  public void visit ( CallExp exp, int level){
    // indent(level);
    //System.out.println("CallExp: " + exp.func);
    // level++;
    exp.args.accept(this, level);
  }


  // we'll start here
  public void visit (SimpleDec dec, int level){
    
    
    //level++;
    dec.type.accept(this, level);
    // indent(level);
    // System.out.println("SimpleDec: " + dec.name + " level: " + level);
    insert(new NodeType(dec.name, dec, level));
  }

  public void visit (ArrayDec dec, int level){
    // indent(level);
    // System.out.println("ArrayDec: " + dec.name);
    // level++;
    dec.type.accept(this, level);
    dec.size.accept(this, level);
  }

  // when we enter the function scope...
  public void visit (FunctionDec dec, int level){
    // System.out.println("FunctionDec: " + dec.func);
    indent(level);

    System.out.println("Entering the scope for function f: " + dec.func);

    
    dec.result.accept(this, level);

    insert(new NodeType(dec.func, dec, level));

    dec.params.accept(this, level + 1);
    dec.body.accept(this, level);

    indent(level);

    System.out.println("Leaving the scope for function f: " + dec.func);
  }

  


  public void visit(NilExp exp, int level){
    // do nothing! easy. 
  }

  public void visit(NilDec exp, int level){
    // do nothing! easy. 
  }

  public void visit(NilVarDec exp, int level){
    // do nothing! easy. 
  }

}
