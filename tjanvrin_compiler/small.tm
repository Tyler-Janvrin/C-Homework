  0:    LD 6, 0(0)	load gp with maxaddress
  1:   LDA 5, 0(6)	copy gp to fp
  2:    ST 0, 0(0)	clear location 0
  4:    ST 0, -1(5)	store return
  5:    IN 0, 0, 0	input
  6:    LD 7, -1(5)	return to caller
* that's the input routine
  7:    ST 0, -1(5)	store return
  8:    LD 0, -2(5)	load output value
  9:   OUT 0, 0, 0	output
 10:    LD 7, -1(5)	return to caller
* that's the output routine
  3:   LDA 7, 7(7)	skip to the end of io
* Begin function: main
 12:    ST 0, -1(5)	store return address
* Code inside function
 13:   LDC 0, 42(0)	load an arbitrary number to see if it works
* Size of frameOffset: -4
 14:   LDA 0, -3(5)	load the address for a variable
 15:    ST 0, -5(5)	store the address for a variable
* done loading and storing addresses for lhs of assignment
 16:   LDC 0, 100(0)	load the integer value into memory
 17:    ST 0, -6(5)	store the integer value into memory
* done storing address of integer
 18:    LD 0, -5(5)	load assignment location into memory
 19:    LD 1, -6(5)	load assignment value into memory
 20:    ST 1, 0(0)	store value to location of variable loaded in memory
 21:    ST 1, -4(5)	store value to result of equals operation
* done assigning value to variable
 22:   LDA 0, -2(5)	load the address for a variable
 23:    ST 0, -5(5)	store the address for a variable
* done loading and storing addresses for lhs of assignment
 24:   LDC 0, 200(0)	load the integer value into memory
 25:    ST 0, -8(5)	store the integer value into memory
* done storing address of integer
 26:   LDC 0, 250(0)	load the integer value into memory
 27:    ST 0, -9(5)	store the integer value into memory
* done storing address of integer
 28:    LD 0, -8(5)	load stored value of opExp lhs
 29:    LD 1, -9(5)	load stored value of opExp rhs
 30:   ADD 0, 0, 1	add opExp arguments
 31:    ST 0, -7(5)	store result of opExp in memory
* done an opExp
 32:   LDC 0, 50(0)	load the integer value into memory
 33:    ST 0, -8(5)	store the integer value into memory
* done storing address of integer
 34:    LD 0, -7(5)	load stored value of opExp lhs
 35:    LD 1, -8(5)	load stored value of opExp rhs
 36:   SUB 0, 0, 1	subtract opExp arguments
 37:    ST 0, -6(5)	store result of opExp in memory
* done an opExp
 38:    LD 0, -5(5)	load assignment location into memory
 39:    LD 1, -6(5)	load assignment value into memory
 40:    ST 1, 0(0)	store value to location of variable loaded in memory
 41:    ST 1, -4(5)	store value to result of equals operation
* done assigning value to variable
 42:    LD 7, -1(5)	load the address stored when we entered function: main
* Exit main
 11:   LDA 7, 31(7)	jump past function: main
 43:    ST 5, 0(5)	push ofp
 44:   LDA 5, 0(5)	push frame
 45:   LDA 0, 1(7)	load ac with ret addr
 46:   LDA 7, -35(7)	jump to main loc
 47:    LD 5, 0(5)	pop frame
 48:  HALT 0, 0, 0	halt
