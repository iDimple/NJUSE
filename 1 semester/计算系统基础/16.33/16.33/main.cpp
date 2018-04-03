#include<stdio.h>
#define num 4
void   compare(int a[]);
int main(){
int b[num];
int i;
for(i=0;i<num;i++){
printf("please input the number %d",i+1);
scanf("%d",&b[i]);
}
printf("%d",b[0]);
compare (b);
}
void compare(int a[]){
#define TRUE 1
#define FALSE 0
	int same;
int c,d;
for(c=1;c<num;c++){
	same=FALSE;
for(d=0;d<c;d++)

	if((a[c]-a[d])==0){
same=TRUE;
break;
	}

	if(same==FALSE)
printf("%d",a[c]);
}

}