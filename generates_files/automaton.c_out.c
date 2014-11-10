#include <stdio.h>
#define read_cs254(x_cs254)scanf ("%d" ,&x_cs254)
#define write_cs254(x_cs254)printf ("%d\n" ,x_cs254)
#include<stdlib.h>
#define top mem[0]
#define base mem[1]
#define jumpReg mem[2]
#define membase 3
int mem[2000];int main() {top = membase;mem[top] = 0;base = top + 1;
goto main;
getnextdigit_cs254:
top = base + 4;
mem[base+1]=0==0;
goto label2;
label1:
printf("Give me a number (-1 to quit): ");

read_cs254(mem[base+0]);
mem[base+1]=-1<=mem[base+0];
mem[base+2]=1>=mem[base+0];
mem[base+3]=mem[base+1]&&mem[base+2];
if(mem[base+3])
goto label4;
goto label5;
label4:
goto label3;

goto label5;
label5:

printf("I need a number that's either 0 or 1.\n");

mem[base+1]=0==0;
label2:
if(mem[base+1])
goto label1;
label3:


mem[base-2] = mem[base+0];goto label0;

label0:
top = mem[base-3];
jumpReg = mem[base-1];
base = mem[base-4];
goto jumpTable;
state_0_cs254:
top = base + 2;
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 1;
base = top + 4;
goto getnextdigit_cs254;
label_1:
mem[base+1]=mem[top +2];

mem[base+0]=mem[base+1];
mem[base+1]=-1==mem[base+0];
if(mem[base+1])
goto label7;
goto label8;
label7:
printf("You gave me an even number of 0's.\n");

printf("You gave me an even number of 1's.\n");

printf("I therefore accept this input.\n");

goto label6;

goto label8;
label8:

mem[base+1]=0==mem[base+0];
if(mem[base+1])
goto label9;
goto label10;
label9:
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 2;
base = top + 4;
goto state_2_cs254;
label_2:

goto label10;
label10:

mem[base+1]=1==mem[base+0];
if(mem[base+1])
goto label11;
goto label12;
label11:
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 3;
base = top + 4;
goto state_1_cs254;
label_3:

goto label12;
label12:

label6:
top = mem[base-3];
jumpReg = mem[base-1];
base = mem[base-4];
goto jumpTable;
state_1_cs254:
top = base + 2;
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 4;
base = top + 4;
goto getnextdigit_cs254;
label_4:
mem[base+1]=mem[top +2];

mem[base+0]=mem[base+1];
mem[base+1]=-1==mem[base+0];
if(mem[base+1])
goto label14;
goto label15;
label14:
printf("You gave me an even number of 0's.\n");

printf("You gave me an odd number of 1's.\n");

printf("I therefore reject this input.\n");

goto label13;

goto label15;
label15:

mem[base+1]=0==mem[base+0];
if(mem[base+1])
goto label16;
goto label17;
label16:
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 5;
base = top + 4;
goto state_3_cs254;
label_5:

goto label17;
label17:

mem[base+1]=1==mem[base+0];
if(mem[base+1])
goto label18;
goto label19;
label18:
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 6;
base = top + 4;
goto state_0_cs254;
label_6:

goto label19;
label19:

label13:
top = mem[base-3];
jumpReg = mem[base-1];
base = mem[base-4];
goto jumpTable;
state_2_cs254:
top = base + 2;
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 7;
base = top + 4;
goto getnextdigit_cs254;
label_7:
mem[base+1]=mem[top +2];

mem[base+0]=mem[base+1];
mem[base+1]=-1==mem[base+0];
if(mem[base+1])
goto label21;
goto label22;
label21:
printf("You gave me an odd number of 0's.\n");

printf("You gave me an even number of 1's.\n");

printf("I therefore reject this input.\n");

goto label20;

goto label22;
label22:

mem[base+1]=0==mem[base+0];
if(mem[base+1])
goto label23;
goto label24;
label23:
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 8;
base = top + 4;
goto state_0_cs254;
label_8:

goto label24;
label24:

mem[base+1]=1==mem[base+0];
if(mem[base+1])
goto label25;
goto label26;
label25:
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 9;
base = top + 4;
goto state_3_cs254;
label_9:

goto label26;
label26:

label20:
top = mem[base-3];
jumpReg = mem[base-1];
base = mem[base-4];
goto jumpTable;
state_3_cs254:
top = base + 2;
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 10;
base = top + 4;
goto getnextdigit_cs254;
label_10:
mem[base+1]=mem[top +2];

mem[base+0]=mem[base+1];
mem[base+1]=-1==mem[base+0];
if(mem[base+1])
goto label28;
goto label29;
label28:
printf("You gave me an odd number of 0's.\n");

printf("You gave me an odd number of 1's.\n");

printf("I therefore reject this input.\n");

goto label27;

goto label29;
label29:

mem[base+1]=0==mem[base+0];
if(mem[base+1])
goto label30;
goto label31;
label30:
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 11;
base = top + 4;
goto state_1_cs254;
label_11:

goto label31;
label31:

mem[base+1]=1==mem[base+0];
if(mem[base+1])
goto label32;
goto label33;
label32:
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 12;
base = top + 4;
goto state_2_cs254;
label_12:

goto label33;
label33:

label27:
top = mem[base-3];
jumpReg = mem[base-1];
base = mem[base-4];
goto jumpTable;
main:
top = base + 0;
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 13;
base = top + 4;
goto state_0_cs254;
label_13:

label34:
jumpReg = mem[base-1];
goto jumpTable;
jumpTable:
switch(jumpReg) { case 0: exit(0);
case 1:
goto label_1;
case 2:
goto label_2;
case 3:
goto label_3;
case 4:
goto label_4;
case 5:
goto label_5;
case 6:
goto label_6;
case 7:
goto label_7;
case 8:
goto label_8;
case 9:
goto label_9;
case 10:
goto label_10;
case 11:
goto label_11;
case 12:
goto label_12;
case 13:
goto label_13;
default: exit(0);
}
}