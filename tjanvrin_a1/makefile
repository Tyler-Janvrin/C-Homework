JAVAC=javac
#JFLEX=jflex
JFLEX=jflex

all: Token.class Lexer.class Scanner.class

%.class: %.java
	$(JAVAC) $^

Lexer.java: news.flex
	$(JFLEX) news.flex

clean:
	rm -f Lexer.java *.class *~
