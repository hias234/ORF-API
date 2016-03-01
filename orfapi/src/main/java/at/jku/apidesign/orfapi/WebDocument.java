package at.jku.apidesign.orfapi;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

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
	
	public String loadFromWeb() throws IOException {
		InputStream in = url.openStream();
		String content = IOUtils.toString(in);
//		FileUtils.writeStringToFile(getProtocolCacheFile(),
//					this.protocolContent);
		
		return content;
	}
}
