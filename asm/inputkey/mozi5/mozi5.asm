bits 16
org 0x100
global main
main:
	;welcome"
	mov ah,9
	mov dx,welcome
	int 21h
	
	jmp test
	

welcome db "OK.",0dh,0ah,'$'

%include "print.asm"