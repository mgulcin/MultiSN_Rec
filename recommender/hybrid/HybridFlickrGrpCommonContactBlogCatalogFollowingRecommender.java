package recommender.hybrid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import similarity.Similarity.SimType;


public class HybridFlickrGrpCommonContactBlogCatalogFollowingRecommender extends HybridRecommenderBase{

	static String[] simTableNameListFlickr = {"UserSimFlickrGroups", "UserSimFlickrCmnContacts"};
	static String[] simTableNameListBlogCatalog ={"UserSimBlogCatalogFollowing"};
	
	static SimType[] fieldNamesFlickr = {SimType.FLICKR_GRP, SimType.FLICKR_CMN_CONTACTS};
	static SimType[] fieldNamesBlogCatalog = {SimType.BLOGCATALOG_FOLLOWING};
	
	
	public String getRecName(){
		return "HU_GrpCmnContactsBlogCatalogFollowing";
	}
	public HybridFlickrGrpCommonContactBlogCatalogFollowingRecommender
	(String targetTableName){
		super(targetTableName);

		// which sim tables are used		
		simTableNamesList = new ArrayList<List<String>>();
		List<String> listFlickr = Arrays.asList(simTableNameListFlickr);
		List<String> listBlogCatalog = Arrays.asList(simTableNameListBlogCatalog);
		simTableNamesList.add(listFlickr);
		simTableNamesList.add(listBlogCatalog);

		// which features are used for the hybridization
		featuresToUseList =new ArrayList<List<SimType>>();
		List<SimType> listFlickrSim = Arrays.asList(fieldNamesFlickr);
		List<SimType> listBlogCatalogSim = Arrays.asList(fieldNamesBlogCatalog);
		featuresToUseList.add(listFlickrSim);
		featuresToUseList.add(listBlogCatalogSim);
	}	
}
