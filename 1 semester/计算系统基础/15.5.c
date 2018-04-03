#include <stdio.h>
int prime(int i);
int main(){
	int r;
	int i;
	int y=0;
	for(i=1;i<=100;i++){
    r=prime(i);
	if(r==1){
		printf("the number %d is prime\n",i);
	
        y=y+1;
	}
	}
printf("小于100的素数共有%d个",y);
	
}			
int prime(int i){
	int r=1;
	int divisor;
	if(i==1)
	return 0;
	if(i==2)
		return 1;
	if(i>2){
		for(divisor=2;divisor<i;divisor++)
			if((i%divisor)==0){
				r=0;
				break;
			}
	}
return r;			
}
