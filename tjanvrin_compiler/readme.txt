I made use of the provided C1-Package files as a starting point for this assignment.

To bulid the project, type "make". To clean up generated files, type "make clean". This will also remove .abs files.

To test source code like "yourfile.cm", type 
  "java -cp /usr/share/java/cup.jar:. CM yourfile.cm -a" 

To test just the scanner, type:
  "cat yourfile.cm | java -cp /usr/share/java/cup.jar:. Scanner"

I've provided the following test files, as requested: 1.cm, 2.cm, 3.cm, 4.cm, 5.cm

In my implementation of comments, they can't be nested. Trying to nest them will result in an error. I hope that's okay.
