package at.jku.apidesign.orfapi.webdocument;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import at.jku.apidesign.orfapi.exception.OrfApiException;
import at.jku.apidesign.orfapi.util.OrfApiUtil;

/**
 * Downloads and caches the HTML-Document specified in the URL.
 * 
 * @author marku
 */
public class WebDocument {

	private static final String CACHE_SUB_DIRECTORY = ".orf-api-cache";
	
	private URL url;
	private boolean shouldCache;

	public WebDocument(URL url, boolean shouldCache) {
		super();
		this.url = url;
		this.shouldCache = shouldCache;
	}

	public WebDocument(String url, boolean shouldCache) {
		super();
		try {
			this.url = new URL(url);
			this.shouldCache = shouldCache;
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static Document getJSoupDocument(String url, boolean shouldCache) throws OrfApiException {
		WebDocument orfDoc = new WebDocument(url, shouldCache);
		try {
			return Jsoup.parse(orfDoc.load());
		} catch (IOException e) {
			throw new OrfApiException("API while trying to load ORF-Document with URL: " + url, e);
		}
	}

	public String load() throws IOException {
		if (shouldCache && isCached()) {
			return loadFromCache();
		}
		return loadFromWeb();
	}
	
	private String loadFromCache() throws IOException {
		return FileUtils
				.readFileToString(getCacheFile());
	}

	private boolean isCached() {
		return getCacheFile().exists();
	}

	private String loadFromWeb() throws IOException {
		try (InputStream in = url.openStream()) {
			String content = IOUtils.toString(in);

			if (shouldCache) {
				 FileUtils.writeStringToFile(getCacheFile(),
						 content);
			}

			return content;
		} catch (IOException e) {
			throw e;
		}
	}

	private File getCacheFile() {
		String cacheFileName = url.toString().replaceAll("[\\\\/:\\.]", "_");
		return new File(OrfApiUtil.getCachePath(CACHE_SUB_DIRECTORY, cacheFileName));
	}
	
	
}
