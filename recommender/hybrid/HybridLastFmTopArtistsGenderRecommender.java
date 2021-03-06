package recommender.hybrid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import similarity.Similarity.SimType;


public class HybridLastFmTopArtistsGenderRecommender 
extends HybridRecommenderBase{

	static String[] simTableNameListLastFm ={"UserSimLastFmTopArtists"};

	static SimType[] fieldNamesLastFm = {SimType.LASTFM_TOP_ARTISTS, SimType.LASTFM_GENDER};
	
	public String getRecName(){
		return "HU_LastFmTopArtistsGender";
	}

	public HybridLastFmTopArtistsGenderRecommender
	(String targetTableName){
		super(targetTableName);

		// which sim tables are used		
		simTableNamesList = new ArrayList<List<String>>();
		List<String> listLastFm = Arrays.asList(simTableNameListLastFm);
		simTableNamesList.add(listLastFm);


		// which features are used for the hybridization
		featuresToUseList =new ArrayList<List<SimType>>();
		List<SimType> listLastFmSim = Arrays.asList(fieldNamesLastFm);
		featuresToUseList.add(listLastFmSim);

	}

}
