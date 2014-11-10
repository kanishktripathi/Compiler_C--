package edu.rochester.cs454;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.rochester.cs454.Token.TokenType;

/**
 * The class generates inter function code. In the generated code, all the local variables are changes to a
 * single array mem[<constant>]. There is only a single main function and no other functions. All the other
 * function calls are generated using goto statements and jump table. The functions parameters and values
 * are maintained in a memory stack and they are navigated using top and base references.
 * All the parameters are passed by value.
 * The language is a subset of C 
 * programming language. 
 * 
 */
public class CodeGenerator {
	
	/** The array of tokens returned from the scanner. */
	private Token[] tokens;
	
	/** The current index of the token array when parsing. */
	private int currentIndex;
	
	/** The String builder for generated code. */
	private StringBuilder codeGen;
	
	/** The label counter. Counter for generating goto labels */
	private int labelCounter;
	
	/** The label counter. Counter for generating goto labels */
	private int functionLabelCounter;
	
	/** The function and symbol table. */
	private Map<String, Map<String, Integer>> functionAndSymbolTable;
	
	/** The global declaration table. */
	private Map<String, Integer> globalDeclarationTable;
	
	/** The current symbol table. Current symbol table traversing*/
	private Map<String, Integer> currentSymbolTable;
	
	/** The current function base. The base index for temporary variables inside a function */
	private int currentFunctionBase;
	
	/** Stack to track the current loop traversal. Used for generating goto labels in case
	 * of break and continue statements*/
	private Deque<WhileLoopHandler> loopStack;
	
	/**
	 * Instantiates a new LL parser. On the basis of token list.
	 *
	 * @param tokens the tokens
	 * @param functionAndSymbolTable the function and symbol table
	 * @param globalDeclarationTable the global declaration table
	 */
	public CodeGenerator(Token[] tokens, Map<String, Map<String, Integer>> functionAndSymbolTable, 
			Map<String, Integer> globalDeclarationTable) {
		this.tokens = tokens;
		codeGen = new StringBuilder();
		this.loopStack = new ArrayDeque<>();
		this.currentIndex = 0;
		this.functionLabelCounter = 0;
		this.functionAndSymbolTable = functionAndSymbolTable;
		this.globalDeclarationTable = globalDeclarationTable;
	}

	/**
	 * Generates the assembly language equivalent code for the language.
	 *
	 * @return the string
	 * @throws ParsingException the parsing exception
	 */
	public String generateCode() throws ParsingException  {
		try {			
			checkMetaStatements();
			initProgram();
			program();
			generateJumpTable();
			//System.out.println(sb.toString());
		} catch(IndexOutOfBoundsException e){
			e.printStackTrace();
			throw new ParsingException(e.getMessage());
		}
		return codeGen.toString();
	}
	
	/**
	 * The starting function of the code generator.
	 *
	 * @throws ParsingException the parsing exception
	 */
	private void program() throws ParsingException {
		String identifier;
		if(matchAndIncrement(Constants.VOID)) {
			identifier = matchNextToken();
			matchNextToken();
			functionDefinition(identifier);
		} else if(checkAndIncrementToken(Constants.TYPES)) {
			identifier = matchNextToken();
			functionOrDataDeclaration(identifier);
		}
	}
	
	/**
	 * Parses the complete function declaration
	 * the declaration is a function or not.
	 *
	 * @param identifier the identifier
	 * @throws ParsingException the parsing exception
	 */
	private void functionDefinition(String identifier) throws ParsingException {
		this.currentSymbolTable = functionAndSymbolTable.get(identifier);
		this.currentFunctionBase = this.currentSymbolTable.get(Constants.BASE_COUNT);
		incrementTokens(Constants.RIGHT_PAREN);
		functionBody(identifier);			
		functionList();
	}
		
	/**
	 * Function list. Starts parsing of next function if end of file has not been reached
	 * @throws ParsingException the parsing exception
	 */
	private void functionList() throws ParsingException {
		if(!isEnd()) {
			matchNextToken();
			String identifier = matchNextToken();
			functionDefinition(identifier);
		}
	}
	
