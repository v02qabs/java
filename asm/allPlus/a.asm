bits 16
org 0x100


mov ah, 08h
int 21h

inc al
	
mov ah, 02h
mov dl, al
int 21h

mov ah,08h
int 21h


mov ah, 4ch
mov al, 0
int 21h
