  0:    LD 6, 0(0)	load gp with maxaddress
  1:   LDA 5, 0(6)	copy gp to fp
  2:    ST 0, 0(0)	clear location 0
* Begin function: main
  4:    ST 0, -1(5)	store return address
* Code inside function
  5:   LDC 0, 42(0)	load an arbitrary number to see if it works
* Size of frameOffset: -3
  6:   LDA 0, -3(5)	load the variable storing address
  7:   LDA 0, -4(5)	load the variable storing address
  8:    LD 7, -1(5)	load the address stored when we entered function: main
* Exit main
  3:   LDA 7, 5(7)	jump past function: main
  9:    ST 5, 0(5)	push ofp
 10:   LDA 5, 0(5)	push frame
 11:   LDA 0, 1(7)	load ac with ret addr
 12:   LDA 7, -9(7)	jump to main loc
 13:    LD 5, 0(5)	pop frame
 14:  HALT 0, 0, 0	halt
