package at.jku.apidesign.orfapi;

import java.util.Date;
import java.util.List;

import at.jku.apidesign.orfapi.model.Category;
import at.jku.apidesign.orfapi.model.NewsArticle;
import at.jku.apidesign.orfapi.model.Region;

/**
 * @author dpril
 *
 */
public interface OrfApi {
	/**
	 * @return
	 */
	public List<NewsArticle> getTopNews();
	
	/**
	 * @param category
	 * @return
	 */
	public List<NewsArticle> getTopNewsByCategory(Category category);
	
	/**
	 * @param query
	 * @return
	 */
	public List<NewsArticle> searchTopNews(String query);
	
	/**
	 * @param region
	 * @return
	 */
	public List<NewsArticle> getNewsByRegion(Region region);
	
	/**
	 * @param region
	 * @param query
	 * @return
	 */
	public List<NewsArticle> searchNewsByRegion(Region region, String query);
	
	/**
	 * @param region
	 * @param from
	 * @param to
	 * @return
	 */
	public List<NewsArticle> getNewsByRegionAndDate(Region region, Date from, Date to);

	
}
