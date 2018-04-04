#include<iostream>
#include "mid3.h"
using namespace std;

int get_last_val(SortList& list)
{	
	int result=9999;
	Node* node=list.first;
	if(!node)
	{
		return result;
	}
	while(node->next)
	{
		node=node->next;
	}
	return node->tprt;

}

void print_list(SortList& list)
{
	cout<<"Head:";
	Node* n=list.first;
	while(n)
	{
		cout<<n->tprt<<" "; 
		n=n->next;
	}
	cout<<endl;
}

void insert(SortList& list,int year,int tprt)
{
	Node *node=new Node;
	node->next=NULL;
	node->tprt=tprt;
	node->year=year;
	
	Node* n=list.first;
	if(!n)  //insert to empty list
	{
		list.first=node;
		return;
	}
	if((node->tprt)<(n->tprt))
	{
		node->next=n;
		list.first=node;
		return;
	}
	while(n->next)
	{
		if((node->tprt)<(n->next->tprt))
		{
			break;
		}
		n=n->next;
	}
	if(n->tprt==node->tprt)
	{
		return;
	}
	node->next=n->next;
	n->next=node;
	
}

void deleteFirst(SortList& list)
{
	if(!list.first)
	{
		return;
	}
	Node *n=list.first;
	list.first=n->next;
	delete n;
}

void release(SortList& list)
{
	Node* node=list.first;
	while(list.first)
	{
		deleteFirst(list);
	}
}

void list_test()
{
	SortList*list=new SortList;
	SortList& l=*list;
	l.first=NULL;
	insert(l,2000,2);
	insert(l,2000,3);
	print_list(l);
	insert(l,2000,1);
	print_list(l);
	insert(l,2000,9);
	print_list(l);
	insert(l,2000,7);
	print_list(l);
	insert(l,2000,2);
	print_list(l);
	insert(l,2000,2);
	print_list(l);
}
