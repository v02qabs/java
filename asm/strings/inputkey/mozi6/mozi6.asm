bits 16
org 0x100

global start

start:
	mov ah,02
	mov dl, 41h
	int 21h
	
	mov ah,4ch
	mov al,0
	int 21h
	
	