/*
 * main.c
 *
 *  Created on: 2016年4月25日
 *      Author: Administrator
 */



#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define DEFUALTCOLOR 1
#define COLOR 3
//结构定义
typedef struct FileInfo{
	char fileName[0xC];//文件名
	int isFolder;//是否是目录
	int fstClus;//开始簇号
	int fileSize;//文件大小
	char *fileContent;//文件内容
} FILEINFO, *LPFILEINFO;
//树节点
typedef struct TreeNode{
	LPFILEINFO fileinfo;
	struct TreeNode *parent;
	struct TreeNode *next;
	struct TreeNode *firstChild;
} TREENODE, * LPTREENODE;
void my_print();//汇编输出函数
char* _str_ = "\033[31msdf\n\033[0m";
int len_str = 13;
//char* _str1_ = "\033[35m";//BLUE
char* _str1_ = "\033[34m";
int len1_str = 5;
char* _str2_ ="\033[0m";
int len2_str = 4;
void showCount(LPTREENODE node,int i);//打印数目录和文件数的结果
void setColor();//设置颜色输出
void clearColor();//恢复默认颜色
void initTree();//初始化树
void releaseTree();//释放树
LPTREENODE addToTree(LPFILEINFO fileinfo, LPTREENODE parent);//把一个文件添加到树上
int isEnd(unsigned char buf[0x20]);//判断一个目录结构是否结束
int isDelete(unsigned char buf[0x20]);//判断是否是已删除的文件
int isTrash(LPFILEINFO fileinfo);//判断是否是回收站
void readFileContent(LPFILEINFO fileinfo);//读取文件的内容
LPFILEINFO readFile();//读取文件的信息
int readOnePiece(int offset, LPTREENODE parent);//读取一条目录项
void readFolder(LPTREENODE parent, int clus);//读取一个目录
void readRoot();//读取全部的文件
void readBoot();//读取引导信息
int readTree();//读取所有的文件信息
void printNode(LPTREENODE node);//输出节点文件名
LPTREENODE getNode(char folder[50][50], int index, int max, LPTREENODE parent);//根据目录字符串获取目录节点
void displayFile(LPTREENODE node);//输出文件内容
void displayNode(LPTREENODE node);//输出一个节点的所有文件
void displayRoot();//输出所有的文件
int countFile(LPTREENODE node);//计算文件数
int countDir(LPTREENODE node);//计算目录数
int split(char dest[50][50], char src[50]);//把输入的字符串路径拆分
void print(char *buf, int color);//输出带颜色的字符串
int isValid(char*name);
int main();
FILE *file;//文件句柄
LPTREENODE root;//树根

unsigned int BPB_BytsPerSec;//每扇区字节数
unsigned int BPB_SecPerClus;//每簇扇区数
unsigned int BPB_RsvdSecCnt;//Boot记录占用多少扇区
unsigned int BPB_BumFATs;//共有多少FAT表
unsigned int BPB_RootEntCnt;//根目录文件数最大值
unsigned int BPB_TotSec16;//扇区总数
unsigned int BPB_FATSz16;//每FAT扇区数
int main(){
	if (readTree()){//读文件，并创建一棵树
		displayRoot();//打印路径
		char read[50];
		char realread[50];
		char folder[50][50];
		int num;
		while (1){
			print("please input ----------------------------------------\nPlease input the path,quit please input exit:\n", DEFUALTCOLOR);
			scanf("%s",read);

			if (strcmp(read, "exit") == 0)
				break;

			if(strcmp(read,"count") != 0) {//搜索功能
				//第几层
				num = split(folder, read);
				//max根据/划分的层数，index从0层开始往下读，从根目录开始
				//深度搜索功能
				//得到用户输入的最后一个/后面的node
				LPTREENODE node = getNode(folder, 0, num, root);
				if (node == NULL){//没搜到
					print("Unknown file\n", DEFUALTCOLOR);
				}else if (!node->fileinfo->isFolder){//文件。打印内容
					displayFile(node);
				}else{//文件夹，打印所有子路经
					displayNode(node);
				}
			}else {//如果是count命令
				scanf("%s",realread);//读如要count的目录
				//第几层
				num = split(folder, realread);
				//max根据/划分的层数，index从0层开始往下读，从根目录开始
				//深度搜索功能
				//得到用户输入的最后一个/后面的node
				LPTREENODE node = getNode(folder, 0, num, root);
				if (node == NULL){
					print("Unknown file\n", DEFUALTCOLOR);
				}else if (!node->fileinfo->isFolder){//文件
					print("it's not a directory",DEFUALTCOLOR);
					print("\n",DEFUALTCOLOR);
					clearColor();
				}else {//文件夹，打印count
					showCount(node,0);
				}
			}
		}

		releaseTree();
	}
	return 0;
}
void initTree(){
	root = malloc(sizeof(TREENODE));
	root->parent = NULL;
	root->next = NULL;
	root->firstChild = NULL;
}

