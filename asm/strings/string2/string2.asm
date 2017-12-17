bits 16
org 0x100

global start

start:
    PUSH BP
    MOV BP, SP

    PUSH prompt
    CALL print1

    PUSH name_buf
    PUSH WORD name_buf_len
    CALL gets

    PUSH hello_1
    CALL print1

    PUSH name_buf
    CALL print1

    PUSH 1H
    CALL exit

print1:
    PUSH BP
    MOV BP, SP
    MOV AH, 6H
    MOV BX, [BP+4]
.print1_loop:
    MOV DL, [DS:BX]
    CMP DL, 0H
    JZ .print1_end
    INT 21H
    INC BX
    JMP .print1_loop
.print1_end:
    POP BP
    RET 2

; arg1(2byte) : address of buffer
; arg2(2byte) : buffer length (including null terminator)
; return : non
gets:
    PUSH BP
    MOV BP, SP
    XOR AX, AX
    MOV AX, [BP+4]
    SUB SP, AX
    PUSH AX

    XOR AX, AX
    MOV AH, 0AH
    MOV DX, SP
    INT 21H

    ; retrieve "number of read chars"
    MOV BX, SP
    INC BX
    XOR AX, AX
    MOV AL, BYTE [BX]
    ; overwrite CR to 0
    MOV BX, SP
    ADD BX, 2
    ADD BX, AX
    MOV BYTE [BX], 0H

    MOV SI, SP
    ADD SI, 2
    MOV DI, [BP+6]
    XOR CX, CX
    MOV CL, AL
    INC CX  ; for terminate character
    REP MOVSB

    POP AX
    MOV AX, [BP+4]
    ADD SP, AX
    POP BP
    RET

exit:
    PUSH BP
    MOV BP, SP
    MOV AH, 4CH
    MOV AL, [BP+4]
    INT 21H
section .data
prompt:
    db "Who are you nameÅH : ", 0dh, 0ah, 0

hello_1:
    db `\r\nHello, `, 0

section .bss
name_buf resb name_buf_len
name_buf_len equ 10