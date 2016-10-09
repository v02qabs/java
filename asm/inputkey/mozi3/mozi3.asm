	bits 16
	ORG	100h
	global START
START:
	mov	ah,1
	int	21h		;key in
	sub	al,'0'		;文字コードから数値へ
	cmp	al,9
	ja	BYEBYE		;9より上(数字以外が入力された場合は「BYEBYE」へ飛ぶ)
	mov	bl,al

;改行
;	mov	ah,2
;	mov	dl,0dh
;	int	21h
;	mov	ah,2
;	mov	dl,0ah
;	int	21h		;改行

	mov	ah,1
	int	21h		;Key in
	sub	al,'0'		;文字コードから数値へ
	cmp	al,9
	ja	BYEBYE		;9より上(数字以外が入力された場合は「BYEBYE」へ飛ぶ)

	mul	bl		;かけ算の結果がAXに(1桁同士のかけ算は81以下なのでAH=0)
	mov	cl,10
	div	cl		;10で割った結果、商(10の位)がALに、余り(1の位)がAHに入る
	mov	bx,ax

	mov	ah,2
	mov	dl,0dh
	int	21h
	mov	ah,2
	mov	dl,0ah
	int	21h		;改行

	cmp	bl,0
	jz	ICHINOKURAI	;10の位が0ならばジャンプ

	mov	ah,2
	mov	dl,bl
	add	dl,'0'		;文字コードに変換
	int	21h		;10の位表示

ICHINOKURAI:
	mov	ah,2
	mov	dl,bh
	add	dl,'0'		;文字コードに変換
	int	21h		;1の位表示

BYEBYE:
	mov	ax,4c00h
	int	21h		;終了