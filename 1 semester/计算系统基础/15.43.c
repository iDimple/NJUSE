#include<stdio.h>
double newton(int x);
int main(){
	int a;
	double answer;
	printf("please enter ");
	scanf("%d",&a);
answer=newton(a);
printf("the result is %lf",answer);
}

double newton(int x){
double result,b;
result =x/2.0;
result=(result+x/result)/2.0;
do{b=result;
	result=(b+b/result)/2.0;
}
while( (result-b)<0.00001);

return result;
}
