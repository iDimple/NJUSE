const char END[]="#";
struct Instruct
{
	char address[5];
	char pos[5];
	char op[5];
};


struct Address
{
	int index;
	Address* next;
};

struct AddressList
{
	Address* first;
};


void initChars(char *chars,int num);
void get_instructs(Instruct* ins);
bool has_jump(Instruct* ins);
void to_ins(Instruct* ins,char* chars);
int find_index(Instruct* ins,int sum_of_ins,char *chars);
void init_instructs(Instruct* ins,int num);
bool is_branch(Instruct*ins);
bool has_jump(Instruct* ins);
void helper_test();
void print_a_list(AddressList &list);
void insert(AddressList& list,int index); 
