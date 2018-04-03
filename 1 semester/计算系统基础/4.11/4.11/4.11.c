#include<stdio.h>
int main(){
	int n,j,i;
	printf("please printf the n");
	scanf("%d",&n);
	for(i=1;i<=n;i++){
		for(j=1;j<=i;j++)
			printf("%d  ",i);
	printf("\n");
	}
}