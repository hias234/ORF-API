package at.jku.apidesign.orfapi;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class ParserTest {

	@Test
	public void test() throws IOException {
		WebDocument orfDoc = new WebDocument("http://news.orf.at");
		Document orfDocument = Jsoup.parse(orfDoc.loadFromWeb());

		for (Element ressort : orfDocument.select("main .ticker .ressort")) {
			String topic = getHeader(ressort, "h1");
			
			System.out.println(topic);
			System.out.println();
			
			for (Element article : ressort.select(".stories article")) {
				String title = getHeader(article, "h2");
				System.out.println(title);
				
				String text = article.select(".text p").text();
				System.out.println(text);
			}
			
			System.out.println("------");
			System.out.println();
		}
	}

	private String getHeader(Element element, String tagName) {
		String header = null;
		Elements headerElement = element.getElementsByTag(tagName);
		if (!headerElement.isEmpty()) {
			header = headerElement.get(0).text();
		}
		return header;
	}
}