void releaseTree(){
	while (root){
		LPTREENODE p = root;
		root = root->firstChild;
		while (p){
			LPTREENODE t = p->next;
			if (p->fileinfo){
				if (p->fileinfo->fileContent){
					free(p->fileinfo->fileContent);
				}
				free(p->fileinfo);
			}
			free(p);
			p = t;
		}
	}
}

LPTREENODE addToTree(LPFILEINFO fileinfo, LPTREENODE parent){
	LPTREENODE node = malloc(sizeof(TREENODE));
	node->fileinfo = fileinfo;
	node->parent = parent;
	node->next = NULL;
	node->firstChild = NULL;
	if (parent->firstChild == NULL){
		parent->firstChild = node;
	}else{
		LPTREENODE previous = parent->firstChild;
		while (previous->next != NULL){
			previous = previous->next;
		}
		previous->next = node;
	}
	return node;
}

int isEnd(unsigned char buf[0x20]){
	int i;
	for (i = 0; i < 0x20; i ++){
		if (buf[i] != 0)
			return 0;
	}
	return 1;
}

int isDelete(unsigned char buf[0x20]){
	char c=buf[0];
	if (c == 0xE5)
		return 1;

	if (! ((c >= '0' && c <= '9')
			|| (c >= 'a' && c <= 'z')
			|| (c >= 'A' && c <= 'Z')
			|| c == ' '
	)){
		return 1;
	}

	return 0;
}

int isTrash(LPFILEINFO fileinfo){
	const char trash[] = "TRASH-~1";
	int i;
	for (i = 0; i < 9; i ++){
		if (trash[i] != fileinfo->fileName[i])
			return 0;
	}
	return 1;
}



void readFileContent(LPFILEINFO fileinfo){

	fileinfo->fileContent = malloc(0x200);
	int current_off=ftell(file);
	if (fileinfo->fstClus == 0)
		return;
	int offset = 0x2600 + BPB_RootEntCnt * 0x20 + (fileinfo->fstClus - 2) * 0x200;
	fseek(file, offset, SEEK_SET);
	fread(fileinfo->fileContent, 0x200, 1, file);
	fseek(file,current_off,SEEK_SET);
}

