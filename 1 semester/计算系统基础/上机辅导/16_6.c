#include<stdio.h>

void StrCat(char* firstStr, char* secondStr);
void StrCpy(char* firstStr, char* secondStr);

int main(){
	char str1[100] = "hello";
	char str2[100] = "world";
	char str3[100];
	StrCpy(str3,str1);
	StrCat(str1,str2);
	printf("%s\n", str1);
	printf("%s\n", str3);
	return 1;
}

void StrCat(char* str1, char* str2){
	int i=0;
	int j=0;
	for(i=0; str1[i]!='\0'; i++){
	}
	for(j=0; str2[j]!='\0'; j++){
		str1[i+j] = str2[j];
	}
	str1[i+j]='\0';
}

void StrCpy(char* str3, char* str1){
	int i=0;
	for(i=0; str1[i]!='\0'; i++){
		str3[i] = str1[i];
	}
	str3[i] = '\0';
}


