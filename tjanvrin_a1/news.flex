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
      return "";
  }

   // checks to see if two tags have the same name
  private static boolean compareTagNames(String name1, String name2){
      return false;
  }

  // static method such as getTagName can be defined here as well
%};

/* A line terminator is a \r (carriage return), \n (line feed), or
   \r\n. */
LineTerminator = \r|\n|\r\n
   
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
/* Words can have letters in them, so */
letternumber = [a-zA-Z0-9]
word = {letternumber}+

/* A tagname can contain numbers, uppercase, and lowercase letters, and dashes */

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
  
(<{ws}*{tagname}({ws}*{attvalpair})*{ws}*>)                    { return new Token(Token.OPEN_TAG, yytext(), yyline, yycolumn); }
(<{ws}*"/"{ws}*{tagname}({ws}*{attvalpair})*{ws}*>)                   { return new Token(Token.CLOSE_TAG, yytext(), yyline, yycolumn); }
/*
Commenting these out while I work
"<DOC>"                    { return new Token(Token.OPEN_DOC, yytext(), yyline, yycolumn); }
"</DOC>"                   { return new Token(Token.CLOSE_DOC, yytext(), yyline, yycolumn); }
"<TEXT>"                   { return new Token(Token.OPEN_TEXT, yytext(), yyline, yycolumn); }
"</TEXT>"                  { return new Token(Token.CLOSE_TEXT, yytext(), yyline, yycolumn); }
"<DATE>"                   { return new Token(Token.OPEN_DATE, yytext(), yyline, yycolumn); }
"</DATE>"                  { return new Token(Token.CLOSE_DATE, yytext(), yyline, yycolumn); }
"<DOCNO>"                  { return new Token(Token.OPEN_DOCNO, yytext(), yyline, yycolumn); }
"</DOCNO>"                 { return new Token(Token.CLOSE_DOCNO, yytext(), yyline, yycolumn); }
"<HEADLINE>"               { return new Token(Token.OPEN_HEADLINE, yytext(), yyline, yycolumn); }
"</HEADLINE>"              { return new Token(Token.CLOSE_HEADLINE, yytext(), yyline, yycolumn); }
"<LENGTH>"                 { return new Token(Token.OPEN_LENGTH, yytext(), yyline, yycolumn); }
"</LENGTH>"                { return new Token(Token.CLOSE_LENGTH, yytext(), yyline, yycolumn); }
*/


// there's a special case here for number - that's to recognize numbers of the form .1234, which in my opinion is a valid number 
(("+"|"-")?{number}+("."{number}+)?)|(("+"|"-")?"."{number}+)           { return new Token(Token.NUMBER, yytext(), yyline, yycolumn); }
({letter}*"'")+({letter}*)                   { return new Token(Token.APOSTROPHIZED, yytext(), yyline, yycolumn); }
({letter}*"-")+({letter}*)                   { return new Token(Token.HYPHENATED, yytext(), yyline, yycolumn); }
{word}                             { return new Token(Token.WORD, yytext(), yyline, yycolumn); } 
// putting word after number, because words can have numbers in but numbers can't have letters
[^\w\s]+                                     { return new Token(Token.PUNCTUATION, yytext(), yyline, yycolumn); }
{ws}+                                        { /* skip whitespace */ }   
// "{"[^\}]*"}"       { /* skip comments */ } // don't skip comments
.                                            { return new Token(Token.ERROR, yytext(), yyline, yycolumn); } // all other characters are ERRORs.
