package edu.rochester.cs454;

/**
 * Stores the start, conditional and end labels for a while loop. The object of this 
 * class is maintained in a stack when traversing a while loop statement. 
 * This is done to handle the break and continue statements in the loop.
 * 
 */
public class WhileLoopHandler {
	
	/** The start label. */
	private String startLabel;
	
	/** The condition code. */
	private String conditionCode;
	
	/** The end label. */
	private String endLabel;
	
	private boolean foundbreak;

	/**
	 * Instantiates a new while loop handler.
	 *
	 * @param startLabel the start label
	 * @param conditionCode the condition code
	 * @param endLabel the end label
	 */
	public WhileLoopHandler(String startLabel, String conditionCode,
			String endLabel) {
		super();
		this.startLabel = startLabel;
		this.conditionCode = conditionCode;
		this.endLabel = endLabel;
	}

	/**
	 * Gets the start label.
	 *
	 * @return the start label
	 */
	public String getStartLabel() {
		return startLabel;
	}

	/**
	 * Gets the condition code.
	 *
	 * @return the condition code
	 */
	public String getConditionCode() {
		return conditionCode;
	}

	/**
	 * Gets the end label.
	 *
	 * @return the end label
	 */
	public String getEndLabel() {
		return endLabel;
	}

	public boolean isFoundbreak() {
		return foundbreak;
	}

	public void setFoundbreak(boolean foundbreak) {
		this.foundbreak = foundbreak;
	}
}
