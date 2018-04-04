extern len_str;
extern _str_;

global my_print

section .text

my_print :
	mov	edx,[len_str]
	mov 	ecx,[_str_]
	mov	ebx,1
	mov	eax,4
	int	0x80
ret
