package at.jku.apidesign.orfapi.webdocument;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import at.jku.apidesign.orfapi.exception.OrfApiException;

public class OrfWebDocumentUtil {
	
	public static String getBaseCachePath(){
		return System.getProperty("user.home");
	}
	
	public static String getCachePath(String specificDirectory, String fileName) {
		String cachePath = getBaseCachePath();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(cachePath);
		if (!cachePath.endsWith(File.separator)) {
			sb.append(File.separatorChar);
		}
		
		if (specificDirectory != null && !specificDirectory.isEmpty()){
			sb.append(specificDirectory);
			if (!specificDirectory.endsWith(File.separator)) {
				sb.append(File.separatorChar);
			}
		}
		
		sb.append(fileName);
		return sb.toString();
	}
	
	public static String getHeader(Element element, String tagName) {
		String header = null;
		Elements headerElement = element.getElementsByTag(tagName);
		if (!headerElement.isEmpty()) {
			header = headerElement.get(0).text();
		}
		return header;
	}

	public static Document getJsoupDocument(String url, boolean shouldCache) {
		Document document;
		try {
			document = WebDocument.getJSoupDocument(url, shouldCache);
		} catch (IOException e) {
			throw new OrfApiException("Exception get Document with URL: " + url, e);
		}
		return document;
	}
}
