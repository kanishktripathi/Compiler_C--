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
getinput_cs254:
top = base + 2;
mem[base+0]=-1;
mem[base+1]=0>mem[base+0];
goto label2;
label1:
read_cs254(mem[base+0]);
mem[base+1]=0>mem[base+0];
if(mem[base+1])
goto label4;
goto label5;
label4:
printf("I need a non-negative number: ");

goto label5;
label5:

mem[base+1]=0>mem[base+0];
label2:
if(mem[base+1])
goto label1;


mem[base-2] = mem[base+0];goto label0;

label0:
top = mem[base-3];
jumpReg = mem[base-1];
base = mem[base-4];
goto jumpTable;
main:
top = base + 28;
printf("Welcome to the United States 1040 federal income tax program.\n");

printf("(Note: this isn't the real 1040 form. If you try to submit your\n");

printf("taxes this way, you'll get what you deserve!\n\n");

printf("Answer the following questions to determine what you owe.\n\n");

printf("Total wages, salary, and tips? ");

mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 1;
base = top + 4;
goto getinput_cs254;
label_1:
mem[base+25]=mem[top +2];

mem[base+0]=mem[base+25];
printf("Taxable interest (such as from bank accounts)? ");

mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 2;
base = top + 4;
goto getinput_cs254;
label_2:
mem[base+25]=mem[top +2];

mem[base+1]=mem[base+25];
printf("Unemployment compensation, qualified state tuition, and Alaska\n");

printf("Permanent Fund dividends? ");

mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 3;
base = top + 4;
goto getinput_cs254;
label_3:
mem[base+25]=mem[top +2];

mem[base+2]=mem[base+25];
mem[base+25]=mem[base+0]+mem[base+1];
mem[base+26]=mem[base+25]+mem[base+2];

mem[base+3]=mem[base+26];
printf("Your adjusted gross income is: ");

write_cs254(mem[base+3]);
printf("Enter <1> if your parents or someone else can claim you on their");

printf(" return. \nEnter <0> otherwise: ");

mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 4;
base = top + 4;
goto getinput_cs254;
label_4:
mem[base+25]=mem[top +2];

mem[base+14]=mem[base+25];
mem[base+25]=0!=mem[base+14];
if(mem[base+25])
goto label7;
goto label8;
label7:
mem[base+25]=mem[base+0]+250;

mem[base+16]=mem[base+25];
mem[base+17]=700;
mem[base+18]=mem[base+17];
mem[base+25]=mem[base+18]<mem[base+16];
if(mem[base+25])
goto label9;
goto label10;
label9:
mem[base+18]=mem[base+16];
goto label10;
label10:

printf("Enter <1> if you are single, <0> if you are married: ");

mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 5;
base = top + 4;
goto getinput_cs254;
label_5:
mem[base+25]=mem[top +2];

mem[base+15]=mem[base+25];
mem[base+25]=0!=mem[base+15];
if(mem[base+25])
goto label11;
goto label13;
label11:
mem[base+19]=7350;
goto label12;
label13:
mem[base+19]=7350;
goto label12;
label12:

mem[base+20]=mem[base+18];
mem[base+25]=mem[base+20]>mem[base+19];
if(mem[base+25])
goto label14;
goto label15;
label14:
mem[base+20]=mem[base+19];
goto label15;
label15:

mem[base+21]=0;
mem[base+25]=mem[base+15]==0;
if(mem[base+25])
goto label16;
goto label17;
label16:
printf("Enter <1> if your spouse can be claimed as a dependant, ");

printf("enter <0> if not: ");

mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 6;
base = top + 4;
goto getinput_cs254;
label_6:
mem[base+25]=mem[top +2];

mem[base+24]=mem[base+25];
mem[base+25]=0==mem[base+24];
if(mem[base+25])
goto label18;
goto label19;
label18:
mem[base+21]=2800;
goto label19;
label19:

goto label17;
label17:

mem[base+25]=mem[base+20]+mem[base+21];

mem[base+22]=mem[base+25];
mem[base+4]=mem[base+22];
goto label8;
label8:

