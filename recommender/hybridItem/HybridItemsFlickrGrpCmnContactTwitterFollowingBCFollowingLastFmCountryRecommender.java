package recommender.hybridItem;

import java.util.ArrayList;

import recommender.Recommendation;
import recommender.cf.CFBlogCatalogFollowingRecommender;
import recommender.cf.CFFlickrCommonContactsRecommender;
import recommender.cf.CFFlickrGrpRecommender;
import recommender.cf.CFLastFmUserCountryRecommender;
import recommender.cf.CFTwitterFollowingRecommender;

public class HybridItemsFlickrGrpCmnContactTwitterFollowingBCFollowingLastFmCountryRecommender 
extends HybridItemsRecommenderBase{
	
	public String getRecName(){
		return "HI_GrpCmnContactsTwitterFollowingBCFollowingLastFmCountry";
	}
	
	public HybridItemsFlickrGrpCmnContactTwitterFollowingBCFollowingLastFmCountryRecommender
	(String targetTableName){
		super(targetTableName);
	}

	// 
	public ArrayList<Recommendation> recommend(Integer target, Integer neighborCount, 
			Integer outputListSize) {
		ArrayList<Recommendation> resultRecs = null;
		
		try{
		ArrayList<ArrayList<Recommendation>> allRecs = new ArrayList<ArrayList<Recommendation>>();
		// get Group based recs
		CFFlickrGrpRecommender cfGR = new CFFlickrGrpRecommender(targetTableName);
		ArrayList<Recommendation> recCFGR = cfGR.recommend(target, neighborCount, outputListSize);
		allRecs.add(recCFGR);

		// get CmnContact based recs
		CFFlickrCommonContactsRecommender cfCCR = new CFFlickrCommonContactsRecommender(targetTableName);
		ArrayList<Recommendation> recCFCCR = cfCCR.recommend(target, neighborCount, outputListSize);
		allRecs.add(recCFCCR);

		// get TwitterFollowing based recs
		CFTwitterFollowingRecommender crTFR = new CFTwitterFollowingRecommender(targetTableName);
		ArrayList<Recommendation> recCRTFR = crTFR.recommend(target, neighborCount, outputListSize);
		allRecs.add(recCRTFR);

		// get BlogCatalogFollowing based recs
		CFBlogCatalogFollowingRecommender crBFR = new CFBlogCatalogFollowingRecommender(targetTableName);
		ArrayList<Recommendation> recCRBFR = crBFR.recommend(target, neighborCount, outputListSize);
		allRecs.add(recCRBFR);
		
		// get LastFmCountry based recs
		CFLastFmUserCountryRecommender crLC = new CFLastFmUserCountryRecommender(targetTableName);
		ArrayList<Recommendation> recCRLC = crLC.recommend(target, neighborCount, outputListSize);
		allRecs.add(recCRLC);

		// combine and get results
		resultRecs = combine( outputListSize, allRecs);
		} catch(Exception e){
			e.printStackTrace();
		}
		// return 
		return resultRecs;

	}
}
