
;bit16 宣言
bits 16

;main 関数
main:
	;'A'の表示。システムコール、02番。
	mov ah,02
	mov dl,'A'
	int 21h

	;キー入力して、プログラム、終了
	mov ah,08
	int 21h

	;終了コード
	mov ah,4ch
	int 21h
