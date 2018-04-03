#include<stdio.h>
int main(){
	int a,b,c,d;
	printf("please enter the seconds");
	scanf("%d",&a);
	b=a/3600;
	c=(a-b*3600)/60;
	d=a-b*3600-c*60;
	printf("%dh%dm%ds",b,c,d);
}