	/**
	 * Function body. Generates function body code
	 *
	 * @param identifier the identifier
	 * @throws ParsingException the parsing exception
	 */
	private void functionBody(String identifier) throws ParsingException {
		if(!matchAndIncrement(Constants.SEMICOLON)) {
			codeGen.append(identifier).append(Constants.COLON).append('\n');
			matchNextToken();
			while(Constants.TYPES.contains(getcurrentValue())) {
				incrementTokens(Constants.SEMICOLON);
			}
			int top = currentSymbolTable.get(Constants.BASE_COUNT) + currentSymbolTable.get(Constants.OPERAND_COUNT);
			codeGen.append("top = base + ").append(top).append(";\n");
			String value = getcurrentValue();
			String returnLabel = getLabel();
			while(!Constants.RIGHT_BRACE.equals(value)) {
				statements(returnLabel);
				value = getcurrentValue();
			}
			matchNextToken();
			if(Constants.MAIN.equals(identifier)) {
				codeGen.append(returnLabel).append(":\n").append("jumpReg = mem[base-1];\ngoto jumpTable;\n");
			} else {				
				codeGen.append(returnLabel).append(":\n").append("top = mem[base-3];\njumpReg = mem[base-1];\n")
				.append("base = mem[base-4];\ngoto jumpTable;\n");		
			}
		}
	}
	
	/**
	 * Function or data declaration. Check to identify if the declaration is a function
	 * or a variable.
	 * Check for the rule::
	 *
	 * @param identifier the identifier
	 * @throws ParsingException the parsing exception
	 */
	private void functionOrDataDeclaration(String identifier) throws ParsingException {
		if(matchAndIncrement(Constants.LEFT_PAREN)) {
			functionDefinition(identifier);
		} else {
			incrementTokens(Constants.SEMICOLON);
			program();
		}
	}
	
