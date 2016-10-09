print:
	mov ah,09
	mov dx,split
	int 21h
	
split db "split", 0dh,0ah, '$'
