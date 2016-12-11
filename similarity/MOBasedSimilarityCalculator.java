package similarity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class MOBasedSimilarityCalculator {

	public <T>ArrayList<Similarity<T>> findNonDominatedSims(
			ArrayList<Similarity<T>> userSimVals, Double[][] dominanceMatrix) {
		/*
		 * Non-dominated solutions have a zero in the column total
		 */	
		// get colmn totals
		int columnSize = userSimVals.size();
		int rowSize = columnSize;
		Double[] colmTotals = new Double[columnSize];
		for(int j=0; j<columnSize; j++)
		{
			Double colnTotal = 0.0;
			for(int i=0; i<rowSize; i++)
			{
				colnTotal += dominanceMatrix[i][j];
			}

			colmTotals[j] = colnTotal;
		}

		// create the returned list -- may do this in the prev loop.
		ArrayList<Similarity<T>> nonDominatedSims  = new ArrayList<Similarity<T>>();
		for(int j=0; j<columnSize; j++)
		{
			if(colmTotals[j].equals(0.0))
			{
				Similarity<T> sim = userSimVals.get(j);
				nonDominatedSims.add(sim);
			}
		}

		return nonDominatedSims;
	}

	public <T> Double[][] createDominanceMatrix(ArrayList<Similarity<T>> userSimVals,
			ArrayList<Similarity.SimType> featuresToUse) {
		// create dominanceMatrix
		int size = userSimVals.size();
		Double[][] dominanceMatrix = new Double[size][size];

		// set dominanceMatrix
		int rowSize = size;
		int columnSize = size;
		for(int i=0; i<rowSize; i++)
		{
			Similarity<T> s1 = userSimVals.get(i);
			
			for(int j=0; j<columnSize; j++)
			{
				Similarity<T> s2 = userSimVals.get(j);
				Double val = doesDominate(s1,s2, featuresToUse);
				
				dominanceMatrix[i][j] = val;
			}
		}

		return dominanceMatrix;
	}
	

	protected <T>Double doesDominate(Similarity<T> s1, Similarity<T> s2, 
			ArrayList<Similarity.SimType> featuresToUse) {
		/*
		 *  In order to be dominated a solution must have a 
		 *  �score� of 0 in pairwise comparison.
		 *  
		 *  Pairwise comp: Compare elements of similarity 
		 *  st s1.FeatureScore > s2.FeatureScore --> s1.DomScore = 1 s2.DomScore = 0
		 *     s1.FeatureScore < s2.FeatureScore --> s1.DomScore = 0 s2.DomScore = 1
		 *      s1.FeatureScore == s2.FeatureScore --> s1.DomScore = 0 s2.DomScore = 0 ??
		 *      
		 *  If s1.DomScore > s2.DomScore && s2.DomScore = 0 --> return 1
		 *  Else return 0
		 */
		Double s1DomScore = 0.0;
		Double s2DomScore = 0.0;
		
		// compare s1 and s2
		int simTypecount = featuresToUse.size();
		for(int i = 0; i< simTypecount; i++)
		{
			Similarity.SimType key = featuresToUse.get(i);
			Double score1 = s1.getSimilarityMap().get(key);
			Double score2 = s2.getSimilarityMap().get(key);
			
			if(score1 != null && score2!= null &&
					score1.compareTo(score2) > 0)
			{
				s1DomScore += 1;
			}
			else if(score1 != null && score2!= null &&
					score1.compareTo(score2) < 0)
			{
				s2DomScore += 1;
			}
		}
		
		// If s1.DomScore > s2.DomScore && s2.DomScore = 0 --> return 1
		// Else return 0;
		Double dominanceVal = 0.0;
		if(s1DomScore.compareTo(s2DomScore) > 0 && s2DomScore == 0)
		{
			dominanceVal = 1.0;
		}
		
		return dominanceVal;
	}

	public ArrayList<Integer> sortBy(ArrayList<Similarity<Integer>> similarities,
			ArrayList<Similarity.SimType> sortOrder, 
			Integer numberOfSimilarUsers) {

		ArrayList<Similarity<Integer>> combinedSimilarities = 
				new ArrayList<Similarity<Integer>>(similarities);
		
		// sort, starting from last element to first(of sortOrder)
		//reverse order
		Collections.reverse(sortOrder);
		//sort
		for(Similarity.SimType sortType: sortOrder)
		{
			Collections.sort(similarities, Similarity.getComparatorType(sortType));
		}
		
		
		//get sublist
		int size = numberOfSimilarUsers;
		if(combinedSimilarities.size() < size)
		{
			size = combinedSimilarities.size();
		}

		List<Similarity<Integer>> simSubList = combinedSimilarities.subList(0, size);

		//create return list
		ArrayList<Integer> similarUsers = new ArrayList<Integer>();
		for(Similarity<Integer> s:simSubList){
			similarUsers.add(s.getNeighbor());
		}

		return similarUsers;

	}

	public <T> ArrayList<Similarity<T>> findDominaterSims(
			ArrayList<Similarity<T>> userSimVals, Double[][] dominanceMatrix, 
			Integer numberOfSimilarUsers) {
		/*
		 * Dominater solutions have a >zero in the row total
		 */	
		// get row totals
		int columnSize = userSimVals.size();
		int rowSize = columnSize;
		Double[] rowTotals = new Double[rowSize];
		for(int j=0; j<rowSize; j++)
		{
			Double rowTotal = 0.0;
			for(int i=0; i<columnSize; i++)
			{
				rowTotal += dominanceMatrix[j][i];
			}

			rowTotals[j] = rowTotal;
		}

		// create the returned list -- may do this in the prev loop.
		// collect the ones who have domainated most umber of other users
		HashMap<Double,HashSet<Similarity<T>>> dominaterMap = new HashMap<Double, HashSet<Similarity<T>>>();
		for(int j=0; j<rowSize; j++)
		{
			Double numberOfDominated = rowTotals[j];
			Similarity<T> sim = userSimVals.get(j);
			
			if(dominaterMap.containsKey(numberOfDominated)){
				// already in the map
				HashSet<Similarity<T>> simSet = dominaterMap.get(numberOfDominated);
				simSet.add(sim);
				dominaterMap.put(numberOfDominated, simSet);
			} else{
				// new to set
				HashSet<Similarity<T>> simSet = new HashSet<Similarity<T>>();
				simSet.add(sim);
				dominaterMap.put(numberOfDominated, simSet);
			}
		}
		
		// hashmap is sorted acc. key!!
		ArrayList<Similarity<T>> dominaterSims  = new ArrayList<Similarity<T>>();
		for(HashSet<Similarity<T>> simSet:dominaterMap.values()){
			if(dominaterSims.size() < numberOfSimilarUsers){
				int addSize = simSet.size();
				if((dominaterSims.size() + addSize) < numberOfSimilarUsers){
					// add all elements
					dominaterSims.addAll(simSet);
				}else{
					// add only first n 
					for(Similarity<T> sim:simSet){
						if(dominaterSims.size() < numberOfSimilarUsers){
							dominaterSims.add(sim);
						} else{
							break;
						}
					}
				}
			} else{
				break;
			}
				
		}
		
		return dominaterSims;
	}

	
}
