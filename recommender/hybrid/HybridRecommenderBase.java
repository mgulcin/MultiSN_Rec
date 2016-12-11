package recommender.hybrid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

import recommender.Rating;
import recommender.Recommendation;
import recommender.cf.CFRecommenderBase;
import similarity.MOBasedSimilarityCalculator;
import similarity.Similarity;
import similarity.Similarity.SimType;
import util.Util;

public abstract class HybridRecommenderBase extends CFRecommenderBase{

	protected ArrayList<List<String>> simTableNamesList;
	protected ArrayList<List<SimType>> featuresToUseList;
	
	protected double extensionParam;// get all users in descending order
	
	public HybridRecommenderBase(String targetTableName) {
		super(targetTableName);
		extensionParam = 1;
	}

	// abstract methods
	public abstract String getRecName();


	// concrete methods
	// 
	public ArrayList<Recommendation> recommend(Integer target, Integer neighborCount, 
			Integer outputListSize)
					throws IOException {
		// find item-recScore by collobarative filtering

		// 1) Find k-many similar elements 
		Set<Integer> similars = getMostSimilars(target, neighborCount, simTableNamesList);


		// 3) Find recommendations - sorted by score!!
		PriorityQueue<Recommendation> rec = findRecommendations(target,similars); 

		// 4) Return best k recommendation as a result
		ArrayList<Recommendation> resultRecs = getBestKRecommendations(target, rec, outputListSize);

		//
		similars.clear();
		rec.clear();
		
		// return 
		return resultRecs;

	}


	/**
	 * Return most similar users
	 * 	- depending on the threshold on number of users to return and 
	 * Read these users from database
	 * @param simTableNameList
	 * @param  
	 * @param  
	 */
	public Set<Integer> getMostSimilars(Integer target, 
			Integer numberOfSimilarUsers, ArrayList<List<String>> simTableNamesList) {

		// 1) Get k-many similar users 
		Set<Integer> similarUsers = new HashSet<Integer>();
		int similarUsersCount = (int) (numberOfSimilarUsers * extensionParam);

		for(List<String> simTableNames:simTableNamesList){
			// set based on social network
			for(String simTableName:simTableNames){
				// similarities table for each social network
				Set<Integer> foundUsers = getMostSimilars(target, similarUsersCount, simTableName);
				if(foundUsers!=null){
					similarUsers.addAll(foundUsers);
				}
			}
		}

		return similarUsers;
	}


	public ArrayList<Integer> getNonDominatedUsers(
			Set<Integer> alreadySelected,
			ArrayList<Similarity<Integer>> userSimVals, Integer numberOfSimilarUsers) {

		// remove elements(non-dominated users) which are already selected
		ArrayList<Similarity<Integer>> userSimValsPruned = new ArrayList<Similarity<Integer>>();

		for(Similarity<Integer> sim:userSimVals){
			Integer userid = sim.getNeighbor();
			if(alreadySelected.contains(userid)){
				// do nothing
			} else{
				userSimValsPruned.add(sim);
			}
		}

		// run normal getSimilar users & return
		return getMostSimilarUsers(userSimValsPruned, numberOfSimilarUsers);
	}

	public ArrayList<Integer> getMostSimilarUsers(ArrayList<Similarity<Integer>> userSimVals,
			Integer numberOfSimilarUsers) {
		// 1) create dominance matrix
		MOBasedSimilarityCalculator moSimCalc = new MOBasedSimilarityCalculator();
		
		// since I collected features  in hierarchy, I change it to flat here
		ArrayList<Similarity.SimType> featuresToUse = new ArrayList<Similarity.SimType>();
		for(List<SimType> fList:featuresToUseList){
			featuresToUse.addAll(fList);
		}
		Double[][] dominanceMatrix = moSimCalc.createDominanceMatrix(userSimVals, 
				featuresToUse);

		// 2) select non-dominated friends
		ArrayList<Similarity<Integer>> nonDominatedSims = moSimCalc.findNonDominatedSims(userSimVals, 
				dominanceMatrix);

		// 3) sort non-dominated friends by checkin & sn- using the sortOrder
		//ArrayList<Similarity.SimType> sortOrder = new ArrayList<Similarity.SimType>();
		ArrayList<Integer> similarUsers = moSimCalc.sortBy(nonDominatedSims, featuresToUse, 
				numberOfSimilarUsers);		

		//System.out.println("Size: " + similarUsers.size());

		return similarUsers;
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
		
		//
		allRecommendedItems.clear();
		itemRecMap.clear();

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
