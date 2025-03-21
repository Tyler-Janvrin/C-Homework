/*
  Created by: Fei Song
  File Name: Main.java
  To Build: 
  After the Scanner.java, tiny.flex, and tiny.cup have been processed, do:
    javac Main.java
  
  To Run: 
    java -classpath /usr/share/java/cup.jar:. Main gcd.tiny

  where gcd.tiny is an test input file for the tiny language.
*/
   
import java.io.*;
import absyn.*;
   
class CM {
  public final static boolean SHOW_TREE = true;
  static public void main(String argv[]) {    
    /* Start the parser */

    if(argv.length < 2){
      System.out.print("Please add an argument to specify output type: -a, -s, or -c");
      return;
    }

    if(argv[1].equals("-a")){
      String fileName = argv[0];
      if(!fileName.endsWith(".cm")){
        System.out.print("Invalid filename: must end with .cm");
        return;
      }
      String fileFront = fileName.substring(0, fileName.length() - 3);

      try {
        parser p = new parser(new Lexer(new FileReader(argv[0])));
        Absyn result = (Absyn)(p.parse().value);  
        PrintStream filePrintStream = new PrintStream(new File(fileFront + ".abs"));    
        PrintStream console = System.out;
        System.setOut(filePrintStream);
        if (SHOW_TREE && result != null) {
          System.out.println("The abstract syntax tree is:");
           
           AbsynVisitor visitor = new ShowTreeVisitor();
           result.accept(visitor, 0); 
           
        }
        System.setOut(console);
      } catch (Exception e) {
        /* do cleanup here -- possibly rethrow e */
        e.printStackTrace();
      }
    }
    else if(argv[1].equals("-s")){
      // System.out.println("Still working on this code...");
      String fileName = argv[0];
      if(!fileName.endsWith(".cm")){
        System.out.print("Invalid filename: must end with .cm");
        return;
      }
      String fileFront = fileName.substring(0, fileName.length() - 3);

      try {
        parser p = new parser(new Lexer(new FileReader(argv[0])));
        Absyn result = (Absyn)(p.parse().value);  
        PrintStream filePrintStream = new PrintStream(new File(fileFront + ".sym"));    
        PrintStream console = System.out;
        System.setOut(filePrintStream);
        if (SHOW_TREE && result != null && p.valid == true) {
           SemanticAnalyzer visitor = new SemanticAnalyzer();
           visitor.insertSystemFunctions();
           result.accept(visitor, 0); 
           visitor.checkLastWasMain();
        }
        System.setOut(console);
      } catch (Exception e) {
        /* do cleanup here -- possibly rethrow e */
        e.printStackTrace();
      }
    }
    else if(argv[1].equals("-c")){
      System.out.println("Sorry, the .tm code isn't implemented yet.");
    }
  }
}
