import absyn.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SemanticAnalyzer implements AbsynVisitor {

  final static int SPACES = 4;

  final static NodeType intType = new NodeType("int", new SimpleDec(0,0,new NameTy( 0, 0, NameTy.INT), "intType"), 0);
  final static NodeType boolType = new NodeType("bool", new SimpleDec(0,0,new NameTy( 0, 0, NameTy.BOOL), "boolType"), 0);
  final static NodeType voidType = new NodeType("void", new SimpleDec(0,0,new NameTy( 0, 0, NameTy.VOID), "voidType"), 0);

  public boolean valid = true;

  public HashMap<String, ArrayList<NodeType>> table;
  
  public NodeType last_function; // keep track of the last function
  // in the file, to see if it's main.
  public int returnCount; // keep track of how many returns there are in a function

  public SemanticAnalyzer(){
    this.table = new HashMap<String, ArrayList<NodeType>>(10);
  }

  public void checkLastWasMain(){
    String decString = "";
    FunctionDec castNode = (FunctionDec) last_function.dtype;
    VarDecList paramList = castNode.params;
    VarDec param;
    decString = decString + last_function.name + "(";
    while(paramList != null){
      param = paramList.head;
      if(param instanceof NilVarDec){
        // do nothing
      }
      else if(param instanceof ArrayDec){
        decString = decString + param.type.TypeName() + "[]";
      }
      else{
        decString = decString + param.type.TypeName();
      }
      paramList = paramList.tail;
      if(paramList != null){
        decString = decString + ", ";
      }
    }
    decString = decString + ")";

    if(decString.equals("main()") && last_function.dtype.type.type == NameTy.INT){
      // do nothing
    }
    else{
      System.err.println("Error: main missing/not last function in file (last function was " +last_function.dtype.type.TypeName() + " "+ decString + ")");
      valid = false;
    }
  }

  public void insertSystemFunctions(){
    FunctionDec input = new FunctionDec(0,0, new NameTy(0,0,NameTy.INT),"input", new VarDecList(new NilVarDec(0,0), null), new NilExp(0,0));
    FunctionDec output = new FunctionDec(0,0, new NameTy(0,0,NameTy.VOID),"output", new VarDecList(new SimpleDec(0,0, new NameTy(0,0, NameTy.INT), "ajlfjkalejlajkfa"), null), new NilExp(0,0));
    insert(new NodeType("input", input, 0));
    insert(new NodeType("output", output, 0));
  
  }

  // all of these functions rely on the fact that 
  // all of the arraylists will always have the highest node on top
  // I'm not 100% sure they're collision resistant, though
  // I'll add that later.

  // helper function that checks to see ifa node is in a list
  public boolean inList(ArrayList<NodeType> list, NodeType node){
    for(int i = 0; i < list.size(); i++){
      if(list.get(i).level == node.level && list.get(i).name.equals(node.name)){
        return true;
      }
    }
    return false;
  }

  // returns true if insert successful, false if otherwise
  public boolean insert(NodeType node){
    // has to check for collisions
    ArrayList<NodeType> list = table.get(node.name);
    if(list == null){
      list = new ArrayList<NodeType>();
      list.add(node);
      table.put(node.name, list);
      // System.err.println("In the null loop");
      return true;
    }
    else if(list.size() > 0){
      NodeType head = list.get(0);
      if(inList(list, node)){
        // redefining name - not allowed!
        // special case: if head is a function prototype and node is a function, redefinition is allowed
        // might come back here later to do some type checking
        if(head.dtype instanceof FunctionDec && node.dtype instanceof FunctionDec){
          FunctionDec funcHead = (FunctionDec) head.dtype;
          if(funcHead.body instanceof NilExp){
            list.remove(0);
            list.add(0, node); // swap the function prototype for a full function
            return true; // if this happens, everything's fine :)
          }
        }


        System.err.println("Error: row: " + (node.dtype.row + 1) + " column: " + (node.dtype.col + 1) + " variable " + node.name + " declared earlier within same scope");
        valid = false;
        return false;
      }
      else if(head.level < node.level){
        // otherwise, add the node to the list
        list.add(0, node);
         // System.err.println("In the main body loop");
        return true;
      }
      else{ // head level is greater than node level...
        System.err.println("Error: row: " + (node.dtype.row + 1) + " column: " + (node.dtype.col + 1) + " trying to add a node to the list from a lower level.");
        valid = false;
        return false;
      }
    }
    else{
      // actually, there are cases where it's fine for the list to be empty. what a relief :)
      list = new ArrayList<NodeType>();
      list.add(node);
      table.put(node.name, list);
      return true;
    }
  }

  public NodeType lookup (Var var){
    // this will perform a lookup to check if
    // a node with a given name exists in the hashmap
    ArrayList<NodeType> list = table.get(var.name);
    if(list == null){
      // don't do anything
    }
    else{
      for(int i = 0; i < list.size(); i++){
        if(list.get(i).name.equals(var.name)){
          return list.get(i); // found it!
        }
      }
    }

    System.err.println("Error: row: " + (var.row + 1) + " column: " + (var.col + 1) + " variable " + var.name + " not declared within current scope");
    valid = false;
    return null; // didn't find it
  }

  public NodeType lookup (String str){
    // this will perform a lookup to check if
    // a node with a given name exists in the hashmap
    ArrayList<NodeType> list = table.get(str);
    if(list == null){
      // don't do anything
    }
    else{
      for(int i = 0; i < list.size(); i++){
        if(list.get(i).name.equals(str)){
          return list.get(i); // found it!
        }
      }
    }
    return null; // didn't find it
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
          indent(level + 1);
          System.out.println(node.name + ": " + node.dtype.type.TypeName());
        }
        else if(node.dtype instanceof ArrayDec && node.level == level){
          indent(level + 1);
          System.out.println(node.name + ": " + node.dtype.type.TypeName()+ "[]");
        }
        else if(node.dtype instanceof FunctionDec && node.level == level){
          indent(level + 1);
          System.out.print(node.name + ": " + "(");
          FunctionDec castNode = (FunctionDec) node.dtype;
          VarDecList paramList = (VarDecList) castNode.params;
          VarDec param;
          while(paramList != null){
            param = paramList.head;
            if(param instanceof SimpleDec){
              System.out.print(param.type.TypeName());
            }
            else if(param instanceof ArrayDec){
              System.out.print(param.type.TypeName() + "[]");
            }
            paramList = paramList.tail;
            if(paramList != null){
              System.out.print(", ");
            }
          }
          System.out.println(") -> " + node.dtype.type.TypeName());
        }
        else{
          // those are the only ones we care about...
        }
      }
    }
    // System.err.println("Ending display.");

  }

  public boolean isInt(Exp exp){
    if(exp.dtype == null){
      return false;
    }
    if(exp.dtype.type.type == NameTy.INT){
      return true;
    }
    else{
      return false;
    }
  }

  public boolean isBool(Exp exp){
    if(exp.dtype == null){
      return false;
    }
    if(exp.dtype.type.type == NameTy.BOOL){
      return true;
    }
    else{
      return false;
    }
  }

  public boolean isVoid(Exp exp){
    if(exp.dtype == null){
      return false;
    }
    if(exp.dtype.type.type == NameTy.VOID){
      return true;
    }
    else{
      return false;
    }
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

    if(exp.lhs.dtype.type.type != exp.rhs.dtype.type.type){
      System.err.println("Error: row: " + (exp.row + 1) + " column: " + (exp.col + 1) + ": expected arguments to variable assignment to be the same, but they were " + exp.lhs.dtype.type.TypeName() + " and " + exp.rhs.dtype.type.TypeName());
      valid = false;
    }

    exp.dtype = exp.lhs.dtype;
  }

  public void visit( IfExp exp, int level ) {
    //indent( level );
    //System.out.println( "IfExp: " );
    //level++;
    exp.test.accept( this, level );
    exp.thenpart.accept( this, level );
    if (exp.elsepart != null )
       exp.elsepart.accept( this, level );

    if(!(isInt(exp.test) || isBool(exp.test))){
      System.err.println("Error: row: " + (exp.row + 1) + " column: " + (exp.col + 1) + ": expected if statement test condition to be either int or bool: it was " + exp.test.dtype.type.TypeName());
      valid = false;
    }
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
    exp.dtype = intType.dtype;
  }

  public void visit( OpExp exp, int level ) {
    //indent( level );
    // case for arithmetic operators
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
    if (exp.left != null){
      exp.left.accept( this, level );
    }       
    if (exp.right != null){
      exp.right.accept( this, level );
    }
       
    // it would be good to add the name of the operator to the error message...
    if(exp.op == OpExp.PLUS || exp.op == OpExp.MINUS || exp.op == OpExp.TIMES || exp.op == OpExp.DIV){
      exp.dtype = intType.dtype; // return type is always int
      
      if(isInt(exp.left) && isInt(exp.right)){
        // do nothing
      }
      else if(exp.left.dtype == null || exp.right.dtype == null){
        // don't do anything - setting it like this so I can build incrementally
      }
      else{
        System.err.println("Error: row: " + (exp.row + 1) + " column: " + (exp.col + 1) + ": expected arguments to arithmetic operators to be ints, but they were " + exp.left.dtype.type.TypeName() + " and " + exp.right.dtype.type.TypeName());
        valid = false;
      }
    }
    if(exp.op == OpExp.EQ || exp.op == OpExp.NE || exp.op == OpExp.LT || exp.op == OpExp.LE || exp.op == OpExp.GT || exp.op == OpExp.GE){
      exp.dtype = boolType.dtype; // return type is always bool
      if(isInt(exp.left) && isInt(exp.right)){
        // do nothing
      }
      else if(exp.left.dtype == null || exp.right.dtype == null){
        // don't do anything - setting it like this so I can build incrementally
      }
      else{
        System.err.println("Error: row: " + (exp.row + 1) + " column: " + (exp.col + 1) + ": expected arguments to relational operator to be ints, but they were " + exp.left.dtype.type.TypeName() + " and " + exp.right.dtype.type.TypeName());
        valid = false;
      }
    }
    if(exp.op == OpExp.AND || exp.op == OpExp.OR){
      exp.dtype = boolType.dtype; // return type is always bool
      if((isInt(exp.left) || isBool(exp.left)) && (isInt(exp.right) || isBool(exp.right))){
        // do nothing
      }
      else if(exp.left.dtype == null || exp.right.dtype == null){
        // don't do anything - setting it like this so I can build incrementally
      }
      else{
        System.err.println("Error: row: " + (exp.row + 1) + " column: " + (exp.col + 1) + ": expected arguments to boolean operator to be bool or int, but they were " + exp.left.dtype.type.TypeName() + " and " + exp.right.dtype.type.TypeName());
        valid = false;
      }
    }
    if(exp.op == OpExp.UMINUS){
      exp.dtype = intType.dtype; // return type is always int
      if(isInt(exp.right)){
        
      }
      else if(exp.right.dtype == null){
        // don't do anything - setting it like this so I can build incrementally
      }
      else{
        System.err.println("Error: row: " + (exp.row + 1) + " column: " + (exp.col + 1) + ": expected arguments to uminus operator to be int, but it was " + exp.right.dtype.type.TypeName());
        valid = false;
      }
    }
    if(exp.op == OpExp.BITNOT){
      exp.dtype = boolType.dtype; // return type is alwasy bool
      if(isBool(exp.right) || isInt(exp.right)){
        // do nothing
      }
      else if(exp.right.dtype == null){
        // don't do anything - setting it like this so I can build incrementally
      }
      else{
        System.err.println("Error: row: " + (exp.row + 1) + " column: " + (exp.col + 1) + ": expected arguments to not operator to be int or bool but it was " + exp.right.dtype.type.TypeName());
        valid = false;
      }
    }
    
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
    NodeType node = lookup(exp.variable);
    if(node == null){
      exp.dtype = intType.dtype;
      // we already print an error if the node doesn't exist, 
      // so we don't need to print one again
    }
    // working here
    else if(exp.variable instanceof SimpleVar){
      /* 
      if(!(node.dtype instanceof SimpleDec)){
        // we don't have a matching variable
        System.err.println("Error: row: " + (exp.row + 1) + " column: " + (exp.col + 1) + " expected " + exp.variable.name + " to be SimpleVar but it was something else");
        valid = false;
        exp.dtype = intType.dtype; // assume it's an int
      }
        */
      //else{
        // the variable class matches
        exp.dtype = node.dtype;
      //}
    }
    else if (exp.variable instanceof IndexVar){
      if(!(node.dtype instanceof ArrayDec)){
        // we don't have a matching variable
        System.err.println("Error: row: " + (exp.row + 1) + " column: " + (exp.col + 1) + " expected " + exp.variable.name + " to be IndexVar but it was something else");
        valid = false;
        exp.dtype = node.dtype; // assume it's an int
      }
      else{
        // we need arithmetic to be able to do this...
        // System.err.println("Still need to do me!");
        IndexVar castIndexVar = (IndexVar) exp.variable;
        if(!isInt(castIndexVar.index)){
          System.err.println("Error: row: " + (exp.row + 1) + " column: " + (exp.col + 1) + " index of array expression not int type");
          valid = false;
        }
        exp.dtype = node.dtype;
      }
    }
    else{
      System.err.println("Error: row: " + (exp.row + 1) + " column: " + (exp.col + 1) + " mismatched variable class");
      valid = false;
    }
  }

  public void visit (BoolExp exp, int level){
    // indent( level );
    //System.out.println("BoolExp: " + exp.value);
    exp.dtype = boolType.dtype;
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
    exp.dtype = exp.exp.dtype;
    returnCount++;
    if(last_function.dtype.type.type != exp.dtype.type.type){
      System.err.println("Error: row: " + (exp.row + 1) + " column: " + (exp.col + 1) + " mismatched return type: expected " + last_function.dtype.type.TypeName() + " but was " + exp.dtype.type.TypeName());
      valid = false;
    }
    if(last_function.dtype.type.type == NameTy.VOID){
      System.err.println("Error: row: " + (exp.row + 1) + " column: " + (exp.col + 1) + " return statement in void function ");
      valid = false;
    }
  }

  public void visit( WhileExp exp, int level ) {
    // indent( level );
    //System.out.println( "WhileExp: " );
    // level++;
    exp.test.accept( this, level);
    exp.body.accept( this, level);

    if(!(isInt(exp.test) || isBool(exp.test))){
      System.err.println("Error: row: " + (exp.row + 1) + " column: " + (exp.col + 1) + ": expected while statement test condition to be either int or bool: it was " + exp.test.dtype.type.TypeName());
      valid = false;
    }
  }

  public void visit ( CallExp exp, int level){
    // indent(level);
    //System.out.println("CallExp: " + exp.func);
    // level++;
    exp.args.accept(this, level);

    NodeType node = lookup(exp.func);

    if(node == null){
      System.err.println("Error: row: " + (exp.row + 1) + " column: " + (exp.col + 1) + " function " + exp.func + " not declared within current scope");
      valid = false;
    }
    else{
      exp.dtype = node.dtype; // very important
      // node is not null
      if(!(node.dtype instanceof FunctionDec)){
        System.err.println("Error: row: " + (exp.row + 1) + " column: " + (exp.col + 1) + " attempting to call non-function variable " + exp.func);
        valid = false;
      }
      else{
        // node is a function dec

        // get the declaration string
        String decString = "";
        FunctionDec castNode = (FunctionDec) node.dtype;
        VarDecList paramList = castNode.params;
        VarDec param;
        decString = decString + node.name + "(";
        while(paramList != null){
          param = paramList.head;
          if(param instanceof NilVarDec){
            // do nothing
          }
          else if(param instanceof ArrayDec){
            decString = decString + param.type.TypeName() + "[]";
          }
          else{
            decString = decString + param.type.TypeName();
          }
          paramList = paramList.tail;
          if(paramList != null){
            decString = decString + ", ";
          }
        }
        decString = decString + ")";

        // get the expression string
        String expString = "";
        ExpList expList = exp.args;
        Exp item;
        expString = expString + exp.func + "(";
        while(expList != null){
          item = expList.head;
          if(item instanceof NilExp){
            // do nothing
          }
          else if(item instanceof VarExp){
            VarExp varItem = (VarExp) item;
            NodeType varNode = lookup(varItem.variable.name);
            
            if(varNode == null){
              System.err.println("Error: row: " + (exp.row + 1) + " column: " + (exp.col + 1) + " function " + exp.func + " not declared within current scope");
              valid = false;
            }
            else{
              if(varNode.dtype instanceof ArrayDec){
                expString = expString + item.dtype.type.TypeName() + "[]";
              }
              else{
                expString = expString + item.dtype.type.TypeName();
              }
            }
          }
          else{
            expString = expString + item.dtype.type.TypeName();
          }
          expList = expList.tail;
          if(expList != null){
            expString = expString + ", ";
          }
        }
        expString = expString + ")";

        if(!decString.equals(expString)){
          System.err.println("Error: row: " + (exp.row + 1) + " column: " + (exp.col + 1) + " function signature: " + decString + " doesn't match function call: " + expString);
          valid = false;
        }

      }


    }


  }


  // we'll start here
  public void visit (SimpleDec dec, int level){
    
    
    //level++;
    dec.type.accept(this, level);
    // indent(level);
    // System.out.println("SimpleDec: " + dec.name + " level: " + level);
    if(dec.type.type == NameTy.VOID){
      System.err.println("Error: row: " + (dec.row + 1) + " column: " + (dec.col + 1) + " variable declared as type void");
      valid = false;
    }
    
    insert(new NodeType(dec.name, dec, level));
  }

  public void visit (ArrayDec dec, int level){
    // indent(level);
    // System.out.println("ArrayDec: " + dec.name);
    // level++;
    dec.type.accept(this, level);
    dec.size.accept(this, level);

    if(dec.type.type == NameTy.VOID){
      System.err.println("Error: row: " + (dec.row + 1) + " column: " + (dec.col + 1) + " variable declared as type void");
      valid = false;
    }
    
    insert(new NodeType(dec.name, dec, level));
  }

  // when we enter the function scope...
  public void visit (FunctionDec dec, int level){
    // System.out.println("FunctionDec: " + dec.func);
    
    if(!(dec.body instanceof NilExp)) {
      indent(level + 1);
      System.out.println("Entering the scope for function f: " + dec.func);
    }

    
    
    dec.result.accept(this, level);
    if(!(dec.body instanceof NilExp)) {
      dec.params.accept(this, level + 1);
    }

    returnCount = 0; // keep track of return type
    last_function = new NodeType(dec.func, dec, 0);
    
    dec.body.accept(this, level);
    //if(!(dec.body instanceof NilExp)) {
      insert(new NodeType(dec.func, dec, level));
    //}
    


    
    
    if(!(dec.body instanceof NilExp)) {
      // check for missing return statement even if function is not prototype
      if((dec.type.type == NameTy.BOOL || dec.type.type == NameTy.INT) && returnCount == 0){
        System.err.println("Error: row: " + (dec.row + 1) + " column: " + (dec.col + 1) + " missing return statement");
        valid = false;
      }
      indent(level + 1);
      System.out.println("Leaving the scope for function f: " + dec.func);
    }
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
