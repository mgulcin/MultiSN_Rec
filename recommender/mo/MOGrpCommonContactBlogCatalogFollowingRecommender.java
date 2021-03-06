package recommender.mo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import similarity.Similarity.SimType;


public class MOGrpCommonContactBlogCatalogFollowingRecommender extends MORecommenderBase{
	
	static String[] simTableNameListFlickr = {"UserSimFlickrGroups", "UserSimFlickrCmnContacts"};
	static String[] simTableNameListBlogCatalog ={"UserSimBlogCatalogFollowing"};
	
	static SimType[] fieldNamesFlickr = {SimType.FLICKR_GRP, SimType.FLICKR_CMN_CONTACTS};
	static SimType[] fieldNamesBlogCatalog = {SimType.BLOGCATALOG_FOLLOWING};
	

	
	public String getRecName(){
		return "MO_GrpCmnContactsBlogCatalogFollowing";
	}
	public MOGrpCommonContactBlogCatalogFollowingRecommender(
			String targetTableName) {
		super(targetTableName);

		// which sim tables are used		
		simTableNamesList = new ArrayList<List<String>>();
		List<String> listFlickr = Arrays.asList(simTableNameListFlickr);
		List<String> listBlogCatalog = Arrays.asList(simTableNameListBlogCatalog);
		simTableNamesList.add(listFlickr);
		simTableNamesList.add(listBlogCatalog);


		// which features are used 
		featuresToUseList =new ArrayList<List<SimType>>();
		List<SimType> listFlickrSim = Arrays.asList(fieldNamesFlickr);
		List<SimType> listBlogCatalogSim = Arrays.asList(fieldNamesBlogCatalog);
		featuresToUseList.add(listFlickrSim);
		featuresToUseList.add(listBlogCatalogSim);
	}

}
