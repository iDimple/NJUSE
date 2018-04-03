#include<stdio.h>
double suanpai(int n);
int main(){
	double pai;
	int i;
	printf("please enter the number");
	scanf("%d",&i);
	pai=suanpai( i);
	printf("the result is %f",pai);
}
double suanpai(int n){
	double result=4.0;
	if(n==1)
		return result;
	if(n%2!=0){
		result=suanpai(n-1)+4.0/(2*n-1);
		return result;
}
	else{
		result=suanpai(n-1)-4.0/(2*n-1);
		return result;
}
}