	/**
	 * Statements. Traverses all the statements in a block 
	 *
	 * @param returnLabel the return label
	 * @throws ParsingException the parsing exception
	 */
	private void statements(String returnLabel) throws ParsingException {
			String value = getcurrentValue(), stringValue;
			ExpressionSimplifier simplify = null;
			String identifier;
			switch(value) {
				case Constants.PRINTF:
					incrementTokens(Constants.LEFT_PAREN);
					stringValue = matchNextToken();
					printfP(stringValue);
					incrementTokens(Constants.SEMICOLON);
					break;
				case Constants.SCANF:
					incrementTokens(Constants.LEFT_PAREN);
					stringValue = matchNextToken();
					matchNextToken();
					simplify = expression();
					codeGen.append(simplify.getSb());
					incrementTokens(Constants.SEMICOLON);
					codeGen.append(Constants.SCANF).append(Constants.LEFT_PAREN).append(stringValue).
					append(Constants.COMMA).append(simplify.getCurrentExpressionValue())
					.append(Constants.RIGHT_PAREN).append(Constants.SEMICOLON);
					break;
				case Constants.IF:
					incrementTokens(Constants.LEFT_PAREN);
					simplify = new ExpressionSimplifier(currentFunctionBase, 0);
					conditionExp(simplify);
					incrementTokens(Constants.RIGHT_PAREN);
					codeGen.append(simplify.getSb());
					codeGen.append(Constants.IF).append(Constants.LEFT_PAREN).
					append(simplify.getCurrentExpressionValue()).append(Constants.RIGHT_PAREN).append('\n');
					manageIf(returnLabel);
					break;
				case Constants.WHILE:
					// Generate goto statements for while loop
					String loopStart = getLabel();
					String loopTest = getLabel();
					String loopEnd = getLabel();
					incrementTokens(Constants.LEFT_PAREN);
					simplify = new ExpressionSimplifier(currentFunctionBase, 0);
					conditionExp(simplify);
					incrementTokens(Constants.RIGHT_PAREN);
					codeGen.append(simplify.getSb());
					loopStack.push(new WhileLoopHandler(loopTest, simplify.getSb().toString(), loopEnd));
					codeGen.append(Constants.GOTO).append(' ').append(loopTest).append(Constants.SEMICOLON).append('\n');
					codeGen.append(loopStart).append(Constants.COLON).append('\n');
					block(returnLabel);
					codeGen.append(simplify.getSb());
					codeGen.append(loopTest).append(Constants.COLON).append('\n');
					codeGen.append(Constants.IF).append(Constants.LEFT_PAREN).
					append(simplify.getCurrentExpressionValue()).append(Constants.RIGHT_PAREN).append('\n');
					codeGen.append(Constants.GOTO).append(' ').append(loopStart).append(Constants.SEMICOLON).append('\n');
					if(loopStack.pop().isFoundbreak()) {
						codeGen.append(loopEnd).append(Constants.COLON).append('\n');						
					}
/*					if(match(Constants.RIGHT_BRACE)) {
						codeGen.append(Constants.SEMICOLON).append('\n');
					}*/
					break;
				case Constants.RETURN:
					matchNextToken();
					if(!match(Constants.SEMICOLON)) {
						simplify = expression();
						codeGen.append(simplify.getSb()).append('\n');
						codeGen.append("mem[base-2] = ").append(simplify.getCurrentExpressionValue()).append(";");
					}
					codeGen.append("goto ").append(returnLabel).append(";\n");
					matchNextToken();
					break;						
				case Constants.BREAK:
					codeGen.append(Constants.GOTO).append(' ').append(loopStack.peek().getEndLabel()).
					append(Constants.SEMICOLON).append('\n');
					loopStack.peek().setFoundbreak(true);
					matchNextToken();
					matchNextToken();
					break;
				case Constants.CONTINUE:
					codeGen.append(loopStack.peek().getConditionCode()).append('\n');
					matchNextToken();
					matchNextToken();
					codeGen.append(Constants.GOTO).append(' ').append(loopStack.peek().getStartLabel()).
					append(Constants.SEMICOLON).append('\n');
					break;
				default:
					identifier = matchNextToken(); 
					assignOrFuncCall(identifier);
					matchNextToken();
					break;					
			}
			codeGen.append('\n');
	}
	
	/**
	 * Manage if. Change if else into goto statements
	 *
	 * @param returnLabel the return label
	 * @throws ParsingException the parsing exception
	 */
	private void manageIf(String returnLabel) throws ParsingException {
		String ifLabel = getLabel();
		String nextLabel = getLabel();
		codeGen.append(Constants.GOTO).append(' ').append(ifLabel).append(Constants.SEMICOLON).append('\n');
		StringBuilder stackStatements = codeGen;
		this.codeGen = new StringBuilder();
		codeGen.append(ifLabel).append(Constants.COLON).append('\n');
		block(returnLabel);
		codeGen.append(Constants.GOTO).append(' ').append(nextLabel).append(Constants.SEMICOLON).append('\n');
		if(matchAndIncrement(Constants.ELSE)) {
			String elseLabel = getLabel();
			stackStatements.append(Constants.GOTO).append(' ').append(elseLabel).append(Constants.SEMICOLON).append('\n');
			codeGen.append(elseLabel).append(Constants.COLON).append('\n');
			block(returnLabel);
			codeGen.append(Constants.GOTO).append(' ').append(nextLabel).append(Constants.SEMICOLON).append('\n');
		} else {
			stackStatements.append(Constants.GOTO).append(' ').append(nextLabel).append(Constants.SEMICOLON).append('\n');
		}
		codeGen.append(nextLabel).append(Constants.COLON).append('\n');
/*		//String nextValue = getcurrentValue();
		 If the if statement is the last statement in a function block, we generated a printf statement so that
		  the second label does not have empty code
		
		if(match(Constants.RIGHT_BRACE)) {
			codeGen.append(Constants.SEMICOLON).append('\n');
		}*/
		stackStatements.append(codeGen);
		this.codeGen = stackStatements;
	}
	
