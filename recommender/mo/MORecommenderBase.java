package recommender.mo;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import recommender.hybrid.HybridRecommenderBase;
import similarity.Similarity;
import similarity.Similarity.SimType;
import util.Util;

public abstract class MORecommenderBase extends HybridRecommenderBase{

	public MORecommenderBase(String targetTableName) {
		super(targetTableName);
		this.extensionParam = 1;
	}

	// abstract methods
	public abstract String getRecName();


	// concrete methods
	/**
	 * Return most similar users
	 * 	- depending on the threshold on number of users to return and 
	 * Read these users from database
	 * @param tableNameListBlogCatalog 
	 * @param simTableNameListFacebook2 
	 * @param simTableNameListBlogCatalog
	 */
	public Set<Integer> getMostSimilars(Integer target, 
			Integer numberOfSimilarUsers, String[] tableNameListFlickr,
			String[] tableNameListTwitter, String[] tableNameListBlogCatalog,
			String[] tableNameListFacebook) {

		// 1) Get k-many similar users 
		Set<Integer> similarUsers = new HashSet<Integer>();
		int similarUsersCount = (int) (numberOfSimilarUsers * extensionParam);

		for(List<String> simTableNames:simTableNamesList){
			// set based on social network
			for(String simTableName:simTableNames){
				// similarities table for each social network
				Set<Integer> foundUsers = getMostSimilars(target, similarUsersCount, simTableName);
				if(foundUsers != null){
					similarUsers.addAll(foundUsers);
				}
			}
		}


		// 2) Get their Similarities
		ArrayList<Similarity<Integer>> userSimVals = getSimilarities(target, similarUsers, 
				simTableNamesList, featuresToUseList);


		// 3) Get most similar users using multi-obj-opt
		Set<Integer> neighbours = new HashSet<Integer>();
		while(neighbours.size() < numberOfSimilarUsers){
			ArrayList<Integer> similarUsersTemp	= getNonDominatedUsers(neighbours, 
					userSimVals,numberOfSimilarUsers);

			if(similarUsersTemp.size() > 0 ) {
				int size = similarUsersTemp.size();
				if((neighbours.size() + size) <= numberOfSimilarUsers){
					neighbours.addAll(similarUsersTemp);
				}else{
					for(int i = 0; i < size; i++){
						if(neighbours.size() < numberOfSimilarUsers){
							Integer similarUser = similarUsersTemp.get(i);
							neighbours.add(similarUser);
						} else{
							break;
						}
					}
				}
			} else{
				break;
			}
		}

		return neighbours;
	}
	
	/**
	 * Get most smilars and the ones who has equal smVal as the last one!!
	 */
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
				
				//1.5)) Fnd the ones who has the same simVa as the least smilar found in step 1
				Set<Integer> sameValSimilarsTarget = Util.getMostSameValSimilars(targetUser, numberOfSimilarUsers,
						simTableName);
				
				// combine sets of step 1 and step 1.5
				similarsTarget.addAll(sameValSimilarsTarget);
				
				// 2) Revert retIds of similarsTarget to srcIds
				Connection con2 = Util.getConnection();
				similars = Util.mapUsersFromSrc(similarsTarget, con2, srcField, retField);
			}

		} catch(Exception e){
			e.printStackTrace();
		}
		return similars;
	}

	public ArrayList<Similarity<Integer>> getSimilarities(Integer target, 
			Set<Integer> similarUsers, ArrayList<List<String>> simTableNamesList, 
			ArrayList<List<SimType>> featuresToUseList) {

		ArrayList<Similarity<Integer>> userSimVals = new ArrayList<Similarity<Integer>>();
		try{

			int sizeSimTableNamesList = simTableNamesList.size();
			int sizeFeaturesToUseList = featuresToUseList.size();

			if(sizeSimTableNamesList != sizeFeaturesToUseList){
				// there should be a mapping among simTables and fields to be used!!
				System.out.println("simTables and featuresToUse lists' sizes do NOT match!");
				System.exit(-1);
			} else {
				for(int i=0 ; i< sizeSimTableNamesList; i++){
					List<String> simTableNames = simTableNamesList.get(i);
					List<SimType> fieldNames = featuresToUseList.get(i);

					ArrayList<Similarity<Integer>> similarities = getSimilarities(target, similarUsers, 
							simTableNames, fieldNames);
					userSimVals.addAll(similarities);

				}
			}

		} catch(Exception e){
			e.printStackTrace();
		}

		return userSimVals;
	}

	private ArrayList<Similarity<Integer>> getSimilarities(Integer target,
			Set<Integer> similarUsers,	List<String> simTableNameList, List<SimType> fieldNames) {
		ArrayList<Similarity<Integer>> similarities = new ArrayList<>();

		try{
		Connection con = Util.getConnection();
		String retField = getIdFieldName(simTableNameList.get(0));// TODO Find a smart way to decide this!!
		String srcField = getIdFieldName(targetTableName);

		Integer targetUserInTargetSn = Util.mapUserFromSrc(con, target, retField, srcField);
		
		for(Integer simUser:similarUsers){
			Integer simUserInTargetSn = Util.mapUserFromSrc(con, simUser, retField, srcField);
			
			Similarity<Integer> sim = getSimilarityFromDB(targetUserInTargetSn, simUserInTargetSn, 
					simTableNameList, fieldNames);
			
			sim.setNeighbor(simUser);
			similarities.add(sim);			
		}
		
		} catch(Exception e){
			e.printStackTrace();
		}

		return similarities;
	}

	private Similarity<Integer> getSimilarityFromDB(Integer target, Integer simUser,
			List<String> simTableNameList,  List<SimType> fieldNames) {
		// get similarity using fNames
		Similarity<Integer> sim  = null;
		int size = fieldNames.size();
		for(int i = 0; i < size; i++){
			String simTableName = simTableNameList.get(i);
			SimType fName = fieldNames.get(i);
			sim = Util.getSimilarityFromDB(target,simUser, simTableName, fName,sim);
		}

		return sim;
	}



}
