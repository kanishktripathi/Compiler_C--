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
square_cs254:
top = base + 3;
mem[base+0]=mem[base-5]*mem[base-5];
mem[base+1]=mem[base+0]+500;
mem[base+2]=mem[base+1]/1000;

mem[base-2] = mem[base+2];goto label0;

label0:
top = mem[base-3];
jumpReg = mem[base-1];
base = mem[base-4];
goto jumpTable;
complex_abs_squared_cs254:
top = base + 3;
mem[top+0] = mem[base-6];
mem[top+1] = base;
mem[top+2] = top;
mem[top+4] = 1;
base = top + 5;
goto square_cs254;
label_1:
mem[base+0]=mem[top +3];
mem[top+0] = mem[base-5];
mem[top+1] = base;
mem[top+2] = top;
mem[top+4] = 2;
base = top + 5;
goto square_cs254;
label_2:
mem[base+1]=mem[top +3];
mem[base+2]=mem[base+0]+mem[base+1];

mem[base-2] = mem[base+2];goto label1;

label1:
top = mem[base-3];
jumpReg = mem[base-1];
base = mem[base-4];
goto jumpTable;
check_for_bail_cs254:
top = base + 3;
mem[base+0]=mem[base-6]>4000;
mem[base+1]=mem[base-5]>4000;
mem[base+2]=mem[base+0]||mem[base+1];
if(mem[base+2])
goto label3;
goto label4;
label3:

mem[base-2] = 0;goto label2;

goto label4;
label4:

mem[top+0] = mem[base-6];
mem[top+1] = mem[base-5];
mem[top+2] = base;
mem[top+3] = top;
mem[top+5] = 3;
base = top + 6;
goto complex_abs_squared_cs254;
label_3:
mem[base+0]=mem[top +4];
mem[base+1]=1600>mem[base+0];
if(mem[base+1])
goto label5;
goto label6;
label5:

mem[base-2] = 0;goto label2;

goto label6;
label6:


mem[base-2] = 1;goto label2;

label2:
top = mem[base-3];
jumpReg = mem[base-1];
base = mem[base-4];
goto jumpTable;
absval_cs254:
top = base + 1;
mem[base+0]=mem[base-5]<0;
if(mem[base+0])
goto label8;
goto label9;
label8:
mem[base+0]=-1*mem[base-5];

mem[base-2] = mem[base+0];goto label7;

goto label9;
label9:


mem[base-2] = mem[base-5];goto label7;

label7:
top = mem[base-3];
jumpReg = mem[base-1];
base = mem[base-4];
goto jumpTable;
checkpixel_cs254:
top = base + 10;
mem[base+0]=0;
mem[base+1]=0;
mem[base+3]=0;
mem[base+4]=16000;
mem[base+5]=mem[base+3]<255;
goto label12;
label11:
mem[top+0] = mem[base+0];
mem[top+1] = base;
mem[top+2] = top;
mem[top+4] = 4;
base = top + 5;
goto square_cs254;
label_4:
mem[base+5]=mem[top +3];
mem[top+0] = mem[base+1];
mem[top+1] = base;
mem[top+2] = top;
mem[top+4] = 5;
base = top + 5;
goto square_cs254;
label_5:
mem[base+6]=mem[top +3];
mem[base+7]=mem[base+5]-mem[base+6];
mem[base+8]=mem[base+7]+mem[base-6];

mem[base+2]=mem[base+8];
mem[base+5]=2*mem[base+0];
mem[base+6]=mem[base+5]*mem[base+1];
mem[base+7]=mem[base+6]+500;
mem[base+8]=mem[base+7]/1000;
mem[base+9]=mem[base+8]+mem[base-5];

mem[base+1]=mem[base+9];
mem[base+0]=mem[base+2];
mem[top+0] = mem[base+0];
mem[top+1] = base;
mem[top+2] = top;
mem[top+4] = 6;
base = top + 5;
goto absval_cs254;
label_6:
mem[base+5]=mem[top +3];
mem[top+0] = mem[base+1];
mem[top+1] = base;
mem[top+2] = top;
mem[top+4] = 7;
base = top + 5;
goto absval_cs254;
label_7:
mem[base+6]=mem[top +3];
mem[base+7]=mem[base+5]+mem[base+6];
mem[base+8]=mem[base+7]>5000;
if(mem[base+8])
goto label14;
goto label15;
label14:

mem[base-2] = 0;goto label10;

goto label15;
label15:

mem[base+5]=mem[base+3]+1;

mem[base+3]=mem[base+5];
mem[base+5]=mem[base+3]<255;
label12:
if(mem[base+5])
goto label11;


mem[base-2] = 1;goto label10;

label10:
top = mem[base-3];
jumpReg = mem[base-1];
base = mem[base-4];
goto jumpTable;
main:
top = base + 4;
mem[base+1]=950;
mem[base+3]=mem[base+1]>-950;
goto label18;
label17:
mem[base+0]=-2100;
mem[base+3]=mem[base+0]<1000;
goto label21;
label20:
mem[top+0] = mem[base+0];
mem[top+1] = mem[base+1];
mem[top+2] = base;
mem[top+3] = top;
mem[top+5] = 8;
base = top + 6;
goto checkpixel_cs254;
label_8:
mem[base+3]=mem[top +4];

mem[base+2]=mem[base+3];
mem[base+3]=1==mem[base+2];
if(mem[base+3])
goto label23;
goto label24;
label23:
printf("X");

goto label24;
label24:

mem[base+3]=0==mem[base+2];
if(mem[base+3])
goto label25;
goto label26;
label25:
printf(" ");

goto label26;
label26:

mem[base+3]=mem[base+0]+40;

mem[base+0]=mem[base+3];
mem[base+3]=mem[base+0]<1000;
label21:
if(mem[base+3])
goto label20;

printf("\n");

mem[base+3]=mem[base+1]-50;

mem[base+1]=mem[base+3];
mem[base+3]=mem[base+1]>-950;
label18:
if(mem[base+3])
goto label17;

label16:
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
default: exit(0);
}
}