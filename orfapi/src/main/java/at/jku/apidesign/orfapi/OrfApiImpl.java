package at.jku.apidesign.orfapi;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import at.jku.apidesign.orfapi.model.Category;
import at.jku.apidesign.orfapi.model.NewsArticle;
import at.jku.apidesign.orfapi.model.Region;

public class OrfApiImpl implements OrfApi{

	@Override
	public List<NewsArticle> getTopNews() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NewsArticle> getTopNewsByCategory(Category category) {
		return getTopNews()
				.stream()
				.filter(n -> n.getCategory().equals(category))
				.collect(Collectors.toList());
	}

	@Override
	public List<NewsArticle> searchTopNews(String query) {
		String queryLowerCase = query.toLowerCase();
		
		return getTopNews()
				.stream()
				.filter(n -> n.getTitle().toLowerCase().contains(queryLowerCase))
				.collect(Collectors.toList());
	}

	@Override
	public List<NewsArticle> getNewsByRegion(Region region) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NewsArticle> searchNewsByRegion(Region region, String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NewsArticle> getNewsByRegionAndDate(Region region, Date from, Date to) {
		// TODO Auto-generated method stub
		return null;
	}

}
