package at.jku.apidesign.orfapi.extractor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import at.jku.apidesign.orfapi.exception.OrfApiException;
import at.jku.apidesign.orfapi.model.NewsArticle;
import at.jku.apidesign.orfapi.model.Region;
import at.jku.apidesign.orfapi.webdocument.OrfWebDocumentUtil;

/**
 * Extracts the news article of one detail page.
 * 
 * @author marku
 *
 */
public class NewsArticleExtractor {

	private Boolean useCaching;

	public NewsArticleExtractor(Boolean useCaching) {
		super();
		this.useCaching = useCaching;
	}

	public NewsArticle getNewsArticle(String url) {
		Document document = OrfWebDocumentUtil.getJsoupDocument(url, useCaching);

		NewsArticle article = new NewsArticle();

		Element contentElement = document.select(".content").first();
		article.setTitle(OrfWebDocumentUtil.getHeader(contentElement, "h1"));

		Element teaserElement = contentElement.select("p.teaser").first();
		if (teaserElement != null) {
			article.setTeaser(teaserElement.text());
		}

		String bodyStr = getBody(contentElement, teaserElement);
		article.setBody(bodyStr);

		Date date = getDate(contentElement);
		if (date == null) {
			return null;
		}
		article.setDate(date);

		article.setRegion(Region.fromUrl(url));

		return article;
	}

	public String getBody(Element contentElement, Element teaserElement) {
		StringBuilder body = new StringBuilder();

		Element bodyElement;
		if (teaserElement == null) {
			bodyElement = contentElement.select("p").first();
		} else {
			bodyElement = teaserElement.nextElementSibling();
		}

		for (; bodyElement != null
				&& !bodyElement.classNames().contains("storyMeta"); bodyElement = bodyElement.nextElementSibling()) {
			if (!bodyElement.tagName().equals("div")) {
				body.append(bodyElement.text()).append("\n");
			}
		}
		return body.toString();
	}

	public Date getDate(Element contentElement) {
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
}
