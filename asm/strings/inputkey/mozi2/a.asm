bits 16
org 0x100
global main

main:
mov ah,09h
mov dx,msg
int 21h

end:

mov ah,4ch
mov al,0
int 21h

msg db "OK.", 0dh,0ah,'$'