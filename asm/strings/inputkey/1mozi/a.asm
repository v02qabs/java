bits 16
org 100h
global start

start:
	mov ah,2
	mov dl,'A'
	int 21h
	
	jmp add_ret
	
	
add_ret:
	mov ah,2
	mov dl,'OK'
	int 21h
	
end:
	mov ah,4ch
	mov al,0

	int 21h

	msg : db 'OK\r\n'