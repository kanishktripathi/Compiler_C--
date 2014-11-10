package edu.rochester.cs454;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.rochester.cs454.Token.TokenType;

/**
 * The class parses the list of tokens received from the scanner. The language is a subset of C 
 * programming language. 
 * 
 */
public class LLParser {
	
	/** The array of tokens returned from the scanner. */
	private Token[] tokens;
	
	/** The current index of the token array when parsing. */
	private int currentIndex;
	
	/** The function count. */
	private int funcCount;
	
	/** The variable count. */
	private int varCount;
	
	/** The statement count. */
	private int statementCount;
	
	/** The stack count. Useful for checking if there are any variables*/
	private int stackCount;
	
	/** The token evaluated. */
	private boolean tokenEvaluated;
	
	/** The max operands. */
	private int maxOperands;

	/** The function and symbol table. Contains all the symbols and their index for code generator array*/
	private Map<String, Map<String, Integer>> functionAndSymbolTable;
	
	/** The global declaration symbol table. */
	private Map<String, Integer> globalDeclarationTable;
	
	/** The symbol table index. The current index for a variable in a symbol table*/
	private int symbolTableIndex;
	
	/** The current symbol table. */
	private Map<String, Integer> currentSymbolTable;
	
	/** The loop counter. Increments when a parser is traversing a loop. Decrements after passing the loop
	 * Used to check syntax for break and continue outside a loop*/
	private int loopCounter;
	
	/**
	 * Instantiates a new LL parser. On the basis of token list.
	 *
	 * @param tokenList the token list
	 */
	public LLParser(List<Token> tokenList) {
		this.tokens = new Token[tokenList.size()];
		tokenList.toArray(tokens);
		this.currentIndex = 0;
		functionAndSymbolTable = new HashMap<>();
		globalDeclarationTable = new HashMap<>();
	}

	/**
	 * Parses the the expression and prints the count of functions, variables and statements.
	 *
	 * @return the string
	 * @throws ParsingException the parsing exception
	 */
	public String parseLL() throws ParsingException  {
		try {			
			checkMetaStatements();
			program();
			modifyToken();
			CodeGenerator codeGen = new CodeGenerator(tokens, functionAndSymbolTable, globalDeclarationTable);
			if(stackCount > 0) {
				System.out.println("Error in parsing the string:");
			} else {
				System.out.println("Function count::" + this.funcCount);
				System.out.println("Variable count::" + this.varCount);
				System.out.println("Statement count::" + this.statementCount);
			}
			return codeGen.generateCode();
		} catch(IndexOutOfBoundsException e){
			e.printStackTrace();
			throw new ParsingException(e.getMessage());
		}
	}
	
