package recommender.hybridItem;

import java.util.ArrayList;

import recommender.Recommendation;
import recommender.cf.CFLastFmCmnFriendsRecommender;
import recommender.cf.CFLastFmTopArtistsRecommender;

public class HybridItemsLastFmTopArtistsCmnFriendsRecommender 
extends HybridItemsRecommenderBase{
	public String getRecName(){
		return "HI_LastFmTopArtistsCmnFriends";
	}

	public HybridItemsLastFmTopArtistsCmnFriendsRecommender
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

			// get LastFmCmnFriends based recs
			CFLastFmCmnFriendsRecommender crLCF = new CFLastFmCmnFriendsRecommender(targetTableName);
			ArrayList<Recommendation> recCRLCF = crLCF.recommend(target, neighborCount, outputListSize);
			allRecs.add(recCRLCF);


			// combine and get results
			resultRecs = combine( outputListSize, allRecs);
		} catch(Exception e){
			e.printStackTrace();
		}

		// return 
		return resultRecs;

	}
}
