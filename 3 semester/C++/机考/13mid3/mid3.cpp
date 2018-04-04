#include<iostream>
#include<fstream>
#include"mid3.h"
using namespace std;
const char IN[]="input.txt";
const char OUT[]="output.txt";
const int BOUND=200;


void print_address(AddressList& list,Instruct *ins,fstream& fout)
{
	Address *ads=list.first;
	if(ads)
	{
		if(ads->index==-1){
			fout<<"#";
		}else{
			fout<<(ins+ads->index)->address;
		}
		ads=ads->next;
	}
	while(ads)
	{
		fout<<","<<(ins+ads->index)->address;
		ads=ads->next;
	}
}
void print_address(AddressList&list,Instruct* ins)
{
	Address *ads=list.first;
	if(ads)
	{
		if(ads->index==-1){
			cout<<"#";
		}else{
		cout<<(ins+ads->index)->address;
		}
		ads=ads->next;
	}
	while(ads)
	{
		cout<<","<<(ins+ads->index)->address;
		ads=ads->next;
	}

}

void print_add(Instruct* ins,AddressList* get_out,AddressList* get_in,int* valid,int sum_of_ins)
{
	int i=0;
	for(;i<sum_of_ins;i++)
	{
		cout<<"address:"<<(ins+i)->address<<" get_in:";
		print_address(*(get_in+i),ins);
		cout<<" get_out:";
		print_address(*(get_out+i),ins);
		cout<<endl;
	}
}

void print_to_file(Instruct* ins,AddressList* get_out,AddressList* get_in,int* valid,int sum_of_ins)
{
	fstream fout(OUT,ios::out);
	
	int i=0;
	int start=-1;
	for(;i<sum_of_ins;i++)
	{
		if(valid[i]!=0)
		{
			if(start>0&&(get_in+i)->first)
			{
				fout<<(ins+start)->address<<" ";
				insert(*(get_out+i-1),i);
				insert(*(get_in+i),i-1);
				print_address(*(get_in+start),ins,fout);
				fout<<" ";
				print_address(*(get_out+i-1),ins,fout);
				fout<<endl;
			}
			if((get_in+i)->first)
			{
				start=i;
			}
			if((get_out+i)->first)
			{
				fout<<(ins+start)->address<<" ";
				print_address(*(get_in+start),ins,fout);
				fout<<" ";
				print_address(*(get_out+i),ins,fout);
				fout<<endl;
				start=-1;
			}
		}
	}
	fout.close();	
}

void output(Instruct* ins,AddressList* get_out,AddressList* get_in,int* valid,int sum_of_ins)
{
	int i=0;
	int start=-1;
	for(;i<sum_of_ins;i++)
	{
		if(valid[i]!=0)
		{
			if(start>0&&(get_in+i)->first)
			{
				cout<<(ins+start)->address<<" ";
				insert(*(get_out+i-1),i);
				insert(*(get_in+i),i-1);
				print_address(*(get_in+start),ins);
				cout<<" ";
				print_address(*(get_out+i-1),ins);
				cout<<endl;
			}
			if((get_in+i)->first)
			{
				start=i;
			}
			if((get_out+i)->first)
			{
				cout<<(ins+start)->address<<" ";
				print_address(*(get_in+start),ins);
				cout<<" ";
				print_address(*(get_out+i),ins);
				cout<<endl;
				start=-1;
			}
		}
	}
}

void tag(Instruct* ins,AddressList* get_in,AddressList* get_out,int* valid,int sum_of_ins,int start_index)
{
	int i=start_index;
	while(true)
	{
		if(i>=sum_of_ins){
			return;
		}
		do{
			if(valid[i]>=1){
				return;
			}
			valid[i]++;
			if((i==sum_of_ins-1)&&!has_jump(ins+i))
			{
				insert(*(get_out+i),-1);
				return;
			}
		}while(!has_jump(ins+(i))&&(++i)<sum_of_ins);
		
		if(i<sum_of_ins){
			int jump_target=find_index(ins,sum_of_ins,(ins+i)->pos);
			insert(*(get_out+i),jump_target);
			insert(*(get_in+jump_target),i);
			
			if(is_branch(ins+i)){
				tag(ins,get_in,get_out,valid,sum_of_ins,jump_target);
				if((i+1)<sum_of_ins)
					{
					insert(*(get_out+i),i+1);
					insert(*(get_in+i+1),i)	;
					tag(ins,get_in,get_out,valid,sum_of_ins,i+1);
					}
			}else{
				i=jump_target;
			}
		}
	}
	
}

inline void initAList(AddressList &a)
{
	a.first=NULL;
}

inline void initALists(AddressList* list,int num)
{
	for(int i=0;i<num;i++)
	{
		initAList(*(list+i));
	}
}



inline void initInt(int* ints,int num)
{
	for(int i=0;i<num;i++)
	{
		*(ints+i)=0;
	}
}

void to_ins(Instruct* ins,char* chars)
{
	strcpy(ins->address,strtok(chars," "));
	strcpy(ins->op,strtok(NULL," "));
	if(strcmp(ins->op,"BEQZ")==0)
	{
		strtok(NULL," ");
		strcpy(ins->pos,strtok(NULL," "));
	}else if(strcmp(ins->op,"J")==0)
	{
		strcpy(ins->pos,strtok(NULL," "));
	}else
	{
		float address=atof(ins->address);
		address+=4.0;
		char cs[5];
		strcpy(ins->pos,gcvt(address,4,cs));
	}
	
}

void get_instructs(Instruct* ins)
{
	fstream f(IN,ios::in);
	char chars[50];
	initChars(chars,50);
	int i=0;
	while(f.getline(chars,50))
	{
		if(chars[0]!='\0')
		{   
			to_ins((ins+i),chars);
			i++;
		}
		initChars(chars,50);
	}
	i--;
	if(!has_jump(ins+i))
	{
		initChars((ins+i)->pos,5);
		strcpy((ins+i)->pos,END);
	}
	f.close();
}

int main()
{
	Instruct ins[BOUND];
	init_instructs(ins,BOUND);
	get_instructs(ins);
	int sum_of_ins=0;
	while(*((ins+sum_of_ins)->address+0)!='\0')
	{
		cout<<(ins+sum_of_ins)->address<<" "<<(ins+sum_of_ins)->op<<" "<<(ins+sum_of_ins)->pos<<endl;
		/**	if(has_jump(ins+sum_of_ins))
		{
			cout<<":::has jump"<<endl;
		}**/
		sum_of_ins++;
	}
	cout<<"-------------------sum_of_instructions is"<<sum_of_ins<<endl;
	
//	helper_test();
	
	AddressList *get_out=new AddressList[sum_of_ins];
	AddressList *get_in=new AddressList[sum_of_ins];
	int *valid=new int[sum_of_ins]; 
	initInt(valid,sum_of_ins);
	initALists(get_out,sum_of_ins);
	initALists(get_in,sum_of_ins); 
	insert(*get_in,-1);
	
	tag(ins,get_in,get_out,valid,sum_of_ins,0);
//	print_add(ins,get_out,get_in,valid,sum_of_ins);
	output(ins,get_out,get_in,valid,sum_of_ins);
	print_to_file(ins,get_out,get_in,valid,sum_of_ins);
	
}
