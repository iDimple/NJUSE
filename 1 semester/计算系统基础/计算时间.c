#include <stdio.h>

int main(){
	int time;
	int hour;
	int minute;
	int second;


	printf("what is the time in second to be transferred?");
	scanf("%d",&time);

	hour=time/3600;
	minute=(time-3600*hour)/60;
	second=time-3600*hour-60*minute;


	printf("it's %d hour %d minute %d second \n" ,hour,minute,second);

}