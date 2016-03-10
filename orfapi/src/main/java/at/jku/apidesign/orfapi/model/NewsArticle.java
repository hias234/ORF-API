package at.jku.apidesign.orfapi.model;

public class NewsArticle {
	private String title;
	private String teaser;
	private String body;
	private Category category;
	private Region region;
	
	public NewsArticle(){
		
	}
	
	public NewsArticle(String title, String teaser, String body, Category category, Region region) {
		super();
		this.title = title;
		this.teaser = teaser;
		this.body = body;
		this.category = category;
		this.region = region;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTeaser() {
		return teaser;
	}
	public void setTeaser(String teaser) {
		this.teaser = teaser;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	
}
