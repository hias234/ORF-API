package at.jku.apidesign.orfapi.util;

import java.io.File;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OrfApiUtil {
	
	/**
	 * Returns the Default Caching Directory (The user home directory - to stay platform independent)
	 * 
	 * @return base cache directory.
	 */
	public static String getBaseCachePath(){
		return System.getProperty("user.home");
	}
	
	/**
	 * Caching directory with the base cache path from {@link getBaseCachePath} and the specific directory and fileName.
	 * 
	 * @param specificDirectory sub-directory of the basic cache directory
	 * @param fileName file name
	 * @return Path of the caching file.
	 */
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
	
	/**
	 * Returns the first element with the given tag name within the given element
	 * 
	 * @param element The element
	 * @param tagName The tag name (e.g. h1 or h2)
	 * @return first element with the tag name within the given element.
	 */
	public static String getHeader(Element element, String tagName) {
		String header = null;
		Elements headerElement = element.getElementsByTag(tagName);
		if (!headerElement.isEmpty()) {
			header = headerElement.get(0).text();
		}
		return header;
	}
}
