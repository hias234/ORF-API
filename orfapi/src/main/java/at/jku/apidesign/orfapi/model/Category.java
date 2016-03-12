package at.jku.apidesign.orfapi.model;

import java.util.Arrays;
import java.util.List;

public enum Category {
	SPORT("Sport"), FOREIGN_AFFAIRS("Ausland"), OTHER("Anderes");

	private String label;

	private Category(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public static Category fromLabel(String label) throws IllegalArgumentException {
		List<Category> categories = Arrays.asList(Category.values());

		return categories.stream().filter(c -> c.getLabel().equalsIgnoreCase(label)).findFirst().orElse(Category.OTHER);
	}
}
