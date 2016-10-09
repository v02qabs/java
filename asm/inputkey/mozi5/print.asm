test:
	mov ah,09
	mov dx,hello
	int 21h
	
	end:
	mov ah,4ch
	mov al,0
	int 21h
	
	
hello db "Go.", 0dh, 0ah, '$'