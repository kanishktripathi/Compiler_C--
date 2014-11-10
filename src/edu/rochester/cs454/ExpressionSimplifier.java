package edu.rochester.cs454;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Encapsulation to evaluate operands for an expression.
 */
public class ExpressionSimplifier {
	
	/** The sb. Contains the expression evaluation  intermediate code.*/
	private StringBuilder sb;
	
	/** The var gen. */
	private StringBuilder varGen;
	
	/** The operand count. */
	private int operandCount;
	
	/** The base index. */
	private int baseIndex;
	
	/** The current value. */
	private Integer currentValue;
	
	/** The calculate. The flag whether the expression is to be checked for arithmetic constants*/
	private boolean calculate;
	
	/** The simplify. */
	private boolean simplify;
	
	/** The token stack. */
	private Deque<String> tokenStack;
	
	/** The expression list. */
	private List<String> expressionList;
	
	/** The current expression value. */
	private String currentExpressionValue;
	
	/**
	 * Checks if is simplify.
	 *
	 * @return true, if is simplify
	 */
	public boolean isSimplify() {
		return simplify;
	}

	/**
	 * Sets the simplify.
	 *
	 * @param simplify the new simplify
	 */
	public void setSimplify(boolean simplify) {
		this.simplify = simplify;
	}

	/** The current operand. */
	private String currentOperand;
	
	/**
	 * Instantiates a new expression simplifier.
	 */
	public ExpressionSimplifier() {
		this.sb = new StringBuilder();
		this.varGen = new StringBuilder();
		tokenStack = new ArrayDeque<>();
		expressionList = new ArrayList<>();
	}
	
	/**
	 * Instantiates a new expression simplifier.
	 *
	 * @param isCalculate the is calculate
	 */
	public ExpressionSimplifier(boolean isCalculate) {
		this();
		this.calculate = isCalculate;
	}
	
	/**
	 * Instantiates a new expression simplifier.
	 *
	 * @param baseIndex the base index
	 * @param operandIndex the operand index
	 */
	public ExpressionSimplifier(int baseIndex, int operandIndex) {
		this();
		this.calculate = false;
		this.baseIndex = baseIndex;
		this.operandCount = operandIndex;
	}
	
	/**
	 * Gets the sb.
	 *
	 * @return the sb
	 */
	public StringBuilder getSb() {
		return sb;
	}

	/**
	 * Sets the sb.
	 *
	 * @param sb the new sb
	 */
	public void setSb(StringBuilder sb) {
		this.sb = sb;
	}

	/**
	 * Gets the operand count.
	 *
	 * @return the operand count
	 */
	public int getOperandCount() {
		return operandCount;
	}

	/**
	 * Increment operand count.
	 */
	public void incrementOperandCount() {
		this.operandCount++;
	}

	/**
	 * Checks if is calculate.
	 *
	 * @return true, if is calculate
	 */
	public boolean isCalculate() {
		return calculate;
	}

	/**
	 * Sets the calculate.
	 *
	 * @param calculate the new calculate
	 */
	public void setCalculate(boolean calculate) {
		this.calculate = calculate;
	}
	
	/**
	 * Gets the current operand.
	 *
	 * @return the current operand
	 */
	public String getCurrentOperand() {
		return currentOperand;
	}

	/**
	 * Sets the current operand.
	 *
	 * @param currentOperand the new current operand
	 */
	public void setCurrentOperand(String currentOperand) {
		this.currentOperand = currentOperand;
	}

	/**
	 * Gets the current value.
	 *
	 * @return the current value
	 */
	public Integer getCurrentValue() {
		return currentValue;
	}

	/**
	 * Sets the current value.
	 *
	 * @param currentValue the new current value
	 */
	public void setCurrentValue(Integer currentValue) {
		this.currentValue = currentValue;
	}
	
	/**
	 * Operation. Used for doing calculation for constant expressions
	 *
	 * @param operation the operation
	 * @param operand the operand
	 */
	public void operation(String operation, Integer operand) {
		if(Constants.PLUS.equals(operation)) {
			operand += this.currentValue;
		} else if(Constants.MINUS.equals(operation)) {
			operand -= this.currentValue;
		}  else if(Constants.MULT.equals(operation)) {
			operand *= this.currentValue;
		}  else if(Constants.DIV.equals(operation)) {
			operand /= this.currentValue;
		}
		this.currentValue = operand;
	}
	
