#include <stdio.h>
#define read_cs254(x_cs254)scanf ("%d" ,&x_cs254)
#define write_cs254(x_cs254)printf ("%d\n" ,x_cs254)
#include<stdlib.h>
#define top mem[1]
#define base mem[2]
#define jumpReg mem[3]
#define membase 4
int mem[2000];int main() {top = membase;mem[top] = 0;base = top + 1;
goto main;
Calculate_cs254:
top = base + 3;
mem[base+0]=mem[base-5]>0;
if(mem[base+0])
goto label1;
goto label2;
label1:
mem[base+0]=mem[base-5]-1;
mem[top+0] = mem[base+0];
mem[top+1] = base;
mem[top+2] = top;
mem[top+4] = 1;
base = top + 5;
goto Calculate_cs254;
label_1:
mem[base+1]=mem[top +3];
mem[base+2]=mem[base+1]+42;

mem[base-2] = mem[base+2];goto label0;

goto label2;
label2:


mem[base-2] = 0;goto label0;

label0:
top = mem[base-3];
jumpReg = mem[base-1];
base = mem[base-4];
goto jumpTable;
main:
top = base + 2;
printf("Magic positive number is ");

read_cs254(mem[0]);
printf("The meaning of Life is ");

mem[top+0] = mem[0];
mem[top+1] = base;
mem[top+2] = top;
mem[top+4] = 2;
base = top + 5;
goto Calculate_cs254;
label_2:
mem[base+0]=mem[top +3];
mem[base+1]=mem[base+0]/mem[0];
write_cs254(mem[base+1]);
label3:
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