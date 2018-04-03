#include<stdio.h>
#define MAX 10
void jiami(char b[],int x);
int main(){
char a[MAX];
int n;
printf("please enter the words(less than %d words)\n",MAX);
scanf("%s",a);
printf("please enter n \n");
scanf("%d",&n);
jiami(a,n);

}
void jiami(char b[],int x){
	int i,j;
	j=126-x;
	for(i=0;i<MAX;i++){
		if(b[i]!='\0'){
		if(b[i]>j)
			b[i]=b[i]-94+x;
		else
		b[i]=b[i]+x;}
}printf("%s",b);
}