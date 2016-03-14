# ORF-API v1.0

The website of the ORF (http://orf.at/) contains a lot of interesting information which can be extracted by directly calling and reading the page content. News reports (sports, economics, religion....), tv programs and more are accessible on multiple subpages/suburls. This API extracts this unstructured data and returns it in structured form (Java Objects).

# Available APIs

## OrfNewsApi
#### getTopNews
Reads News Articles of the main ORF News stream (http://news.orf.at).

#### getTopNewsByCategory
Reads News Articles of the main ORF News stream (http://news.orf.at) filtered by Category

#### searchTopNews
Searches News Articles of the main ORF News stream (http://news.orf.at)

#### getNewsByRegion
Reads News Articles of a Sub-Region News stream (of provinces in Austria - e.g. ooe.orf.at/news shows news articles of Upper Austria).

#### searchNewsByRegion
Searches News Articles of a Sub-Region News stream (of provinces in Austria - e.g. ooe.orf.at/news shows news articles of Upper Austria).

#### getNewsByRegionAndDate
Reads News Articles of a Sub-Region News stream in a specific Time Range (of provinces in Austria - e.g. ooe.orf.at/news shows news articles of Upper Austria).


## OrfTvApi
#### getUpcomingTvShows
Reads upcoming shows of ORF1 and ORF2

#### getDailyProgramByTvStation
Reads shows for a specific tv station

#### getProgramByTvStationForDay
Reads shows for a specific tv station and date

#### getPrimetimeProgramByTvStationForDay
Reads the primetime program for a tv station and date


# Sample Usage
```java
OrfNewsApi orfApi = new OrfNewsApi();
orfApi.useCaching(true);
List<NewsArticle> topNews = orfApi.getTopNews();

OrfTvApi orfTvApi = new OrfTvApi();
List<TvShow> upcoming = orfTvApi.getUpcomingTvShows();
```

In the folder `sample-app` there is also a sample console application which can be used to test the api and to look up how the API works.
