  0:    LD 6, 0(0)	load gp with maxaddress
  1:   LDA 5, 0(6)	copy gp to fp
  2:    ST 0, 0(0)	clear location 0
* begin the input and output routines
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
* Begin function: seven
 12:    ST 0, -1(5)	store return address
* Code inside function
 13:    LD 7, -1(5)	load return address, leaving: seven
* Exit seven
 11:   LDA 7, 2(7)	jump past function: seven
* Begin function: printSeven
 15:    ST 0, -1(5)	store return address
* Code inside function
* Size of frameOffset: -3
 16:    ST 5, -5(5)	push original frame pointer
 17:   LDA 5, -5(5)	push frame
 18:   LDA 0, 1(7)	load ac with return address
 20:    LD 5, 0(5)	pop frame
 21:    ST 0, -5(5)	save data to offset location
* done with function call
 22:    ST 5, -3(5)	push original frame pointer
 23:   LDA 5, -3(5)	push frame
 24:   LDA 0, 1(7)	load ac with return address
 25:   LDA 7, -19(7)	jump to output
 26:    LD 5, 0(5)	pop frame
 27:    ST 0, -3(5)	save data to offset location
* done with function call
 28:    LD 7, -1(5)	load return address, leaving: printSeven
* Exit printSeven
 14:   LDA 7, 14(7)	jump past function: printSeven
 19:   LDA 7, 10(7)	jump to function body - with skip!
* Begin function: seven
 30:    ST 0, -1(5)	store return address
* Code inside function
* Size of frameOffset: -2
 31:   LDC 0, 7(0)	load the integer value into memory
 32:    ST 0, -2(5)	store the integer value into memory
* done storing address of integer
 33:    LD 0, -2(5)	load result of return exp into memory
 34:    LD 7, -1(5)	load return address, leaving function by return (wish I knew the name)
 35:    LD 7, -1(5)	load return address, leaving: seven
* Exit seven
 29:   LDA 7, 6(7)	jump past function: seven
* Begin function: main
 37:    ST 0, -1(5)	store return address
* Code inside function
* Size of frameOffset: -3
 38:   LDA 0, -2(5)	load the address for a variable
 39:    ST 0, -4(5)	store the address for a variable
* done loading and storing addresses for lhs of assignment
 40:   LDC 0, 5(0)	load the integer value into memory
 41:    ST 0, -5(5)	store the integer value into memory
* done storing address of integer
 42:    LD 0, -4(5)	load assignment location into memory
 43:    LD 1, -5(5)	load assignment value into memory
 44:    ST 1, 0(0)	store value to location of variable loaded in memory
 45:    ST 1, -3(5)	store value to result of equals operation
* done assigning value to variable
 46:    LD 0, -2(5)	load the value of a simple variable
 47:    ST 0, -4(5)	store the value of a simple variable
* done loading the value of a simple variable
 48:   LDC 0, 0(0)	load the integer value into memory
 49:    ST 0, -5(5)	store the integer value into memory
* done storing address of integer
 50:    LD 0, -4(5)	load stored value of opExp lhs
 51:    LD 1, -5(5)	load stored value of opExp rhs
 52:   SUB 0, 0, 1	subtract ac1 from ac, getting difference
 54:   LDC 0, 1(0)	case for true, load 1
 56:   LDC 0, 0(0)	case for false, load 0
 53:   JLT 0, 2(7)	jump to the false case
 55:   LDA 7, 1(7)	jump around the false case
 57:    ST 0, -3(5)	store result of opExp in memory
* done an opExp
 58:    LD 0, -3(5)	load stored value of ifExp test
* Size of frameOffset: -3
 60:   LDA 0, -2(5)	load the address for a variable
 61:    ST 0, -4(5)	store the address for a variable
* done loading and storing addresses for lhs of assignment
 62:    LD 0, -2(5)	load the value of a simple variable
 63:    ST 0, -6(5)	store the value of a simple variable
* done loading the value of a simple variable
 64:   LDC 0, 1(0)	load the integer value into memory
 65:    ST 0, -7(5)	store the integer value into memory
* done storing address of integer
 66:    LD 0, -6(5)	load stored value of opExp lhs
 67:    LD 1, -7(5)	load stored value of opExp rhs
 68:   SUB 0, 0, 1	subtract opExp arguments
 69:    ST 0, -5(5)	store result of opExp in memory
* done an opExp
 70:    LD 0, -4(5)	load assignment location into memory
 71:    LD 1, -5(5)	load assignment value into memory
 72:    ST 1, 0(0)	store value to location of variable loaded in memory
 73:    ST 1, -3(5)	store value to result of equals operation
* done assigning value to variable
 74:    LD 0, -2(5)	load the value of a simple variable
 75:    ST 0, -5(5)	store the value of a simple variable
* done loading the value of a simple variable
 76:    ST 5, -3(5)	push original frame pointer
 77:   LDA 5, -3(5)	push frame
 78:   LDA 0, 1(7)	load ac with return address
 79:   LDA 7, -73(7)	jump to output
 80:    LD 5, 0(5)	pop frame
 81:    ST 0, -3(5)	save data to offset location
* done with function call
 59:   JEQ 0, 23(7)	if false, jump outside the loop
 82:   LDA 7, -37(7)	otherwise, jump back to the start of the loop
 83:    LD 7, -1(5)	load return address, leaving: main
* Exit main
 36:   LDA 7, 47(7)	jump past function: main
 84:    ST 5, 0(5)	push ofp
 85:   LDA 5, 0(5)	push frame
 86:   LDA 0, 1(7)	load ac with ret addr
 87:   LDA 7, -51(7)	jump to main loc
 88:    LD 5, 0(5)	pop frame
 89:  HALT 0, 0, 0	halt
