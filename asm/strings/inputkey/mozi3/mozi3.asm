	bits 16
	ORG	100h
	global START
START:
	mov	ah,1
	int	21h		;key in
	sub	al,'0'		;�����R�[�h���琔�l��
	cmp	al,9
	ja	BYEBYE		;9����(�����ȊO�����͂��ꂽ�ꍇ�́uBYEBYE�v�֔��)
	mov	bl,al

;���s
;	mov	ah,2
;	mov	dl,0dh
;	int	21h
;	mov	ah,2
;	mov	dl,0ah
;	int	21h		;���s

	mov	ah,1
	int	21h		;Key in
	sub	al,'0'		;�����R�[�h���琔�l��
	cmp	al,9
	ja	BYEBYE		;9����(�����ȊO�����͂��ꂽ�ꍇ�́uBYEBYE�v�֔��)

	mul	bl		;�����Z�̌��ʂ�AX��(1�����m�̂����Z��81�ȉ��Ȃ̂�AH=0)
	mov	cl,10
	div	cl		;10�Ŋ��������ʁA��(10�̈�)��AL�ɁA�]��(1�̈�)��AH�ɓ���
	mov	bx,ax

	mov	ah,2
	mov	dl,0dh
	int	21h
	mov	ah,2
	mov	dl,0ah
	int	21h		;���s

	cmp	bl,0
	jz	ICHINOKURAI	;10�̈ʂ�0�Ȃ�΃W�����v

	mov	ah,2
	mov	dl,bl
	add	dl,'0'		;�����R�[�h�ɕϊ�
	int	21h		;10�̈ʕ\��

ICHINOKURAI:
	mov	ah,2
	mov	dl,bh
	add	dl,'0'		;�����R�[�h�ɕϊ�
	int	21h		;1�̈ʕ\��

BYEBYE:
	mov	ax,4c00h
	int	21h		;�I��