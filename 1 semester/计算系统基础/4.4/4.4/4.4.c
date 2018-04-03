#include<stdio.h>
int main(){
	int year,month;
	printf("please enter the year and the month");
	scanf("%d%d",&year,&month);
	if(month!=2){
		if(month==4||month==6||month==9||month==11)
			printf("the month has 30 days\n");
		else
			printf("the month has 31 days\n");
	}
	else{
	if((year%4==0)&&(year%100!=0)||(year%400==0))
		printf("the month has 29 days\n");
	else
		printf("the month has 28 days\n");
	}
}