package at.jku.apidesign.orfapi.console;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import at.jku.apidesign.orfapi.OrfApi;
import at.jku.apidesign.orfapi.OrfApiImpl;
import at.jku.apidesign.orfapi.model.Category;
import at.jku.apidesign.orfapi.model.NewsArticle;

public class OrfApiProgram {
	private static final Scanner s = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.println("Welcome to the ORF-News API (exit...x):");
		System.out.println("(1)...getTopNews");
		System.out.println("(2)...getTopNewsByCategory");
		System.out.println("(3)...searchTopNews");
		System.out.println("(4)...getNewsByRegion");
		System.out.println("(5)...searchNewsByRegion");
		System.out.println("(6)...getNewsByRegionAndDate");

		do {
			if (s.hasNextInt()) {
				int selection = s.nextInt();
				callApiMethod(selection);
			}
		} while (!s.nextLine().toLowerCase().equals("x"));

		System.out.println("Application closed!");
	}

	private static void callApiMethod(int selection) {
		OrfApi orfApi = new OrfApiImpl();
		switch (selection) {
		case 1:
			printNews(orfApi.getTopNews());
			break;
		case 2:
			orfApi.getTopNewsByCategory(enterCategory());
			break;
		default:
			System.out.println("No method mapped to your input.");
			break;
		}
	}

	private static void printNews(List<NewsArticle> news) {
		// TODO print whole news
		// Besser schreiben in file (html, text...)
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		for (NewsArticle article : news) {
			System.out.println(article.getTitle()
					+ ((article.getDate() != null) ? " - " + dateFormat.format(article.getDate()) : ""));
		}
	}

	private static Category enterCategory() {
		System.out.println("Category:");

		for (int i = 0; i < Category.values().length; i++) {
			if (i > 0)
				System.out.print(", ");
			System.out.print(Category.values()[i].name() + "(" + (i + 1) + ")");
		}
		if (s.hasNextInt()) {
			int catIdx = s.nextInt();
			return Category.values()[catIdx];
		}
		return null;
	}
}
