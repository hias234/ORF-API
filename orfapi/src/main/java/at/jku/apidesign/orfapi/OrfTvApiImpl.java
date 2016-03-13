package at.jku.apidesign.orfapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import at.jku.apidesign.orfapi.exception.OrfApiException;
import at.jku.apidesign.orfapi.model.TvShow;
import at.jku.apidesign.orfapi.webdocument.WebDocument;

public final class OrfTvApiImpl implements OrfTvApi {

	@Override
	public final List<TvShow> getUpcomingTvShows() {
		List<TvShow> tvShows = new ArrayList<TvShow>();
		Document orfDocument;
		try {
			orfDocument = WebDocument.getJSoupDocument("http://tv.orf.at/", false);
		} catch (IOException ex) {
			throw new OrfApiException(ex);
		}
		for (Element showElement : orfDocument.select("listitem")) {
			String timeFrom = showElement.select("fpstarttime").text();
			String title = showElement.getElementsByTag("h2").text();
			String subtitle = showElement.getElementsByTag("h3").text();

			tvShows.add(new TvShow(title, subtitle, new Date(), new Date()));
		}
		return tvShows;
	}

	@Override
	public final List<TvShow> getDailyProgramBySender(String tvSender) {
		// TODO Auto-generated method stub
		return null;
	}

}
