#include<stdio.h>
int fibonacci(int n);
int main(){
	int x,result;
	printf("please input the n(n>=0)");
	scanf("%d",&x);
	result=fibonacci(x);
	printf("f(%d) is %d\n",x,result);
}
int fibonacci(int n){
	int sum;
	if(n==0||n==1)
		return 1;
	else{
		sum=fibonacci(n-1)+fibonacci(n-2);
		return sum;
	}
}
