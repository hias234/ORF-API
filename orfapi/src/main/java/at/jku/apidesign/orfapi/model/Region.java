package at.jku.apidesign.orfapi.model;

import java.util.Arrays;
import java.util.List;

public enum Region {

	AUSTRIA("http://oesterreich.orf.at/"), UPPER_AUSTRIA("http://ooe.orf.at/news/"), LOWER_AUSTRIA(
			"http://noe.orf.at/news/"), VIENNA("http://wien.orf.at/news/"), CORINTHIA(
					"http://kaernten.orf.at/news/"), STYRIA("http://steiermark.orf.at/news/"), BURGENLAND(
							"http://burgenland.orf.at/news/"), VORARLBERG("http://vorarlberg.orf.at/news/"), TYROL(
									"http://tirol.orf.at/news/"), SALZBURG("http://salzburg.orf.at/news/");

	private String url;

	private Region(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public static Region fromUrl(String url) {
		List<Region> regions = Arrays.asList(Region.values());

		return regions.stream().filter(r -> url.contains(r.getUrl())).findFirst().orElse(Region.AUSTRIA);
	}
}
