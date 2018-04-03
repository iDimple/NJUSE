#include <stdio.h>

int main(){
	int x;
	int y;

	printf("input an integer:");
	scanf("%d",&x);
	printf("input an integer:");
    scanf("%d",&y);
    if(x>y)
		printf("%d is larger",x);
    else 
		printf("%d is larger",y);
}
    
