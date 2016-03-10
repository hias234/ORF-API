package at.jku.apidesign.orfapi;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		return searchNews(getTopNews().stream(), query).collect(Collectors.toList());
	}

	@Override
	public List<NewsArticle> getNewsByRegion(Region region) {
		return getNewsByRegion(region.getUrl());
	}

	private List<NewsArticle> getNewsByRegion(String url) {
		Document document;
		try {
			document = WebDocument.getJSoupDocument(url);
		} catch (IOException e) {
			throw new OrfApiException(e);
		}

		return getNewsByRegion(url.split("\\?")[0], document);
	}

	private List<NewsArticle> getNewsByRegion(String baseUrl, Document document) {
		List<NewsArticle> articles = new ArrayList<>();
		for (Element story : document.select(".content .storyBox")) {
			Element articleUrlElement = story.select("a").first();
			if (articleUrlElement != null) {
				String articleUrl = articleUrlElement.attr("href");
				articles.add(getNewsArticle(articleUrl));
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

	private NewsArticle getNewsArticle(String url) {
		Document document;
		try {
			document = WebDocument.getJSoupDocument(url);
		} catch (IOException e) {
			throw new OrfApiException(e);
		}

		NewsArticle article = new NewsArticle();

		Element contentElement = document.select(".content").first();
		article.setTitle(getHeader(contentElement, "h1"));

		Element teaserElement = contentElement.select("p.teaser").first();
		if (teaserElement != null) {
			article.setTeaser(teaserElement.text());

			String bodyStr = getBody(teaserElement);
			article.setBody(bodyStr);
		}

		Date date = getDate(contentElement);
		article.setDate(date);

		article.setRegion(getRegion(url));

		return article;
	}

	private Region getRegion(String url) {
		List<Region> regions = Arrays.asList(Region.values());

		return regions.stream().filter(r -> url.contains(r.getUrl())).findFirst().orElse(null);
	}

	private Date getDate(Element contentElement) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		Element dateElement = contentElement.select(".date").first();
		if (dateElement != null) {
			Date date;
			try {
				date = dateFormat.parse(dateElement.text().replaceAll("Publiziert am", ""));
			} catch (ParseException e) {
				throw new OrfApiException(e);
			}
			return date;
		}
		return null;
	}

	private String getBody(Element teaserElement) {
		StringBuilder body = new StringBuilder();
		for (Element bodyElement = teaserElement.nextElementSibling(); bodyElement != null
				&& !bodyElement.classNames().contains("storyMeta"); bodyElement = bodyElement.nextElementSibling()) {
			if (!bodyElement.tagName().equals("div")) {
				body.append(bodyElement.text()).append("\n");
			}
		}
		return body.toString();
	}

	@Override
	public List<NewsArticle> searchNewsByRegion(Region region, String query) {
		return searchNews(getNewsByRegion(region).stream(), query).collect(Collectors.toList());
	}

	@Override
	public List<NewsArticle> getNewsByRegionAndDate(Region region, Date from, Date to) {
		return getNewsByRegion(region).stream()
				.filter(n -> n.getDate().compareTo(from) >= 0 && n.getDate().compareTo(to) <= 0)
				.collect(Collectors.toList());
	}

	private Stream<NewsArticle> searchNews(Stream<NewsArticle> news, String query) {
		String queryLowerCase = query.toLowerCase();

		return news.filter(n -> n.getTitle().toLowerCase().contains(queryLowerCase)
				|| n.getTeaser().toLowerCase().contains(queryLowerCase));
	}
}
