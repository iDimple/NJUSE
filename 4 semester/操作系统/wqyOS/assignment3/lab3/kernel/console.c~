
/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			      console.c
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
						    Forrest Yu, 2005
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/


/*
	回车键: 把光标移到第一列
	换行键: 把光标前进到下一行
*/


#include "type.h"
#include "const.h"
#include "protect.h"
#include "string.h"
#include "proc.h"
#include "tty.h"
#include "console.h"
#include "global.h"
#include "keyboard.h"
#include "proto.h"

PRIVATE void set_cursor(unsigned int position);
PRIVATE void set_video_start_addr(u32 addr);
PRIVATE void flush(CONSOLE* p_con);
extern int color;
extern unsigned int tabs[100];
extern int tabs_len;
extern int isESC;
int escenter;
/*======================================================================*
			   init_screen
 *======================================================================*/
PUBLIC void init_screen(TTY* p_tty)
{
	int nr_tty = p_tty - tty_table;
	p_tty->p_console = console_table + nr_tty;

	int v_mem_size = V_MEM_SIZE >> 1;	/* 显存总大小 (in WORD) */

	int con_v_mem_size                   = v_mem_size / NR_CONSOLES;
	p_tty->p_console->original_addr      = nr_tty * con_v_mem_size;
	p_tty->p_console->v_mem_limit        = con_v_mem_size;
	p_tty->p_console->current_start_addr = p_tty->p_console->original_addr;

	/* 默认光标位置在最开始处 */
	p_tty->p_console->cursor = p_tty->p_console->original_addr;

	if (nr_tty == 0) {
		/* 第一个控制台沿用原来的光标位置 */
		p_tty->p_console->cursor = disp_pos / 2;
		disp_pos = 0;
	}
	else {
		out_char(p_tty->p_console, nr_tty + '0');
		out_char(p_tty->p_console, '#');
	}

	set_cursor(p_tty->p_console->cursor);
char_num=0;
key_num=0;
seek_num=0;
seek=0;
isESC=0;
escenter=0;
}


/*======================================================================*
			   is_current_console
*======================================================================*/
PUBLIC int is_current_console(CONSOLE* p_con)
{
	return (p_con == &console_table[nr_current_console]);
}


/*======================================================================*
			   out_char
 *======================================================================*/
PUBLIC void out_char(CONSOLE* p_con, char ch)
{
	u8* p_vmem = (u8*)(V_MEM_BASE + p_con->cursor * 2);
        char ch_color =  DEFAULT_CHAR_COLOR;
	switch(ch) {
	case '\n':
		if (p_con->cursor < p_con->original_addr +
		    p_con->v_mem_limit - SCREEN_WIDTH) {
			p_con->cursor = p_con->original_addr + SCREEN_WIDTH * 
				((p_con->cursor - p_con->original_addr) /
				 SCREEN_WIDTH + 1);
		}

		break;
	case '\b':
		if (p_con->cursor > p_con->original_addr) {
                 if (tabs_len > 0 && p_con->cursor == tabs[tabs_len-1]+4)
                  {
//remove tab
                   --tabs_len;
                   p_con->cursor -= 4;
                  }
                  else  
                 {
//remove 1 char
                 p_con->cursor--;

                 *(p_vmem-2) = ' ';
	         *(p_vmem-1) = DEFAULT_CHAR_COLOR;
                 p_vmem = (u8*)(V_MEM_BASE + p_con->cursor * 2);
                   if ((p_con->cursor - p_con->original_addr+1) % SCREEN_WIDTH  == 0){//huanhang
                        while (*(p_vmem-2) == ' ' && p_con->cursor > 0)
                        {
                         p_con->cursor--;
                         p_vmem = (u8*)(V_MEM_BASE + p_con->cursor * 2);
                         if ((p_con->cursor - p_con->original_addr) % SCREEN_WIDTH  == 0) break;
                        }
}else{
if(seek==1){
key_num--;
}else{
char_num--;
}
}
                 }
		   }

		break;
	case '\f'://进入查找模式
		seek=1;
key_num=0;
		break;
	case '\a': {//退出超找模式，清除关键字
		int n;

		for(n=0;n<key_num;n++){
			if (p_con->cursor > p_con->original_addr) {
  p_con->cursor--;
                 *(p_vmem-2) = ' ';
	       *(p_vmem-1) = DEFAULT_CHAR_COLOR;
                 p_vmem = (u8*)(V_MEM_BASE + p_con->cursor * 2);
                
                 }
//

			}
		
		//恢复颜色
int p,q;
        for(p=0;p<seek_num;p++){
			u8* s_vmem = (u8*)(V_MEM_BASE + seek_pos[p] * 2);
			for(q=0;q<key_num;q++){
				    s_vmem++;
					*s_vmem ++ =DEFAULT_CHAR_COLOR;
				}
		}


		//查找的关键字组清零
		key_num=0;
		seek_num=0;
		seek=0;
}
		break;
case '\t':{//进入查找模式后按回车
		int j,k;
		int isKey=0;
		for(j=0;j<char_num;j++){
			isKey=1;
			for(k=j;k<j+key_num;k++){
				if(outchar[k]!=seek_key[k-j]){
					isKey=0;
				}
			}
			if(isKey==1){
				seek_pos[seek_num]=char_pos[j];
				seek_num++;
				int m;
				u8* c_vmem = (u8*)(V_MEM_BASE + char_pos[j] * 2);
				for(m=0;m<key_num;m++){
				    c_vmem++;
					*c_vmem++ = 0x0C;
				}
			}
		}
}
		break;
	default:

		if (p_con->cursor <
		    p_con->original_addr + p_con->v_mem_limit - 1) {
                        switch(color)
                        {
                        case 0: 
                          ch_color = DEFAULT_CHAR_COLOR;
                          break;
                        case 1:
                          ch_color = 0x0C;
                          break;
                        case 2:
                          ch_color = 0x0A;
                          break;
                        case 3:
                          ch_color = 0x03;
                          break;
                        case 4:
                          ch_color = 0x06;
                          break;
                        }
			*p_vmem++ = ch;
			*p_vmem++ = ch_color;
if(seek==1){
				seek_key[key_num]=ch;
				key_num++;
			}else{
				outchar[char_num]=ch;
				char_pos[char_num]=p_con->cursor;
				char_num++;
			}
			p_con->cursor++;
		}
		break;
	}

	while (p_con->cursor >= p_con->current_start_addr + SCREEN_SIZE) {
		scroll_screen(p_con, SCR_DN);
	}

	flush(p_con);
}

