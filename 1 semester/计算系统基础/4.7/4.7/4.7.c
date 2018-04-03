#include<stdio.h>
int main(){
	int a,result;
	a=1;
	result=1;
	while(a<=9){
		result=result*a;
		a=a+1;
	}
	printf("the result is %d",result);
}