package recommender.hybridItem;

import java.util.ArrayList;

import recommender.Recommendation;
import recommender.cf.CFBlogCatalogFollowingRecommender;
import recommender.cf.CFFlickrCommonContactsRecommender;
import recommender.cf.CFFlickrGrpRecommender;


public class HybridItemsFlickrGrpCommonContactBlogCatalogFollowingRecommender
extends HybridItemsRecommenderBase{

	public String getRecName(){
		return "HI_GrpCmnContactsBlogCatalogFollowing";
	}

	public HybridItemsFlickrGrpCommonContactBlogCatalogFollowingRecommender
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

			// get BlogCatalogFollowing based recs
			CFBlogCatalogFollowingRecommender crBFR = new CFBlogCatalogFollowingRecommender(targetTableName);
			ArrayList<Recommendation> recCRBFR = crBFR.recommend(target, neighborCount, outputListSize);
			allRecs.add(recCRBFR);

			// combine and get results
			resultRecs = combine( outputListSize, allRecs);
		} catch(Exception e){
			e.printStackTrace();
		}

		// return 
		return resultRecs;

	}



}
