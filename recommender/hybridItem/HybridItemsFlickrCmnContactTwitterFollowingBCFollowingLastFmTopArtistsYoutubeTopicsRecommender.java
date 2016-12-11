package recommender.hybridItem;

import java.util.ArrayList;

import recommender.Recommendation;
import recommender.cf.CFBlogCatalogFollowingRecommender;
import recommender.cf.CFFlickrCommonContactsRecommender;
import recommender.cf.CFLastFmTopArtistsRecommender;
import recommender.cf.CFTwitterFollowingRecommender;
import recommender.cf.CFYoutubeFreebaseUserTopicsRecommender;

public class HybridItemsFlickrCmnContactTwitterFollowingBCFollowingLastFmTopArtistsYoutubeTopicsRecommender 
extends HybridItemsRecommenderBase{
	public String getRecName(){
		return "HI_FlickrCmnContactsTwitterFollowingBCFollowingLastFmTopArtistsYoutubeTopics";
	}

	public HybridItemsFlickrCmnContactTwitterFollowingBCFollowingLastFmTopArtistsYoutubeTopicsRecommender
	(String targetTableName){
		super(targetTableName);
	}

	// 
	public ArrayList<Recommendation> recommend(Integer target, Integer neighborCount, 
			Integer outputListSize) {
		ArrayList<Recommendation> resultRecs = null;

		try{
			ArrayList<ArrayList<Recommendation>> allRecs = new ArrayList<ArrayList<Recommendation>>();

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

			// get LastFmTopArtists based recs
			CFLastFmTopArtistsRecommender crLTA = new CFLastFmTopArtistsRecommender(targetTableName);
			ArrayList<Recommendation> recCRLTA = crLTA.recommend(target, neighborCount, outputListSize);
			allRecs.add(recCRLTA);

			// get YoutubeTopics based recs
			CFYoutubeFreebaseUserTopicsRecommender crYFT = new CFYoutubeFreebaseUserTopicsRecommender(targetTableName);
			ArrayList<Recommendation> recCRYFT = crYFT.recommend(target, neighborCount, outputListSize);
			allRecs.add(recCRYFT);


			// combine and get results
			resultRecs = combine( outputListSize, allRecs);
		} catch(Exception e){
			e.printStackTrace();
		}

		// return 
		return resultRecs;

	}
}
