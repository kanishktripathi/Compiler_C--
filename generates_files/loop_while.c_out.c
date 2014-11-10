#include <stdio.h>
// This test should be okay, since while statements are defined.
#include<stdlib.h>
#define top mem[0]
#define base mem[1]
#define jumpReg mem[2]
#define membase 3
int mem[2000];int main() {top = membase;mem[top] = 0;base = top + 1;
goto main;
main:
top = base + 2;
mem[base+0]=0;
mem[base+1]=mem[base+0]<10;
goto label2;
label1:
printf("hello\n");

mem[base+1]=mem[base+0]+1;

mem[base+0]=mem[base+1];
mem[base+1]=mem[base+0]<10;
label2:
if(mem[base+1])
goto label1;


mem[base-2] = 0;goto label0;

label0:
jumpReg = mem[base-1];
goto jumpTable;
jumpTable:
switch(jumpReg) { case 0: exit(0);
default: exit(0);
}
}