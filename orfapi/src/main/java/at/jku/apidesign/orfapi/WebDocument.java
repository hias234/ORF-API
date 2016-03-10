package at.jku.apidesign.orfapi;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Downloads (and maybe later caches?) the HTML-Document specified in the URL.
 * 
 * @author marku
 *
 */
// TODO maybe add caching of documents?
public class WebDocument {

	private URL url;

	public WebDocument(URL url) {
		super();
		this.url = url;
	}

	public WebDocument(String url) {
		super();
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static Document getJSoupDocument(String url) throws IOException {
		WebDocument orfDoc = new WebDocument(url);
		return Jsoup.parse(orfDoc.loadFromWeb());
	}

	public String loadFromWeb() throws IOException {
		try (InputStream in = url.openStream()) {
			String content = IOUtils.toString(in);

			// FileUtils.writeStringToFile(getProtocolCacheFile(),
			// this.protocolContent);

			return content;
		} catch (IOException e) {
			throw e;
		}

	}
}
