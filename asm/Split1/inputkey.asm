addMethod:
	mov al,5
	add al,5
	
	;al = 5 + 5
	
	sub al, '0'
	cmp al, 1
	
	je comp
	
comp:
	mov ah,02
	mov dx,OK
	int 21h
	
	
OK db "OK.",0dh, 0ah

	