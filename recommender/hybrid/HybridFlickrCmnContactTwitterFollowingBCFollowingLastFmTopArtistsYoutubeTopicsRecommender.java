package recommender.hybrid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import similarity.Similarity.SimType;


public class HybridFlickrCmnContactTwitterFollowingBCFollowingLastFmTopArtistsYoutubeTopicsRecommender 
extends HybridRecommenderBase{

	static String[] simTableNameListFlickr = {"UserSimFlickrCmnContacts"};
	static String[] simTableNameListTwitter ={"UserSimTwitterFollowing"};
	static String[] simTableNameListBlogCatalog ={"UserSimBlogCatalogFollowing"};
	static String[] simTableNameListLastFm ={"UserSimLastFmTopArtists"};
	static String[] simTableNameListYoutube ={"UserSimYoutubeFreebaseUserTopics"};

	static SimType[] fieldNamesFlickr = {SimType.FLICKR_CMN_CONTACTS};
	static SimType[] fieldNamesTwitter = {SimType.TWITTER_FOLLOWING};
	static SimType[] fieldNamesBlogCatalog = {SimType.BLOGCATALOG_FOLLOWING};
	static SimType[] fieldNamesLastFm = {SimType.LASTFM_TOP_ARTISTS};
	static SimType[] fieldNamesYoutube = {SimType.YOUTUBE_FREEBASE_TOPICS};
	
	public String getRecName(){
		return "HU_FlickrCmnContactTwitterFollowingBCFollowingLastFmTopArtistsYoutubeTopics";
	}

	public HybridFlickrCmnContactTwitterFollowingBCFollowingLastFmTopArtistsYoutubeTopicsRecommender
	(String targetTableName){
		super(targetTableName);

		// which sim tables are used		
		simTableNamesList = new ArrayList<List<String>>();
		List<String> listFlickr = Arrays.asList(simTableNameListFlickr);
		List<String> listTwitter = Arrays.asList(simTableNameListTwitter);
		List<String> listBlogCatalog = Arrays.asList(simTableNameListBlogCatalog);
		List<String> listLastFm = Arrays.asList(simTableNameListLastFm);
		List<String> listYoutube = Arrays.asList(simTableNameListYoutube);
		simTableNamesList.add(listFlickr);
		simTableNamesList.add(listTwitter);
		simTableNamesList.add(listBlogCatalog);
		simTableNamesList.add(listLastFm);
		simTableNamesList.add(listYoutube);

		// which features are used for the hybridization
		featuresToUseList =new ArrayList<List<SimType>>();
		List<SimType> listFlickrSim = Arrays.asList(fieldNamesFlickr);
		List<SimType> listTwitterSim = Arrays.asList(fieldNamesTwitter);
		List<SimType> listBlogCatalogSim = Arrays.asList(fieldNamesBlogCatalog);
		List<SimType> listLastFmSim = Arrays.asList(fieldNamesLastFm);
		List<SimType> listYoutubeSim = Arrays.asList(fieldNamesYoutube);
		featuresToUseList.add(listFlickrSim);
		featuresToUseList.add(listTwitterSim);
		featuresToUseList.add(listBlogCatalogSim);
		featuresToUseList.add(listLastFmSim);
		featuresToUseList.add(listYoutubeSim);

		
	}

}
