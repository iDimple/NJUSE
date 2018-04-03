#include<stdio.h>
int main(){
	char nextchar;
	printf("please enter the strings");
	do{
		scanf("%c",&nextchar);
		if(nextchar!=' ')
			printf("%c",nextchar);
	}while(nextchar!='\n');
}
