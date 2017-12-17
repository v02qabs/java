bits 16
org 100h

global start

start:
	;input key
	mov ah,1
	int 21h
	sub al, '0'
	cmp al, 1
	
	je Conversion
	
	;loop start

Conversion:
	mov al, 5
	add al, 5
	int 21h
	
	mov ah,09
	mov dx,hello
	int 21h
	
	mov ah,09
	mov dl,al
	int 21h
	
	jmp end 

end:
	mov ah,4ch
	mov al,0
	int 21h
	

hello db "Hello", 0dh,0ah,'$'