package at.jku.apidesign.orfapi.console;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import at.jku.apidesign.orfapi.OrfNewsApi;
import at.jku.apidesign.orfapi.OrfTvApi;
import at.jku.apidesign.orfapi.model.Category;
import at.jku.apidesign.orfapi.model.NewsArticle;
import at.jku.apidesign.orfapi.model.Region;
import at.jku.apidesign.orfapi.model.TvShow;
import at.jku.apidesign.orfapi.model.TvStation;

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

		System.out.println("(7)...getUpcomingTvShows");
		System.out.println("(8)...getDailyProgramByTvStation");
		System.out.println("(9)...getProgramByTvStationForDay");
		System.out.println("(10)...getPrimetimeProgramByTvStationForDay");

		do {
			if (s.hasNextInt()) {
				int selection = s.nextInt();
				callApiMethod(selection);
			}
		} while (!s.nextLine().toLowerCase().equals("x"));

		System.out.println("Application closed!");
	}

	private static void callApiMethod(int selection) {
		Long startTimestamp = System.currentTimeMillis();

		OrfNewsApi orfApi = new OrfNewsApi();
		OrfTvApi orfTvApi = new OrfTvApi();
		switch (selection) {
		case 1:
			printNews(orfApi.getTopNews());
			break;
		case 2:
			printNews(orfApi.getTopNewsByCategory(enterCategory()));
			break;
		case 3:
			printNews(orfApi.searchTopNews(enterSearchQuery()));
			break;
		case 4:
			printNews(orfApi.getNewsByRegion(enterRegion()));
			break;
		case 5:
			printNews(orfApi.searchNewsByRegion(enterRegion(), enterSearchQuery()));
			break;
		case 6:
			printNews(orfApi.getNewsByRegionAndDate(enterRegion(), enterDate(), enterDate()));
			break;
		case 7:
			printTvProgram(orfTvApi.getUpcomingTvShows());
			break;
		case 8:
			printTvProgram(orfTvApi.getDailyProgramByTvStation(enterTvStation()));
		case 9:
			printTvProgram(orfTvApi.getProgramByTvStationForDay(enterTvStation(), enterDate()));
		case 10:
			printTvProgram(orfTvApi.getPrimetimeProgramByTvStationForDay(enterTvStation(), enterDate()));
		default:
			System.out.println("No method mapped to your input.");
			break;
		}

		Long durationInIllis = System.currentTimeMillis() - startTimestamp;
		System.out.println("Call finished, duration: " + durationInIllis + "ms");
	}

	private static void printTvProgram(List<TvShow> shows) {
		for (TvShow show : shows) {
			System.out.println(show.toString());
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
		System.out.println();
		if (s.hasNextInt()) {
			int catIdx = s.nextInt() - 1;
			return Category.values()[catIdx];
		}
		return null;
	}

	private static Region enterRegion() {
		System.out.println("Region:");

		for (int i = 0; i < Region.values().length; i++) {
			if (i > 0)
				System.out.print(", ");
			System.out.print(Region.values()[i].name() + "(" + (i + 1) + ")");
		}
		System.out.println();
		if (s.hasNextInt()) {
			int regionIdx = s.nextInt() - 1;
			return Region.values()[regionIdx];
		}
		return null;
	}

	private static TvStation enterTvStation() {
		System.out.println("Tv station:");

		for (int i = 0; i < TvStation.values().length; i++) {
			if (i > 0)
				System.out.print(", ");
			System.out.print(TvStation.values()[i].name() + "(" + (i + 1) + ")");
		}
		System.out.println();
		if (s.hasNextInt()) {
			int tvStationIdx = s.nextInt() - 1;
			return TvStation.values()[tvStationIdx];
		}
		return null;
	}

	private static String enterSearchQuery() {
		System.out.println("Query:");

		// Check if empty input is available
		if (s.hasNextLine() && s.nextLine().length() > 0) {
			return s.nextLine();
		} else if (s.hasNextLine()) {
			return s.nextLine();
		}
		return null;
	}

	private static Date enterDate() {
		try {
			System.out.println("Enter date (dd-MM-yyyy):");
			return new SimpleDateFormat("dd-MM-yyyy").parse(s.nextLine());
		} catch (ParseException e) {
			System.out.println("Wrong date entered - todays date will be used!");
			return new Date();
		}
	}
}
