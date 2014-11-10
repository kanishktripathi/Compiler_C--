#include <stdio.h>
#define read_cs254(x_cs254)scanf ("%d" ,&x_cs254)
#define write_cs254(x_cs254)printf ("%d\n" ,x_cs254)
#include<stdlib.h>
#define top mem[32]
#define base mem[33]
#define jumpReg mem[34]
#define membase 35
int mem[2000];int main() {top = membase;mem[top] = 0;base = top + 1;
goto main;
initialize_array_cs254:
top = base + 3;
mem[base+1]=32;
mem[base+0]=0;
mem[base+2]=mem[base+0]<mem[base+1];
goto label2;
label1:
mem[0+mem[base+0]]=-1;
mem[base+2]=mem[base+0]+1;

mem[base+0]=mem[base+2];
mem[base+2]=mem[base+0]<mem[base+1];
label2:
if(mem[base+2])
goto label1;

label0:
top = mem[base-3];
jumpReg = mem[base-1];
base = mem[base-4];
goto jumpTable;
fib_cs254:
top = base + 5;
mem[base+0]=mem[base-5]<2;
if(mem[base+0])
goto label5;
goto label6;
label5:

mem[base-2] = 1;goto label4;

goto label6;
label6:

mem[base+0]=mem[0+mem[base-5]]==-1;
if(mem[base+0])
goto label7;
goto label8;
label7:
mem[base+0]=mem[base-5]-1;
mem[top+0] = mem[base+0];
mem[top+1] = base;
mem[top+2] = top;
mem[top+4] = 1;
base = top + 5;
goto fib_cs254;
label_1:
mem[base+1]=mem[top +3];
mem[base+2]=mem[base-5]-2;
mem[top+0] = mem[base+2];
mem[top+1] = base;
mem[top+2] = top;
mem[top+4] = 2;
base = top + 5;
goto fib_cs254;
label_2:
mem[base+3]=mem[top +3];
mem[base+4]=mem[base+1]+mem[base+3];

mem[0+mem[base-5]]=mem[base+4];
goto label8;
label8:


mem[base-2] = mem[0+mem[base-5]];goto label4;

label4:
top = mem[base-3];
jumpReg = mem[base-1];
base = mem[base-4];
goto jumpTable;
main:
top = base + 3;
mem[base+1]=32;
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 3;
base = top + 4;
goto initialize_array_cs254;
label_3:

mem[base+0]=0;
printf("The first few digits of the Fibonacci sequence are:\n");

mem[base+2]=mem[base+0]<mem[base+1];
goto label11;
label10:
mem[top+0] = mem[base+0];
mem[top+1] = base;
mem[top+2] = top;
mem[top+4] = 4;
base = top + 5;
goto fib_cs254;
label_4:
mem[base+2]=mem[top +3];
write_cs254(mem[base+2]);
mem[base+2]=mem[base+0]+1;

mem[base+0]=mem[base+2];
mem[base+2]=mem[base+0]<mem[base+1];
label11:
if(mem[base+2])
goto label10;

label9:
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
default: exit(0);
}
}