	/**
	 * Program. The starting symbol
	 *<Program> ->  void ID( <Function Definition> | <type> ID | Function or Data Declaration
	 * @throws ParsingException the parsing exception
	 */
	private void program() throws ParsingException {
		stackCount++;
		String identifier;
		if(matchAndIncrement(Constants.VOID)) {
			identifier = getcurrentValue();
			matchTokenForError(getcurrentToken().getTokenType(), TokenType.IDENTIFIER);
			matchTokenForError(Constants.LEFT_PAREN);
			functionDefinition(identifier);
			stackCount--;
		} else if(checkAndIncrementToken(Constants.TYPES)) {
			identifier = getcurrentValue();
			matchTokenForError(getcurrentToken().getTokenType(), TokenType.IDENTIFIER);
			functionOrDataDeclaration(identifier);
			stackCount--;
		} else if(!isEnd()){
			error();
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
		this.currentSymbolTable = new HashMap<>();
		this.functionAndSymbolTable.put(identifier, this.currentSymbolTable);
		this.symbolTableIndex = 0;
		this.maxOperands = 0;
		this.currentSymbolTable.put(Constants.BASE_COUNT, this.symbolTableIndex);
		this.currentSymbolTable.put(Constants.PARAM_COUNT, 0);
		paramList();
		matchTokenForError(Constants.RIGHT_PAREN);
		functionBody();
		this.currentSymbolTable.put(Constants.OPERAND_COUNT, this.maxOperands);
		functionList();
	}
	
	/**
	 * Param list. Parses the parameter list of a function
	 * <Parameter List> -> void | <Non empty Param List>
	 * @throws ParsingException the parsing exception
	 */
	private void paramList() throws ParsingException {
		String value = getcurrentValue();
		switch(value) {
			case Constants.VOID:
				matchAndIncrement(Constants.VOID);
				break;
			case Constants.RIGHT_PAREN:
				break;
			default:
				nonEmptyParamList();
		}	
	}
	
	/**
	 * Non empty param list. Prses when parameter list is not empty
	 * <Non empty Param List> -> <type> ID <Non empty Param List>'
	 * @throws ParsingException the parsing exception
	 */
	private void nonEmptyParamList() throws ParsingException {
		checkToken(Constants.TYPES);
		String value = getcurrentValue();
		matchTokenForError(getcurrentToken().getTokenType(), TokenType.IDENTIFIER);
		int paramCount;
		if(currentSymbolTable.containsKey(value)) {
			error("Duplicate parameter: " + value);
		} else {
			paramCount = this.currentSymbolTable.get(Constants.PARAM_COUNT);
			paramCount++;
			this.currentSymbolTable.put(value, -1 * paramCount);
			this.currentSymbolTable.put(Constants.PARAM_COUNT, paramCount);
		}
		nonEmptyParamListP();
	}
	
	/**
	 * Non empty param list p.
	 * <Non empty Param List>' -> , <Non empty Param List> | $
	 * @throws ParsingException the parsing exception
	 */
	private void nonEmptyParamListP() throws ParsingException {
		if(matchAndIncrement(Constants.COMMA)) {
			nonEmptyParamList();
		}
	}
	
	/**
	 * Function list. Starts parsing of next function if end of file has not been reached
	 * <Function-List> ->  $ | void ID( <Function Definition> | <type> ID <Function Definition>
	 * @throws ParsingException the parsing exception
	 */
	private void functionList() throws ParsingException {
		if(!isEnd()) {
			if(!matchAndIncrement(Constants.VOID)) {
				checkToken(Constants.TYPES);
			}
			String identifier = getcurrentValue();
			matchTokenForError(getcurrentToken().getTokenType(), TokenType.IDENTIFIER);
			matchTokenForError(Constants.LEFT_PAREN);
			functionDefinition(identifier);
		}
	}
	
	/**
	 * Function body. Parses the body of function
	 * <Function-Body> -> ; | { <Data declaration> <statements>
	 * @throws ParsingException the parsing exception
	 */
	private void functionBody() throws ParsingException {
		if(!matchAndIncrement(Constants.SEMICOLON)) {
			matchTokenForError(Constants.LEFT_BRACE);
			funcCount++;
			dataDeclarations();
			String value = getcurrentValue();
			while(!Constants.RIGHT_BRACE.equals(value)) {
				statements();
				value = getcurrentValue();
			}
			matchTokenForError(Constants.RIGHT_BRACE);
		}
	}
	
	/**
	 * Data declarations. Data declaration parser in a function
	 * <Data declaration> -> <type><ID List><Data declaration> | $
	 * @throws ParsingException the parsing exception
	 */
	private void dataDeclarations() throws ParsingException {
		if(checkAndIncrementToken(Constants.TYPES)) {
			idList();
			dataDeclarations();
		}
	}
	
	/**
	 * Function or data declaration. Parser check to identify if the declaration is a function
	 * or a variable.
	 * Check for the rule::
	 * <Function or Data Declaration> (<Function Definition> | [ <expression> ]<Declaration> |<Declaration>
	 *
	 * @param identifier the identifier
	 * @throws ParsingException the parsing exception
	 */
	private void functionOrDataDeclaration(String identifier) throws ParsingException {
		if(matchAndIncrement(Constants.LEFT_PAREN)) {
			functionDefinition(identifier);
		} else if(matchAndIncrement(Constants.LEFT_BRACKET)) {
			int valueIndex = this.currentIndex;
			ExpressionSimplifier simplify = new ExpressionSimplifier(true);
			expression(simplify);
			tokens[valueIndex].setValue(simplify.getCurrentValue().toString());
			valueIndex++;
			while(!Constants.RIGHT_BRACKET.equals(tokens[valueIndex].getValue())) {
				tokens[valueIndex].setValue(null);
				valueIndex++;
				tokenEvaluated = true;
			}
			matchTokenForError(Constants.RIGHT_BRACKET);
			this.currentSymbolTable = this.globalDeclarationTable;
			checkIndetifier(currentSymbolTable, identifier, simplify.getCurrentValue());
			varCount++;
			declaration();
		} else {
			this.currentSymbolTable = this.globalDeclarationTable;
			checkIndetifier(currentSymbolTable, identifier, 1);
			varCount++;
			declaration();
		}
	}
	
	/**
	 * Statements. Parses the statements in a block or a function
	 * <statements> -> } | <statement><statements>
	 * <statement> -> printf<printf> | scanf<scanf>|if<if>| 
	 * while<while>|return<return>| break<break>|continue<continue> | ID <assign or function call>
	 * @throws ParsingException the parsing exception
	 */
	private void statements() throws ParsingException {
			String value = getcurrentValue();
			switch(value) {
				case Constants.PRINTF:
					matchTokenForError(Constants.PRINTF);
					matchTokenForError(Constants.LEFT_PAREN);
					matchTokenForError(getcurrentToken().getTokenType(), TokenType.STRING);
					printfP();
					matchTokenForError(Constants.RIGHT_PAREN);
					matchTokenForError(Constants.SEMICOLON);
					break;
				case Constants.SCANF:
					matchTokenForError(Constants.SCANF);
					matchTokenForError(Constants.LEFT_PAREN);
					matchTokenForError(getcurrentToken().getTokenType(), TokenType.STRING);
					matchTokenForError(Constants.COMMA);
					matchTokenForError(Constants.AND);
					expression();
					matchTokenForError(Constants.RIGHT_PAREN);
					matchTokenForError(Constants.SEMICOLON);
					break;
				case Constants.IF:
					matchTokenForError(Constants.IF);
					matchTokenForError(Constants.LEFT_PAREN);
					conditionExp(new ExpressionSimplifier());
					matchTokenForError(Constants.RIGHT_PAREN);
					block();
					if(matchAndIncrement(Constants.ELSE)) {
						block();
					}
					break;
				case Constants.WHILE:
					matchTokenForError(Constants.WHILE);
					matchTokenForError(Constants.LEFT_PAREN);
					conditionExp(new ExpressionSimplifier());
					matchTokenForError(Constants.RIGHT_PAREN);
					loopCounter++;
					block();
					loopCounter--;
					break;
				case Constants.RETURN:
					matchTokenForError(Constants.RETURN);
					if(!match(Constants.SEMICOLON)) {
						expression();
					}
					matchTokenForError(Constants.SEMICOLON);
					break;						
				case Constants.BREAK:
					if(loopCounter > 0) {
						matchAndIncrement(Constants.BREAK);
						matchTokenForError(Constants.SEMICOLON);						
					} else {
						error(String.format(Constants.LOOP_ERROR, value));
					}
					break;
				case Constants.CONTINUE:
					if(loopCounter > 0) {
						matchAndIncrement(Constants.CONTINUE);
						matchTokenForError(Constants.SEMICOLON);						
					} else {
						error(String.format(Constants.LOOP_ERROR, value));
					}
					break;
				default:
					String identifier = getcurrentValue();
					matchTokenForError(getcurrentToken().getTokenType(), TokenType.IDENTIFIER);
					assignOrFuncCall(identifier);
					matchTokenForError(Constants.SEMICOLON);
					break;					
			}
			statementCount++;
	}
	
	/**
	 * Assign or func call. Checks for assignmet or a function call in a statement
	 * <assign or function call> -> [<expression> ] = <expression> | = <expression> | ( <expresion-list> )
	 *
	 * @param identifier the identifier
	 * @throws ParsingException the parsing exception
	 */
	private void assignOrFuncCall(String identifier) throws ParsingException {
		boolean isArray = false;
		ExpressionSimplifier simplifier = null;
		if(matchAndIncrement(Constants.LEFT_BRACKET)) {
			checkCurrentIdentifier(identifier);
			simplifier = new ExpressionSimplifier();
			expression(simplifier);
			matchTokenForError(Constants.RIGHT_BRACKET);
			isArray = true;
		}
		if(matchAndIncrement(Constants.EQUAL)) {
			checkCurrentIdentifier(identifier);
			expression(simplifier);
			return;
		} 
		if(matchAndIncrement(Constants.LEFT_PAREN) && !isArray) {
			expressionList(new ExpressionSimplifier());
			matchTokenForError(Constants.RIGHT_PAREN);
		} else {
			error();
		}
	}
	
	/**
	 * Expression list.
	 * <expression-list> -> <expression> <expression-list>'
	 * <expression-list>' -> , <expression><expression-list>' | $
	 *
	 * @param simplifier the simplifier
	 * @throws ParsingException the parsing exception
	 */
	private void expressionList(ExpressionSimplifier simplifier) throws ParsingException {
		if(!match(Constants.RIGHT_PAREN)) {
			expression(simplifier);
			if(matchAndIncrement(Constants.COMMA)) {
				expressionList(simplifier);
			}			
		}
	}
	
	/**
	 * Condition exp. Check for conditions in a control statement(if, while)
	 * <condition-expression> -> <condition><condition>'
	 *
	 * @param simplifier the simplifier
	 * @throws ParsingException the parsing exception
	 */
	private void conditionExp(ExpressionSimplifier simplifier) throws ParsingException {
		conditionEval(simplifier);
		if(checkAndIncrementToken(Constants.COND_OP)) {
			simplifier.incrementOperandCount();
			conditionExp(simplifier);
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
		expression(simplifier);
		checkToken(Constants.COMP_OP);
		simplifier.incrementOperandCount();
		expression(simplifier);
	}
	
	/**
	 * Block. A statement block in a control statement
	 * <block>-> { <statements>
	 * @throws ParsingException the parsing exception
	 */
	private void block() throws ParsingException {
		matchTokenForError(Constants.LEFT_BRACE);
		String value = getcurrentValue();
		while(!Constants.RIGHT_BRACE.equals(value)) {
			statements();
			value = getcurrentValue();
		}
		matchTokenForError(Constants.RIGHT_BRACE);
	}
	
	/**
	 * Printf s. Checks for expression in printf statement
	 * <printf>' -> , <expression> | $
	 * @throws ParsingException the parsing exception
	 */
	private void printfP() throws ParsingException {
		if(matchAndIncrement(Constants.COMMA)) {
			expression();
		}
	}
	
	/**
	 * Declaration.
	 * <Declaration> -> comma <ID List><Program> | semicolon<Program>
	 * @throws ParsingException the parsing exception
	 */
	private void declaration() throws ParsingException {
		if(matchAndIncrement(Constants.SEMICOLON)) {
			program();
		} else if(matchAndIncrement(Constants.COMMA)) {
			idList();
			program();
		} else {
			error();
		}
	}
	
	/**
	 * Id list.Prses identifier list
	 * <ID List> -> <id> <ID List>' ;
	 * @throws ParsingException the parsing exception
	 */
	private void idList() throws ParsingException {
		checkForid();
		idListP();
		matchTokenForError(Constants.SEMICOLON);
	}
	
	/**
	 * Id list p.
	 * <ID List>' -> ,<id><ID List>' | $
	 * @throws ParsingException the parsing exception
	 */
	private void idListP() throws ParsingException {
		if(matchAndIncrement(Constants.COMMA)) {
			checkForid();
			idListP();
		}
	}
	
	/**
	 * Check forid. Parses Identifier. Evaluates for a constant expression for array declaration.
	 * <id> -> ID <id>'
	 * <id>' -> [ <constant expression> ] | $
	 * @throws ParsingException the parsing exception
	 */
	private void checkForid() throws ParsingException {
		String idenString = getcurrentValue();
		matchTokenForError(getcurrentToken().getTokenType(), TokenType.IDENTIFIER);
		if(matchAndIncrement(Constants.LEFT_BRACKET)) {
			int valueIndex = this.currentIndex;
			ExpressionSimplifier simplify = new ExpressionSimplifier(true);
			expression(simplify);
			tokens[valueIndex].setValue(simplify.getCurrentValue().toString());
			valueIndex++;
			while(!Constants.RIGHT_BRACKET.equals(tokens[valueIndex].getValue())) {
				tokens[valueIndex].setValue(null);
				tokenEvaluated = true;
				valueIndex++;
			}
			matchTokenForError(Constants.RIGHT_BRACKET);
			checkIndetifier(this.currentSymbolTable, idenString, simplify.getCurrentValue());
		} else {
			checkIndetifier(this.currentSymbolTable, idenString, 1);
		}
		varCount++;
	}
	
	/**
	 * Ex. E
	 * The expression equivalent to grammar rule:<expression> -> <terminal><expression>'
	 * The value returned from T is passed for subsequent evaluation.
	 * @return computer value
	 * @throws ParsingException the parsing exception
	 */
	private void expression() throws ParsingException {
		expression(null);
	}
	
	/**
	 * Expression.
	 *
	 * @param simplify the simplify
	 * @return the expression simplifier
	 * @throws ParsingException the parsing exception
	 */
	private ExpressionSimplifier expression(ExpressionSimplifier simplify) throws ParsingException {
		if(simplify == null) {
			simplify = new ExpressionSimplifier();
		}
		nonTerminal(simplify);
		addOpEx(simplify);
		if(maxOperands < simplify.getOperandCount()) {
			maxOperands = simplify.getOperandCount();
		}
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
		Integer adder;
		if(checkAndIncrementToken(Constants.ADD_OP)) {
			simplify.incrementOperandCount();
			adder = simplify.getCurrentValue();
			nonTerminal(simplify);
			if(simplify.isCalculate()) {
				simplify.operation(expr, adder);
			}
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
		Integer multiple;
		if(checkAndIncrementToken(Constants.MUL_OP)) {
			simplify.incrementOperandCount();
			multiple = simplify.getCurrentValue();
			factor(simplify);
			if(simplify.isCalculate()) {
				simplify.operation(expr, multiple);
			}
			multOpExpression(simplify);
		}
	}
	
	/**
	 * Terminal. F. Raises an exception if evaluating for a constant expression.
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
			if(simplify.isCalculate()) {
				ExpressionSimplifier interExpr = new ExpressionSimplifier(true);
				expression(interExpr);
				simplify.setCurrentValue(interExpr.getCurrentValue());
			} else {
				expression(simplify);				
			}
			matchTokenForError(Constants.RIGHT_PAREN);
		} else if(matchAndIncrement(Constants.MINUS)) {
			simplify.setCurrentValue(-1 * Integer.parseInt(getcurrentValue()));
			simplify.setCurrentOperand(Constants.MINUS.concat(getcurrentValue()));
			matchTokenForError(getcurrentToken().getTokenType(), TokenType.NUMBERS);
		} else if(TokenType.NUMBERS.equals(getcurrentToken().getTokenType())) {
			simplify.setCurrentValue(Integer.parseInt(getcurrentValue()));
			simplify.setCurrentOperand((getcurrentValue()));
			matchTokenForError(getcurrentToken().getTokenType(), TokenType.NUMBERS);
		} else {
			String identifier = getcurrentValue();
			matchTokenForError(getcurrentToken().getTokenType(), TokenType.IDENTIFIER);
			if(simplify.isCalculate()) {
					error("Illegal argument, expecting a constant");
			}
			boolean isVariable = true;
			if(matchAndIncrement(Constants.LEFT_BRACKET)) {
				expression(simplify);
				matchTokenForError(Constants.RIGHT_BRACKET);
			} else if(matchAndIncrement(Constants.LEFT_PAREN)) {
				expressionList(simplify);
				simplify.incrementOperandCount();
				matchTokenForError(Constants.RIGHT_PAREN);
				isVariable = false;
			}
			if(isVariable) {
				checkCurrentIdentifier(identifier);
			}
		}
	}
	
	/**
	 * Checks if is end.
	 *
	 * @return true, if is end
	 */
	private boolean isEnd() {
		return this.currentIndex == tokens.length;
	}
	
	/**
	 * Check white spaces and meta statements.
	 */
	private void checkMetaStatements() {
		while(!isEnd() && tokens[currentIndex].isMetaStatement()) {
			currentIndex++;
		}
	}
	
	/**
	 * Gets the current token.
	 *
	 * @return the current token
	 */
	private Token getcurrentToken() {
		return tokens[currentIndex];
	}
	
	/**
	 * Gets the current value.
	 *
	 * @return the current value
	 */
	private String getcurrentValue() {
		return tokens[currentIndex].getValue();
	}
	
	/**
	 * Error.
	 *
	 * @param errorMessage the error message
	 * @throws ParsingException the parsing exception
	 */
	private void error(String errorMessage) throws ParsingException {
		throw new ParsingException("Parsing error: " + errorMessage + " at index" + currentIndex + " ");
	}
	
	/**
	 * Error.
	 *
	 * @throws ParsingException the parsing exception
	 */
	private void error() throws ParsingException {
		throw new ParsingException("Error in parsing the string " + getcurrentValue() + " at index" + currentIndex
				+ "  ");
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
	 * Matches current with the matcher value
	 * if true, then increment the current index else throw and error.
	 * @param matcher the matcher
	 * @throws ParsingException the parsing exception
	 */
	private void matchTokenForError(String matcher) throws ParsingException {
		String value = getcurrentValue();
		if(matcher.equals(value)) {
			currentIndex++;
			checkMetaStatements();
		} else {
			error(String.format(Constants.INVALID_TOKEN, value, matcher));			
		}
	}
	
	/**
	 * Match token for error. Matches value with the matcher value. Throws an
	 * error if not matches. Used for checking the type of token( identifier, number, symbol)
	 * @param value the value
	 * @param matcher the matcher
	 * @throws ParsingException the parsing exception
	 */
	private void matchTokenForError(TokenType value, TokenType matcher) throws ParsingException {
		if(matcher.equals(value)) {
			currentIndex++;
			checkMetaStatements();
		}else {
			error(String.format(Constants.INVALID_IDENTIFIER, value, matcher));			
		}
	}
	
	/**
	 * Checks whether the token value is present in the lookup set.
	 * Used for checking operators. Throws error if doesn't match
	 * @param lookupSet the lookup set
	 * @throws ParsingException the parsing exception
	 */
	private void checkToken(Set<String> lookupSet) throws ParsingException {
		String value = getcurrentValue();
		if(lookupSet.contains(value)) {
			currentIndex++;
			checkMetaStatements();
		} else {
			error(String.format(Constants.INVALID_SYMBOL, value));			
		}
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
	 * Modify token.
	 */
	private void modifyToken() {
		if(tokenEvaluated) {			
			List<Token> list = new ArrayList<>(tokens.length);
			for(Token t : tokens) {
				if(t.getValue() != null) {
					list.add(t);
				}
			}
			this.tokens = new Token[list.size()];
			list.toArray(tokens);
		}
	}
	
	/**
	 * Check if the identifier has been already declared or not. If already declared, raises an exception.
	 * @param map the map
	 * @param identifier the identifier
	 * @param incrementVal the increment val
	 * @throws ParsingException the parsing exception
	 */
	private void checkIndetifier(Map<String, Integer> map, String identifier, int incrementVal) 
			throws ParsingException {
		if(map.containsKey(identifier)) {
			error(Constants.IDENTIFIER_ALREADY_DECLARED + identifier);
		} else {
			map.put(identifier, this.symbolTableIndex);
			this.symbolTableIndex+=incrementVal;
			map.put(Constants.BASE_COUNT, this.symbolTableIndex);
		}
	}
	
	/**
	 * Checks if the variable used in the program has been declared or using the lookup  
	 * symbol table for a function. It first looks into the current function symbol table and then into 
	 * global variables symbol table.
	 * @param identifier the identifier
	 * @throws ParsingException the parsing exception
	 */
	private void checkCurrentIdentifier(String identifier) throws ParsingException {
		if(!currentSymbolTable.containsKey(identifier) && !globalDeclarationTable.containsKey(identifier)) {
			error("No declaration found:" + identifier);
		}
	}
}
