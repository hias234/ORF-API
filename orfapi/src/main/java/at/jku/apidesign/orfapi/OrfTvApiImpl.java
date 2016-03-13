package at.jku.apidesign.orfapi;

import java.io.IOException;
import java.text.ParseException;
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
		for (Element showElement : orfDocument.select(".listitem")) {
			Element timeElement = showElement.select(".fpstarttime").first();
			if (timeElement == null) {
				continue;
			}
			String timeFromString = timeElement.getElementsByTag("span").get(1).text();
			Date timeFrom;
			try {
				timeFrom = TvShow.TV_DATE_FORMAT.parse(timeFromString);
			} catch (ParseException e) {
				e.printStackTrace();
				continue;
			}
			String tvStation = "";
			if (timeElement.getElementsByTag("span").get(1).hasClass("orf1text"))
				tvStation = "ORF1";
			else if (timeElement.getElementsByTag("span").get(1).hasClass("orf2text"))
				tvStation = "ORF2";
			String title = showElement.getElementsByTag("h2").text();
			String subtitle = showElement.getElementsByTag("h3").text();

			tvShows.add(new TvShow(title, subtitle, timeFrom, new Date(), tvStation));
		}
		return tvShows;
	}

	@Override
	public final List<TvShow> getDailyProgramByTvStation(String tvStation) {
		return getProgramByTvStationForDay(tvStation, new Date()); // Use
																	// ForDay-Method
																	// with
																	// current
																	// date
	}

	@Override
	public List<TvShow> getProgramByTvStationForDay(String tvStation, Date day) {
		// TODO Auto-generated method stub
		return null;
	}

}
