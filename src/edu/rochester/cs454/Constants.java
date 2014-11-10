package edu.rochester.cs454;

import java.util.HashSet;
import java.util.Set;

/**
 * The Class Constants. Contains Data sets and constants for all keywords and symbols
 */
public class Constants {
	/** The Constant LEFT_PAREN. */
	public static final String LEFT_PAREN = "(";
	
	/** The Constant RIGHT_PAREN. */
	public static final String RIGHT_PAREN = ")";
	
	/** The Constant PLUS. */
	public static final String PLUS = "+";
	
	/** The Constant MINUS. */
	public static final String MINUS = "-";
	
	/** The Constant MULT. */
	public static final String MULT = "*";
	
	/** The Constant DIV. */
	public static final String DIV = "/";
	
	/** The Constant LEFT_BRACKET. */
	public static final String LEFT_BRACKET = "[";
	
	/** The Constant RIGHT_BRACKET. */
	public static final String RIGHT_BRACKET = "]";
	
	/** The Constant IF. */
	public static final String IF = "if";
	
	/** The Constant ELSE. */
	public static final String ELSE = "else";
	
	/** The Constant WHILE. */
	public static final String WHILE = "while";
	
	/** The Constant VOID. */
	public static final String VOID = "void";
	
	/** The Constant RETURN. */
	public static final String RETURN = "return";
	
	/** The Constant PRINTF. */
	public static final String PRINTF = "printf";
	
	/** The Constant SCANF. */
	public static final String SCANF = "scanf";
	
	/** The Constant BREAK. */
	public static final String BREAK = "break";
	
	/** The Constant CONTINUE. */
	public static final String CONTINUE = "continue";
	
	/** The Constant LEFT_BRACE. */
	public static final String LEFT_BRACE = "{";
	
	/** The Constant RIGHT_BRACE. */
	public static final String RIGHT_BRACE = "}";
	
	/** The Constant SEMICOLON. */
	public static final String SEMICOLON = ";";
	
	/** The Constant AND. */
	public static final String AND = "&";
	
	/** The Constant COMMA. */
	public static final String COMMA = ",";
	
	/** The Constant EQUAL. */
	public static final String EQUAL = "=";
	
	/** The Constant COLON. */
	public static final String COLON = ":";
	
	/** The Constant GOTO. */
	public static final String GOTO = "goto";
	
	/** The Constant COMP_OP. */
	public static final Set<String> COMP_OP;
	
	/** The Constant ADD_OP. */
	public static final Set<String> ADD_OP;
	
	/** The Constant MUL_OP. */
	public static final Set<String> MUL_OP;
	
	/** The Constant COND_OP. */
	public static final Set<String> COND_OP;
	
	/** The Constant TYPES. */
	public static final Set<String> TYPES;
	
	static {
		COMP_OP = new HashSet<>();
		COMP_OP.add("<=");COMP_OP.add("<");COMP_OP.add(">=");COMP_OP.add(">");
		COMP_OP.add("==");COMP_OP.add("!=");
		ADD_OP = new HashSet<>();ADD_OP.add("+");ADD_OP.add("-");
		MUL_OP = new HashSet<>();MUL_OP.add("*");MUL_OP.add("/");
		COND_OP = new HashSet<>();COND_OP.add("&&");COND_OP.add("||");
		TYPES = new HashSet<>();TYPES.add("int");
	}
	
	/** The Constant INVALID_TOKEN. */
	public static final String INVALID_TOKEN = "Invalid token:'%s'. %s expected";
	
	/** The Constant INVALID_IDENTIFIER. */
	public static final String INVALID_IDENTIFIER = "Invalid indetifier: '%s'. %s expected";
	
	/** The Constant INVALID_SYMBOL. */
	public static final String INVALID_SYMBOL = "Not a valid symbol: '%s'";
	
	/** The Constant INVALID_ExPRESSION. */
	public static final String INVALID_EXPRESSION = "Only 2 operands are allowed in the expression";
	
	/** The Constant IDENTIFIER_ALREADY_DECLARED. */
	public static final String IDENTIFIER_ALREADY_DECLARED = "Identifier already declared: ";
	
	/** The Constant IDENTIFIER_ALREADY_DECLARED. */
	public static final String LOOP_ERROR = "Statement %s not allowed outside loop: ";
	
	/** The Constant OPERAND_COUNT. */
	public static final String OPERAND_COUNT = "0_OPERAND";
	
	/** The Constant BASE_COUNT. */
	public static final String BASE_COUNT = "0_BASE_COUNT";
	
	/** The Constant LOCAL_VAR. */
	public static final String LOCAL_VAR = "temp";
	
	/** The Constant GLOBAL_VAR. */
	public static final String GLOBAL_VAR = "mem";
	
	public static final String PARAM_COUNT = "paramCount";
	
	public static final String MAIN = "main";
	
	public static final String READ = "read_cs254";
	
	public static final String WRITE = "write_cs254";
}
