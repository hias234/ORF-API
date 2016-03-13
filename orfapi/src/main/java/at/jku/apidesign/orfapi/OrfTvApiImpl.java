package at.jku.apidesign.orfapi;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import at.jku.apidesign.orfapi.exception.OrfApiException;
import at.jku.apidesign.orfapi.model.TvShow;
import at.jku.apidesign.orfapi.model.TvStation;
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
			TvStation tvStation = null;
			if (timeElement.getElementsByTag("span").get(1).hasClass("orf1text"))
				tvStation = TvStation.ORF1;
			else if (timeElement.getElementsByTag("span").get(1).hasClass("orf2text"))
				tvStation = TvStation.ORF2;
			String title = showElement.getElementsByTag("h2").text();
			String subtitle = showElement.getElementsByTag("h3").text();

			tvShows.add(new TvShow(title, subtitle, timeFrom, new Date(), tvStation));
		}
		return tvShows;
	}

	@Override
	public final List<TvShow> getDailyProgramByTvStation(TvStation tvStation) {
		return getProgramByTvStationForDay(tvStation, new Date()); // Use
																	// ForDay-Method
																	// with
																	// current
																	// date
	}

	@Override
	public List<TvShow> getProgramByTvStationForDay(TvStation tvStation, Date day) {
		List<TvShow> tvShows = new ArrayList<TvShow>();
		Document orfDocument;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMdd");
			orfDocument = WebDocument.getJSoupDocument(
					"http://tv.orf.at/program/" + tvStation.name().toLowerCase() + "/" + dateFormat.format(day), false);
		} catch (IOException ex) {
			throw new OrfApiException(ex);
		}
		tvShows.addAll(getProgramByDayTime(tvStation, orfDocument, ".tsmorning"));
		tvShows.addAll(getProgramByDayTime(tvStation, orfDocument, ".tsafternoon"));
		tvShows.addAll(getProgramByDayTime(tvStation, orfDocument, ".tsevening"));
		tvShows.addAll(getProgramByDayTime(tvStation, orfDocument, ".tsprimetime"));
		tvShows.addAll(getProgramByDayTime(tvStation, orfDocument, ".tsnight"));
		return tvShows;
	}

	@Override
	public List<TvShow> getPrimetimeProgramByTvStationForDay(TvStation tvStation, Date day) {
		Document orfDocument;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMdd");
			orfDocument = WebDocument.getJSoupDocument(
					"http://tv.orf.at/program/" + tvStation.name().toLowerCase() + "/" + dateFormat.format(day), false);
		} catch (IOException ex) {
			throw new OrfApiException(ex);
		}
		return getProgramByDayTime(tvStation, orfDocument, ".tsprimetime");
	}

	private List<TvShow> getProgramByDayTime(TvStation tvStation, Document orfDocument, String dayTimeStringCss) {
		List<TvShow> tvShows = new ArrayList<>();
		for (Element showElement : orfDocument.select(dayTimeStringCss)) {
			Element timeElement = showElement.select(".starttime").first();
			if (timeElement == null) {
				continue;
			}
			String timeFromString = timeElement.getElementsByTag("h3").first().text();
			Date timeFrom;
			try {
				timeFrom = TvShow.TV_DATE_FORMAT.parse(timeFromString);
			} catch (ParseException e) {
				e.printStackTrace();
				continue;
			}

			String title = showElement.select(".teaser h2").text();
			String subtitle = showElement.select(".teaser h3").text();

			tvShows.add(new TvShow(title, subtitle, timeFrom, new Date(), tvStation));
		}
		return tvShows;
	}

}
