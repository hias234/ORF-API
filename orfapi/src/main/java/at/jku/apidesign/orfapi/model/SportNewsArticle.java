package at.jku.apidesign.orfapi.model;

import java.util.Date;

public class SportNewsArticle extends NewsArticle {
	private String sport;

	public SportNewsArticle() {
		super();
	}

	public SportNewsArticle(String title, String teaser, String body, Category category, Region region, Date date,
			String sport) {
		super(title, teaser, body, category, region, date);
		this.sport = sport;
	}

	public SportNewsArticle(NewsArticle newsArticle) {
		super(newsArticle.getTitle(), newsArticle.getTeaser(), newsArticle.getBody(), newsArticle.getCategory(),
				newsArticle.getRegion(), newsArticle.getDate());
	}

	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	@Override
	public String toString() {
		return sport + ": " + super.toString();
	}
}
