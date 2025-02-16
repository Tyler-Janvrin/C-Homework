I made use of the provided C1-Package files as a starting point for this assignment.

To test source code like "yourfile.cm", type 
  "java -cp /usr/share/java/cup.jar:. Main yourfile.cm" 

To test just the scanner, type:
  "cat yourfile.cm | java -cp /usr/share/java/cup.jar:. Scanner"

In my implementation of comments, they can't be nested. Trying to nest them will result in an error. I hope that's okay.
