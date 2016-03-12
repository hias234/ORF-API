package at.jku.apidesign.orfapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import at.jku.apidesign.orfapi.exception.OrfApiException;
import at.jku.apidesign.orfapi.model.Category;
import at.jku.apidesign.orfapi.model.NewsArticle;
import at.jku.apidesign.orfapi.model.Region;

public final class OrfApiImpl implements OrfApi {

	private String getHeader(Element element, String tagName) {
		String header = null;
		Elements headerElement = element.getElementsByTag(tagName);
		if (!headerElement.isEmpty()) {
			header = headerElement.get(0).text();
		}
		return header;
	}

	@Override
	public List<NewsArticle> getTopNews() throws OrfApiException {
		List<NewsArticle> topNews = new ArrayList<NewsArticle>();
		Document orfDocument;
		try {
			orfDocument = WebDocument.getJSoupDocument("http://news.orf.at");
		} catch (IOException ex) {
			throw new OrfApiException(ex);
		}
		for (Element ressort : orfDocument.select("main .ticker .ressort")) {
			String topic = getHeader(ressort, "h1");

			for (Element article : ressort.select(".stories article")) {
				String title = getHeader(article, "h2");
				// article.getelement

				String text = article.select(".text").get(0).text();

				topNews.add(new NewsArticle(title, "", text, Category.FOREIGN_AFFAIRS, null, null));
			}

			System.out.println("------");
			System.out.println();
		}
		return topNews;
	}

	@Override
	public List<NewsArticle> getTopNewsByCategory(Category category) {
		return getTopNews().stream().filter(n -> n.getCategory().equals(category)).collect(Collectors.toList());
	}

	@Override
	public List<NewsArticle> searchTopNews(String query) {
		String queryLowerCase = query.toLowerCase();

		return getTopNews().stream().filter(n -> n.getTitle().toLowerCase().contains(queryLowerCase))
				.collect(Collectors.toList());
	}

	@Override
	public List<NewsArticle> getNewsByRegion(Region region) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NewsArticle> searchNewsByRegion(Region region, String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NewsArticle> getNewsByRegionAndDate(Region region, Date from, Date to) {
		// TODO Auto-generated method stub
		return null;
	}

}
