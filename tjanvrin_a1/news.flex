/*
  File Name: tiny.flex
  JFlex specification for the TINY language
  I got this code from the warmup package in the courselink.
  The original code was written by Dr. Fei Song, and was used with permission.
*/

import java.util.ArrayList;
   
%%
   
%class Lexer
%type Token
%line
%column
    
%eofval{
  //System.out.println("*** Reaching end of file");
  // prints an error if there are tags still on the stack.
   if (tagStack.size() > 0){
      System.err.println("Error: end of file reached but not all tags matched.\n");
      System.err.println("Tags still on stack:\n");
      for(int i = 0; i < tagStack.size(); i++){
         System.err.println(tagStack.get(i) + "\n");   
      }
   }

  return null;
%eofval};

%{

   // global stack to hold elements as they are added
  private static ArrayList<String> tagStack = new ArrayList<String>();
  // variable to keep track of how many "ignore-type" tags are on the stack
  // we only generate tokens if this is zero (but we still keep track of the stack)
  private static int filterCount = 0;

   /* Returns true if we should be ignoring text.*/
   private static boolean ignoring(){
      return filterCount > 0;
   }


   /* Peek operation for the tag stack. Returns empty string if the stack is empty.*/
  private static String peek(){
      if(tagStack.size() == 0){
         return "";
      }
      else{
         return tagStack.get(tagStack.size() - 1);
      }
  }

   /* Pop operation for the tag stack. Returns empty string if the stack is empty.*/
  private static String pop(){
      if(tagStack.size() == 0){
         return "";
      }
      else{
         String returnval = tagStack.get(tagStack.size() - 1);
         tagStack.remove(tagStack.size() - 1);
         return returnval;
      }
  }

   /* Push operation for the tag stack. */ 
  private static void push(String name){
      tagStack.add(name);
  }

   // feels slightly better to declare this string this way. less "magic number-ey"
   final static String TAGNAMECHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-";

   /* Since tag names can contain hyphens, I need to define this function to check if characters are valid. */
  private static boolean isTagNameChar(char c){
      return TAGNAMECHARS.indexOf(c) >= 0;
  }
  
  /* Extracts the tag name from strings matching the tag regex. We can assume that input
  into this function is formatted as a tag, becasue it will only be called after matching to a regex.*/
  private static String getTagName(String tag){
      String name = "";

      int start = 0;
      int end = 0;

      boolean reading = false;

      for(int i = 0; i < tag.length(); i++){
         if(!reading){
            if(isTagNameChar(tag.charAt(i))){ // read in characters until we see a tagname character
               reading = true; // we start reading, and read until we hit a non-tagname character
               name = name + tag.charAt(i);
            }
         }
         else{
            if(!isTagNameChar(tag.charAt(i))){
               break; // we've seen all the tag name characters, and have hit whitespace or >
            }
            else{
               name = name + tag.charAt(i); // otherwi
            }
         }
      }
      return name.toUpperCase();
  }

   // checks to see if two tags have the same name
  private static boolean compareTagNames(String name1, String name2){
      return false;
  }

  // static method such as getTagName can be defined here as well
%};

/* A line terminator is a \r (carriage return), \n (line feed), or
   \r\n. This has been deprecated. */
// LineTerminator = \r|\n|\r\n
   
/* White space is a line terminator, space, tab, or form feed. */

ws = \s
// ws = {LineTerminator} | [ \t\f] // using a short form for whitespace, cause I might want to use it a lot. // leaving this for reference
   
/* A literal integer is is a number beginning with a number between
   one and nine followed by zero or more numbers between zero and nine
   or just a zero.  */
digit = [0-9]
number = {digit}+
   
/* A letter is anything from A-Z or a-z*/
letter = [a-zA-Z]
/* Words can have numbers in them. */
letternumber = [a-zA-Z0-9]
word = {letternumber}+

/* A tagname can contain numbers, uppercase, and lowercase letters, and dashes. */

tagnamechar = [-a-zA-Z0-9]
tagname = {tagnamechar}+

/* An attribute-value pair is complex. I'm going to assume that the initial value can be any word
, but it has to start with a letter and that the bit in the value can be any string */

attvalpair = {letter}{word}*=\".*\"


   
%%
   
/*
   This section contains regular expressions and actions, i.e. Java
   code, that will be executed when the scanner matches the associated
   regular expression. */

   // put the new stuff here
  
