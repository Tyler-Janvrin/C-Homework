I made use of the provided C1-Package files as a starting point for this assignment.

To bulid the project, type "make". To clean up generated files, type "make clean". This will also remove .abs files.

To test source code like "yourfile.cm", type 
  "java -cp /usr/share/java/cup.jar:. CM yourfile.cm -c" 

This will create a file called "yourfile.tm" containing the executable tinymachine code.

To test just the scanner, type:
  "cat yourfile.cm | java -cp /usr/share/java/cup.jar:. Scanner"

To test just the parser, type:
  "java -cp /usr/share/java/cup.jar:. CM yourfile.cm -a" 

To test just the semantic analyzer, type:
  "java -cp /usr/share/java/cup.jar:. CM yourfile.cm -s" 

I've provided the required test files, as requested.

In my implementation of comments, they can't be nested. Trying to nest them will result in an error. I hope that's okay.


