  0:    LD 6, 0(0)	load gp with maxaddress
  1:   LDA 5, 0(6)	copy gp to fp
  2:    ST 0, 0(0)	clear location 0
* Hello again from visit!
  3:    ST 5, 0(5)	push ofp
  4:   LDA 5, 0(5)	push frame
  5:   LDA 0, 1(7)	load ac with ret addr
  6:   LDA 7, 111(7)	jump to main loc
  7:    LD 5, 0(5)	pop frame
  8:  HALT 0, 0, 0	halt
