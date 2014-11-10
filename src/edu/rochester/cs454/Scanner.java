package edu.rochester.cs454;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.rochester.cs454.Token.TokenType;

/**
 * The Class Scanner. This class scans and tokenizes the input file string
 */
public class Scanner {
	
	/** The Constant OPERATOR. A Map of first character of all operators*/
	public static final Map<Character, Object> OPERATOR = new HashMap<Character, Object>() {
		{
		put('(', null);put(')', null);
		put('}', null);put('{', null);
		put('[', null);put(']', null);
		put(',', null);put(';', null);
		put('+', null);put('-', null);
		put('*', null);put('/', null);
		put('=', null);put('!', null);
		put('>', null);put('<', null);
		put('&', null);	put('|', null);
		}
	};
	
	/** The Constant TWO_CHAR_NO_REPEAT_OPERATORS. A map of characters which do not repeat in a two character
	 * symbol Example: >= !=
	 */
	public static final Map<Character, Object> TWO_CHAR_NO_REPEAT_OPERATORS = new HashMap<Character, Object>() {
		{ 
			put('!', null);
		}
	};
	
	/** The Constant MULTICHAR_OPERATORS. */
	public static final Map<String, Object> MULTICHAR_OPERATORS = new HashMap<String, Object>() {
		{ 
			put("!=", null);put(">=", null);put("<=", null);put("==", null);put("||", null);put("&&", null);
		}
	};	
	
	/** The Constant RESERVED. All the reserved keywords */
	public static final Map<String, Boolean> RESERVED = new HashMap<String, Boolean>() {
		{
			put("int", null);put("void", null);put("while", null);put("if", null);
			put("continue", null);put("return", null);put("break", null);put("scanf", null);
			put("printf", null);put("else", null);	
		}
	};
	
	/**
	 * Scan. Scans the text file strings and outputs all tokens separated by space
	 *
	 * @param inputFileText the input file text String
	 * @throws ParsingException 
	 */
	public List<Token> getTokens(String inputFileText) throws ParsingException {
		int fileLength = inputFileText.length();
		int currentIndex = 0;
		Token currentToken = null, previousToken = null;
		StringBuilder sb = new StringBuilder();
		List<Token> tokenList = new ArrayList<>();;
		while(currentIndex < fileLength) {
			if(currentToken != null && !currentToken.isWhiteSpace()) {
				previousToken = currentToken;
			}
			currentToken = getNextToken(currentIndex, inputFileText);
			if(currentToken.isValid(previousToken)) {
				if(currentToken.isMetaStatement() && currentToken.getValue().charAt(0) == '#') {
					changeMetaStatement(currentToken);
				}
				if(!currentToken.isWhiteSpace()) {
					if(TokenType.SYMBOL.equals(currentToken.getTokenType())) {
						sb.append(currentToken.getValue());						
					} else {
						sb.append(currentToken.getValue()).append(' ');
					}
					tokenList.add(currentToken);
				}
			} else {
				System.out.println("Invalid Token:" + currentToken.getValue());
				sb.setLength(0);
				throw new ParsingException("Invalid Token:" + currentToken.getValue());
			}
			currentIndex = currentToken.getNextIndex();
		}
		return tokenList;
	}
	
	
	/**
	 * Changes the values in meta statements.
	 *
	 * @param token the token parameter
	 */
	public void changeMetaStatement(Token token) {
		String value = token.getValue();
		int length = value.length();
		StringBuilder sb = new StringBuilder();
		int currentIndex = 0;
		while(currentIndex < length && value.charAt(currentIndex)!= ' ') {
			currentIndex++;
		}
		String identifier = value.substring(0, currentIndex);	//No regular expression used
		sb.append(identifier);
		if("#define".equals(identifier)) {
			sb.append(' ');
			String valueString = value.substring(currentIndex);
			Token newToken = null;
			currentIndex = 0;
			length = valueString.length();
			while(currentIndex < length) {				
				newToken = getNextToken(currentIndex, valueString);
				currentIndex = newToken.getNextIndex();
				if(!newToken.isWhiteSpace()) {
					if(newToken.isIdentifier()) {
						sb.append(newToken.getValue()).append("_cs254");					
					} else if(newToken.isSymbol()) {
						sb.append(newToken.getValue());
					} else {
						sb.append(newToken.getValue()).append(' ');
					}
				}
			}
			sb.append("\n\r");
			token.setValue(sb.toString());
		}
	}
	
