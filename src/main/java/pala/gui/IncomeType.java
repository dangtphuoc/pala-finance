package pala.gui;

public enum IncomeType {
	WIFE ("00000001", "Wife's salary"), HUSBAND("00000002", "Husband's salary"); 
	
	private String id;
	private String title;
	
	private IncomeType(String id, String title) {
		this.id = id;
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getId() {
		return id;
	}
	
	public String toString() {
		return title;
	}
}
