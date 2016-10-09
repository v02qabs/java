%include "print.asm"


main:
	;mov ah,02
	;mov al,31h
	;int 21h
	
	jmp print
	
	;mov ah,4ch
	;mov al,0
	;int 21h