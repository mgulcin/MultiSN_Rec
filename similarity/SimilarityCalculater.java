package similarity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import util.FilePrinter;
import util.Util;

public class SimilarityCalculater {

	static String srcField = "userAnonymizedBlogCatalogId";

	public static void main(String[] args) {
		try {
			/*--// calculate sim of flickr users' groups
			calculateFlickrGroupsSimTrain();--*/

			// calculate sim of flickr users' groups
			/*calculateFlickrGroupsSim();

			// calculate sim of flickr users' contacts 
			//Use 1.0 for contacts and 0.0 for the rest
			calculateFlickrContactsSim();

			// calculate sim of flickr users' contacts
			calculateFlickrCmnContactsSim();*/

			// calculate sim of bc users' followings
			calculateBCFollowingSimTrain(); 

			//-- calculate sim of bc users' followings
			//--calculateBCFollowingSim();--

			// calculate sim of twitter users' followings
			/*calculateTwitterFollowingSim();

			// calculate sim of facebook users' activity level (binary, i.e. same aLevel-->1 o.w. 0)
			calculateFacebookActivityLevelSim();

			// calculate sim of lastfm users' friends (cos sim)
			calculateLastFmCmnFriendsSim();

			// calculate sim of lastfm users' friends (binary: is my friend or not)
			calculateLastFmFriendsSim();

			// calculate sim of lastfm users' top albums 
			calculateLastFmTopAlbumsSim();

			// calculate sim of lastfm users' top artists 
			calculateLastFmTopArtistsSim();

			//-- calculate sim of lastfm users' top artists 
			//--calculateLastFmTopArtistsSimTrain()--;

			// calculate sim of lastfm users' gender // binary (m/f/n-unknown)
			calculateLastFmUserGenderSim();

			// calculate sim of lastfm users' country // binary 
			calculateLastFmUserCountrySim();

			// calculate sim of youtube users' subscriptions (cos sim)
			calculateYoutubeCmnSubscriptionsSim();

			// calculate sim of youtube users' subscriptions (binary: is my friend or not)
			calculateYoutubeSubscriptionsSim();

			// calculate sim of youtube users' activity Type (cos sim)
			calculateYoutubeActivityTypeSim();

			// calculate sim of youtube users' FreeBase Topics Type (cos sim)
			calculateYoutubeFreebaseUserTopicsSim();

			// calculate sim of youtube users' VideoCount (binary: is in the same cluster of videoCounts)
			calculateYoutubeUserVideoCountSim();*/

			// close any connection if exist
			Util.closeConnection();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private static void calculateYoutubeUserVideoCountSim() {
		// read input data
		String tableName = "youtubeusersvideocountanonymizedtable";
		String queryField = "videoCountLevel";
		String retField = "userAnonymizedYoutubeId";

		HashMap<Integer, Collection<Integer>> data = new HashMap<Integer, Collection<Integer>>();

		Set<Integer> targetSet = Util.getAllUsersForYoutube(srcField);
		for(Integer target:targetSet){
			ArrayList<Integer> sameCountryUsers = readDataFromDbCluster(tableName, target, queryField, retField);
			data.put(target, sameCountryUsers);
		}

		String tableNameOut = "userSimYoutubeUserVideoCount";
		assignBinarySimAndWriteToDb(tableNameOut, data);
		
		/*// assign binary sim among gender
		HashSet<SimilarityVal> fgSim = assignBinarySim(data);


		// populate db
		String tableNameOut = "userSimYoutubeUserVideoCount";
		writeToDb(tableNameOut,fgSim);*/

	}

	private static void calculateYoutubeFreebaseUserTopicsSim() {
		// read input data
		String tableName = "youtubeFreebaseUserTopicsAnonymizedTable";
		String queryField = "userAnonymizedYoutubeId";
		String retField = "freebaseTopicNotableId";

		HashMap<Integer, Collection<String>> data = new HashMap<Integer, Collection<String>>();

		Set<Integer> targetSet = Util.getAllUsersForYoutube(srcField);
		for(Integer target:targetSet){
			ArrayList<String>  subscribeds= readStringDataFromDb(tableName, target, queryField, retField);
			data.put(target, subscribeds);
		}

		// map the fields & populate db
		String tableNameOut = "userSimYoutubeFreebaseUserTopics";
		calculateCosineAndWriteToDb(tableNameOut, data);

		/*// calculate cosine sim among users
		HashSet<SimilarityVal> fgSim = calculateCosine(data);


		// populate db
		String tableNameOut = "userSimYoutubeFreebaseUserTopics";
		writeToDb(tableNameOut,fgSim);*/

	}

	private static <T> void calculateYoutubeActivityTypeSim() {
		// read input data
		String tableName = "youtubeusersactivityanonymizedtable";
		String queryField = "userAnonymizedYoutubeId";
		String retField = "type";

		HashMap<Integer, Collection<String>> data = new HashMap<Integer, Collection<String>>();

		Set<Integer> targetSet = Util.getAllUsersForYoutube(srcField);
		for(Integer target:targetSet){
			ArrayList<String>  subscribeds = readStringDataFromDb(tableName, target, queryField, retField);
			data.put(target, subscribeds);
		}

		// map the fields & populate db
		String tableNameOut = "userSimYoutubeActivityType";
		calculateCosineAndWriteToDb(tableNameOut, data);

		/*// calculate cosine sim among users
		HashSet<SimilarityVal> fgSim = calculateCosine(data);


		// populate db
		String tableNameOut = "userSimYoutubeActivityType";
		writeToDb(tableNameOut,fgSim);*/

	}

	private static void calculateYoutubeSubscriptionsSim() {
		// read input data
		String tableName = "youtubeuserssubscriptionanonymizedtable";
		String queryField = "userAnonymizedYoutubeId";
		String retField = "subscribedAnonymizedYoutubeId";

		HashMap<Integer, Collection<Integer>> data = new HashMap<Integer, Collection<Integer>>();

		Set<Integer> targetSet = Util.getAllUsersForYoutube(srcField);
		for(Integer target:targetSet){

			ArrayList<Integer>  subscribeds= readDataFromDb(tableName, target, queryField, retField);

			data.put(target, subscribeds);
		}
		
		String tableNameOut = "userSimYoutubeSubscriptions";
		assignBinarySimAndWriteToDb(tableNameOut, data);

		/*// calculate cosine sim among users
		HashSet<SimilarityVal> fgSim = assignBinarySim(data);


		// populate db
		String tableNameOut = "userSimYoutubeSubscriptions";
		writeToDb(tableNameOut,fgSim);
*/
	}


	private static void calculateYoutubeCmnSubscriptionsSim() {
		// read input data
		String tableName = "youtubeuserssubscriptionanonymizedtable";
		String queryField = "userAnonymizedYoutubeId";
		String retField = "subscribedAnonymizedYoutubeId";

		HashMap<Integer, Collection<Integer>> data = new HashMap<Integer, Collection<Integer>>();

		Set<Integer> targetSet = Util.getAllUsersForYoutube(srcField);
		for(Integer target:targetSet){

			ArrayList<Integer>  subscribeds= readDataFromDb(tableName, target, queryField, retField);

			data.put(target, subscribeds);
		}

		// map the fields & populate db
		String tableNameOut = "userSimYoutubeCmnSubscriptions";
		calculateCosineAndWriteToDb(tableNameOut, data);

		/*// calculate cosine sim among users
		HashSet<SimilarityVal> fgSim = calculateCosine(data);


		// populate db
		String tableNameOut = "userSimYoutubeCmnSubscriptions";
		writeToDb(tableNameOut,fgSim);*/

	}

	/**
	 * Calculate binary similarity among users
	 * Use lastfmusersinfoanonymizedtable
	 */
	private static void calculateLastFmUserCountrySim() {
		// read input data
		String tableName = "lastfmusersinfoanonymizedtable";
		String queryField = "countryAnonymizedLastFmId";
		String retField = "userAnonymizedLastFmId";

		HashMap<Integer, Collection<Integer>> data = new HashMap<Integer, Collection<Integer>>();

		Set<Integer> targetSet = Util.getAllUsersForLastFm(srcField);
		for(Integer target:targetSet){
			ArrayList<Integer> sameCountryUsers = readDataFromDbCluster(tableName, target, queryField, retField);
			data.put(target, sameCountryUsers);
		}

		String tableNameOut = "userSimLastFmCountry";
		assignBinarySimAndWriteToDb(tableNameOut, data);

		/*// assign binary sim among gender
		HashSet<SimilarityVal> fgSim = assignBinarySim(data);


		// populate db
		String tableNameOut = "userSimLastFmCountry";
		writeToDb(tableNameOut,fgSim);*/

	}

	/**
	 * Calculate binary similarity among users
	 * Use lastfmusersinfoanonymizedtable
	 */
	private static void calculateLastFmUserGenderSim() {
		// read input data
		String tableName = "lastfmusersinfoanonymizedtable";
		String queryField = "gender";
		String retField = "userAnonymizedLastFmId";

		HashMap<Integer, Collection<Integer>> data = new HashMap<Integer, Collection<Integer>>();

		Set<Integer> targetSet = Util.getAllUsersForLastFm(srcField);
		for(Integer target:targetSet){
			ArrayList<Integer> sameGenderUsers = readDataFromDbCluster(tableName, target, queryField, retField);
			data.put(target, sameGenderUsers);
		}

		String tableNameOut = "userSimLastFmGender";
		assignBinarySimAndWriteToDb(tableNameOut, data);
		
		/*// assign binary sim among gender
		HashSet<SimilarityVal> fgSim = assignBinarySim(data);


		// populate db
		String tableNameOut = "userSimLastFmGender";
		writeToDb(tableNameOut,fgSim);*/
	}

	/**
	 * Calculate binary similarity among users
	 * Use lastfmuserstopartistsanonymizedtable
	 */
	private static void calculateLastFmTopArtistsSim() {
		// read input data
		String tableName = "lastfmuserstopartistsanonymizedtable";
		String queryField = "userAnonymizedLastFmId";
		String retField = "artistAnonymizedLastFmId";

		HashMap<Integer, Collection<Integer>> data = new HashMap<Integer, Collection<Integer>>();

		Set<Integer> targetSet = Util.getAllUsersForLastFm(srcField);
		for(Integer target:targetSet){
			ArrayList<Integer> topArtists = readDataFromDb(tableName, target, queryField, retField);
			data.put(target, topArtists);
		}

		// map the fields & populate db
		String tableNameOut = "userSimLastFmTopArtists";
		calculateCosineAndWriteToDb(tableNameOut, data);

		/*// assign cosine sim among top albums
		HashSet<SimilarityVal> fgSim = calculateCosine(data);

		// populate db
		String tableNameOut = "userSimLastFmTopArtists";
		writeToDb(tableNameOut,fgSim);*/

	}


	private static void calculateLastFmTopArtistsSimTrain() {
		// read input data
		String path =  ".//data//lastFmArtistsTrain.csv";
		HashMap<Integer, Collection<Integer>> data = Util.readData(path);

		// map the fields & populate db
		String tableNameOut = "userSimLastFmTopArtists";
		calculateCosineAndWriteToDb(tableNameOut, data);

		/*// assign cosine sim among top albums
		HashSet<SimilarityVal> fgSim = calculateCosine(data);

		// populate db
		String tableNameOut = "userSimLastFmTopArtists";
		writeToDb(tableNameOut,fgSim);*/

	}

	/**
	 * Calculate binary similarity among users
	 * Use lastfmuserstopalbumsanonymizedtable
	 */
	private static void calculateLastFmTopAlbumsSim() {
		// read input data
		String tableName = "lastfmuserstopalbumsanonymizedtable";
		String queryField = "userAnonymizedLastFmId";
		String retField = "albumAnonymizedLastFmId";

		HashMap<Integer, Collection<Integer>> data = new HashMap<Integer, Collection<Integer>>();

		Set<Integer> targetSet = Util.getAllUsersForLastFm(srcField);
		for(Integer target:targetSet){
			ArrayList<Integer> topAlbums = readDataFromDb(tableName, target, queryField, retField);
			data.put(target, topAlbums);
		}

		// map the fields & populate db
		String tableNameOut = "userSimLastFmTopAlbums";
		calculateCosineAndWriteToDb(tableNameOut, data);

		/*// assign cosine sim among top albums
		HashSet<SimilarityVal> fgSim = calculateCosine(data);

		// populate db
		String tableNameOut = "userSimLastFmTopAlbums";
		writeToDb(tableNameOut,fgSim);
		 */
	}

	/**
	 * Calculate binary similarity among users
	 * Use lastfmfriendsanonymizedtable
	 */
	private static void calculateLastFmFriendsSim() {
		// read input data
		String tableName = "lastfmfriendsanonymizedtable";
		String queryField = "userAnonymizedLastFmId";
		String retField = "friendAnonymizedLastFmId";

		HashMap<Integer, Collection<Integer>> data = new HashMap<Integer, Collection<Integer>>();

		Set<Integer> targetSet = Util.getAllUsersForLastFm(srcField);
		for(Integer target:targetSet){
			ArrayList<Integer> friends = readDataFromDb(tableName, target, queryField, retField);
			data.put(target, friends);
		}

		String tableNameOut = "userSimLastFmFriends";
		assignBinarySimAndWriteToDb(tableNameOut, data);
		
		/*// assign binary sim among friends
		HashSet<SimilarityVal> fgSim = assignBinarySim(data);


		// populate db
		String tableNameOut = "userSimLastFmFriends";
		writeToDb(tableNameOut,fgSim);
*/
	}

	/**
	 * Calculate cosine similarity among users
	 * Use lastfmfriendsanonymizedtable
	 */
	private static void calculateLastFmCmnFriendsSim() {
		// read input data
		String tableName = "lastfmfriendsanonymizedtable";
		String queryField = "userAnonymizedLastFmId";
		String retField = "friendAnonymizedLastFmId";

		HashMap<Integer, Collection<Integer>> data = new HashMap<Integer, Collection<Integer>>();

		Set<Integer> targetSet = Util.getAllUsersForLastFm(srcField);
		for(Integer target:targetSet){

			ArrayList<Integer> sameActivityLevelUsers = readDataFromDb(tableName, target, queryField, retField);

			data.put(target, sameActivityLevelUsers);
		}

		// map the fields & populate db
		String tableNameOut = "userSimLastFmCmnFriends";
		calculateCosineAndWriteToDb(tableNameOut, data);

		/*// calculate cosine sim among users
		HashSet<SimilarityVal> fgSim = calculateCosine(data);


		// populate db
		String tableNameOut = "userSimLastFmCmnFriends";
		writeToDb(tableNameOut,fgSim);*/

	}

	private static void calculateFacebookActivityLevelSim() {
		// read input data
		String tableName = "fbusersactivitylevelanonymizedtable";
		String queryField = "activityLevel";
		String retField = "userAnonymizedFacebookId";

		HashMap<Integer, Collection<Integer>> data = new HashMap<Integer, Collection<Integer>>();

		Set<Integer> targetSet = Util.getAllUsersForFacebook(srcField);
		for(Integer target:targetSet){

			ArrayList<Integer> sameActivityLevelUsers = readDataFromDbCluster
					(tableName, target, queryField, retField);

			data.put(target, sameActivityLevelUsers);
		}
		
		String tableNameOut = "userSimfbActivityLevel";
		assignBinarySimAndWriteToDb(tableNameOut, data);

		/*// assign binary sim value to the users with the same activity level
		HashSet<SimilarityVal> fgSim = assignBinarySim(data);


		// populate db
		String tableNameOut = "userSimfbActivityLevel";
		writeToDb(tableNameOut,fgSim);*/

	}

	/**
	 * Different from readDataFromDb:
	 * 	I'm looking for other users having same queryField as target have
	 * @param tableName
	 * @param target
	 * @param queryField
	 * @param retField
	 * @return
	 */
	private static ArrayList<Integer> readDataFromDbCluster(
			String tableName, Integer target, String queryField, String retField) {
		ArrayList<Integer> users = new ArrayList<>();
		try {
			//Using the Connection Object now Create a Statement
			Connection con = Util.getConnection();
			Statement stmnt = con.createStatement();

			// get neigbors' ids having similar feature (coolect cluster of users)			
			String sqlSelect = "select  "+ retField
					+ " from " + tableName
					+ " where "+ queryField +" in " 
					+ " (select " + queryField + " from " + tableName 
					+ " where "+ retField +"="+ target +")";

			System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging

			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				Integer nId = rs.getInt(retField);
				users.add(nId);
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();


		}  catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return users;
	}


	private static void calculateFlickrContactsSim() {
		// read input data
		String tableName = "flickrcontactsanonymizedtable";
		String queryField = "userAnonymizedFlickrId";
		String retField = "contactAnonymizedFlickrId";

		HashMap<Integer, Collection<Integer>> data = new HashMap<Integer, Collection<Integer>>();

		Set<Integer> targetList = Util.getAllUsersForFlickr(srcField);
		for(Integer target:targetList){
			ArrayList<Integer> contacts = readDataFromDb(tableName, target, queryField, retField);
			data.put(target, contacts);
		}
		
		String tableNameOut = "usersimflickrcontacts";
		assignBinarySimAndWriteToDb(tableNameOut, data);
		
		/*// assign binary sim val among users
		HashSet<SimilarityVal> fgSim = assignBinarySim(data);

		// populate db
		String tableNameOut = "usersimflickrcontacts";
		writeToDb(tableNameOut,fgSim);*/

	}

	private static void calculateFlickrCmnContactsSim() {
		// read input data
		String tableName = "flickrcontactsanonymizedtable";
		String queryField = "userAnonymizedFlickrId";
		String retField = "contactAnonymizedFlickrId";

		HashMap<Integer, Collection<Integer>> data = new HashMap<Integer, Collection<Integer>>();

		Set<Integer> targetList = Util.getAllUsersForFlickr(srcField);
		for(Integer target:targetList){
			ArrayList<Integer> contacts = readDataFromDb(tableName, target, queryField, retField);
			data.put(target, contacts);
		}

		// map the fields & populate db
		String tableNameOut = "usersimflickrcmncontacts";
		calculateCosineAndWriteToDb(tableNameOut, data);

		/*// calculate cosine sim among users
		HashSet<SimilarityVal> fgSim = calculateCosine(data);

		// populate db
		String tableNameOut = "usersimflickrcmncontacts";
		writeToDb(tableNameOut,fgSim);*/

	}
	private static void calculateBCFollowingSimTrain() {
		// read input data
		String path =  ".//data//blogCatalogFallowingTrain.csv";
		HashMap<Integer, Collection<Integer>> data = Util.readData(path);

		// map the fields & populate db
		String tableNameOut = "userSimBlogCatalogFollowing";
		calculateCosineAndWriteToDb(tableNameOut, data);

		/*// calculate cosine sim among users
		HashSet<SimilarityVal> fgSim = calculateCosine(data);

		// populate db
		String tableName = "userSimBlogCatalogFollowing";
		writeToDb(tableName,fgSim);*/
	}

	private static void calculateBCFollowingSim() {
		// parameters
		String tableName = "blogcatalogfollowinganonymizedtable";
		String queryField = "userAnonymizedBlogCatalogId";
		String retField = "followingAnonymizedBlogCatalogId";
		HashMap<Integer, Collection<Integer>> data = new HashMap<Integer, Collection<Integer>>();

		Set<Integer> targetSet = Util.getAllUsersForBlogCatalog(srcField);
		for(Integer target:targetSet){

			ArrayList<Integer> contacts = readDataFromDb(tableName, target, 
					queryField, retField);

			data.put(target, contacts);
		}

		// map the fields & populate db
		String tableNameOut = "userSimBlogCatalogFollowing";
		calculateCosineAndWriteToDb(tableNameOut, data);

		/*// map the fields
		// calculate cosine sim among users
		HashSet<SimilarityVal> fgSim = calculateCosine(data);

		// populate db
		String tableNameOut = "userSimBlogCatalogFollowing";
		writeToDb(tableNameOut,fgSim);*/
	}

	private static void calculateTwitterFollowingSim() {
		// read input data
		String tableName = "twitteruserfollowinganonymizedtable";
		String queryField = "userAnonymizedTwitterId";
		String retField = "followingAnonymizedTwitterId";
		HashMap<Integer, Collection<Integer>> data = new HashMap<Integer, Collection<Integer>>();


		Set<Integer> targetSet = Util.getAllUsersForTwitter(srcField);
		for(Integer target:targetSet){

			ArrayList<Integer> contacts = readDataFromDb(tableName, target, 
					queryField, retField);

			data.put(target, contacts);
		}

		// map the fields & populate db
		String tableNameOut = "userSimTwitterFollowing";
		calculateCosineAndWriteToDb(tableNameOut, data);

		/*// calculate cosine sim among users
		HashSet<SimilarityVal> fgSim = calculateCosine(data);

		// populate db
		String tableNameOut = "userSimTwitterFollowing";
		writeToDb(tableNameOut,fgSim);*/
	}

	private static void calculateFlickrGroupsSimTrain() {
		// read input data
		String path =  ".//data//flickrGroupsTrain.csv";
		HashMap<Integer, Collection<Integer>> data = Util.readData(path);

		// map the fields & populate db
		String tableNameOut = "userSimFlickrGroups";
		calculateCosineAndWriteToDb(tableNameOut, data);

		/*// calculate cosine sim among users
		HashSet<SimilarityVal> fgSim = calculateCosine(data);

		// print results
		String outpath = ".//data//flickrGroupsSim.csv";
		writeSimValues(outpath, fgSim);


		// populate db
		String tableName = "userSimFlickrGroups";
		writeToDb(tableName,fgSim);*/
	}

	private static void calculateFlickrGroupsSim() {
		HashMap<Integer, Collection<Integer>> data = new HashMap<Integer, Collection<Integer>>();

		Set<Integer> targetList = Util.getAllUsersForFlickr(srcField);

		// read input data
		String tableName = "flickrgroupsanonymizedtable";
		String queryField = "userAnonymizedFlickrId";
		String retField = "groupAnonymizedFlickrId";
		for(Integer target:targetList){
			ArrayList<Integer> contacts = readDataFromDb(tableName, target, queryField, retField);
			data.put(target, contacts);
		}

		// map the fields & populate db
		String tableNameOut = "usersimflickrgroups";
		calculateCosineAndWriteToDb(tableNameOut, data);

		/*// assign cos sim val among users
		HashSet<SimilarityVal> fgSim = calculateCosine(data);

		// populate db
		String tableNameOut = "usersimflickrgroups";
		writeToDb(tableNameOut,fgSim);*/
	}


	/**
	 * Look for field values that my target have
	 * @param tableName
	 * @param target
	 * @param queryField
	 * @param retField
	 * @return
	 */
	private static ArrayList<Integer> readDataFromDb(
			String tableName, Integer target, 
			String queryField, String retField) {
		ArrayList<Integer> users = new ArrayList<>();
		try {
			//Using the Connection Object now Create a Statement
			Connection con = Util.getConnection();
			Statement stmnt = con.createStatement();

			// get neigbors' ids			
			String sqlSelect = "select  "+ retField
					+ " from " + tableName
					+ " where "+ queryField +"=" + target;

			System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging

			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				Integer nId = rs.getInt(retField);
				users.add(nId);
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();


		}  catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return users;
	}

	/**
	 * Look for field values that my target have
	 * @param tableName
	 * @param target
	 * @param queryField
	 * @param retField
	 * @return
	 */
	private static ArrayList<String> readStringDataFromDb(
			String tableName, Integer target, 
			String queryField, String retField) {
		ArrayList<String> output = new ArrayList<>();
		try {
			//Using the Connection Object now Create a Statement
			Connection con = Util.getConnection();
			Statement stmnt = con.createStatement();

			// get neigbors' ids			
			String sqlSelect = "select  "+ retField
					+ " from " + tableName
					+ " where "+ queryField +"=" + target;

			System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging

			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				String nId = rs.getString(retField);
				output.add(nId);
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();


		}  catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return output;
	}


	static void writeToDb(String tableName,HashSet<SimilarityVal> simSet ) {
		Connection connection = null;

		PreparedStatement statement = null;
		try {
			connection = Util.getConnection();
			// create table
			createTable(connection, tableName);

			// fill the table
			statement = connection.prepareStatement("insert into "+ tableName+ "(userid1,userid2,simVal) "
					+ "values ( ?, ?, ?)");

			System.out.println("simSize: " +simSet.size());

			int i=0;
			for (SimilarityVal sim:simSet) {

				statement.setInt(1, sim.getUser1id());
				statement.setInt(2, sim.getUser2id());
				statement.setDouble(3, sim.getSimVal());

				statement.addBatch();
				i++;

				if ((i + 1) % 50 == 0) {
					statement.executeBatch(); // Execute every 100 items.

					//System.out.println(i +" executed");
				}
			}
			// execute batch for the remaining items
			statement.executeBatch();

		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}

		}


	}

	private static void createTable(Connection con, String tableName) {
		try{
			// control if table exists
			boolean tableExists = doesTableExist(con, tableName);

			// if there is no such table create it
			if(tableExists == false){
				//Using the Connection Object now Create a Statement
				Statement stmnt = con.createStatement();
				String sqlCreate = "create table " + tableName 
						+ "(userid1 int(16),userid2 int(16),simVal varchar(50),"
						+ " primary key(userid1,userid2))";
				System.out.println("The SQL query is: " + sqlCreate);  // Echo for debugging
				stmnt.execute(sqlCreate);
				//Close the Statement & connection
				stmnt.close();
			}
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static boolean doesTableExist(Connection con, String tableName){
		boolean tableExists = false;
		try{
			// control if table exists
			Statement stmnt0 = con.createStatement();
			String sqlControl = "SELECT 1 FROM " + tableName + " LIMIT 1;";
			stmnt0.execute(sqlControl);

			tableExists = true;

			stmnt0.close();
		}  catch (SQLException e0) {
			// do nothing
		}

		return tableExists;

	}
	private static void writeSimValues(String path, HashSet<SimilarityVal> simSet) {
		try {
			FilePrinter printer = new FilePrinter();
			for(SimilarityVal sim:simSet){

				// create output string
				StringBuffer sb = new StringBuffer();
				sb.append(sim.toString());

				// print to file
				printer.printString(path, sb.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static  <T> HashSet<SimilarityVal> calculateCosine(
			HashMap<Integer, ArrayList<T>> trainData) {
		HashSet<SimilarityVal> simSet = new HashSet<SimilarityVal>();

		Set<Integer> allUsers = trainData.keySet();
		for(Integer user1:allUsers){
			for(Integer user2:allUsers){
				Double cosVal = 0.0;
				if(user1.equals(user2)){
					// same user
					cosVal = 0.0;
				} else {
					// calculate cosine
					ArrayList<T> user1Data = trainData.get(user1);
					ArrayList<T> user2Data = trainData.get(user2);
					cosVal = Cosine.cosine(user1Data, user2Data);
				}

				SimilarityVal sim = new SimilarityVal(user1, user2, cosVal);
				simSet.add(sim);
			}
		}


		return simSet;
	}

	private static  <T> void calculateCosineAndWriteToDb(String tableName,
			HashMap<Integer, Collection<T>> trainData) {

		HashSet<SimilarityVal> simSet = new HashSet<SimilarityVal>();

		Set<Integer> allUsers = trainData.keySet();
		for(Integer user1:allUsers){

			for(Integer user2:allUsers){
				Double cosVal = 0.0;
				if(user1.equals(user2)){
					// same user
					cosVal = 0.0;
				} else {
					// calculate cosine
					Collection<T> user1Data = trainData.get(user1);
					Collection<T> user2Data = trainData.get(user2);
					cosVal = Cosine.cosine(user1Data, user2Data);
				}

				if(cosVal !=0.0){
					SimilarityVal sim = new SimilarityVal(user1, user2, cosVal);
					simSet.add(sim);
				}



				// write to db if there are at least 1000 entries
				if (simSet.size() != 0 && (simSet.size()) % 1000 == 0) {
					// populate db
					writeToDb(tableName,simSet);
					simSet.clear();
				}

			}

		}
		// write to db the rest of the entries
		if (simSet.size() != 0 ){
			// populate db
			writeToDb(tableName,simSet);
			simSet.clear();
		}
	}



	/**
	 *  If user2 is among allTargetUsers and the valueList, then assign 1 to simVal
	 * @param trainData
	 * @return
	 */
	private static HashSet<SimilarityVal> assignBinarySim(
			HashMap<Integer, Collection<Integer>> trainData) {
		HashSet<SimilarityVal> simSet = new HashSet<SimilarityVal>();

		Set<Integer> allUsers = trainData.keySet();

		for(Integer user1:allUsers){
			Collection<Integer> valueList = trainData.get(user1);

			for(Integer user2:allUsers){
				Double cosVal = 0.0;
				if(user1.equals(user2)){
					// same user
					cosVal = 0.0;
				} else {
					// if there is a mapping among user1 and user2 assign 1.0
					if(valueList.contains(user2)){
						cosVal = 1.0;
					}
				}

				SimilarityVal sim = new SimilarityVal(user1, user2, cosVal);
				simSet.add(sim);

			}
		}


		return simSet;
	}

	/**
	 *  If user2 is among allTargetUsers and the valueList, then assign 1 to simVal
	 * @param trainData
	 * @return
	 */
	private static HashSet<SimilarityVal> assignBinarySimAndWriteToDb(String tableName,
			HashMap<Integer, Collection<Integer>> trainData) {
		HashSet<SimilarityVal> simSet = new HashSet<SimilarityVal>();

		Set<Integer> allUsers = trainData.keySet();

		for(Integer user1:allUsers){
			Collection<Integer> valueList = trainData.get(user1);

			for(Integer user2:allUsers){
				Double cosVal = 0.0;
				if(user1.equals(user2)){
					// same user
					cosVal = 0.0;
				} else {
					// if there is a mapping among user1 and user2 assign 1.0
					if(valueList.contains(user2)){
						cosVal = 1.0;
					}
				}

				if(cosVal != 0.0){
					SimilarityVal sim = new SimilarityVal(user1, user2, cosVal);
					simSet.add(sim);
				}
				// write to db if there are at least 1000 entries
				if (simSet.size() != 0 && (simSet.size()) % 1000 == 0) {
					// populate db
					writeToDb(tableName,simSet);
					simSet.clear();
				}

			}
		}

		// write to db - for the remaining
		if (simSet.size() != 0) {
			// populate db
			writeToDb(tableName,simSet);
			simSet.clear();
		}
		return simSet;
	}



}
