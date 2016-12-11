package util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import similarity.Similarity;
import similarity.Similarity.SimType;


public class Util {

	static Connection con = null;
	public static Connection getConnection() throws ClassNotFoundException, SQLException{

		if(con == null){
			Class.forName("com.mysql.jdbc.Driver");
			String dbConn = "jdbc:mysql:path";
			String userName = "xxxxxxxx";
			String password = "xxxxxxxx";

			//Connecting to MYSQL Database
			//SQL Database name is java
			//SQL server is localhost, username:root, password:nopassword 
			con = DriverManager.getConnection(dbConn, userName,password);
		}
		return con;
	}
	public static void closeConnection() throws SQLException {
		if(con != null){
			con.close();
		}
	}


	public static HashMap<Integer, Collection<Integer>> readData(String path){
		HashMap<Integer, Collection<Integer>> resultMap = new HashMap<Integer, Collection<Integer>>();
		try{
			// Open the file
			FileInputStream fstream = new FileInputStream(path);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			//Read File Line By Line until the index of target
			String strLine = null;
			while ((strLine = br.readLine()) != null)   // read info
			{
				// read userid-->list of suggested ids
				String[] splitted = strLine.split(",");
				Integer targetuser = Integer.valueOf(splitted[0]);

				ArrayList<Integer> idList = new ArrayList<Integer>();
				for(int i=1; i<splitted.length; i++){
					Integer gId = Integer.valueOf(splitted[i]);
					idList.add(gId);

				}

				resultMap.put(targetuser, idList);
			}
			
			// close
			br.close();
			in.close();
			fstream.close();

		} catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

	public static Set<Integer> getAllUsersForTwitter(String srcField) {
		Set<Integer> usersForTwitter = null;

		try {
			Connection con = Util.getConnection();
			HashMap<Integer, Collection<Integer>> resultMap = getAllUsersFromDb(srcField);
			Set<Integer> users = resultMap.keySet();


			String retField = "userAnonymizedTwitterId";
			usersForTwitter = mapUsersFromSrc(users, con,retField, srcField);


		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return usersForTwitter;
	}
	
	public static Set<Integer> getAllUsersForFacebook(String srcField) {
		Set<Integer> usersForFacebook = null;

		try {
			Connection con = Util.getConnection();
			HashMap<Integer, Collection<Integer>> resultMap = getAllUsersFromDb(srcField);
			Set<Integer> users = resultMap.keySet();


			String retField = "userAnonymizedFacebookId";
			usersForFacebook = mapUsersFromSrc(users, con,retField, srcField);


		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return usersForFacebook;
	}

	public static Set<Integer> getAllUsersForLastFm(String srcField) {
		Set<Integer> usersForLastFm = null;

		try {
			Connection con = Util.getConnection();
			HashMap<Integer, Collection<Integer>> resultMap = getAllUsersFromDb(srcField);
			Set<Integer> users = resultMap.keySet();


			String retField = "userAnonymizedLastFmId";
			usersForLastFm = mapUsersFromSrc(users, con,retField, srcField);


		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return usersForLastFm;
	}

	public static Set<Integer> getAllUsersForYoutube(String srcField) {
		Set<Integer> usersForYoutube = null;

		try {
			Connection con = Util.getConnection();
			HashMap<Integer, Collection<Integer>> resultMap = getAllUsersFromDb(srcField);
			Set<Integer> users = resultMap.keySet();

			String retField = "userAnonymizedYoutubeId";
			usersForYoutube = mapUsersFromSrc(users, con,retField, srcField);


		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return usersForYoutube;
	}

	public static Integer mapUserFromSrc(Connection con, Integer userSrcId, 
			String retFieldName, String srcFieldName) {
		Integer userRetId = null;

		String tableNameAllUsersMapping = "all6socnwusersmapping";
		try {
			// prepare statement			
			String sql = "select "+ retFieldName
					+ " as usrRetId"
					+ " from "+ tableNameAllUsersMapping
					+ " where " + srcFieldName + "= ?"
					+ " and " + retFieldName +" is not NULL" ;
			PreparedStatement preparedStatement = con.prepareStatement(sql);

			preparedStatement.setInt(1, userSrcId);
			// execute query
			System.out.println(preparedStatement.toString());
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				userRetId = result.getInt("usrRetId");

			}
			// close stmt
			preparedStatement.close();

		} catch (Exception e) {
			e.printStackTrace();
		}


		return userRetId;
	}

	// TODO change parameter to enum, so that I can differantiate the lastfm -X from lastfm-Y!!
	protected static HashMap<Integer, Collection<Integer>> getAllUsersFromDb(String srcField){
		HashMap<Integer, Collection<Integer>> resultMap = null;
		switch(srcField){
		case "userAnonymizedFlickrId": resultMap = ReadDataFromDb.readFlickrGroups(con); break;
		case "userAnonymizedLastFmId": resultMap = ReadDataFromDb.readLastFmArtists(con); break;
		case "userAnonymizedBlogCatalogId": resultMap = ReadDataFromDb.readBlogCatalogFallowing(con); break;
		default: System.out.println("Wrong srcField type to read users from!!!"); System.exit(-1); break;
		}
		
		return resultMap;
	}
	
	public static Set<Integer> getAllUsersForBlogCatalog(String srcField) {
		Set<Integer> usersForBlogCatalog = null;
		try {
			Connection con = Util.getConnection();
			HashMap<Integer, Collection<Integer>> resultMap = getAllUsersFromDb(srcField);
			Set<Integer> users = resultMap.keySet();

			// map the fields
			String retField = "userAnonymizedBlogCatalogId";
			usersForBlogCatalog = mapUsersFromSrc(users, con, retField, srcField);


		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return usersForBlogCatalog;
	}

	public static Set<Integer> getAllUsersForFlickr(String srcField) {
		Set<Integer> usersForFlickr = null;
		try {
			Connection con = Util.getConnection();
			HashMap<Integer, Collection<Integer>> resultMap = getAllUsersFromDb(srcField);
			Set<Integer> users = resultMap.keySet();

			// map the fields
			String retField = "userAnonymizedFlickrId";
			usersForFlickr = mapUsersFromSrc(users, con, retField, srcField);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return usersForFlickr;
	}
	public static Set<Integer> mapUsersFromSrc(Set<Integer> users, Connection con, 
			String retField, String srcField) {

		Set<Integer> usersForRetNw = new HashSet<Integer>();
		for(Integer useridSrc:users){
			Integer useridRet = mapUserFromSrc(con, useridSrc, retField, srcField);
			if(useridRet!=null){
				usersForRetNw.add(useridRet);
			}
		}

		return usersForRetNw;
	}

	/**
	 * If target or simUserId is null then returns 0.0. 
	 * Target= Null: This may happen when there is a flickr user as test, but there is no mapping of the test user to the facebook(youtube/lastfm)
	 * simUserId= Null: This may happen when there is a flickr neighbor userfound by other methods (in hybrid or mo apps),
	 * 	 but there is no mapping of the neighbor user, to the facebook(youtube/lastfm) for example
	 * @param target
	 * @param simUserId
	 * @param simTableName
	 * @param simType
	 * @param sim
	 * @return
	 */
	public static Similarity<Integer> getSimilarityFromDB(Integer target, Integer simUserId,
			String simTableName, SimType simType, Similarity<Integer> sim) {

		try {
			Connection con = Util.getConnection();
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get similarity		
			String sqlSelect = "select userid2, simVal "
					+ " from " + simTableName
					+ " where userid1=" + target
					+ " and userid2=" + simUserId;

			System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging

			Double simVal = 0.0;
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				simVal = rs.getDouble("simVal");
				break;
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();


			// create/update similarity 
			if(sim == null){
				// create new sim
				sim = new Similarity<Integer>(simUserId,simVal,simType);	
			} else{
				// update sim
				HashMap<SimType, Double> simMap = sim.getSimilarityMap();
				simMap.put(simType, simVal);
				sim.setSimilarityMap(simMap);
			}


		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sim;

	}
	
	/**
	 * 
	 * @param neighbour
	 * @param tableName
	 * @return
	 */
	public static ArrayList<Integer> getPreviousItems(Integer neighbour,
			String tableName) {
		ArrayList<Integer> previousItems = new ArrayList<Integer>();
		switch(tableName){
		case "flickrGroupsAnonymizedTable": previousItems = getPreviousFlickrGroups(neighbour, tableName); break;
		case "lastfmuserstopartistsanonymizedtable": previousItems = getPreviousLastFmArtists(neighbour, tableName); break;
		case "blogcatalogfollowinganonymizedtable": previousItems = getPreviousBlogCatalogFollowings(neighbour, tableName); break;
		default: System.out.println("Wrong tableName. Debug the code :("); System.exit(-1); break;
		}
		return previousItems;
	}

	/**
	 * TODO normally get tableName from somewhere, but now I give it by hand!!
	 * @param neighbour
	 * @param tableName
	 * @return
	 */
	public static ArrayList<Integer> getPreviousFlickrGroups(Integer neighbour,
			String tableName) {
		ArrayList<Integer> previousItems = new ArrayList<Integer>();
		try {
			// get connection
			Connection con = Util.getConnection();

			// prepare statement			
			String sql = "select userAnonymizedFlickrId, groupAnonymizedFlickrId "
					+ " from "+tableName
					+  " where userAnonymizedFlickrId ="+ neighbour;
			PreparedStatement preparedStatement = con.prepareStatement(sql);

			// execute query
			ResultSet result = preparedStatement.executeQuery();

			while (result.next()) {
				Integer userid = result.getInt("userAnonymizedFlickrId");
				Integer groupId = result.getInt("groupAnonymizedFlickrId");
				previousItems.add(groupId);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return previousItems;
	}
	
	/**
	 * TODO normally get tableName from somewhere, but now I give it by hand!!
	 * @param neighbour
	 * @param tableName
	 * @return
	 */
	public static ArrayList<Integer> getPreviousLastFmArtists(Integer neighbour,
			String tableName) {
		ArrayList<Integer> previousItems = new ArrayList<Integer>();
		try {
			// get connection
			Connection con = Util.getConnection();

			// prepare statement			
			String sql = "select userAnonymizedLastFmId, artistAnonymizedLastFmId "
					+ " from "+tableName
					+  " where userAnonymizedLastFmId ="+ neighbour;
			PreparedStatement preparedStatement = con.prepareStatement(sql);

			// execute query
			ResultSet result = preparedStatement.executeQuery();

			while (result.next()) {
				Integer userid = result.getInt("userAnonymizedLastFmId");
				Integer groupId = result.getInt("artistAnonymizedLastFmId");
				previousItems.add(groupId);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return previousItems;
	}

	/**
	 * TODO normally get tableName from somewhere, but now I give it by hand!!
	 * @param neighbour
	 * @param tableName
	 * @return
	 */
	public static ArrayList<Integer> getPreviousBlogCatalogFollowings(Integer neighbour,
			String tableName) {
		ArrayList<Integer> previousItems = new ArrayList<Integer>();
		try {
			// get connection
			Connection con = Util.getConnection();

			// prepare statement
			
			String sql = "select userAnonymizedBlogCatalogId, followingAnonymizedBlogCatalogId "
					+ " from "+tableName
					+  " where userAnonymizedBlogCatalogId ="+ neighbour;
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			
			// execute query
			ResultSet result = preparedStatement.executeQuery();

			while (result.next()) {
				Integer userid = result.getInt("userAnonymizedBlogCatalogId");
				Integer followingId = result.getInt("followingAnonymizedBlogCatalogId");
				previousItems.add(followingId);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return previousItems;
	}

	/**
	 * Return most similar users
	 * 	- depending on the threshold on number of users to return and 
	 * 	- the similarity threshold
	 * Read these users from database
	 */
	public static Set<Integer> getMostSimilars(Integer target,
			Integer numberOfSimilarUsers, 
			String tableName) {
		Set<Integer> mostSimilarUsers = new HashSet<Integer>();
		try {
			//Using the Connection Object now Create a Statement
			Connection con = Util.getConnection();
			Statement stmnt = con.createStatement();

			// get neigbors' ids			
			String sqlSelect = "select userid2 "
					+ " from " + tableName
					+ " where userid1=" + target
					+ " order by simVal";

			// retun only 0 to numberOfSimilarUsers results
			if(numberOfSimilarUsers > 0){
				sqlSelect += " desc limit 0," + numberOfSimilarUsers;
			}

			System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging

			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				Integer nId = rs.getInt("userid2");
				mostSimilarUsers.add(nId);
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();


		}  catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mostSimilarUsers;
	}
	
	/**
	 * Return the ones with the same similarity value with the last one 
	 * (least similar one)
	 * Read these users from database
	 */
	public static Set<Integer> getMostSameValSimilars(Integer target,
			Integer numberOfSimilarUsers, 
			String tableName) {
		Set<Integer> mostSimilarUsers = new HashSet<Integer>();
		try {
			//Using the Connection Object now Create a Statement
			Connection con = Util.getConnection();
			Statement stmnt = con.createStatement();
			// get neigbors' ids			
			String sqlSelect = "select userid2 "
					+ " from " + tableName
					+ " where userid1=" + target
					+ " and simVal = "
					+ "("
					+ " select simVal from "  + tableName
					+ " where userid1=" + target
					+ " order by simVal"
					+ " desc limit " + (numberOfSimilarUsers-1) +",1"
					+ ")";
					

			
			System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging

			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				Integer nId = rs.getInt("userid2");
				mostSimilarUsers.add(nId);
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();


		}  catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mostSimilarUsers;
	}

	public static <T> void writeToFile(String path,HashMap<Integer, Collection<T>> resultMap) {
		try {
			FilePrinter printer = new FilePrinter();
			for(Entry<Integer, Collection<T>> entry: resultMap.entrySet()){
				Integer id = entry.getKey();
				Collection<T> resultList = entry.getValue();

				// create output string
				StringBuffer sb = new StringBuffer();
				sb.append(id.toString());
				sb.append(",");
				for(T gid: resultList){
					sb.append(gid.toString());
					sb.append(",");
				}
				// delete the last comma
				sb.deleteCharAt(sb.length()-1);

				// print to file
				printer.printString(path, sb.toString());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static <T> void writeToFileMultiLine(String path, 
			HashMap<Integer, Collection<T>> resultMap) {
		try {
			FilePrinter printer = new FilePrinter();
			for(Entry<Integer, Collection<T>> entry: resultMap.entrySet()){
				Integer id = entry.getKey();
				Collection<T> resultList = entry.getValue();

				// create output string

				for(T gid: resultList){
					StringBuffer sb = new StringBuffer();
					sb.append(id.toString());
					sb.append(",");
					sb.append(gid.toString());
					// print to file
					printer.printString(path, sb.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
