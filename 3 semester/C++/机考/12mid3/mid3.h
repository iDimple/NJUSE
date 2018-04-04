
struct Record
{
	int year;
	int tprt;
};

struct Node{
	int year;
	int tprt;
	Node* next;
};

struct SortList
{
	Node* first;
};

void list_test();
void release(SortList& list);
void deleteFirst(SortList& list);
void insert(SortList& list,int year,int tprt);
void print_list(SortList& list);
void initChars(char* chars,int num);
int get_last_val(SortList& list);