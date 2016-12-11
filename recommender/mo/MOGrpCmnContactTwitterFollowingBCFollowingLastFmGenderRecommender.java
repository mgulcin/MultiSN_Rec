package recommender.mo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import similarity.Similarity.SimType;


public class MOGrpCmnContactTwitterFollowingBCFollowingLastFmGenderRecommender 
extends MORecommenderBase{

	static String[] simTableNameListFlickr = {"UserSimFlickrGroups", "UserSimFlickrCmnContacts"};
	static String[] simTableNameListTwitter ={"UserSimTwitterFollowing"};
	static String[] simTableNameListBlogCatalog ={"UserSimBlogCatalogFollowing"};
	static String[] simTableNameListLastFm ={"UserSimLastFmGender"};

	static SimType[] fieldNamesFlickr = {SimType.FLICKR_GRP, SimType.FLICKR_CMN_CONTACTS};
	static SimType[] fieldNamesTwitter = {SimType.TWITTER_FOLLOWING};
	static SimType[] fieldNamesBlogCatalog = {SimType.BLOGCATALOG_FOLLOWING};
	static SimType[] fieldNamesLastFm = {SimType.LASTFM_GENDER};

	
	public String getRecName(){
		return "MO_GrpCmnContactTwitterFollowingBCFollowingLastFmGender";
	}

	public MOGrpCmnContactTwitterFollowingBCFollowingLastFmGenderRecommender(
			String targetTableName) {
		super(targetTableName);

		// which sim tables are used		
		simTableNamesList = new ArrayList<List<String>>();
		List<String> listFlickr = Arrays.asList(simTableNameListFlickr);
		List<String> listTwitter = Arrays.asList(simTableNameListTwitter);
		List<String> listBlogCatalog = Arrays.asList(simTableNameListBlogCatalog);
		List<String> listLastFm = Arrays.asList(simTableNameListLastFm);
		simTableNamesList.add(listFlickr);
		simTableNamesList.add(listTwitter);
		simTableNamesList.add(listBlogCatalog);
		simTableNamesList.add(listLastFm);

		// which features are used 
		featuresToUseList =new ArrayList<List<SimType>>();
		List<SimType> listFlickrSim = Arrays.asList(fieldNamesFlickr);
		List<SimType> listTwitterSim = Arrays.asList(fieldNamesTwitter);
		List<SimType> listBlogCatalogSim = Arrays.asList(fieldNamesBlogCatalog);
		List<SimType> listLastfmSim = Arrays.asList(fieldNamesLastFm);
		featuresToUseList.add(listFlickrSim);
		featuresToUseList.add(listTwitterSim);
		featuresToUseList.add(listBlogCatalogSim);
		featuresToUseList.add(listLastfmSim);
	}

	
}
