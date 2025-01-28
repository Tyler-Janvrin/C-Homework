This assignment makes use of the example code provided by Dr. Fei Song on the Courselink site: tiny.flex, Scanner.java, and Token.java

To compile the code, run "make". You might have to change the filepath to jflex in the makefile. Right now, it's just set to "JFLEX=jflex", which works on the SOCS server. 
I added a target called cleanw that's the same as clean, but with del instead of rm so I can develop on windows.

To run the code, run "java Scanner < yourfile.txt", where yourfile.txt can be whatever file you want the scanner to run on.

On windows, "cat yourfile.txt | java Scanner > outputfile.txt" seems to work.

-

For HYPHENATED and APOSTROPHIZED, I'm making the assumption that things like -dog or 'em are valid apostrophized words, because they are. I could have chosen differently, but this is what I chose to do.