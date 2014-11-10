#include <stdio.h>
#include<stdlib.h>
#define top mem[0]
#define base mem[1]
#define jumpReg mem[2]
#define membase 3
int mem[2000];int main() {top = membase;mem[top] = 0;base = top + 1;
goto main;
main:
top = base + 4;
mem[base+1]=0;
mem[base+0]=1;
mem[base+3]=mem[base+0]+mem[base+1];

mem[base+2]=mem[base+3];
printf("19 s=%2d\n",mem[base+2]);

label0:
jumpReg = mem[base-1];
goto jumpTable;
jumpTable:
switch(jumpReg) { case 0: exit(0);
default: exit(0);
}
}