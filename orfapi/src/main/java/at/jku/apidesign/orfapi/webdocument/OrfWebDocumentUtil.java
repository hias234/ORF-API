package at.jku.apidesign.orfapi.webdocument;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import at.jku.apidesign.orfapi.exception.OrfApiException;

public class OrfWebDocumentUtil {
	public static String getHeader(Element element, String tagName) {
		String header = null;
		Elements headerElement = element.getElementsByTag(tagName);
		if (!headerElement.isEmpty()) {
			header = headerElement.get(0).text();
		}
		return header;
	}

	public static Date getDate(Element contentElement) {
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

	public static Document getJsoupDocument(String url) {
		Document document;
		try {
			document = WebDocument.getJSoupDocument(url);
		} catch (IOException e) {
			throw new OrfApiException(e);
		}
		return document;
	}

	public static String getBody(Element contentElement, Element teaserElement) {
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
}