	/**
	 * Assign or func call. Checks for assignmet or a function call in a statement
	 * @param identifier the identifier
	 * @throws ParsingException the parsing exception
	 */
	private void assignOrFuncCall(String identifier) throws ParsingException {
		boolean isArray = false;
		ExpressionSimplifier simplifier = null;
		if(matchAndIncrement(Constants.LEFT_BRACKET)) {
			simplifier = expression();
			matchNextToken();
			isArray = true;
			identifier = getEquivalentIdentifier(identifier, simplifier.getCurrentExpressionValue());
		}
		if(matchAndIncrement(Constants.EQUAL)) {
			if(!isArray) {
				identifier = getEquivalentIdentifier(identifier, null);		
			}			
			ExpressionSimplifier currentSimplifier = new ExpressionSimplifier(currentFunctionBase, 0);
			currentSimplifier.mergeSimplifiers(simplifier);
			expression(currentSimplifier);
			if(currentSimplifier.getOperandCount() >= 1) {
				codeGen.append(currentSimplifier.getSb()).append('\n');				
			}
			codeGen.append(identifier).append(Constants.EQUAL).
			append(currentSimplifier.getCurrentExpressionValue()).append(Constants.SEMICOLON);
			return;
		} 
		if(matchAndIncrement(Constants.LEFT_PAREN) && !isArray) {
//			StringBuilder sb = new StringBuilder();
			ExpressionSimplifier currentSimplifier = new ExpressionSimplifier(currentFunctionBase, 0);
			currentSimplifier.mergeSimplifiers(simplifier);
			expressionList(currentSimplifier);
			codeGen.append(currentSimplifier.getSb());
			if(Constants.READ.equals(identifier) || Constants.WRITE.equals(identifier)) {
				codeGen.append(identifier).append(Constants.LEFT_PAREN).append(currentSimplifier.getCommaSeparatedExpression())
				.append(Constants.RIGHT_PAREN).append(Constants.SEMICOLON);
			} else {
				String functionProlog = generateFunctionProlog(identifier, currentSimplifier.getExpressionList());
				codeGen.append(functionProlog);
				
			}
			matchNextToken();
//			codeGen.append(sb.toString());
		}
	}
	
	/**
	 * Expression list. Generates parameter code for a function.
	 *
	 * @param simplifier the simplifier
	 * @throws ParsingException the parsing exception
	 */
	private void expressionList(ExpressionSimplifier simplifier) throws ParsingException {
		if(!match(Constants.RIGHT_PAREN)) {
			ExpressionSimplifier currentSimplifier = new ExpressionSimplifier(simplifier.getBaseIndex(), 
					simplifier.getOperandCount());
			expression(currentSimplifier);
			simplifier.mergeSimplifiers(currentSimplifier);
			simplifier.addExpressionList(currentSimplifier.getCurrentExpressionValue());
			if(matchAndIncrement(Constants.COMMA)) {
				expressionList(simplifier);
			}			
		}
	}
	
	/**
	 * Condition exp. Checks for conditions in a control statement(if, while)
	 *
	 * @param simplifier the simplifier
	 * @throws ParsingException the parsing exception
	 */
	private void conditionExp(ExpressionSimplifier simplifier) throws ParsingException {
		conditionEval(simplifier);
		String op = getcurrentValue();
		if(checkAndIncrementToken(Constants.COND_OP)) {
			ExpressionSimplifier currentSimplifier = new ExpressionSimplifier(simplifier.getBaseIndex(), 
					simplifier.getOperandCount());
			conditionExp(currentSimplifier);
			simplifier.process(currentSimplifier.getCurrentExpressionValue());
			simplifier.mergeSimplifiers(currentSimplifier);
			simplifier.operation(op);
		}
	}
	
