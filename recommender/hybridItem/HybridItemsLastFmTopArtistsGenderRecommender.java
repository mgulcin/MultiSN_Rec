package recommender.hybridItem;

import java.util.ArrayList;

import recommender.Recommendation;
import recommender.cf.CFLastFmTopArtistsRecommender;
import recommender.cf.CFLastFmUserGenderRecommender;

public class HybridItemsLastFmTopArtistsGenderRecommender 
extends HybridItemsRecommenderBase{
	public String getRecName(){
		return "HI_LastFmTopArtistsGender";
	}

	public HybridItemsLastFmTopArtistsGenderRecommender
	(String targetTableName){
		super(targetTableName);
	}

	// 
	public ArrayList<Recommendation> recommend(Integer target, Integer neighborCount, 
			Integer outputListSize) {
		ArrayList<Recommendation> resultRecs = null;

		try{
			ArrayList<ArrayList<Recommendation>> allRecs = new ArrayList<ArrayList<Recommendation>>();

			// get LastFmTopArtists based recs
			CFLastFmTopArtistsRecommender crLTA = new CFLastFmTopArtistsRecommender(targetTableName);
			ArrayList<Recommendation> recCRLTA = crLTA.recommend(target, neighborCount, outputListSize);
			allRecs.add(recCRLTA);

			// get LastFmGender based recs
			CFLastFmUserGenderRecommender crLG = new CFLastFmUserGenderRecommender(targetTableName);
			ArrayList<Recommendation> recCRLG = crLG.recommend(target, neighborCount, outputListSize);
			allRecs.add(recCRLG);


			// combine and get results
			resultRecs = combine( outputListSize, allRecs);
		} catch(Exception e){
			e.printStackTrace();
		}

		// return 
		return resultRecs;

	}
}
