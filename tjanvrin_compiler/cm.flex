/*
  Created By: Fei Song
  File Name: cm.flex
  To Build: jflex cm.flex

  and then after the parser is created
    javac Lexer.java
*/
   
/* --------------------------Usercode Section------------------------ */
   
import java_cup.runtime.*;
      
%%
   
/* -----------------Options and Declarations Section----------------- */
   
/* 
   The name of the class JFlex will create will be Lexer.
   Will write the code to the file Lexer.java. 
*/
%class Lexer

%eofval{
  return null;
%eofval};

/*
  The current line number can be accessed with the variable yyline
  and the current column number with the variable yycolumn.
*/
%line
%column
    
/* 
   Will switch to a CUP compatibility mode to interface with a CUP
   generated parser.
*/
%cup
   
/*
  Declarations
   
  Code between %{ and %}, both of which must be at the beginning of a
  line, will be copied letter to letter into the lexer class source.
  Here you declare member variables and functions that are used inside
  scanner actions.  
*/
%{   
    /* To create a new java_cup.runtime.Symbol with information about
       the current token, the token will have no value in this
       case. */
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    /* Also creates a new java_cup.runtime.Symbol with information
       about the current token, but this object has a value. */
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}
   

/*
  Macro Declarations
  
  These declarations are regular expressions that will be used latter
  in the Lexical Rules Section.  
*/

/* we're defining a comment state */ 

%state COMMENT
   
/* A line terminator is a \r (carriage return), \n (line feed), or
   \r\n. */
LineTerminator = \r|\n|\r\n
   
/* White space is a line terminator, space, tab, or form feed. */
WhiteSpace     = {LineTerminator} | [ \t\f]

// whitespace is fine - looks like I don't need to change it
   
/* A literal integer is is a number beginning with a number between
   one and nine followed by zero or more numbers between zero and nine
   or just a zero.  */
digit = [0-9]
number = {digit}+

// integer also looks fine
   
/* A identifier consists of upper and lowercase letters, numbers, and underscores,
but can't start with a number */
identifier = [_a-zA-Z][_a-zA-Z0-9]*
   
%%
/* ------------------------Lexical Rules Section---------------------- */
   
/*
   This section contains regular expressions and actions, i.e. Java
   code, that will be executed when the scanner matches the associated
   regular expression. */

/* Keywords */
<YYINITIAL> "bool"                { return symbol(sym.BOOL);}
<YYINITIAL> "else"                { return symbol(sym.ELSE);}
<YYINITIAL> "if"                  { return symbol(sym.IF);}
<YYINITIAL> "int"                 { return symbol(sym.INT);}
<YYINITIAL> "return"              { return symbol(sym.RETURN);}
<YYINITIAL> "void"                { return symbol(sym.VOID);}
<YYINITIAL> "while"               { return symbol(sym.WHILE);}
<YYINITIAL> "false"               { return symbol(sym.FALSE);}
<YYINITIAL> "true"                { return symbol(sym.TRUE);}

/* Special Symbols */
<YYINITIAL> "+"                   { return symbol(sym.PLUS);}
<YYINITIAL> "-"                   { return symbol(sym.MINUS);}
<YYINITIAL> "*"                   { return symbol(sym.TIMES);}
<YYINITIAL> "/"                   { return symbol(sym.DIV);}
<YYINITIAL> "<"                   { return symbol(sym.LT);}
<YYINITIAL> "<="                  { return symbol(sym.LE);}
<YYINITIAL> ">"                   { return symbol(sym.GT);}
<YYINITIAL> ">="                  { return symbol(sym.GE);}
<YYINITIAL> "=="                  { return symbol(sym.EQ);}
<YYINITIAL> "!="                  { return symbol(sym.NE);}
<YYINITIAL> "~"                   { return symbol(sym.BITNOT);}
<YYINITIAL> "||"                  { return symbol(sym.OR);}
<YYINITIAL> "&&"                  { return symbol(sym.AND);}
<YYINITIAL> "="                   { return symbol(sym.ASSIGN);}
<YYINITIAL> ";"                   { return symbol(sym.SEMI);}
<YYINITIAL> ","                   { return symbol(sym.COMMA);}
<YYINITIAL> "("                   { return symbol(sym.LP);}
<YYINITIAL> ")"                   { return symbol(sym.RP);}
<YYINITIAL> "["                   { return symbol(sym.LB);}
<YYINITIAL> "]"                   { return symbol(sym.RB);}
<YYINITIAL> "{"                   { return symbol(sym.LC);}
<YYINITIAL> "}"                   { return symbol(sym.RC);}


/* Tokens*/
<YYINITIAL> {number}              { return symbol(sym.NUM, yytext()); }
<YYINITIAL> {identifier}          { return symbol(sym.ID, yytext()); }

/* Comments and whitespace*/ 
<YYINITIAL> "/*"                  { yybegin(COMMENT); }
<YYINITIAL> {WhiteSpace}+         { /* skip whitespace */ }   
         
<YYINITIAL> .                     { return symbol(sym.ERROR, yytext()); }
<COMMENT> "*/"                    { yybegin(YYINITIAL); }
<COMMENT> [^]|\n                    { /* skip comments */ }

