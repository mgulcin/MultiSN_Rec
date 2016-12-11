package recommender.mo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import similarity.Similarity.SimType;


public class MOLastFmTopArtistsGenderRecommender extends MORecommenderBase{

	static String[] simTableNameListLastFm ={"UserSimLastFmTopArtists"};
	
	static SimType[] fieldNamesLastFm = {SimType.LASTFM_TOP_ARTISTS,SimType.LASTFM_GENDER};
	

	public String getRecName(){
		return "MO_LastFmTopArtistsGender";
	}

	public MOLastFmTopArtistsGenderRecommender(
			String targetTableName) {
		super(targetTableName);

		// which sim tables are used		
		simTableNamesList = new ArrayList<List<String>>();
		List<String> listLastFm = Arrays.asList(simTableNameListLastFm);
		simTableNamesList.add(listLastFm);
		
		// which features are used 
		featuresToUseList =new ArrayList<List<SimType>>();
		List<SimType> listLastfmSim = Arrays.asList(fieldNamesLastFm);
		featuresToUseList.add(listLastfmSim);
	}
}