	/**
	 * Gets the next token starting from the index. Finds meta statements, symbols, numbers, identifiers,
	 * strings and reserved words.
	 * @param index the starting index from which the token will be generated
	 * @param fileString the string of the file
	 * @return the next token
	 */
	private Token getNextToken(int index, String fileString) {
		char currentChar = fileString.charAt(index);
		char nextChar='`';
		int stringLength = fileString.length(), nextIndex = index + 1;
		if(index < stringLength - 1) {
			nextChar = fileString.charAt(nextIndex);
		}
		StringBuilder sb = new StringBuilder();
		Token token = null;
		// Check for meta statements ((#|//)(*)*(\n))
		if(currentChar == '#' || (currentChar == '/' && nextChar == '/')) {
			while(index < stringLength && (currentChar != '\n')) {
				currentChar = fileString.charAt(index);
				sb.append(fileString.charAt(index));
				index++;
			}
			sb.append('\r');
			token = new Token(sb.toString(), index, Token.TokenType.META_STATEMENT);
		} else if(currentChar == '"') { // Check for strings
			sb.append(currentChar);
			index++;
			do {
				currentChar = fileString.charAt(index);
				sb.append(currentChar);
				index++;
			} while(index < stringLength && currentChar != '"');
			token = new Token(sb.toString(), index, Token.TokenType.STRING);
		} else if(currentChar == '\n' || currentChar == ' ' || currentChar == '\t' || currentChar == '\r') {
			//continue till end of spaces and new lines. This part is written to skip white spaces and new lines
			while(index < stringLength &&(fileString.charAt(index) == '\n' || fileString.charAt(index) == ' ' || 
					fileString.charAt(index) == '\t' || fileString.charAt(index) == '\r')) {
				index++;
			}
			token = new Token(null, index, Token.TokenType.WHITESPACE);
		} else if(OPERATOR.containsKey(currentChar)) {
			// Look for symbol tokens
			sb.append(currentChar);
			index++;
			if(TWO_CHAR_NO_REPEAT_OPERATORS.containsKey(currentChar)) {
				sb.append(nextChar);
				index++;
			} else if(OPERATOR.containsKey(nextChar)){
				StringBuilder tempStr = new StringBuilder();
				tempStr.append(currentChar).append(nextChar);
				if(MULTICHAR_OPERATORS.containsKey(tempStr.toString())) {
					sb.append(nextChar);
					index++;
				}
			}
			token = new Token(sb.toString(), index, Token.TokenType.SYMBOL);
		} else {
			// Look for identifiers or numbers
			boolean flag = true;
			while(flag && index < fileString.length()) {
				if(OPERATOR.containsKey(currentChar) || currentChar == '\n' || currentChar == ' ' 
						|| currentChar == '\r') {
					flag = false;
				} else {
					sb.append(currentChar);
					index++;
					if(index < fileString.length()) {
						currentChar = fileString.charAt(index);						
					}
				}
			}
			String value = sb.toString();
			if(RESERVED.containsKey(value)) {
				token = new Token(value, index, Token.TokenType.KEYWORDS);
			} else {
				char firstChar = value.charAt(0);
				if(firstChar >= '0' && firstChar <= '9') {
					token = new Token(sb.toString(), index, Token.TokenType.NUMBERS);
				} else {
					token = new Token(sb.toString(), index, Token.TokenType.IDENTIFIER);					
				}
			}
		}
		return token;
	}
	
	/**
	 * Checks if is valid character.
	 *
	 * @param value the value
	 * @return true, if is valid character
	 */
	public static boolean isValidCharacter(char value) {
		return (value >= 'A' && value <= 'Z') || (value >= 'a' && value <= 'z') || value == '_';
		
	}
	
	/**
	 * Checks if is valid number.
	 *
	 * @param value the value
	 * @return true, if is valid number
	 */
	public static boolean isValidNumber(char value) {
		return value >= '0' && value <= '9';		
	}
	
