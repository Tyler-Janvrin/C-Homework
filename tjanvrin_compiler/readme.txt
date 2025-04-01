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

For the 0-9 example files I've included in this project, I've re-used some of the files I generated for previous projects for numbers 4-8.
I hope that's okay; I didn't feel like it made sense to rewrite them, since I already have perfectly good semantic and syntactic tests from checkpoints 1 and 2. 
If it turns out that this is a problem, sorry!
