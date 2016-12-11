package recommender.hybridItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import recommender.Recommendation;
import recommender.RecommenderBase;

public abstract class HybridItemsRecommenderBase extends RecommenderBase{

	protected String targetTableName;
	
	public HybridItemsRecommenderBase(String targetTableName) {
		this.targetTableName = targetTableName;
	}

	// abstact methods
	public abstract String getRecName();
	public abstract ArrayList<Recommendation> recommend(Integer target, Integer neighborCount, 
			Integer outputListSize);
	
	// concrete methods
	protected ArrayList<Recommendation> combine(Integer outputListSize,
			ArrayList<ArrayList<Recommendation>> allRecs) {

		// combine lists
		HashMap<Integer, Double> recommendedMap = new HashMap<Integer, Double>();
		for(ArrayList<Recommendation> recs: allRecs){
			addRecommendationToMap(recommendedMap, recs);	
		}

		// find items that contains related item & sort acc score
		PriorityQueue<Recommendation> rec = createRecs(recommendedMap);

		// get k elements with highest score
		ArrayList<Recommendation> resultRecs = getBestKRecommendations(rec, outputListSize);

		return resultRecs;
	}
	
	private ArrayList<Recommendation> getBestKRecommendations(PriorityQueue<Recommendation> rec, 
			Integer outputListSize) {

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

	private void addRecommendationToMap(HashMap<Integer, Double> recommendedMap,
			ArrayList<Recommendation> recList) {
		if(recList != null){
			for(Recommendation rec: recList){
				if(rec != null){
					Integer recommendedItem = rec.getRecommended();
					Double recommendedScore = rec.getScore();

					// insert to hashmap
					Double score = recommendedMap.get(recommendedItem);
					if(score == null){
						score = recommendedScore;
						recommendedMap.put(recommendedItem, score);
					} else {
						score += recommendedScore;
						recommendedMap.put(recommendedItem, score);
					}
				}	
			}

		}
	}
}