	/**
	 * Evaluates a condition syntax.
	 * <condition> -> <expression><compare-op><expression>
	 *
	 * @param simplifier the simplifier
	 * @throws ParsingException the parsing exception
	 */
	private void conditionEval(ExpressionSimplifier simplifier) throws ParsingException {
		ExpressionSimplifier exp1 = new ExpressionSimplifier(simplifier.getBaseIndex(), 
				simplifier.getOperandCount()); 
		expression(exp1);
		simplifier.mergeSimplifiers(exp1);
		simplifier.process(exp1.getCurrentExpressionValue());
		String compOp = matchNextToken();
		ExpressionSimplifier exp2 = new ExpressionSimplifier(simplifier.getBaseIndex(), 
				simplifier.getOperandCount());
		expression(exp2);
		simplifier.process(exp2.getCurrentExpressionValue());
		simplifier.mergeSimplifiers(exp2);
		simplifier.operation(compOp);
	}
	
	/**
	 * Block. A statement block in a control statement
	 *
	 * @param returnLabel the return label
	 * @throws ParsingException the parsing exception
	 */
	private void block(String returnLabel) throws ParsingException {
		matchNextToken();
		String value = getcurrentValue();
		while(!Constants.RIGHT_BRACE.equals(value)) {
			statements(returnLabel);
			value = getcurrentValue();
		}
		matchNextToken();
	}
	
	/**
	 * Printf s. Checks for expression in printf statement
	 * <printf>' -> , <expression> | $
	 *
	 * @param stringValue the string value
	 * @throws ParsingException the parsing exception
	 */
	private void printfP(String stringValue) throws ParsingException {
		String exprValue = "";
		if(matchAndIncrement(Constants.COMMA)) {
			ExpressionSimplifier simplifier = null;
			simplifier = expression();
			codeGen.append(simplifier.getSb());
			exprValue = Constants.COMMA.concat(simplifier.getCurrentExpressionValue());
		}
		codeGen.append(Constants.PRINTF).append(Constants.LEFT_PAREN).append(stringValue).append(exprValue)
		.append(Constants.RIGHT_PAREN).append(Constants.SEMICOLON).append('\n');;
	}

	/**
	 * Ex. E
	 * The value returned from T is passed for subsequent evaluation.
	 * 
	 * @return The simplified expresssion
	 * @throws ParsingException the parsing exception
	 */
	private ExpressionSimplifier expression() throws ParsingException {
		return expression(null);
	}
	
	/**
	 * Expression.
	 *
	 * @param simplify the simplifier object
	 * @return the expression simplifier
	 * @throws ParsingException the parsing exception
	 */
	private ExpressionSimplifier expression(ExpressionSimplifier simplify) throws ParsingException {
		if(simplify == null) {
			simplify = new ExpressionSimplifier(this.currentFunctionBase, 0);
		}
		nonTerminal(simplify);
		addOpEx(simplify);
		return simplify;
	}
	
	/**
	 * Equivalent to grammar rule <expression>' ->  <add-op><terminal><expression>' |  $.
	 *
	 * @param simplify the simplify
	 * @return computer value
	 * @throws ParsingException the parsing exception
	 */
	private void addOpEx(ExpressionSimplifier simplify) throws ParsingException {
		String expr = getcurrentValue();
		if(checkAndIncrementToken(Constants.ADD_OP)) {
			nonTerminal(simplify);
			simplify.operation(expr);
			addOpEx(simplify);
		}
	}
	
	/**
	 * Non terminal. 
	 * <terminal> -> <factor><terminal>'
	 *
	 * @param simplify the simplify
	 * @return computer value
	 * @throws ParsingException the parsing exception
	 */
	private void nonTerminal(ExpressionSimplifier simplify) throws ParsingException {
		factor(simplify);
		multOpExpression(simplify);
	}
	
	/**
	 * Non terminal prime. T'
	 * Implementation of grammar rule <terminal>' -> <mult-op><factor><terminal>' | $
	 *
	 * @param simplify the simplify
	 * @return computer value
	 * @throws ParsingException the parsing exception
	 */
	private void multOpExpression(ExpressionSimplifier simplify) throws ParsingException {
		String expr = getcurrentValue();
		if(checkAndIncrementToken(Constants.MUL_OP)) {
			factor(simplify);
			simplify.operation(expr);
			multOpExpression(simplify);
		}
	}
	
