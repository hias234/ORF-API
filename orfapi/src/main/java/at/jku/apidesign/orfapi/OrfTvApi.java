package at.jku.apidesign.orfapi;

import java.util.Date;
import java.util.List;

import at.jku.apidesign.orfapi.model.TvShow;

public interface OrfTvApi {
	/**
	 * @return upcoming shows of ORF1 and ORF2
	 */
	public List<TvShow> getUpcomingTvShows();

	/**
	 * @param tvSender
	 *            (ORF1, ORF2, ORF3, Orf Sport +)
	 * @return
	 */
	public List<TvShow> getDailyProgramByTvStation(String tvStation);

	/**
	 * 
	 * @param tvSender
	 * @param day
	 * @return the tv program of a tv-station for a specific day
	 */
	public List<TvShow> getProgramByTvStationForDay(String tvStation, Date day);
}
