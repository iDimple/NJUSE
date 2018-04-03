#include<stdio.h>
#define FALSE 0
#define TRUE 1
int main(){
	char nextchar;
	int goti=FALSE;
	int gotin=FALSE;
	int count =0;
	printf("enter your string");
	do{
		scanf("%c",&nextchar);
		switch(nextchar){
		case'i':
			goti=TRUE;
			gotin=FALSE;
			break;
		case'n':
			if(gotin)
				gotin=FALSE;
			if(goti)
				gotin=TRUE;
				goti=FALSE;
				break;
		case't':
			if(gotin)
				count++;
			goti=FALSE;
			gotin=FALSE;
			break;
		default:
			goti=FALSE;
			gotin=FALSE;
		}
	}while(nextchar!='\n');
	printf("count=%d\n",count);
}