/*======================================================================*
                           flush
*======================================================================*/
PRIVATE void flush(CONSOLE* p_con)
{
        set_cursor(p_con->cursor);
        set_video_start_addr(p_con->current_start_addr);
}

/*======================================================================*
			    set_cursor
 *======================================================================*/
PRIVATE void set_cursor(unsigned int position)
{
	disable_int();
	out_byte(CRTC_ADDR_REG, CURSOR_H);
	out_byte(CRTC_DATA_REG, (position >> 8) & 0xFF);
	out_byte(CRTC_ADDR_REG, CURSOR_L);
	out_byte(CRTC_DATA_REG, position & 0xFF);
	enable_int();
}

/*======================================================================*
			  set_video_start_addr
 *======================================================================*/
PRIVATE void set_video_start_addr(u32 addr)
{
	disable_int();
	out_byte(CRTC_ADDR_REG, START_ADDR_H);
	out_byte(CRTC_DATA_REG, (addr >> 8) & 0xFF);
	out_byte(CRTC_ADDR_REG, START_ADDR_L);
	out_byte(CRTC_DATA_REG, addr & 0xFF);
	enable_int();
}



/*======================================================================*
			   select_console
 *======================================================================*/
PUBLIC void select_console(int nr_console)	/* 0 ~ (NR_CONSOLES - 1) */
{
	if ((nr_console < 0) || (nr_console >= NR_CONSOLES)) {
		return;
	}

	nr_current_console = nr_console;

	set_cursor(console_table[nr_console].cursor);
	set_video_start_addr(console_table[nr_console].current_start_addr);
}

/*======================================================================*
			   scroll_screen
 *----------------------------------------------------------------------*
 滚屏.
 *----------------------------------------------------------------------*
 direction:
	SCR_UP	: 向上滚屏
	SCR_DN	: 向下滚屏
	其它	: 不做处理
 *======================================================================*/
PUBLIC void scroll_screen(CONSOLE* p_con, int direction)
{
	if (direction == SCR_UP) {
		if (p_con->current_start_addr > p_con->original_addr) {
			p_con->current_start_addr -= SCREEN_WIDTH;
		}
	}
	else if (direction == SCR_DN) {
		if (p_con->current_start_addr + SCREEN_SIZE <
		    p_con->original_addr + p_con->v_mem_limit) {
			p_con->current_start_addr += SCREEN_WIDTH;
		}
	}
	else{
	}

	set_video_start_addr(p_con->current_start_addr);
	set_cursor(p_con->cursor);
}

