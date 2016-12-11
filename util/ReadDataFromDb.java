package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import similarity.SimilarityVal;

/**
 * Mostly used for Asonam paper, for the 6sn version see MultiSNCrawler
 * @author mg
 *
 */
public class ReadDataFromDb {

	public static void main(String[] args) {

		// read flickrgroups from db and write to file
		// readAndWriteFlickrGroups();
		//readAndWriteFlickrContacts();
		//readAndWriteContactSims();
		//readAndWriteLastFmGenderSim();
		

		/*// create train-test set for flickr groups
		Double ratio = 0.20;
		createTrainTestSetFlickrGroups(ratio);*/

		/*// create train-test set for lastfm Artists!!
		Double ratio = 0.20;
		createTrainTestSetLastFmArtists(ratio);*/

		/*// create train-test set for blog catalog following!!
		Double ratio = 0.20;
		createTrainTestSetBlogCatalogFallowing(ratio);*/

	}
	
	private static void createTrainTestSetBlogCatalogFallowing(Double ratio) {
		try {
			// get connection
			Connection con = Util.getConnection();

			// read data from db (may read from file?)
			// user id --> collection of other users ids (following ids)
			HashMap<Integer, Collection<Integer>> resultMap = readBlogCatalogFallowing(con);

			// divide the data into train and test sets
			ArrayList<HashMap<Integer, Collection<Integer>>> trainTestSets = 
					creteTrainTestSet(ratio,resultMap);
			HashMap<Integer, Collection<Integer>> trainMap = trainTestSets.get(0);
			HashMap<Integer, Collection<Integer>> testMap = trainTestSets.get(1);

			// write the results to a file
			String pathTrainMultiLine  = ".//data//blogCatalogFallowingTrainMultiLine.csv";
			Util.writeToFileMultiLine(pathTrainMultiLine, trainMap);
			String pathTrain  = ".//data//blogCatalogFallowingTrain.csv";
			Util.writeToFile(pathTrain, trainMap);

			// write the results to a file
			String pathTestMultiLine  = ".//data//blogCatalogFallowingTestMultiLine.csv";
			Util.writeToFileMultiLine(pathTestMultiLine, testMap);
			String pathTest  = ".//data//blogCatalogFallowingTest.csv";
			Util.writeToFile(pathTest, testMap);



		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void createTrainTestSetLastFmArtists(Double ratio) {
		try {
			// get connection
			Connection con = Util.getConnection();

			// read data from db (may read from file?)
			// user id --> collection of artist ids
			HashMap<Integer, Collection<Integer>> resultMap = readLastFmArtists(con);

			// divide the data into train and test sets
			ArrayList<HashMap<Integer, Collection<Integer>>> trainTestSets = 
					creteTrainTestSet(ratio,resultMap);
			HashMap<Integer, Collection<Integer>> trainMap = trainTestSets.get(0);
			HashMap<Integer, Collection<Integer>> testMap = trainTestSets.get(1);

			// write the results to a file
			String pathTrainMultiLine  = ".//data//lastFmArtistsTrainMultiLine.csv";
			Util.writeToFileMultiLine(pathTrainMultiLine, trainMap);
			String pathTrain  = ".//data//lastFmArtistsTrain.csv";
			Util.writeToFile(pathTrain, trainMap);

			// write the results to a file
			String pathTestMultiLine  = ".//data//lastFmArtistsTestMultiLine.csv";
			Util.writeToFileMultiLine(pathTestMultiLine, testMap);
			String pathTest  = ".//data//lastFmArtistsTest.csv";
			Util.writeToFile(pathTest, testMap);



		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static HashMap<Integer, Collection<Integer>> readLastFmArtists(Connection con) {
		HashMap<Integer, Collection<Integer>> resultMap = 
				new HashMap<Integer, Collection<Integer>>();
		String tableName = "lastfmuserstopartistsanonymizedtable";
		try {
			// prepare statement			
			String sql = "select userAnonymizedLastFmId, artistAnonymizedLastFmId "
					+ " from "+tableName;
			PreparedStatement preparedStatement = con.prepareStatement(sql);

			// execute query
			System.out.println(preparedStatement.toString());
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				Integer userid = result.getInt("userAnonymizedLastFmId");
				Integer artistId = result.getInt("artistAnonymizedLastFmId");

				// populate resultMap
				Collection<Integer> artistOfUser = resultMap.get(userid);
				if(artistOfUser == null){
					// no artist is added for user1 yet, create the set and add this artist
					artistOfUser = new HashSet<Integer>();
					artistOfUser.add(artistId);
					resultMap.put(userid, artistOfUser);
				} else{
					// there is at least one artist for userid, add this artist too
					artistOfUser.add(artistId);
					resultMap.put(userid, artistOfUser);
				}
			}
			// close stmt
			preparedStatement.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	static HashMap<Integer, Collection<Integer>> readBlogCatalogFallowing(Connection con) {
		HashMap<Integer, Collection<Integer>> resultMap = 
				new HashMap<Integer, Collection<Integer>>();
		String tableName = "blogcatalogfollowinganonymizedtable";
		try {
			// prepare statement			
			String sql = "select userAnonymizedBlogCatalogId, followingAnonymizedBlogCatalogId "
					+ " from "+tableName;
			PreparedStatement preparedStatement = con.prepareStatement(sql);

			// execute query
			System.out.println(preparedStatement.toString());
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				Integer userid = result.getInt("userAnonymizedBlogCatalogId");
				Integer followingId = result.getInt("followingAnonymizedBlogCatalogId");

				// populate resultMap
				Collection<Integer> followingOfUser = resultMap.get(userid);
				if(followingOfUser == null){
					// no followingId is added for user1 yet, create the set and add this followingId
					followingOfUser = new HashSet<Integer>();
					followingOfUser.add(followingId);
					resultMap.put(userid, followingOfUser);
				} else{
					// there is at least one followingId for userid, add this followingId too
					followingOfUser.add(followingId);
					resultMap.put(userid, followingOfUser);
				}
			}
			// close stmt
			preparedStatement.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	private static void createTrainTestSetFlickrGroups(Double ratio) {
		try {
			// get connection
			Connection con = Util.getConnection();

			// read data from db (may read from file?)
			// user id --> collection of group ids
			HashMap<Integer, Collection<Integer>> resultMap = readFlickrGroups(con);

			// divide the data into train and test sets
			ArrayList<HashMap<Integer, Collection<Integer>>> trainTestSets = 
					creteTrainTestSet(ratio,resultMap);
			HashMap<Integer, Collection<Integer>> trainMap = trainTestSets.get(0);
			HashMap<Integer, Collection<Integer>> testMap = trainTestSets.get(1);

			// write the results to a file
			String pathTrainMultiLine  = ".//data//flickrGroupsTrainMultiLine.csv";
			Util.writeToFileMultiLine(pathTrainMultiLine, trainMap);
			String pathTrain  = ".//data//flickrGroupsTrain.csv";
			Util.writeToFile(pathTrain, trainMap);

			// write the results to a file
			String pathTestMultiLine  = ".//data//flickrGroupsTestMultiLine.csv";
			Util.writeToFileMultiLine(pathTestMultiLine, testMap);
			String pathTest  = ".//data//flickrGroupsTest.csv";
			Util.writeToFile(pathTest, testMap);



		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Divide resultMap into train and test set.
	 * The group count of the user in the result map: GC
	 * If GC*ratio >= 1--> randomly chose int(GC*ratio) many elements add to test set, and add the rest to train set
	 * Otherwise, add all to the train set
	 * @param ratio
	 * @param resultMap
	 * @return
	 */
	private static ArrayList<HashMap<Integer, Collection<Integer>>> creteTrainTestSet(
			Double ratio, HashMap<Integer, Collection<Integer>> resultMap) {
		// create train and test sets
		HashMap<Integer, Collection<Integer>> trainSet = new HashMap<Integer, Collection<Integer>>();
		HashMap<Integer, Collection<Integer>> testSet = new HashMap<Integer, Collection<Integer>>();

		// divide the original data into train and test
		for(Entry<Integer, Collection<Integer>> entry:resultMap.entrySet()){
			Integer userid = entry.getKey();
			Collection<Integer> groups = entry.getValue();

			Integer countTest = (int) Math.floor(groups.size()*ratio);
			if(countTest > 0){
				// randomly select countTest many groups, and add them to the test set. 
				// Add the rest to the train set
				Integer[] groupIdArray = new Integer[groups.size()];
				groupIdArray = groups.toArray(groupIdArray);

				// get random indices
				int max = groups.size()-1;
				int min = 0;
				Set<Integer> indices = collectRandomVals(min, max, countTest);


				Collection<Integer> groupsTest = new HashSet<Integer>();
				for(Integer index:indices){
					groupsTest.add(groupIdArray[index]);
				}
				testSet.put(userid, groupsTest);

				// populate train set
				Collection<Integer> groupsTrain = new HashSet<Integer>();
				for(Integer index=0; index < groups.size(); index++){
					if(indices.contains(index) == false){
						groupsTrain.add(groupIdArray[index]);
					}
				}
				trainSet.put(userid, groupsTrain);

			} else{
				// the user has not many groups, I will not send anything to test set
				Collection<Integer> groupsCopy = new HashSet<Integer>(groups);
				trainSet.put(userid, groupsCopy);
			}

		}

		// create returning array and populate with the created sets
		ArrayList<HashMap<Integer, Collection<Integer>>> trainTestSets = 
				new ArrayList<HashMap<Integer,Collection<Integer>>>();
		trainTestSets.add(trainSet);
		trainTestSets.add(testSet);

		// return
		return trainTestSets;
	}

	private static Set<Integer> collectRandomVals(int min, int max,
			Integer countTest) {
		Set<Integer> randomVals = new HashSet<Integer>(countTest);
		while(randomVals.size() < countTest){
			int randVal = randInt(min,max);
			randomVals.add(randVal);
		}
		return randomVals;
	}


	/**
	 * Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	private static void readAndWriteFlickrGroups() {
		try {

			// get connection
			Connection con = Util.getConnection();

			// get flickrgroups (my aim is to suggest new groups to the users)
			// user id --> collection of group ids
			HashMap<Integer, Collection<Integer>> resultMap = readFlickrGroups(con);

			// write the results to a file
			String path  = ".//data//flickrGroups.csv";
			Util.writeToFile(path, resultMap);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void readAndWriteContactSims() {
		try {

			// get connection
			Connection con = Util.getConnection();

			// get flickrgroups (my aim is to suggest new groups to the users)
			// user id --> collection of group ids
			HashMap<Integer, Collection<SimilarityVal>> resultMap = readFlickrContactSims(con);

			// write the results to a file
			String path  = ".//data//flickrContactSimsMultiLine.csv";
			Util.writeToFileMultiLine(path, resultMap);



		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private static void readAndWriteFlickrContacts() {
		try {

			// get connection
			Connection con = Util.getConnection();

			// get flickrgroups (my aim is to suggest new groups to the users)
			// user id --> collection of group ids
			HashMap<Integer, Collection<Integer>> resultMap = readFlickrContacts(con);

			// write the results to a file
			String path  = ".//data//flickrContactsMultiLine.csv";
			Util.writeToFileMultiLine(path, resultMap);


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void readAndWriteLastFmGenderSim() {
		try {

			// get connection
			Connection con = Util.getConnection();

			// get flickrgroups (my aim is to suggest new groups to the users)
			// user id --> collection of group ids
			HashMap<Integer, Collection<SimilarityVal>> resultMap = readLastFmGenderSim(con);

			// write the results to a file
			String path  = ".//data//lastFmGenderSimMultiLine.csv";
			Util.writeToFileMultiLine(path, resultMap);


		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	static HashMap<Integer, Collection<Integer>> readFlickrGroups(Connection con) {
		HashMap<Integer, Collection<Integer>> resultMap = 
				new HashMap<Integer, Collection<Integer>>();
		String tableName = "flickrGroupsAnonymizedTable";
		try {
			// prepare statement			
			String sql = "select userAnonymizedFlickrId, groupAnonymizedFlickrId "
					+ " from "+tableName;
			PreparedStatement preparedStatement = con.prepareStatement(sql);

			// execute query
			System.out.println(preparedStatement.toString());
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				Integer userid = result.getInt("userAnonymizedFlickrId");
				Integer groupId = result.getInt("groupAnonymizedFlickrId");

				// populate resultMap
				Collection<Integer> groupsOfUser = resultMap.get(userid);
				if(groupsOfUser == null){
					// no groups are added for user1 yet, create the set and add this group
					groupsOfUser = new HashSet<Integer>();
					groupsOfUser.add(groupId);
					resultMap.put(userid, groupsOfUser);
				} else{
					// there is at least one group for userid, add this group too
					groupsOfUser.add(groupId);
					resultMap.put(userid, groupsOfUser);
				}
			}
			// close stmt
			preparedStatement.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}



	static HashMap<Integer, Collection<SimilarityVal>> readFlickrContactSims(Connection con) {
		HashMap<Integer, Collection<SimilarityVal>> resultMap = 
				new HashMap<Integer, Collection<SimilarityVal>>();
		String tableName = "UserSimFlickrCmnContacts";
		try {
			// prepare statement			
			String sql = "select userid1,userid2, simVal "
					+ " from "+tableName;
			PreparedStatement preparedStatement = con.prepareStatement(sql);

			// execute query
			System.out.println(preparedStatement.toString());
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				Integer userid1 = result.getInt("userid1");
				Integer userid2 = result.getInt("userid2");
				Double simVal = result.getDouble("simVal");
				SimilarityVal contactSim = new SimilarityVal(userid1, userid2, simVal);

				// populate resultMap
				Collection<SimilarityVal> contactsOfUser = resultMap.get(userid1);
				if(contactsOfUser == null){
					// no contacts are added for user1 yet, create the set and add this group
					contactsOfUser = new HashSet<SimilarityVal>();
					contactsOfUser.add(contactSim);
					resultMap.put(userid1, contactsOfUser);
				} else{
					// there is at least one contact for userid, add this group too
					contactsOfUser.add(contactSim);
					resultMap.put(userid1, contactsOfUser);
				}
			}
			// close stmt
			preparedStatement.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	static HashMap<Integer, Collection<SimilarityVal>> readLastFmGenderSim(Connection con) {
		HashMap<Integer, Collection<SimilarityVal>> resultMap = 
				new HashMap<Integer, Collection<SimilarityVal>>();
		String tableName = "UserSimLastFmGender";
		try {
			// prepare statement			
			String sql = "select userid1,userid2, simVal "
					+ " from "+tableName;
			PreparedStatement preparedStatement = con.prepareStatement(sql);

			// execute query
			System.out.println(preparedStatement.toString());
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				Integer userid1 = result.getInt("userid1");
				Integer userid2 = result.getInt("userid2");
				Double simVal = result.getDouble("simVal");
				SimilarityVal simValue = new SimilarityVal(userid1, userid2, simVal);

				// populate resultMap
				Collection<SimilarityVal> simVals = resultMap.get(userid1);
				if(simVals == null){
					// no contacts are added for user1 yet, create the set and add this group
					simVals = new HashSet<SimilarityVal>();
					simVals.add(simValue);
					resultMap.put(userid1, simVals);
				} else{
					// there is at least one contact for userid, add this group too
					simVals.add(simValue);
					resultMap.put(userid1, simVals);
				}
			}
			// close stmt
			preparedStatement.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	static HashMap<Integer, Collection<Integer>> readFlickrContacts(Connection con) {
		HashMap<Integer, Collection<Integer>> resultMap = 
				new HashMap<Integer, Collection<Integer>>();
		String tableName = "flickcontactsanonymizedtable";
		try {
			// prepare statement			
			String sql = "select userAnonymizedFlickrId, contactAnonymizedFlickrId "
					+ " from "+tableName;
			PreparedStatement preparedStatement = con.prepareStatement(sql);

			// execute query
			System.out.println(preparedStatement.toString());
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				Integer userid = result.getInt("userAnonymizedFlickrId");
				Integer contactId = result.getInt("contactAnonymizedFlickrId");

				// populate resultMap
				Collection<Integer> contactsOfUser = resultMap.get(userid);
				if(contactsOfUser == null){
					// no groups are added for user1 yet, create the set and add this group
					contactsOfUser = new HashSet<Integer>();
					contactsOfUser.add(contactId);
					resultMap.put(userid, contactsOfUser);
				} else{
					// there is at least one group for userid, add this group too
					contactsOfUser.add(contactId);
					resultMap.put(userid, contactsOfUser);
				}
			}
			// close stmt
			preparedStatement.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}




}
