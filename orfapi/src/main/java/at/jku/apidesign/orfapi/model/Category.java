package at.jku.apidesign.orfapi.model;

public enum Category {
	SPORT("Sport"), FOREIGN_AFFAIRS("Ausland"), OTHER("Anderes");
	
	private String label;
	
	private Category(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
}
