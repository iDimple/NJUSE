#include<stdio.h>
#define max 40
int SearchStr(char *a,char *b);
int main(){
	char a[max];
	char b[max];
	int times;
	printf(" first  string");
	scanf("%s",a);
	printf("second string");
	scanf("%s",b);
	times = SearchStr(a,b);
	printf("%d",times);
}
int SearchStr(char*a,char* b){
	int j,same;
	int i=0 ;
	int time=0;
	int length1=0; 
	int length2=0;
	while(*(a+length1)!='\0')
		length1++;
	while(*(b+length2)!='\0')
		length2++;
	while(i<length1){
		if(*(a+i)==*b){
			for(j=0;j<length2;j++){
if(*(a+i+j)==*(b+j))
	same=1;
else{
		same=0;
			break;
}
			}
			if(same)
				time++;}

		i++;}
return time;
}