package recommender.mo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import similarity.Similarity.SimType;


public class MOGrpCommonContactTwitterFollowingRecommender extends MORecommenderBase{
	
	static String[] simTableNameListFlickr = {"UserSimFlickrGroups", "UserSimFlickrCmnContacts"};
	static String[] simTableNameListTwitter ={"UserSimTwitterFollowing"};
	
	static SimType[] fieldNamesFlickr = {SimType.FLICKR_GRP, SimType.FLICKR_CMN_CONTACTS};
	static SimType[] fieldNamesTwitter = {SimType.TWITTER_FOLLOWING};
	
	
	public String getRecName(){
		return "MO_GrpCmnContactsTwitterFollowing";
	}
	

	public MOGrpCommonContactTwitterFollowingRecommender(
			String targetTableName) {
		super(targetTableName);

		// which sim tables are used		
		simTableNamesList = new ArrayList<List<String>>();
		List<String> listFlickr = Arrays.asList(simTableNameListFlickr);
		List<String> listTwitter = Arrays.asList(simTableNameListTwitter);
		simTableNamesList.add(listFlickr);
		simTableNamesList.add(listTwitter);

		// which features are used 
		featuresToUseList =new ArrayList<List<SimType>>();
		List<SimType> listFlickrSim = Arrays.asList(fieldNamesFlickr);
		List<SimType> listTwitterSim = Arrays.asList(fieldNamesTwitter);
		featuresToUseList.add(listFlickrSim);
		featuresToUseList.add(listTwitterSim);
	}

}
