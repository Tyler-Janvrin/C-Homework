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
  return null;
%eofval};

%{
  private static ArrayList<String> tagStack = new ArrayList<String>();
  
  // extracts the tag name out of the tag string
  private static String getTagName(String tag){

  }

   // checks to see if two tags have the same name
  private static boolean compareTagNames(String name1, String name2){

  }

  // static method such as getTagName can be defined here as well
%};

/* A line terminator is a \r (carriage return), \n (line feed), or
   \r\n. */
LineTerminator = \r|\n|\r\n
   
/* White space is a line terminator, space, tab, or form feed. */
WhiteSpace     = {LineTerminator} | [ \t\f]
   
/* A literal integer is is a number beginning with a number between
   one and nine followed by zero or more numbers between zero and nine
   or just a zero.  */
digit = [0-9]
number = {digit}+
   
/* A identifier integer is a word beginning a letter between A and
   Z, a and z, or an underscore followed by zero or more letters
   between A and Z, a and z, zero and nine, or an underscore. */
letter = [a-zA-Z]
identifier = {letter}+
   
%%
   
/*
   This section contains regular expressions and actions, i.e. Java
   code, that will be executed when the scanner matches the associated
   regular expression. */

   // put the new stuff here
  
"<TAG>"                    { return new Token(Token.OPEN_TAG, yytext(), yyline, yycolumn); }
"</TAG>"                   { return new Token(Token.CLOSE_TAG, yytext(), yyline, yycolumn); }
"<DOC>"                    { return new Token(Token.OPEN_DOC, yytext(), yyline, yycolumn); }
"</DOC>"                   { return new Token(Token.CLOSE_DOC, yytext(), yyline, yycolumn); }
"<TEXT>"                   { return new Token(Token.OPEN_TEXT, yytext(), yyline, yycolumn); }
"</TEXT>"                  { return new Token(Token.CLOSE_TEXT, yytext(), yyline, yycolumn); }
"<DATE>"                   { return new Token(Token.OPEN_DATE, yytext(), yyline, yycolumn); }
"</DATE>"                  { return new Token(Token.CLOSE_DATE, yytext(), yyline, yycolumn); }
"<DOCNO>"                  { return new Token(Token.OPEN_DOCNO, yytext(), yyline, yycolumn); }
"</DOCNO>"                 { return new Token(Token.CLOSE_DOCNO, yytext(), yyline, yycolumn); }
"<HEADLINE>"               { return new Token(Token.OPEN_HEADLINE, yytext(), yyline, yycolumn); }
"</HEADLINE>"                { return new Token(Token.CLOSE_HEADLINE, yytext(), yyline, yycolumn); }
"<LENGTH>"                { return new Token(Token.OPEN_LENGTH, yytext(), yyline, yycolumn); }
"</LENGTH>"                { return new Token(Token.CLOSE_LENGTH, yytext(), yyline, yycolumn); }
{letter}+                { return new Token(Token.WORD, yytext(), yyline, yycolumn); }
{number}                { return new Token(Token.NUMBER, yytext(), yyline, yycolumn); }
({letter}*"'")+({letter}*)               { return new Token(Token.APOSTROPHIZED, yytext(), yyline, yycolumn); }
({letter}*"-")+({letter}*)                 { return new Token(Token.HYPHENATED, yytext(), yyline, yycolumn); }
[^\w\s]+              { return new Token(Token.PUNCTUATION, yytext(), yyline, yycolumn); }
{WhiteSpace}+      { /* skip whitespace */ }   
// "{"[^\}]*"}"       { /* skip comments */ } // don't skip comments
.                  { return new Token(Token.ERROR, yytext(), yyline, yycolumn); }
