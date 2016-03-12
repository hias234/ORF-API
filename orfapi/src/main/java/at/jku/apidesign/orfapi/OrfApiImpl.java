package at.jku.apidesign.orfapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import at.jku.apidesign.orfapi.exception.OrfApiException;
import at.jku.apidesign.orfapi.model.Category;
import at.jku.apidesign.orfapi.model.NewsArticle;
import at.jku.apidesign.orfapi.model.Region;

public final class OrfApiImpl implements OrfApi {

	@Override
	public final List<NewsArticle> getTopNews() throws OrfApiException {
		List<NewsArticle> topNews = new ArrayList<NewsArticle>();
		Document orfDocument;
		try {
			orfDocument = WebDocument.getJSoupDocument("http://news.orf.at");
		} catch (IOException ex) {
			throw new OrfApiException(ex);
		}
		for (Element ressort : orfDocument.select("main .ticker .ressort")) {
			String topic = OrfWebDocumentUtil.getHeader(ressort, "h1");
			Category category = Category.fromLabel(topic);

			for (Element article : ressort.select(".stories article")) {
				Element articleUrlElement = article.select("a").first();
				if (articleUrlElement != null) {
					String articleUrl = articleUrlElement.attr("href");
					NewsArticle a = getNewsArticle(articleUrl);
					if (a != null) {
						a.setCategory(category);
						topNews.add(a);
					}
				}
			}
		}
		return topNews;
	}

	@Override
	public final List<NewsArticle> getTopNewsByCategory(Category category) {
		return getTopNews().stream().filter(n -> n.getCategory() != null && n.getCategory().equals(category))
				.collect(Collectors.toList());
	}

	@Override
	public final List<NewsArticle> searchTopNews(String query) {
		return searchNews(getTopNews().stream(), query).collect(Collectors.toList());
	}

	@Override
	public final List<NewsArticle> getNewsByRegion(Region region) {
		return getNewsByRegion(region.getUrl());
	}

	@Override
	public final List<NewsArticle> searchNewsByRegion(Region region, String query) {
		return searchNews(getNewsByRegion(region).stream(), query).collect(Collectors.toList());
	}

	@Override
	public final List<NewsArticle> getNewsByRegionAndDate(Region region, Date from, Date to) {
		return getNewsByRegion(region).stream()
				.filter(n -> n.getDate() != null && n.getDate().compareTo(from) >= 0 && n.getDate().compareTo(to) <= 0)
				.collect(Collectors.toList());
	}

	private List<NewsArticle> getNewsByRegion(String url) {
		Document document = OrfWebDocumentUtil.getJsoupDocument(url);

		return getNewsByRegion(url.split("\\?")[0], document);
	}

	private List<NewsArticle> getNewsByRegion(String baseUrl, Document document) {
		List<NewsArticle> articles = new ArrayList<>();
		for (Element story : document.select(".content .storyBox")) {
			Element articleUrlElement = story.select("a").first();
			if (articleUrlElement != null) {
				String articleUrl = articleUrlElement.attr("href");
				NewsArticle article = getNewsArticle(articleUrl);
				if (article != null) {
					articles.add(article);
				}
			}
		}

		Element nextPageElement = document.select(".pageNav .next a").first();
		if (nextPageElement != null) {
			String nextPageUrl = baseUrl + nextPageElement.attr("href");

			System.out.println(nextPageUrl);
			articles.addAll(getNewsByRegion(nextPageUrl));
		}

		return articles;
	}

	private Stream<NewsArticle> searchNews(Stream<NewsArticle> news, String query) {
		String queryLowerCase = query.toLowerCase();

		return news.filter(n -> n.getTitle().toLowerCase().contains(queryLowerCase)
				|| (n.getTeaser() != null && n.getTeaser().toLowerCase().contains(queryLowerCase)));
	}

	private NewsArticle getNewsArticle(String url) {
		Document document = OrfWebDocumentUtil.getJsoupDocument(url);

		NewsArticle article = new NewsArticle();

		Element contentElement = document.select(".content").first();
		article.setTitle(OrfWebDocumentUtil.getHeader(contentElement, "h1"));

		Element teaserElement = contentElement.select("p.teaser").first();
		if (teaserElement != null) {
			article.setTeaser(teaserElement.text());
		}

		String bodyStr = OrfWebDocumentUtil.getBody(contentElement, teaserElement);
		article.setBody(bodyStr);

		Date date = OrfWebDocumentUtil.getDate(contentElement);
		if (date == null) {
			return null;
		}
		article.setDate(date);

		article.setRegion(Region.fromUrl(url));

		return article;
	}

}
