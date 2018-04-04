#include<iostream>
#include"mid3.h"
using namespace std;

void insert(AddressList& list,int index)
{
	Address *ads=new Address;
	ads->next=NULL;
	ads->index=index;
	Address *node=list.first;
	
	if(!node)
	{
		list.first=ads;
		return ;
	}else if(node->index==index)
	{
		return ;
	}
 	while(node->next&&((node->next->index)<index))
	{
		node=node->next;
	}
	if(node->next&&node->next->index==index)
	{
		return ;
	}else{
		if(!node->next){
			node->next=ads;
		}else{
			ads->next=node->next;
			node->next=ads;
		}
	}
	
}

void print_a_list(AddressList &list)
{
	Address *a=list.first;
	while(a)
	{
		cout<<"->"<<a->index;
		a=a->next;
	}
	cout<<endl;
}

void initChars(char *chars,int num)
{
	int i=0;
	for(;i<num;i++)
	{
		*(chars+i)='\0';
	}
}

void init_instructs(Instruct* ins,int num)
{
	int i=0;
	for(;i<num;i++)
	{
		initChars((ins+i)->address,5);
		initChars((ins+i)->op,5);
		initChars((ins+i)->pos,5);
	}
}

int find_index(Instruct* ins,int sum_of_ins,char *chars)
{
	int i=0;
	for(;i<sum_of_ins;i++)
	{
		if(strcmp((ins+i)->address,chars)==0)
		{
			return i;
		}
	}
	return -1;
}

bool is_branch(Instruct*ins)
{
	return (strcmp(ins->op,"BEQZ")==0);
}

bool has_jump(Instruct* ins)
{
	if(strcmp(ins->op,"BEQZ")==0)
	{
		return true;
	}else if(strcmp(ins->op,"J")==0)
	{
		return true;
	}
	return false;
}

void helper_test()
{
	AddressList *alist=new AddressList;
	alist->first=NULL;
	insert(*alist,3);
	print_a_list(*alist);
	insert(*alist,4);
	print_a_list(*alist);
	insert(*alist,6);
	print_a_list(*alist);
	insert(*alist,6);
	print_a_list(*alist);
	insert(*alist,7);
	print_a_list(*alist);
	insert(*alist,5);
	print_a_list(*alist);
	insert(*alist,4);
	print_a_list(*alist);
	insert(*alist,3);
	print_a_list(*alist);
	insert(*alist,4);
	print_a_list(*alist);
}