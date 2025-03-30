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
* Begin function: eight
 15:    ST 0, -1(5)	store return address
* Code inside function
 16:    LD 7, -1(5)	load return address, leaving: eight
* Exit eight
 14:   LDA 7, 2(7)	jump past function: eight
* Begin function: printSeven
 18:    ST 0, -1(5)	store return address
* Code inside function
* Size of frameOffset: -3
 19:    ST 5, -5(5)	push original frame pointer
 20:   LDA 5, -5(5)	push frame
 21:   LDA 0, 1(7)	load ac with return address
 23:    LD 5, 0(5)	pop frame
 24:    ST 0, -5(5)	save data to offset location
* done with function call
 25:    ST 5, -3(5)	push original frame pointer
 26:   LDA 5, -3(5)	push frame
 27:   LDA 0, 1(7)	load ac with return address
 28:   LDA 7, -22(7)	jump to output
 29:    LD 5, 0(5)	pop frame
 30:    ST 0, -3(5)	save data to offset location
* done with function call
 31:    LD 7, -1(5)	load return address, leaving: printSeven
* Exit printSeven
 17:   LDA 7, 14(7)	jump past function: printSeven
 22:   LDA 7, 10(7)	jump to function body - with skip!
* Begin function: seven
 33:    ST 0, -1(5)	store return address
* Code inside function
* Size of frameOffset: -2
 34:   LDC 0, 7(0)	load the integer value into memory
 35:    ST 0, -2(5)	store the integer value into memory
* done storing address of integer
 36:    LD 0, -2(5)	load result of return exp into memory
 37:    LD 7, -1(5)	load return address, leaving function by return (wish I knew the name)
 38:    LD 7, -1(5)	load return address, leaving: seven
* Exit seven
 32:   LDA 7, 6(7)	jump past function: seven
* Begin function: main
 40:    ST 0, -1(5)	store return address
* Code inside function
* Size of frameOffset: -5
 41:   LDC 0, 3(0)	load the integer value into memory
 42:    ST 0, -7(5)	store the integer value into memory
* done storing address of integer
 43:    ST 5, -5(5)	push original frame pointer
 44:   LDA 5, -5(5)	push frame
 45:   LDA 0, 1(7)	load ac with return address
 46:   LDA 7, -29(7)	jump to function body
 47:    LD 5, 0(5)	pop frame
 48:    ST 0, -5(5)	save data to offset location
* done with function call
 49:    ST 5, -5(5)	push original frame pointer
 50:   LDA 5, -5(5)	push frame
 51:   LDA 0, 1(7)	load ac with return address
 53:    LD 5, 0(5)	pop frame
 54:    ST 0, -5(5)	save data to offset location
* done with function call
 55:    LD 7, -1(5)	load return address, leaving: main
* Exit main
 39:   LDA 7, 16(7)	jump past function: main
 56:    ST 5, 0(5)	push ofp
 57:   LDA 5, 0(5)	push frame
 58:   LDA 0, 1(7)	load ac with ret addr
 59:   LDA 7, -20(7)	jump to main loc
 60:    LD 5, 0(5)	pop frame
 61:  HALT 0, 0, 0	halt
