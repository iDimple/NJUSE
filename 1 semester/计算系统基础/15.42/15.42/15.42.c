#include<stdio.h>
double xdejie(double a,double b);
int main(){
	int a;
	int b;
	double answer;
	printf("please enter a");
	scanf("%d",&a);
	printf("please enter b");
	scanf("%d",&b);
answer=xdejie(a,b);
printf("the answer is %f",answer);
}
double xdejie(double a,double b){
	double answer;
	if(a==0)
		answer=-1;
	else
	answer=-b/a;
		return answer;
}
