package at.jku.apidesign.orfapi;

import java.util.Date;
import java.util.List;

import at.jku.apidesign.orfapi.exception.OrfApiException;
import at.jku.apidesign.orfapi.model.Category;
import at.jku.apidesign.orfapi.model.NewsArticle;
import at.jku.apidesign.orfapi.model.Region;

/**
 * @author dpril
 *
 */
public interface OrfNewsApi {

	/**
	 * Extracts the NewsArticles of news.orf.at
	 * 
	 * @return
	 */
	public List<NewsArticle> getTopNews() throws OrfApiException;

	/**
	 * @param category
	 * @return
	 */
	public List<NewsArticle> getTopNewsByCategory(Category category) throws OrfApiException;

	/**
	 * @param query
	 * @return
	 */
	public List<NewsArticle> searchTopNews(String query) throws OrfApiException;

	/**
	 * @param region
	 * @return
	 */
	public List<NewsArticle> getNewsByRegion(Region region) throws OrfApiException;

	/**
	 * @param region
	 * @param query
	 * @return
	 */
	public List<NewsArticle> searchNewsByRegion(Region region, String query) throws OrfApiException;

	/**
	 * @param region
	 * @param from
	 * @param to
	 * @return
	 */
	public List<NewsArticle> getNewsByRegionAndDate(Region region, Date from, Date to) throws OrfApiException;
}