	/**
	 * Terminal. F
	 * Implementation of rules:
	 * <factor> -> ID <ID'>| <NUM> | (<expression>) | <ID>' -> [ <expression> ] | (<expression list>)
	 * <NUM> -> -NUMBER | NUMBER
	 *
	 * @param simplify the simplify
	 * @return integer value
	 * @throws ParsingException the parsing exception
	 */
	private void factor(ExpressionSimplifier simplify) throws ParsingException {
		if(matchAndIncrement(Constants.LEFT_PAREN)) {
			expression(simplify);				
			matchNextToken();
		} else if(matchAndIncrement(Constants.MINUS)) {
			simplify.process(Constants.MINUS.concat(matchNextToken()));
		} else if(TokenType.NUMBERS.equals(getcurrentToken().getTokenType())) {
			simplify.process(matchNextToken());
		} else {
			String identifier = matchNextToken();
			ExpressionSimplifier currentSimplify;
			if(matchAndIncrement(Constants.LEFT_BRACKET)) {
				currentSimplify = new ExpressionSimplifier(simplify.getBaseIndex(), simplify.getOperandCount());
				expression(currentSimplify);
				matchNextToken();
				simplify.mergeSimplifiers(currentSimplify);
				identifier = getEquivalentIdentifier(identifier, currentSimplify.getCurrentExpressionValue());
			} else if(matchAndIncrement(Constants.LEFT_PAREN)) {
				currentSimplify = new ExpressionSimplifier(simplify.getBaseIndex(), simplify.getOperandCount());
				expressionList(currentSimplify);
				matchNextToken();
				simplify.mergeSimplifiers(currentSimplify);
				String functionProlog = generateFunctionProlog(identifier, currentSimplify.getExpressionList());
				identifier = simplify.simplifyFunctionCall(functionProlog, currentSimplify.getExpressionList().size());
			} else {
				identifier = getEquivalentIdentifier(identifier, null);
			}
			simplify.process(identifier);
		}
	}	
	
	/**
	 * Checks if is end.
	 * @return true, if is end
	 */
	private boolean isEnd() {
		return this.currentIndex == tokens.length;
	}
	
	/**
	 * Check white spaces.
	 */
	private void checkMetaStatements() {
		while(!isEnd() && tokens[currentIndex].isMetaStatement()) {
			codeGen.append(tokens[currentIndex].getValue());
			currentIndex++;
		}
	}
	
	/**
	 * Gets the current token.
	 * @return the current token
	 */
	private Token getcurrentToken() {
		return tokens[currentIndex];
	}
	
	/**
	 * Gets the current value.
	 * @return the current value
	 */
	private String getcurrentValue() {
		return tokens[currentIndex].getValue();
	}
	
	/**
	 *  Matches the current token with the matcher value and increments the index. 
	 *  Used for selecting a production rule.
	 * @param matcher the matcher
	 * @return true, if successful
	 */
	private boolean matchAndIncrement(String matcher) {
		String value = getcurrentValue();
		if(matcher.equals(value)) {
			currentIndex++;
			checkMetaStatements();
			return true;
		} else {
			return false;			
		}
	}
	
	/**
	 * Match. Matches the current token with the matcher value but does not increment the index
	 *
	 * @param matcher the matcher
	 * @return true, if successful
	 */
	private boolean match(String matcher) {
		return matcher.equals(getcurrentValue());
	}
	
	/**
	 * Gets the current value and increments the token traversal.
	 *
	 * @return the next token
	 * @throws ParsingException the parsing exception
	 */
	private String matchNextToken() throws ParsingException {
		String value = getcurrentValue();
		currentIndex++;
		checkMetaStatements();
		return value;
	}
	
	/**
	 * Checks if the increment token is present in the lookup set or not.
	 * if the value is present, then it increments the current index and returns true, else
	 * returns false. Used for selecting a production rule.
	 *
	 * @param lookupSet the lookup set
	 * @return true, if successful
	 */
	private boolean checkAndIncrementToken(Set<String> lookupSet) {
		String value = getcurrentValue();
		if(lookupSet.contains(value)) {
			currentIndex++;
			checkMetaStatements();
			return true;
		} else {
			return false;		
		}
	}
	
