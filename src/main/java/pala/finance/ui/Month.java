package pala.finance.ui;

public enum Month {
	JANUARY("January", 1), FEBRUARY("February", 2), MARCH("March", 3), APRIL("April", 4), MAY("May", 5), JUNE("June", 6), JULY("July", 7), 
	AUGUST("August", 8), SEPTEMBER("September", 9), OCTOBER("October", 10), NOVEMBER("November", 11), DECEMBER("December", 12);
	private String text;
	private int value;
	
	private Month(String text, int value) {
		this.text = text;
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return this.text;
	}
}
