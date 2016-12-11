package recommender.mo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import similarity.Similarity.SimType;


public class MOGrpCommonContactRecommender extends MORecommenderBase{
	static String[] simTableNameListFlickr = {"UserSimFlickrGroups", "UserSimFlickrCmnContacts"};
	static SimType[] fieldNamesFlickr = {SimType.FLICKR_GRP, SimType.FLICKR_CMN_CONTACTS};
	
	public String getRecName(){
		return "MO_GrpCmnContacts";
	}
	
	
	public MOGrpCommonContactRecommender(
			String targetTableName) {
		super(targetTableName);

		// which sim tables are used		
		simTableNamesList = new ArrayList<List<String>>();
		List<String> listFlickr = Arrays.asList(simTableNameListFlickr);
		simTableNamesList.add(listFlickr);

		// which features are used 
		featuresToUseList =new ArrayList<List<SimType>>();
		List<SimType> listFlickrSim = Arrays.asList(fieldNamesFlickr);
		featuresToUseList.add(listFlickrSim);
	}
		
}