	/**
	 * Process. Takes an operand. Pushes it to the stack if there's an already existing operation
	 *
	 * @param tokenValue the token value
	 */
	public void process(String tokenValue) {
		if(this.currentExpressionValue == null) {
			this.currentExpressionValue = tokenValue;
		} else {
			this.tokenStack.push(currentExpressionValue);
			this.currentExpressionValue = tokenValue;
		}
	}
	
	/**
	 * Operation. This is like a Push down automata evaluation.
	 * If takes the current value from the simplifier and pops the top of the token stack.
	 * Generates an intermediate operation code for them. If we have current value as "a"
	 * The value on stack top is "b". Then we have an intermediate code generated as
	 * temp = a + b
	 * @param operation the operation
	 */
	public void operation(String operation) {
		varGen.append(Constants.GLOBAL_VAR).append(Constants.LEFT_BRACKET).append("base+")
		.append(baseIndex + operandCount).append(Constants.RIGHT_BRACKET);
		String temp = varGen.toString();
		varGen.append(Constants.EQUAL).append(tokenStack.pop()).append(operation).append(currentExpressionValue)
		.append(Constants.SEMICOLON).append('\n');
		String statement = varGen.toString();
		currentExpressionValue = temp;
		varGen.setLength(0);
		sb.append(statement);
		operandCount++;
	}
	
	/**
	 * Gets the base index.
	 *
	 * @return the base index
	 */
	public int getBaseIndex() {
		return baseIndex;
	}

	/**
	 * Sets the base index.
	 *
	 * @param baseIndex the new base index
	 */
	public void setBaseIndex(int baseIndex) {
		this.baseIndex = baseIndex;
	}

	/**
	 * Gets the token stack.
	 *
	 * @return the token stack
	 */
	public Deque<String> getTokenStack() {
		return tokenStack;
	}

	/**
	 * Sets the token stack.
	 *
	 * @param tokenStack the new token stack
	 */
	public void setTokenStack(Deque<String> tokenStack) {
		this.tokenStack = tokenStack;
	}

	/**
	 * Sets the operand count.
	 *
	 * @param operandCount the new operand count
	 */
	public void setOperandCount(int operandCount) {
		this.operandCount = operandCount;
	}

	/**
	 * Gets the current expression value.
	 *
	 * @return the current expression value
	 */
	public String getCurrentExpressionValue() {
		return currentExpressionValue;
	}
	
	/**
	 * Merge simplifiers.
	 *
	 * @param simplifier the simplifier
	 */
	public void mergeSimplifiers(ExpressionSimplifier simplifier) {
		if(simplifier != null) {
			sb.append(simplifier.sb.toString());
			this.baseIndex = simplifier.baseIndex;
			this.operandCount+=(simplifier.operandCount - this.operandCount);			
		} 
	}
	
	/**
	 * Simplify function call.
	 *
	 * @param functionPrologue the function prologue
	 * @param paramCount the param count
	 * @return the string
	 */
	public String simplifyFunctionCall(String functionPrologue, int paramCount) {
		sb.append(functionPrologue);
		varGen.append(Constants.GLOBAL_VAR).append(Constants.LEFT_BRACKET).append("base+")
		.append(baseIndex + operandCount).append(Constants.RIGHT_BRACKET);
		String temp = varGen.toString();
		varGen.append(Constants.EQUAL).append("mem[top +").append(paramCount + 2).
		append(Constants.RIGHT_BRACKET).append(Constants.SEMICOLON).append('\n');
		String statement = varGen.toString();
		varGen.setLength(0);
		sb.append(statement);
		operandCount++;
		return temp;
	}
	
	/**
	 * Adds the expression list. Useful for generating expression list for a function call.
	 * @param expression the expression
	 */
	public void addExpressionList(String expression) {
		expressionList.add(expression);
	}
	
	/**
	 * Gets the expression list.
	 *
	 * @return the expression list
	 */
	public List<String> getExpressionList() {
		return this.expressionList;
	}
	
	/**
	 * Gets the comma separated expression.
	 *
	 * @return the comma separated expression
	 */
	public String getCommaSeparatedExpression() {
		StringBuilder sb = new StringBuilder();
		for(String s : this.getExpressionList()) {
			sb.append(s).append(Constants.COMMA);
		}
		return sb.substring(0, sb.length()-1);
	}
}
