package at.jku.apidesign.orfapi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import at.jku.apidesign.orfapi.exception.OrfApiException;
import at.jku.apidesign.orfapi.extractor.NewsArticleExtractor;
import at.jku.apidesign.orfapi.model.Category;
import at.jku.apidesign.orfapi.model.NewsArticle;
import at.jku.apidesign.orfapi.model.Region;
import at.jku.apidesign.orfapi.util.OrfApiUtil;
import at.jku.apidesign.orfapi.webdocument.WebDocument;

/**
 * Extracts News-Article from http://news.orf.at and puts them into Java-Objects.
 * 
 * @author dpril
 *
 */
public final class OrfNewsApi {

	private static final String TOP_NEWS_URL = "http://news.orf.at";
	
	private boolean useCaching;
	private NewsArticleExtractor newsArticleExtractor;
	
	/**
	 * Creates an instance of an ORF-API. Caching is enabled.
	 */
	public OrfNewsApi() {
		this(true);
	}
	
	/**
	 * Creates an instance of an ORF-API.
	 * 
	 * @param useCaching Determines if the News-Detail HTML-pages should be cached locally.
	 */
	public OrfNewsApi(boolean useCaching) {
		this.useCaching = useCaching;
		this.newsArticleExtractor = new NewsArticleExtractor(useCaching);
	}
	
	/**
	 * Returns if Caching News-Detail HTML-pages is enabled.
	 * 
	 * @return true, if caching is enabled.
	 */
	public boolean isUsingCaching() {
		return useCaching;
	}

	/**
	 * Sets if caching for the News-Detail HTML-pages should be used.
	 * 
	 * @param useCaching Determines if caching is enabled.
	 * @return The Instance of the API.
	 */
	public OrfNewsApi useCaching(boolean useCaching) {
		this.useCaching = useCaching;
		return this;
	}

	/**
	 * Extracts the NewsArticles of news.orf.at
	 * 
	 * @return All Articles on the Top News Page of the ORF
	 */
	public final List<NewsArticle> getTopNews() throws OrfApiException {
		List<NewsArticle> topNews = new ArrayList<NewsArticle>();
		Document orfDocument = WebDocument.getJSoupDocument(TOP_NEWS_URL, false);
		
		for (Element ressort : orfDocument.select("main .ticker .ressort")) {
			String topic = OrfApiUtil.getHeader(ressort, "h1");
			Category category = Category.fromLabel(topic);

			for (Element article : ressort.select(".stories article")) {
				Element articleUrlElement = article.select("a").first();
				if (articleUrlElement != null) {
					String articleUrl = articleUrlElement.attr("href");
					NewsArticle a = newsArticleExtractor.getNewsArticle(articleUrl);
					if (a != null) {
						a.setCategory(category);
						topNews.add(a);
					}
				}
			}
		}
		return topNews;
	}

	/**
	 * Returns filtered News Articles of only the given category
	 * 
	 * @param category The category
	 * @return News articles of the given category.
	 */
	public final List<NewsArticle> getTopNewsByCategory(Category category) {
		return getTopNews().stream().filter(n -> n.getCategory() != null && n.getCategory().equals(category))
				.collect(Collectors.toList());
	}

	/**
	 * Searches the top news articles title and teaser for containing words.
	 * 
	 * @param query The query
	 * @return Search results of matching the query with the top news.
	 */
	public final List<NewsArticle> searchTopNews(String query) {
		return searchNews(getTopNews().stream(), query).collect(Collectors.toList());
	}

	/**
	 * Returns the news article of a specific region (e.g. Upper Austria)
	 * 
	 * @param region The region.
	 * @return News articles on the region's news page (e.g. ooe.orf.at/news
	 */
	public final List<NewsArticle> getNewsByRegion(Region region) {
		return getNewsByRegion(region.getUrl());
	}

	/**
	 * Searches regional news articles' title and teaser for containing words.
	 * 
	 * @param region The region.
	 * @param query The query.
	 * @return Search results of matching the query with the regional news.
	 */
	public final List<NewsArticle> searchNewsByRegion(Region region, String query) {
		return searchNews(getNewsByRegion(region).stream(), query).collect(Collectors.toList());
	}

	/**
	 * Filters regional news by dates.
	 * 
	 * @param region The region.
	 * @param from Date from (inclusive)
	 * @param to Date to (inclusive)
	 * @return All regional news from the given region and time range.
	 */
	public final List<NewsArticle> getNewsByRegionAndDate(Region region, Date from, Date to) {
		return getNewsByRegion(region).stream()
				.filter(n -> n.getDate() != null && n.getDate().compareTo(from) >= 0 && n.getDate().compareTo(to) <= 0)
				.collect(Collectors.toList());
	}

	private List<NewsArticle> getNewsByRegion(String url) {
		Document document = WebDocument.getJSoupDocument(url, false);

		return getNewsByRegion(url.split("\\?")[0], document);
	}

	private List<NewsArticle> getNewsByRegion(String baseUrl, Document document) {
		List<NewsArticle> articles = new ArrayList<>();
		for (Element story : document.select(".content .storyBox")) {
			Element articleUrlElement = story.select("a").first();
			if (articleUrlElement != null) {
				String articleUrl = articleUrlElement.attr("href");
				NewsArticle article = newsArticleExtractor.getNewsArticle(articleUrl);
				if (article != null) {
					articles.add(article);
				}
			}
		}

		Element nextPageElement = document.select(".pageNav .next a").first();
		if (nextPageElement != null) {
			String nextPageUrl = baseUrl + nextPageElement.attr("href");

			articles.addAll(getNewsByRegion(nextPageUrl));
		}

		return articles;
	}

	private Stream<NewsArticle> searchNews(Stream<NewsArticle> news, String query) {
		String queryLowerCase = query.toLowerCase();

		return news.filter(n -> n.getTitle().toLowerCase().contains(queryLowerCase)
				|| (n.getTeaser() != null && n.getTeaser().toLowerCase().contains(queryLowerCase)));
	}

	

}
