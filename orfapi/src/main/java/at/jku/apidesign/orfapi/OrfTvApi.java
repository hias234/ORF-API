package at.jku.apidesign.orfapi;

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

/**
 * Loads the ORF HTML-Pages from the TV-Shows and wraps them into Java-Objects.
 * 
 * @author marku
 *
 */
public final class OrfTvApi {

	private static final String ORF_TV_URL = "http://tv.orf.at/";

	/**
	 * @return upcoming shows of ORF1 and ORF2
	 */
	public final List<TvShow> getUpcomingTvShows() throws OrfApiException {
		List<TvShow> tvShows = new ArrayList<TvShow>();
		Document orfDocument = WebDocument.getJSoupDocument(ORF_TV_URL, false);

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

	/**
	 * @param tvSender
	 *            (ORF1, ORF2, ORF3, Orf Sport +)
	 * @return the program of the current day
	 */
	public final List<TvShow> getDailyProgramByTvStation(TvStation tvStation) throws OrfApiException {
		return getProgramByTvStationForDay(tvStation, new Date());
	}

	/**
	 * 
	 * @param tvSender
	 * @param day
	 * @return the tv program of a tv-station for a specific day
	 */
	public final List<TvShow> getProgramByTvStationForDay(TvStation tvStation, Date day) throws OrfApiException {
		List<TvShow> tvShows = new ArrayList<TvShow>();

		Document orfDocument = getTvDocumentOfTvStationAndDay(tvStation, day);

		tvShows.addAll(getProgramByDayTime(tvStation, orfDocument, ".tsmorning"));
		tvShows.addAll(getProgramByDayTime(tvStation, orfDocument, ".tsafternoon"));
		tvShows.addAll(getProgramByDayTime(tvStation, orfDocument, ".tsevening"));
		tvShows.addAll(getProgramByDayTime(tvStation, orfDocument, ".tsprimetime"));
		tvShows.addAll(getProgramByDayTime(tvStation, orfDocument, ".tsnight"));
		return tvShows;
	}

	/**
	 * 
	 * @param tvSender
	 * @param day
	 * @return the tv program during prime time of a tv-station for a specific
	 *         day
	 */
	public final List<TvShow> getPrimetimeProgramByTvStationForDay(TvStation tvStation, Date day)
			throws OrfApiException {
		Document orfDocument = getTvDocumentOfTvStationAndDay(tvStation, day);

		return getProgramByDayTime(tvStation, orfDocument, ".tsprimetime");
	}

	private Document getTvDocumentOfTvStationAndDay(TvStation tvStation, Date day) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMdd");
		Document orfDocument = WebDocument.getJSoupDocument(
				"http://tv.orf.at/program/" + tvStation.name().toLowerCase() + "/" + dateFormat.format(day), false);
		return orfDocument;
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
