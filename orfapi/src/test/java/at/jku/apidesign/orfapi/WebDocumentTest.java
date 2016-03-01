package at.jku.apidesign.orfapi;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class WebDocumentTest {

	@Test
	public void testLoadWeb() throws IOException {
		WebDocument doc = new WebDocument("http://news.orf.at");
		
		String loadedContent = doc.loadFromWeb();
		assertTrue(loadedContent.length() > 0);
		
		System.out.println(loadedContent);
	}
	
}
