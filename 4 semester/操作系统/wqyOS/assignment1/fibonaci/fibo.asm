section .bss
	in:resb 1 ;input
	out:resb 1;print
	
section .data
	msg: db "please input the numbers:"
	size:equ $-msg
	space:db 32
Color:db 27,"[1;31m",27,"[1;32m",27,"[1;33m",27,"[1;34m"
	;red green yellow blue
	num:db 0
	result:dd 0
        result_len:db 0
ColorPointer:db 0

section .text
	global main
	main:
	mov eax,4
	mov ebx,1
	mov ecx,msg
	mov edx,size
	int 80h
	
reading:
	mov eax,3
	mov ebx,0
	mov ecx,in
	mov edx,1
	int 80h


	cmp byte[in],32
	je fibo
	cmp byte[in],10
	je fibo

	movzx eax,byte[num]
	mov ebx,10
	mul ebx

	sub byte[in],30h
	mov bl,byte[in]

	add eax,ebx
	mov byte[num],al
	jmp reading

fibo:
	cmp byte[num],1
	je result1
	mov ebx,1
	mov ecx,1
	sub byte[num],1

comfibo:
;result=ebx+ecx
;ecx=ebx
;ebx=result
	mov dword[result],ecx
	add dword[result],ebx
	mov ecx,ebx
	mov ebx,dword[result]
	dec byte[num]
	cmp byte[num],0
	je result2
	jmp comfibo

result1:
	mov eax,1
	jmp processtheresult

result2:
	mov eax,dword[result]
	mov byte[result_len],0

processtheresult: 
	mov ebx,10
;to prevent float core dump
	mov edx,0
;edx the remainder,eax the quotient
	div ebx
;when quotient=0,finished
	inc byte[result_len]
	cmp eax,0
	je print
;the remainder 
	push edx
	jmp processtheresult

print:
;the last remainder
	push edx

;to change the color
	
	mov ebx,1
	mov edx,Color
	movzx eax,byte[ColorPointer]
	add edx,eax
	mov ecx,edx
	mov eax,4
	mov edx,7
	int 80h
;next color
	add byte[ColorPointer],7
	cmp byte[ColorPointer],28
	je bezero


subprint:
	pop eax
	add eax,30h
	mov byte[out],al

	mov eax,4
	mov ebx,1
	mov ecx,out
	mov edx,1
	int 80h

	dec byte[result_len]
	jnz subprint

	cmp byte[in],10
	je exit

;to print a space between 2 results
	mov eax,4
	mov ebx,1
	mov ecx,space
	mov edx,1
	int 80h

;to read next num
	mov byte[num],0
	jmp reading
bezero:
	mov byte[ColorPointer],0
	jmp subprint

exit:
	mov eax,1
	mov ebx,0
	int 80h


