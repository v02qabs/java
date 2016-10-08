bits 16

org 100h

global start

start:
	mov ah,02
	mov dl,'A'
	int 21h

	jmp hello

hello:
	mov ah,02
	mov dl,'b'
	int 21h

end:
	mov ah,4ch
	mov al,0
	int 21h
