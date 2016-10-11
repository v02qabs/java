print:
	mov ah,2
	mov dl,'s'
	int 21h

end:
	mov ah,4ch
	mov al,0
	int 21h