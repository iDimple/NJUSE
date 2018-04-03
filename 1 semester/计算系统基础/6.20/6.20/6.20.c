#include<stdio.h>
int main(){
	int i,j;
	printf("please enter an integer");
	scanf("%d",&i);
	for(j=31;j>=0;j--){
		if(i&(1<<j))
			printf("1");
		else
			printf("0");
	}
}