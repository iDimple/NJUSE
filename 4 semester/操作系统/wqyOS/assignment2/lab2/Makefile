objects	=	main.o my_print.o
main	:	$(objects)
	gcc -o main $(objects)
main.o	:	main.c
	gcc -c -o main.o main.c
my_print.o	:	my_print.asm
		nasm -f elf -o my_print.o my_print.asm	
.PHONY	:	clean
clean	:
	rm main $(objects)