(<{ws}*{tagname}({ws}*{attvalpair})*{ws}*>) { 
      String name = getTagName(yytext());

      push(name);

         // System.out.println(name + "\n");
      if(name.equals("DOC")){
         if(!ignoring()){
            return new Token(Token.OPEN_DOC, yytext(), yyline, yycolumn);
         } 
      }
      else if(name.equals("TEXT")){
         if(!ignoring()){
            return new Token(Token.OPEN_TEXT, yytext(), yyline, yycolumn);
         }    
      }
      else if(name.equals("DATE")){
         if(!ignoring()){
            return new Token(Token.OPEN_DATE, yytext(), yyline, yycolumn);
         }    
         
      }
      else if(name.equals("DOCNO")){
         if(!ignoring()){
            return new Token(Token.OPEN_DOCNO, yytext(), yyline, yycolumn);
         }    
         
         
      }
      else if(name.equals("HEADLINE")){
         if(!ignoring()){
            return new Token(Token.OPEN_HEADLINE, yytext(), yyline, yycolumn);
         }    
         
      }
      else if(name.equals("LENGTH")){
         
         if(!ignoring()){
            return new Token(Token.OPEN_LENGTH, yytext(), yyline, yycolumn);
         }    
      }
      else if(name.equals("P")){
         if(!ignoring()){
            return new Token(Token.OPEN_P, yytext(), yyline, yycolumn);
         }    
         
      }
      else{
         filterCount++; // if we're adding an irrelevant tag, filter count increases
         if(!ignoring()){
            return new Token(Token.OPEN_TAG, yytext(), yyline, yycolumn);
         }
      }

      
                                                                      }
(<{ws}*"/"{ws}*{tagname}({ws}*{attvalpair})*{ws}*>)                   { 
      String name = getTagName(yytext());

      String stackname = peek();

      if(stackname.equals(name)){
         pop(); // pop the name of the stack and continue as normal
         if(name.equals("DOC")){
            if(!ignoring()){
               return new Token(Token.CLOSE_DOC, yytext(), yyline, yycolumn);
            }         
         }
         else if(name.equals("TEXT")){
            if(!ignoring()){
               return new Token(Token.CLOSE_TEXT, yytext(), yyline, yycolumn);
            }       
            
         }
         else if(name.equals("DATE")){
            if(!ignoring()){
               return new Token(Token.CLOSE_DATE, yytext(), yyline, yycolumn);
            }       
            
         }
         else if(name.equals("DOCNO")){
            if(!ignoring()){
               return new Token(Token.CLOSE_DOCNO, yytext(), yyline, yycolumn);
            }       
            
         }
         else if(name.equals("HEADLINE")){
            if(!ignoring()){
               return new Token(Token.CLOSE_HEADLINE, yytext(), yyline, yycolumn);
            }       
            
         }
         else if(name.equals("LENGTH")){
            if(!ignoring()){
               return new Token(Token.CLOSE_LENGTH, yytext(), yyline, yycolumn);
            }       
            
         }
         else if(name.equals("P")){
            if(!ignoring()){
               return new Token(Token.CLOSE_P, yytext(), yyline, yycolumn);
            }    
            
         }
         else{
            // if it doesn't match any of the relevant tags, we know we're removing an IRRELEVANT tag.
            if(!ignoring()){
               return new Token(Token.CLOSE_TAG, yytext(), yyline, yycolumn);
            }    
            
            filterCount--; // if we're closing an irrelevant tag, we can reduce the filter count.
         }
      }
      else{
         // if the name doesn't match, add an error token and don't pop.
         // do this even if tokens are being filtered
         System.err.println("ERROR: closing tag " + yytext() + " doesn't match. Skipping.\n");
         // return new Token(Token.ERROR, yytext(), yyline, yycolumn);
      }
}

// there's a special case here for number - that's to recognize numbers of the form .1234, which in my opinion is a valid number 
(("+"|"-")?{number}+("."{number}+)?)|(("+"|"-")?"."{number}+)                 { if (!ignoring()){return new Token(Token.NUMBER, yytext(), yyline, yycolumn);} }

// this is long and complicated to deal with the fact that you could have things like -word- or word-word-, but not word---word
'?{word}'({word}')*({word}'|{word})?|('{word}|{word})?('{word})*'{word}'?     { if(!ignoring()){return new Token(Token.APOSTROPHIZED, yytext(), yyline, yycolumn);} }
-?{word}-({word}-)*({word}-|{word})?|(-{word}|{word})?(-{word})*-{word}-?     { if(!ignoring()){return new Token(Token.HYPHENATED, yytext(), yyline, yycolumn); }}
{word}                                                                        {if(!ignoring()){ return new Token(Token.WORD, yytext(), yyline, yycolumn); }} 
// putting word after number, because words can have numbers in but numbers can't have letters
[^\w\s]                                                                      { if(!ignoring()){ return new Token(Token.PUNCTUATION, yytext(), yyline, yycolumn); }}
{ws}+                                                                         { /* skip whitespace */ }   
// "{"[^\}]*"}"       { /* skip comments */ } // don't skip comments
.                                                                             {if(!ignoring()){ return new Token(Token.ERROR, yytext(), yyline, yycolumn); }} // all other characters are ERRORs
