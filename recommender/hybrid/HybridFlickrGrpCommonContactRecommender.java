package recommender.hybrid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import similarity.Similarity.SimType;


public class HybridFlickrGrpCommonContactRecommender extends HybridRecommenderBase{

	static String[] simTableNameListFlickr = {"UserSimFlickrGroups", "UserSimFlickrCmnContacts"};
	static SimType[] fieldNamesFlickr = {SimType.FLICKR_GRP, SimType.FLICKR_CMN_CONTACTS};
	
	public String getRecName(){
		return "HU_GrpCmnContacts";
	}
	public HybridFlickrGrpCommonContactRecommender
	(String targetTableName){
		super(targetTableName);

		// which sim tables are used		
		simTableNamesList = new ArrayList<List<String>>();
		List<String> listFlickr = Arrays.asList(simTableNameListFlickr);
		simTableNamesList.add(listFlickr);

		// which features are used for the hybridization
		featuresToUseList =new ArrayList<List<SimType>>();
		List<SimType> listFlickrSim = Arrays.asList(fieldNamesFlickr);
		featuresToUseList.add(listFlickrSim);

	}	
}