mem[base+25]=0==mem[base+14];
if(mem[base+25])
goto label20;
goto label21;
label20:
printf("Enter <1> if you are single, <0> if you are married: ");

mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 7;
base = top + 4;
goto getinput_cs254;
label_7:
mem[base+25]=mem[top +2];

mem[base+15]=mem[base+25];
mem[base+25]=0!=mem[base+15];
if(mem[base+25])
goto label22;
goto label23;
label22:
mem[base+4]=12950;
goto label23;
label23:

mem[base+25]=0==mem[base+15];
if(mem[base+25])
goto label24;
goto label25;
label24:
mem[base+4]=7200;
goto label25;
label25:

goto label21;
label21:

mem[base+25]=mem[base+3]-mem[base+4];

mem[base+5]=mem[base+25];
mem[base+25]=mem[base+5]<0;
if(mem[base+25])
goto label26;
goto label27;
label26:
mem[base+5]=0;
goto label27;
label27:

printf("Your taxable income is: ");

write_cs254(mem[base+5]);
printf("Enter the amount of Federal income tax withheld: ");

mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 8;
base = top + 4;
goto getinput_cs254;
label_8:
mem[base+25]=mem[top +2];

mem[base+6]=mem[base+25];
printf("Enter <1> if you get an earned income credit (EIC); ");

printf("enter 0 otherwise: ");

mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 9;
base = top + 4;
goto getinput_cs254;
label_9:
mem[base+25]=mem[top +2];

mem[base+23]=mem[base+25];
mem[base+7]=0;
mem[base+25]=0!=mem[base+23];
if(mem[base+25])
goto label28;
goto label29;
label28:
printf("OK, I'll give you a thousand dollars for your credit.\n");

mem[base+7]=1000;
goto label29;
label29:

mem[base+25]=mem[base+7]+mem[base+6];

mem[base+8]=mem[base+25];
printf("Your total tax payments amount to: ");

write_cs254(mem[base+8]);
mem[base+25]=mem[base+5]*28;
mem[base+26]=mem[base+25]+50;
mem[base+27]=mem[base+26]/100;

mem[base+9]=mem[base+27];
printf("Your total tax liability is: ");

write_cs254(mem[base+9]);
mem[base+25]=mem[base+8]-mem[base+9];

mem[base+10]=mem[base+25];
mem[base+25]=mem[base+10]<0;
if(mem[base+25])
goto label30;
goto label32;
label30:
mem[base+12]=0;
goto label31;
label32:
mem[base+12]=0;
goto label31;
label31:

mem[base+25]=mem[base+10]>0;
if(mem[base+25])
goto label33;
goto label34;
label33:
printf("Congratulations, you get a tax refund of $");

write_cs254(mem[base+10]);
goto label34;
label34:

mem[base+25]=mem[base+9]-mem[base+8];

mem[base+11]=mem[base+25];
mem[base+25]=mem[base+11]>=0;
if(mem[base+25])
goto label35;
goto label36;
label35:
printf("Bummer. You owe the IRS a check for $");

write_cs254(mem[base+11]);
goto label36;
label36:

mem[base+25]=mem[base+11]<0;
if(mem[base+25])
goto label37;
goto label39;
label37:
mem[base+13]=0;
goto label38;
label39:
mem[base+13]=0;
goto label38;
label38:

write_cs254(mem[base+5]);
write_cs254(mem[base+8]);
write_cs254(mem[base+9]);
write_cs254(mem[base+17]);
write_cs254(mem[base+20]);
write_cs254(mem[base+19]);
write_cs254(mem[base+12]);
write_cs254(mem[base+13]);
mem[base+5]=mem[base+9];
mem[base+7]=0;
mem[base+9]=0;
mem[base+25]=mem[base+17]+mem[base+13];

mem[base+12]=mem[base+25];
mem[base+25]=mem[base+20]+mem[base+19];

mem[base+13]=mem[base+25];
printf("Thank you for using ez-tax.\n");

label6:
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
default: exit(0);
}
}