	/**
	 * Checks if is valid number string.
	 *
	 * @param value the value
	 * @return true, if is valid number string
	 */
	public static boolean isValidNumberString(String value) {
		int i = 0;
		boolean returnVal = true;
		while(i < value.length()) {
			if(!isValidNumber(value.charAt(i))) {
				returnVal = false;
				break;
			}
			i++;
		}
		return returnVal;
	}
	
	/**
	 * Checks if is valid identifier.
	 *
	 * @param value the value
	 * @return true, if is valid identifier
	 */
	public static boolean isValidIdentifier(String value) {
		int i = 0;
		boolean returnVal = true;
		while(i < value.length()) {
			if(i == 0) {
				if(!isValidCharacter(value.charAt(i))) {
					returnVal = false;
					break;
				}
			} else if(!isValidCharacter(value.charAt(i)) && !isValidNumber(value.charAt(i))) {
					returnVal = false;
					break;
			}
			i++;
		}
		return returnVal;
	}
}

/**
 * This class represents a token. It contains token value, the type of token and the next index
 * from which the next token search can be started in for a string
 */
class Token {
	
	enum TokenType{IDENTIFIER, SYMBOL, META_STATEMENT, WHITESPACE, KEYWORDS, STRING, NUMBERS};
	
	/** The value of a token*/
	private String value;
	
	/** The next index*/
	private int nextIndex;
	
	/** The type of token*/
	private TokenType tokenType;	
	
	public Token(String value, int nextIndex, TokenType tokenType) {
		super();
		this.value = value;
		this.nextIndex = nextIndex;
		this.tokenType = tokenType;
	}
	
	/**
	 * Method to check whether the token is valid according to the rules mentioned in the problem statement.
	 * See: http://www.cs.rochester.edu/drupal/u/cding/csc-254-2014-programming-language-design-and-implementation#Handout 
	 */
	public boolean isValid(Token previousToken) {
		boolean returnVal = true;
		if(TokenType.IDENTIFIER.equals(tokenType)) {
			returnVal = Scanner.isValidIdentifier(value);
			if(returnVal && !value.equals("main")) {
				this.value = value.concat("_cs254");
			}
		} else if(TokenType.STRING.equals(tokenType)) {
			returnVal =  (value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"');
		} else if(TokenType.META_STATEMENT.equals(tokenType)) {
			char nextChar = value.charAt(1);
			if((value.charAt(0) == '#' || (value.charAt(0)== '/' && nextChar == '/'))
					&& (value.charAt(value.length() - 1) == '\n' || value.charAt(value.length() - 1) == '\r')) {
				returnVal = true;
			} else {
				returnVal = false;
			}
		} else if(TokenType.KEYWORDS.equals(tokenType)) {
				returnVal = Scanner.RESERVED.containsKey(value);
		} else if(TokenType.SYMBOL.equals(tokenType)) {
			if(value.length() > 1) {
				returnVal = Scanner.MULTICHAR_OPERATORS.containsKey(value);
			} else {
				char val = value.charAt(0);
				if(val == '*' && previousToken != null) {
					boolean flag = Scanner.RESERVED.containsKey(previousToken.getValue());
					if(flag) {
						value = previousToken.getValue().concat(" ").concat(value);
						returnVal = false;
					}
				} else {
					returnVal = Scanner.OPERATOR.containsKey(value.charAt(0));					
				}
			}
		} else if(TokenType.NUMBERS.equals(tokenType)) {
			returnVal = Scanner.isValidNumberString(value);
		} else if(TokenType.WHITESPACE.equals(tokenType)) {
			returnVal = true;
		} else {
			returnVal = false;
		}
		return returnVal;
	}
	
	public boolean isWhiteSpace() {
		return TokenType.WHITESPACE.equals(tokenType);
	}
	
	public boolean isMetaStatement() {
		return TokenType.META_STATEMENT.equals(tokenType);
	}
	
	public boolean isIdentifier() {
		return TokenType.IDENTIFIER.equals(tokenType);
	}
	
	public boolean isSymbol() {
		return TokenType.SYMBOL.equals(tokenType);
	}
	
	public String getValue() {
		return value;
	}

	public int getNextIndex() {
		return nextIndex;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public void setValue(String value) {
		this.value = value;
	}
}