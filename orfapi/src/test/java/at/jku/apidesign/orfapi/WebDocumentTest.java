package at.jku.apidesign.orfapi;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import at.jku.apidesign.orfapi.webdocument.WebDocument;

public class WebDocumentTest {

	@Test
	public void testLoadWeb() throws IOException {
		WebDocument doc = new WebDocument("http://news.orf.at", true);
		
		String loadedContent = doc.load();
		assertTrue(loadedContent.length() > 0);
		
		System.out.println(loadedContent);
	}
	
}
