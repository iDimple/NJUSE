#include<stdio.h>
int mi(int x,int y);
int main(){
	int a,b;
	printf("please input the x");
	scanf("%d",&a);
	printf("please input the y");
	scanf("%d",&b);
	printf("the result is %d",mi(a,b));
}
int mi(int x,int y){
	int i;
	int result=1;
	if(x==0){
		if(y<=0)
			result=-1;
	}
	else{
		if(y==0)
			printf("1");
		else
		for(i=1;i<=y;i++)
			result=result*x;
	
}
	return result;
}