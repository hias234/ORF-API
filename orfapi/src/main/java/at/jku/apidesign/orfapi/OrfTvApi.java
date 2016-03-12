package at.jku.apidesign.orfapi;

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
	public List<TvShow> getDailyProgramBySender(String tvSender);
}
