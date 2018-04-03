#include<stdio.h>
char dianhua(int R);
int main(){
int a;
int b;
int answer1,answer2;
printf("please enter the first number");
scanf("%d",&a);
answer1=dianhua(a);
printf("please enter the second number");
scanf("%d",&b);
answer2=dianhua(b);
printf("%c%c",answer1,answer2);
}
char dianhua(int R){

 switch(R){
case 2: return 'a','b','c';break;
case 3: return 'd','e','f';break;
case 4: return 'g','h','i';break;
case 5: return 'j','k','l';break;
case 6: return 'm','n','o';break;
case 7: return 'p','q','r','s';break;
case 8: return 't','u','v';break;
case 9:return 'w','x','y','z';break; 
default:
return -1;
}
}