	/**
	 * Gets the equivalent symbol for an identifier from the symbol table.
	 * @param identifier the identifier
	 * @param arrIndex the arr index
	 * @return the equivalent identifier
	 */
	private String getEquivalentIdentifier(String identifier, String arrIndex) {
		String retuString = null;
		StringBuilder sb = new StringBuilder();
		boolean global = false;
		int index = 0, paramCount;
		if(currentSymbolTable.containsKey(identifier)) {
			index = currentSymbolTable.get(identifier);
		} else if(globalDeclarationTable.containsKey(identifier)) {
			global = true;
			index = globalDeclarationTable.get(identifier);
		}
		sb.append(Constants.GLOBAL_VAR).append(Constants.LEFT_BRACKET);
		if(index < 0) {
			//retuString = identifier;
			paramCount = currentSymbolTable.get(Constants.PARAM_COUNT);
			paramCount = (-4-(paramCount + index + 1)) * -1;
			sb.append("base-").append(paramCount);
		} else {
			if(global) {
				sb.append(index);					
			} else {
				sb.append("base+").append(index);					
			}
			if(arrIndex != null) {
				sb.append(Constants.PLUS).append(arrIndex);
			}
		}
		sb.append(Constants.RIGHT_BRACKET);
		retuString = sb.toString();
		return retuString;
	}
	
	/**
	 * Increment tokens.
	 *
	 * @param string the string
	 */
	private void incrementTokens(String string) {
		while(!string.equals(getcurrentValue())) {
			currentIndex++;
		}
		currentIndex++;
		checkMetaStatements();
	}
	
	/**
	 * Gets the label. Generates labels for goto statements
	 *
	 * @return the label
	 */
	private String getLabel() {
		String val = "label" + labelCounter;
		labelCounter++;
		return val;
	}
	
	/**
	 * Inits the program.
	 */
	private void initProgram() {
		int globalVarBase = 0;
		if(this.globalDeclarationTable.containsKey(Constants.BASE_COUNT)) {
			globalVarBase = this.globalDeclarationTable.get(Constants.BASE_COUNT);
		}
		codeGen.append("#include<stdlib.h>\n#define top mem[").append(globalVarBase++).append("]\n#define base mem[")
		.append(globalVarBase++).append("]\n#define jumpReg mem[").append(globalVarBase++)
		.append("]\n#define membase ").append(globalVarBase++)
		.append("\nint mem[2000];").append("int main() {top = membase;mem[top] = 0;base = top + 1;\n")
		.append("goto main;\n");
	}
	
	/**
	 * Generates function prolog. Saves the current address, stack and copies the parameters before branching
	 * to the function label.
	 * @param functionName the function name
	 * @param paramList the param list
	 * @return the string
	 */
	private String generateFunctionProlog(String functionName, List<String> paramList) {
		StringBuilder sb = new StringBuilder();
		functionLabelCounter++;
		int count = 0;
		for(String param : paramList) {
			sb.append("mem[top+").append(count++).append("] = ").append(param).append(";\n");
		}
		sb.append("mem[top+").append(count++).append("] = base;\n");
		sb.append("mem[top+").append(count++).append("] = top;\n");
		count++;
		sb.append("mem[top+").append(count++).append("] = ").append(functionLabelCounter).append(";\n");
		sb.append("base = top + ").append(count).append(";\n");
		sb.append("goto ").append(functionName).append(";\n");
		sb.append("label_").append(functionLabelCounter).append(":\n");
		return sb.toString();
	}
	
	/**
	 * Generate jump table.
	 */
	private void generateJumpTable() {
		codeGen.append("jumpTable:\nswitch(jumpReg) { case 0: exit(0);\n");
		int i = 1;
		while(i <= functionLabelCounter) {
			codeGen.append("case ").append(i).append(":\ngoto label_").append(i).append(";\n");
			i++;
		}
		codeGen.append("default: exit(0);\n}\n}"); 
	}
}
