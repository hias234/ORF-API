package at.jku.apidesign.orfapi.console;

import java.util.List;

import at.jku.apidesign.orfapi.OrfApiImpl;
import at.jku.apidesign.orfapi.model.NewsArticle;
import at.jku.apidesign.orfapi.model.Region;

public class OrfApiProgram {

	public static void main(String[] args) {
		System.out.println("Hello");
		
//		List<NewsArticle> austria = new OrfApiImpl().getNewsByRegion(Region.VORARLBERG);
		List<NewsArticle> austria = new OrfApiImpl().getTopNews();
		austria.forEach(n -> System.out.println(n.getTitle() + "    " + n.getBody() + "\n\n\n"));
	}
}
