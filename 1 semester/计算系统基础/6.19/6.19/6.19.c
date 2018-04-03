#include<stdio.h>
int main(){
	char nextchar;
	printf("please enter the string");
	do{
		scanf("%c",&nextchar);
		if((nextchar<='z')&&(nextchar>='a')){
			nextchar=nextchar-('a'-'A');
			printf("%c",nextchar);
	}
		else
			printf("%c",nextchar);
	}while(nextchar!='\n');
}