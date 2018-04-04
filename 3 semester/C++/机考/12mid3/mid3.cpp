#include<iostream>
#include<fstream>
#include"mid3.h"
using namespace std;

const char DATA[]="E:data.txt";
const char MAP[]="E:121250083.map";
const char SHF[]="E:121250083.shf";
const char RED[]="E:121250083.red";
const int SUM=10;
typedef char MSG[200];


void initChars(char* chars,int num)

{
	for(int i=0;i<num;i++)
	{
		*(chars+i)='\0';
	}
}

bool is_valid(MSG *msg)
{
	bool is_valid=true;
	if(is_valid)
		if(*(*(msg+0)+88)=='9'&&*(*(msg+0)+89)=='9'&&*(*(msg+0)+90)=='9'&&*(*(msg+0)+91)=='9')
		{
			is_valid=false;
		}
		if(*(*(msg)+92)!='0'&&*(*(msg)+92)!='1'&&*(*(msg)+92)!='4'&&*(*(msg)+92)!='5'&&*(*(msg)+92)!='9')
		{
			is_valid=false;
		}
		return is_valid;
}

Record* from_data_to_map(MSG *msg)
{
	Record *r=new Record[SUM];
	for(int j=0;j<SUM;j++)
	{
		(r+j)->tprt=9999;
		(r+j)->year=0;
	}
	for(int i=0;i<SUM&&**(msg+i)!='\0';i++)
	{
		cout<<"first:"<<*(*(msg+i))<<endl;
		int year=0;
		int k=0;
		for(k=15;k<19;k++)
		{
			year=year*10+*(*(msg+i)+k)-'0';
		}
		int tprt=0;
		for(k=88;k<92;k++)
		{
			tprt=tprt*10+*(*(msg+i)+k)-'0';
		}
		if(*(*(msg+i)+87)=='-')
		{
			tprt=(0-tprt);
		}
		(r+i)->tprt=tprt;
		(r+i)->year=year;
	}
	return r;
}

void write_to_map(Record *r)
{
	fstream f(MAP,ios::out|ios::app);
	for(int i=0;i<SUM;i++)
	{
		if((r+i)->year==0)
		{
			break;
		}else{
			f<<(r+i)->year<<","<<(r+i)->tprt<<endl;
			cout<<(r+i)->year<<","<<(r+i)->tprt<<endl;
		}
	}
	
	delete[] r;
	f.close();
}

inline int get_year(char* chars)
{
	return (chars[3]-'0')+2000;
}

int get_tprt(char* chars)
{
	char cs[5]={'\0'};
	for(int i=0;chars[i+5]!='\0';i++)
	{
		cs[i]=chars[i+5];
	}
	
	return atoi(cs);
}

int sum_of_lines(const char* file_name)
{
	int count=0;
	fstream f(file_name,ios::in);
	char chars[200];
	initChars(chars,200);
	while(!f.eof())
	{
		f.getline(chars,199);
		count++;
	}
	f.close();
	return count;
}

void map()
{
	int sum_of_line=sum_of_lines(DATA);
	int sum_of_blocks=(sum_of_line-1)/SUM+1;
	cout<<sum_of_blocks<<endl;
	int i=0;
	for(;i<sum_of_blocks;i++)
	{
		MSG* msg=new MSG[SUM];
		for(int k=0;k<SUM;k++)
		{
			initChars(*(msg+k),200);
		}
		
		int from_line=i*SUM;
		int j=0;
		char chars[200];
		fstream f(DATA,ios::in);
		for(;(j<from_line)&&!f.eof();j++)
		{
			initChars(chars,200);
			f.getline(chars,199);
		}
		for(j=0;j<SUM&&!f.eof();j++)
		{
			f.getline(*(msg+j),199);
		}
		f.close();
		
		write_to_map(from_data_to_map(msg));
		delete [] msg;
		cout<<i<<endl;
	}
}

void initList(SortList *lists,int sum){
	for(int i=0;i<sum;i++)
	{
		(lists+i)->first=NULL;
	}
}

void write_to_shf(SortList* lists,int num)
{
	fstream f(SHF,ios::out|ios::app);

	for(int i=0;i<num;i++)
	{
		Node *node=(lists+i)->first;
		if(!node)
		{
			continue;
		}
		f<<(2000+i)<<",[";
		if(!node->next)
		{
			f<<node->year<<"]"<<endl;
			continue;
		}
		while(node->next)
		{
			f<<node->tprt<<",";
			node=node->next;
		}
		f<<node->tprt<<"]"<<endl;
	}
	f.close();
}

void write_to_red(SortList* lists,int num)
{
	fstream f(RED,ios::out|ios::app);
	for(int i=0;i<num;i++)
	{
		Node *node=(lists+i)->first;
		if(!node)
		{
			continue;
		}
		int max=get_last_val(*(lists+i));

		f<<(2000+i)<<","<<max<<endl;
		
	}
	f.close();
}

void shfANDreduce()
{
	SortList* lists=new SortList[13];//for from 2000-2012
	initList(lists,13);
	fstream f(MAP,ios::in);
	while(!f.eof())
	{
		char chars[200];
		initChars(chars,200);
		f.getline(chars,199);
		
		if(chars[0]!='\0')
		{
		int year=get_year(chars);
		int tprt=get_tprt(chars);
		int index=year-2000;
		insert(*(lists+index),year,tprt);
	//	print_list(*(lists+3));
		}
	}
	f.close();
	write_to_shf(lists,13);
	write_to_red(lists,13);

	delete[] lists;
}

void main()
{
	map();
	//	list_test();
	shfANDreduce();
}