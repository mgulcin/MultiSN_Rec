package recommender.cf;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

import recommender.Rating;
import recommender.Recommendation;
import recommender.RecommenderBase;
import util.Util;

public abstract class CFRecommenderBase extends RecommenderBase{
	// for which table I'm looking for recommendation
	protected String targetTableName;

	// which table I'm looking for the similarities
	protected String simTableName; 


	public CFRecommenderBase(String targetTableName) {
		// for which table I'm looking for recommendation
		this.targetTableName = targetTableName;
	}


	// abstract methods
	public abstract String getRecName();


	// concrete methods
	protected String getIdFieldName(String tableName) {
		String idField = null;
		if(tableName.toLowerCase().contains("flickr")){
			idField = "userAnonymizedFlickrId"; 
		} else if(tableName.toLowerCase().contains("blogcatalog")){
			idField = "userAnonymizedBlogCatalogId"; 
		} else if(tableName.toLowerCase().contains("twitter")){
			idField = "userAnonymizedTwitterId"; 
		} else if(tableName.toLowerCase().contains("lastfm")){
			idField = "userAnonymizedLastFmId"; 
		} else if(tableName.toLowerCase().contains("facebook") || tableName.toLowerCase().contains("fb")){
			idField = "userAnonymizedFacebookId"; 
		} else if(tableName.toLowerCase().contains("youtube")){
			idField = "userAnonymizedYoutubeId"; 
		} else {
			System.out.println("Error in table Name. Debuf code!!");
			System.exit(-1);
		}

		return idField;
	}

	public ArrayList<Recommendation> recommend(Integer target, Integer neighborCount, 
			Integer outputListSize)
					throws IOException {
		// find item-recScore by collobarative filtering
		ArrayList<Recommendation> resultRecs = null;

		// 1) Find k-many similar elements 
		Set<Integer> similars = getMostSimilars(target, neighborCount, simTableName);

		if(similars != null){
			// 2) Find recommendations - sorted by score!!
			PriorityQueue<Recommendation> rec = findRecommendations(target,similars); 

			// 3) Return best k recommendation as a result
			resultRecs = getBestKRecommendations(target, rec, outputListSize);
		}
		// return 
		return resultRecs;

	}
	
	

	public Set<Integer> getMostSimilars(Integer target, 
			Integer numberOfSimilarUsers, String simTableName) {

		Set<Integer> similars = null;
		try{
			//0) map src user to ret user 
			Connection con = Util.getConnection();
			String retField = getIdFieldName(simTableName);
			String srcField = getIdFieldName(targetTableName);
			Integer targetUser = Util.mapUserFromSrc(con, target, retField, srcField);

			if(targetUser != null){
				// 1) Find k-many similar elements 
				Set<Integer> similarsTarget = Util.getMostSimilars(targetUser, numberOfSimilarUsers,
						simTableName);

				// 2) Revert retIds of similarsTarget to srcIds
				Connection con2 = Util.getConnection();
				similars = Util.mapUsersFromSrc(similarsTarget, con2, srcField, retField);
			}

		} catch(Exception e){
			e.printStackTrace();
		}
		return similars;
	}

	protected ArrayList<Recommendation> getBestKRecommendations(Integer target,
			PriorityQueue<Recommendation> rec, Integer outputListSize) {

		// Return best k recommendation as a result
		ArrayList<Recommendation> resultMap = new ArrayList<Recommendation>();
		while(resultMap.size() < outputListSize){
			Recommendation r = rec.poll();

			if(r!=null){
				resultMap.add(r);	
			} else {
				// no element left in the queue
				break;
			}

		}
		return resultMap;
	}


	protected PriorityQueue<Recommendation> findRecommendations(
			Integer target, Set<Integer> similars) {
		// 1) Get ratings from similar users (rating, usersWhoRecommendedThisItem )
		// item --> rating list
		HashMap<Integer, ArrayList<Rating<Integer>>> allRecommendedItems= 
				combineItems(target, similars);

		// 2) Calculate recommendation prob./score for each itemList-??
		HashMap<Integer, Double> itemRecMap = 
				calculateItemScores(target, allRecommendedItems);

		// 3) find items that contains related item & sort acc score
		PriorityQueue<Recommendation> rec = createRecs(itemRecMap);

		return rec;
	}


	protected HashMap<Integer, Double> calculateItemScores(Integer target,
			HashMap<Integer, ArrayList<recommender.Rating<Integer>>> allRecommendedItems) {
		// calculate average, for checkins
		HashMap<Integer, Double> recScores = new HashMap<Integer, Double>();

		for(Map.Entry<Integer, ArrayList<Rating<Integer>>> r:allRecommendedItems.entrySet())
		{
			Integer item = r.getKey();
			ArrayList<Rating<Integer>> recommenders = r.getValue();

			Double recScore = findRecScore(item, target, recommenders);

			recScores.put(item, recScore);
		}
		return recScores;
	}


	private Double findRecScore(Integer item, Integer target,
			ArrayList<recommender.Rating<Integer>> recommenders) {

		Double recScore = 0.0;

		// all users are assumed to be equal
		Double recommenderSize = (double) recommenders.size();
		recScore = recommenderSize;

		return recScore;

	}

	private HashMap<Integer, ArrayList<Rating<Integer>>> combineItems(
			Integer target, Set<Integer> similars) {
		// combine users & items, note do not consider user herself/hisself
		// item --> rating
		HashMap<Integer, ArrayList<Rating<Integer>>> recommendedItemsMap =
				new HashMap<Integer, ArrayList<Rating<Integer>>>();

		for(Integer neighbour:similars)
		{
			if(neighbour.equals(target))
			{
				// do not consider user herself/himself
				continue;
			}

			// for each neighbour, get previous item preferences
			ArrayList<Integer> previousItemsList =  Util.getPreviousItems(neighbour,targetTableName);

			// for each new item add to map
			// for each item add this user to map
			for(Integer item: previousItemsList)
			{
				if(recommendedItemsMap.containsKey(item)){
					// already on map -- add this neighbour to the list
					ArrayList<Rating<Integer>> ratingList = recommendedItemsMap.get(item);
					Rating<Integer> rat = new Rating<Integer>(neighbour, target, 1.0);
					ratingList.add(rat);

					// put the updated checkin val
					recommendedItemsMap.put(item, ratingList);
				} else {
					// new to map -- create new recommender rating list
					ArrayList<Rating<Integer>> ratingList = new ArrayList<Rating<Integer>>();
					Rating<Integer> rat = new Rating<Integer>(neighbour, target, 1.0);
					ratingList.add(rat);
					recommendedItemsMap.put(item, ratingList);
				}

			}

		}
		return recommendedItemsMap;
	}


	private PriorityQueue<Recommendation> createRecs(
			HashMap<Integer, Double> itemRecMap) {
		// for each item add score and create recommendation
		Comparator<Recommendation> recComp = Recommendation.ScoreComparator;
		PriorityQueue<Recommendation> recList = 
				new PriorityQueue<Recommendation>(100, recComp);

		for(Entry<Integer, Double> e:itemRecMap.entrySet())
		{
			Integer item = e.getKey();
			Double score = e.getValue();

			//item based
			Recommendation rec = new Recommendation(item, score);
			recList.add(rec);
		}

		return recList;
	}

}