LPFILEINFO readFile(){
	unsigned char piece[0x20];
	fread(piece, 0x20, 1, file);
	if (isEnd(piece))
		return NULL;
	//TODO

	//可以判断文件是否有效
	LPFILEINFO fileinfo = malloc(sizeof(FILEINFO));
	fileinfo->fileContent = NULL;
	//文件属性（是否是文件夹）
	if (piece[0xB] == 0x10)
		fileinfo->isFolder = 1;
	else
		fileinfo->isFolder = 0;

	//文件名
	int len;
	if (!fileinfo->isFolder){

		for (len = 0; len < 8; len ++){
			if (piece[len] == 0x20)
				break;
			fileinfo->fileName[len] = piece[len];

		}

		fileinfo->fileName[len] = '.';
		len ++;
		int i;
		for (i = 8; i < 0xB; i ++){
			if (piece[i] == 20)
				break;
			fileinfo->fileName[len] = piece[i];
			len ++;
		}

	}else{
		for (len = 0; len < 0xB; len ++){
			if (piece[len] == 0x20)
				break;
			fileinfo->fileName[len] = piece[len];
		}
	}
	fileinfo->fileName[len] = 0;
	//开始的簇号
	fileinfo->fstClus = piece[0x1A] + (piece[0x1B] * 256);
	//文件大小
	fileinfo->fileSize = *((int*)&piece[0x1C]);
	if (!fileinfo->isFolder && !isDelete((unsigned char *)fileinfo)){
		readFileContent(fileinfo);
	}
	//第11位是0f
		if (piece[0xB] == 0x0f)
			fileinfo->fileName[0] = 0xe5;
			return fileinfo;
}
//深度遍历
int readOnePiece(int offset, LPTREENODE parent){
	fseek(file, offset, SEEK_SET);
	//创建树节点需要的fileinfo,在文件中的位置已设定好
	LPFILEINFO fileinfo = readFile();
	offset = ftell(file);
	if (fileinfo == NULL)
		return 0;
	//不是有效节点返回当前位置，继续往下读
	if (isDelete((unsigned char *)fileinfo))
		return offset;
	if (isTrash(fileinfo))
		return offset;
	//有效节点，加入树
	LPTREENODE node = addToTree(fileinfo, parent);
	//如果是目录，继续往下读
	if (fileinfo->isFolder){
		readFolder(node, fileinfo->fstClus);
	}
	return offset;
}
//数据区读目录
void readFolder(LPTREENODE parent, int clus){
	int offset = 0x2600 + BPB_RootEntCnt * 0x20 + (clus - 2) * 0x200 + 0x40;
	while (1){
		//读节点，加入树
		offset = readOnePiece(offset, parent);
		if (offset == 0)
			break;
	}
}

void readRoot(){
	int offset = 0x2600;
	while (1){//广度遍历
	//深度遍历
		offset = readOnePiece(offset, root);
		if (offset == 0)
			break;
	}
}

void readBoot(){
	unsigned char boot[32];
	fread(boot, 32, 1, file);
	BPB_BytsPerSec = boot[11] + (boot[12] * 256);
	BPB_SecPerClus = boot[13];
	BPB_RsvdSecCnt = boot[14] + (boot[15] * 256);
	BPB_BumFATs = boot[16];
	BPB_RootEntCnt = boot[17] + (boot[18] * 256);
	BPB_TotSec16 = boot[19] + (boot[20] * 256);
	BPB_FATSz16 = boot[22] + (boot[23] * 256);
}

int readTree(){
	file = fopen("a.img", "rb");
	if (file == NULL){
		print("打开文件错误！\n", DEFUALTCOLOR);
		return 0;
	}
	//读引导扇区
	readBoot();
	//初始化树
	initTree();
	//从根目录开始读文件目录
	readRoot();
	fclose(file);
	return 1;
}
//打印node的信息
void printNode(LPTREENODE node){
	if (node == root){
		return;
	}
	//从根目录开始打印
	printNode(node->parent);
	if (node->fileinfo->isFolder){
		print(node->fileinfo->fileName, COLOR);
		print("/", COLOR);
	}else{
		print(node->fileinfo->fileName, DEFUALTCOLOR);
	}
}
//max根据/划分的层数，index从0层开始往下读，从根目录开始
//深度搜索功能
LPTREENODE getNode(char folder[50][50], int index, int max, LPTREENODE parent){
	LPTREENODE node = parent->firstChild;
	if (max == index)
		return parent;
	while (node){
		if (strcmp(folder[index], node->fileinfo->fileName) == 0){//一层相同，继续往下读
			return getNode(folder, index + 1, max, node);
		}
		node = node->next;
	}
	//没搜到
	return NULL;
}
//打印文件内容
void displayFile(LPTREENODE node){
	char output[1000];
	print( node->fileinfo->fileContent,DEFUALTCOLOR);
	print(output, DEFUALTCOLOR);
}
//打印node下面的所有子路经
void displayNode(LPTREENODE node){
	if (node == NULL){
		return;
	}
	//他的firstchile的所有兄弟
	LPTREENODE childnode = node->firstChild;
	if (childnode == NULL){
		//一个空目录，或是文件，那么他们就没有child直接答应他自己
		printNode(node);
		print("\n", DEFUALTCOLOR);
	}else{
		while (childnode != NULL){
			//深度遍历
			displayNode(childnode);
			//广度遍历
			childnode = childnode->next;
		}
	}
}

