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
recursedigit_cs254:
top = base + 5;
mem[base+1]=0==mem[base-5];
if(mem[base+1])
goto label1;
goto label2;
label1:
goto label0;

goto label2;
label2:

mem[base+0]=0;
mem[base+1]=mem[base-5]/2;
mem[base+2]=mem[base+1]*2;
mem[base+3]=mem[base-5]-mem[base+2];
mem[base+4]=0!=mem[base+3];
if(mem[base+4])
goto label3;
goto label4;
label3:
mem[base+0]=1;
goto label4;
label4:

mem[base+1]=mem[base-5]/2;
mem[top+0] = mem[base+1];
mem[top+1] = base;
mem[top+2] = top;
mem[top+4] = 1;
base = top + 5;
goto recursedigit_cs254;
label_1:

mem[base+1]=0==mem[base+0];
if(mem[base+1])
goto label5;
goto label6;
label5:
printf("0");

goto label6;
label6:

mem[base+1]=1==mem[base+0];
if(mem[base+1])
goto label7;
goto label8;
label7:
printf("1");

goto label8;
label8:

label0:
top = mem[base-3];
jumpReg = mem[base-1];
base = mem[base-4];
goto jumpTable;
main:
top = base + 2;
mem[base+1]=0>=mem[base+0];
goto label11;
label10:
printf("Give me a number: ");

read_cs254(mem[base+0]);
mem[base+1]=0>=mem[base+0];
if(mem[base+1])
goto label13;
goto label14;
label13:
printf("I need a positive integer.\n");

goto label14;
label14:

mem[base+1]=0>=mem[base+0];
label11:
if(mem[base+1])
goto label10;

printf("The binary representation of: ");

write_cs254(mem[base+0]);
printf("is: ");

mem[top+0] = mem[base+0];
mem[top+1] = base;
mem[top+2] = top;
mem[top+4] = 2;
base = top + 5;
goto recursedigit_cs254;
label_2:

printf("\n\n");

label9:
jumpReg = mem[base-1];
goto jumpTable;
jumpTable:
switch(jumpReg) { case 0: exit(0);
case 1:
goto label_1;
case 2:
goto label_2;
default: exit(0);
}
}