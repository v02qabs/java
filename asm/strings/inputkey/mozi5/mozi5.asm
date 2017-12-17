bits 16
org 100h

global start

start:
	mov ah,9
	mov dx,hello
	int 21h
	
	jmp mozi
mozi:
	jmp print

hello db "Hello", 0dh,0ah,'$'
%include "print.asm"