void displayRoot(){
	LPTREENODE node = root->firstChild;
	while (node != NULL){
		displayNode(node);
		node = node->next;
	}
}
//数node下面的dir，不包括node
int countDir(LPTREENODE node) {
	int dir = 0;
	if (node == NULL) {
		return -1;
	}
	LPTREENODE childnode = node->firstChild;
	//第一个子女和他的的兄弟就是他的所有子
	if(childnode == NULL) {//已为叶节点
		dir+=0;
		return dir;
	}else {
		while(childnode != NULL) {
			if(childnode->fileinfo->isFolder){
				dir++;
			}
			dir += countDir(childnode);
			childnode = childnode->next;
		}

	}
	return dir;
}
//数node下面的file，不包括node
int countFile(LPTREENODE node) {
	int files = 0;
	if(node == NULL) {
		return -1;
	}
	LPTREENODE childnode = node->firstChild;
	//第一个子女和他的的兄弟就是他的所有子
	if(childnode == NULL) {//已经是叶节点
		if(node->fileinfo->isFolder)
			files+=0;
		else
			files+=1;
		return files;
	}else {
		while(childnode != NULL) {
			files += countFile(childnode);
			childnode = childnode->next;
		}
	}

	return files;

}
//拆分字符串
//用于解析输入目录
int split(char dest[50][50], char src[50]){//解读输入
	int num = 0;
	//数有几个/
	char *str = strtok(src, "/");
	while (str){
		strcpy(dest[num], str);
		num ++;
		//TODO
		str = strtok(NULL, "/");
	}
	return num;
}

void print(char *buf, int color){

	if(color == COLOR){//如果有颜色要求
		setColor();
		len_str = strlen(buf);
		_str_ = buf;
		my_print();
		clearColor();
	}else{//默认颜色输出
		len_str = strlen(buf);
		_str_ = buf;
		my_print();
	}
}
//打印count，i第几层，从0开始，为了控制空格的输出
void showCount(LPTREENODE node,int i) {
	//i计数空格数
	//区分父子目录
	int dir = countDir(node);
	int files = countFile(node);
	if(node->fileinfo->isFolder) {//文件夹
		int j = 0;
		for(;j<i;j++) {
			print(" ",DEFUALTCOLOR);
		}
		print(node->fileinfo->fileName,DEFUALTCOLOR);
		print(": ",DEFUALTCOLOR);
		//数字要加0x30变成ascii
		char tep = (dir+0x30);
		char* tepPtr = &tep;
		tepPtr[1]='\0';
		print(tepPtr,DEFUALTCOLOR);
		print(" directory, ",DEFUALTCOLOR);
		char tep1 = (files+0x30);
		char* tepPtr1 = &tep1;
		tepPtr1[1]='\0';
		print(tepPtr1,DEFUALTCOLOR);
		print(" file\n",DEFUALTCOLOR);
	}
	//输出node的子目录
	LPTREENODE childnode = node->firstChild;
	while(childnode != NULL) {
		//深度遍历的打印
		showCount(childnode,i+1);
		//广度遍历
		childnode = childnode->next;
	}

}

void setColor() {
	_str_ = _str1_;
	len_str = len1_str;
	my_print();
}

void clearColor() {
	_str_ = _str2_;
	len_str = len2_str;
	my_print();
}

int isValid(char*name){
	int j = 0;
	for (j = 0; j < 8; j++){
		char c = name[j];
		if (! ((c >= '0' && c <= '9')
				|| (c >= 'a' && c <= 'z')
				|| (c >= 'A' && c <= 'Z')
				|| c == ' '
		)){
			return 0;
		}
	}
	return 1;
}



