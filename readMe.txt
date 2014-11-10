
This is a compiler which compiles and generates single function code for a C like language. We call the language C--.
It is a subset of the C language. The grammar is given in the file Context_free_grammar.txt
Some important files of the source code.

1. CodeGenerator.java : Generates the processed code. Removes all local variables and global variables and replaces them with a single array. 
	Local variable name: temp
	Global variable name: glo
	
	Add inter function code generation. A common variable added for all functions. Inter function code generated using stack generation.

2. LLParser.java : This file contains code for doing all the syntactic code checking. It also generates function and symbol tables for the CodeGenerator. Evaluates for a constant expressions in array declaration.

3. ExpressionSimplifier.java : Simplifies expression code. Stores and generates intermediate code for an expression evaluation. Returns the current expression 
value for an expression. Functionality added to handle inter function code generation.

4. Constants.java: New constants added to avoid hard coding

5. WhileLoopHandler.java: Stores the start, conditional and end labels for a while loop. The object of this class is maintained in a stack when traversing a while loop statement. This is done to handle the break and continue statements in the loop.

6. Run.java: This file calls the scanner and then the parser. It gets the generated code from the parser and writes to an output file.
The output file is saved in the same location as input file. The output file name is : <input file name>_out.c
Example: Input file: ab.c . Ouput : ab.c_out.c

7. Scanner.java : Tokenizes the program into different types of tokens (i.e. : variables, symbols, numbers, keywords)

Code comments are present in the java files wherever applicable.

Running the compiler:

Run the jar file using the command. java -jar Compiler.jar <filePath>



Test result summary:

All the test cases provided were run using the compiler. The result was an output file by name <input file name>_out.c. This file was compiled using gcc and
binary file thus generated was run. The generated program files and executables are submitted with the assignment.
The results were manually compiled in the result report as we had problem running the ruby script.
All the programs in the generated code were successfully compiled by gcc.

Here's the summary and comparison of the results with the provided results. Output of the programs run are also provided in test_results.txt:

Program			Result
=======================
ab.c			Pass
fibonacci.c		Pass
automaton.code		Pass
loop_for.c 		Parsing error: Invalid token:'='. ) expected. Since the for loop is not defined as a keyword or a 			rule in the grammar, its taken as a function and parsed accordingly. The '=' sign in the for loop 			gives the parse error
loop_while.c		Pass
automaton.c		Pass
mandel.c		Pass
meaningOfLife.c		Pass
tax.c			Pass
