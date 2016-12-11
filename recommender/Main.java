package recommender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import recommender.cf.CFBlogCatalogFollowingRecommender;
import recommender.cf.CFFacebookActivityLevelRecommender;
import recommender.cf.CFFlickrCommonContactsRecommender;
import recommender.cf.CFFlickrContactRecommender;
import recommender.cf.CFFlickrGrpRecommender;
import recommender.cf.CFLastFmCmnFriendsRecommender;
import recommender.cf.CFLastFmFriendsRecommender;
import recommender.cf.CFLastFmTopAlbumsRecommender;
import recommender.cf.CFLastFmTopArtistsRecommender;
import recommender.cf.CFLastFmUserCountryRecommender;
import recommender.cf.CFLastFmUserGenderRecommender;
import recommender.cf.CFTwitterFollowingRecommender;
import recommender.cf.CFYoutubeActivityTypeRecommender;
import recommender.cf.CFYoutubeCmnSubscriptionsRecommender;
import recommender.cf.CFYoutubeFreebaseUserTopicsRecommender;
import recommender.cf.CFYoutubeSubscriptionsRecommender;
import recommender.cf.CFYoutubeUserVideoCountRecommender;
import recommender.hybrid.HybridFlickrCmnContactTwitterFollowingBCFollowingLastFmTopArtistsYoutubeTopicsRecommender;
import recommender.hybrid.HybridFlickrGrpCmnContactTwitterFollowingBCFollowingFBActivityLevelRecommender;
import recommender.hybrid.HybridFlickrGrpCmnContactTwitterFollowingBCFollowingLastFmCountryRecommender;
import recommender.hybrid.HybridFlickrGrpCmnContactTwitterFollowingBCFollowingLastFmGenderRecommender;
import recommender.hybrid.HybridFlickrGrpCmnContactTwitterFollowingBCFollowingLastFmTopArtistsRecommender;
import recommender.hybrid.HybridFlickrGrpCmnContactTwitterFollowingBCFollowingYoutubeTopicsRecommender;
import recommender.hybrid.HybridFlickrGrpCommonContactBlogCatalogFollowingRecommender;
import recommender.hybrid.HybridFlickrGrpCommonContactRecommender;
import recommender.hybrid.HybridFlickrGrpCommonContactTwitterFollowingBlogCatalogFollowingRecommender;
import recommender.hybrid.HybridFlickrGrpCommonContactTwitterFollowingRecommender;
import recommender.hybrid.HybridLastFmTopArtistsCmnFriendsRecommender;
import recommender.hybrid.HybridLastFmTopArtistsGenderRecommender;
import recommender.hybridItem.HybridItemsFlickrCmnContactTwitterFollowingBCFollowingLastFmTopArtistsYoutubeTopicsRecommender;
import recommender.hybridItem.HybridItemsFlickrGrpCmnContactTwitterFollowingBCFollowingFBActivityLevelRecommender;
import recommender.hybridItem.HybridItemsFlickrGrpCmnContactTwitterFollowingBCFollowingLastFmCountryRecommender;
import recommender.hybridItem.HybridItemsFlickrGrpCmnContactTwitterFollowingBCFollowingLastFmGenderRecommender;
import recommender.hybridItem.HybridItemsFlickrGrpCmnContactTwitterFollowingBCFollowingLastFmTopArtistsRecommender;
import recommender.hybridItem.HybridItemsFlickrGrpCmnContactTwitterFollowingBCFollowingYoutubeTopicsRecommender;
import recommender.hybridItem.HybridItemsFlickrGrpCommonContactBlogCatalogFollowingRecommender;
import recommender.hybridItem.HybridItemsFlickrGrpCommonContactRecommender;
import recommender.hybridItem.HybridItemsFlickrGrpCommonContactTwitterFollowingBlogCatalogFollowingRecommender;
import recommender.hybridItem.HybridItemsFlickrGrpCommonContactTwitterFollowingRecommender;
import recommender.hybridItem.HybridItemsLastFmTopArtistsCmnFriendsRecommender;
import recommender.hybridItem.HybridItemsLastFmTopArtistsGenderRecommender;
import recommender.mo.MOFlickrCmnContactTwitterFollowingBCFollowingLastFmTopArtistsYoutubeTopicsRecommender;
import recommender.mo.MOGrpCmnContactTwitterFollowingBCFollowingFBActivityLevelRecommender;
import recommender.mo.MOGrpCmnContactTwitterFollowingBCFollowingLastFmCountryRecommender;
import recommender.mo.MOGrpCmnContactTwitterFollowingBCFollowingLastFmGenderRecommender;
import recommender.mo.MOGrpCmnContactTwitterFollowingBCFollowingLastFmTopArtistsRecommender;
import recommender.mo.MOGrpCmnContactTwitterFollowingBCFollowingYoutubeTopicsRecommender;
import recommender.mo.MOGrpCommonContactBlogCatalogFollowingRecommender;
import recommender.mo.MOGrpCommonContactRecommender;
import recommender.mo.MOGrpCommonContactTwitterFollowingBlogCatalogFollowingRecommender;
import recommender.mo.MOGrpCommonContactTwitterFollowingRecommender;
import recommender.mo.MOLastFmTopArtistsCmnFriendsRecommender;
import recommender.mo.MOLastFmTopArtistsGenderRecommender;
import util.FilePrinter;
import util.Util;


public class Main {

	static Integer[] neighborCountList = {50}; 
	static Integer[] outputSizeList = {1};

	static String targetTableName = "blogcatalogfollowinganonymizedtable";
	static String baseTestFile = ".//data//blogCatalogFollowingTest.csv";

	public static void main(String[] args) {		
		try {

			// read test data
			HashMap<Integer, Collection<Integer>> baseData = Util.readData(baseTestFile);
			// test users are the ones that are seen in the test list
			Set<Integer> targetList = baseData.keySet();

			// get methods to be experimented
			ArrayList<RecommenderBase> recMethods = getRecMethodsToExperiment();

			for(RecommenderBase cfr:recMethods){
				for(Integer outputSize:outputSizeList){
										
					for(Integer neighborCount:neighborCountList){
						
						for(Integer target:targetList){

							// recommend
							ArrayList<Recommendation> resultRecs = cfr.recommend(target, neighborCount, outputSize);

							// Print recommendations 
							String recOutPath = ".//output//rec-"+cfr.getRecName()
									+"N"+neighborCount
									+"O"+outputSize+".csv";
							writeToFile(recOutPath, resultRecs, target);

							// TODO should I force gc?
							if(resultRecs != null){
								resultRecs.clear();
							}
							resultRecs = null;
						}

						Thread.sleep(100);
						
					}
					
				}

			}
			// close any connection if exist
			Util.closeConnection();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private static ArrayList<RecommenderBase> getRecMethodsToExperiment() {

		ArrayList<RecommenderBase> recMethodsToExperiment = new ArrayList<RecommenderBase>();

		// recommend -flickr groups- for Asonam
		/*CFFlickrGrpRecommender cfr1 = new CFFlickrGrpRecommender(targetTableName);
		recMethodsToExperiment.add(cfr1);

		CFFlickrContactRecommender cfr2 = new CFFlickrContactRecommender(targetTableName);
		recMethodsToExperiment.add(cfr2);

		CFFlickrCommonContactsRecommender cfr3 = new CFFlickrCommonContactsRecommender(targetTableName);
		recMethodsToExperiment.add(cfr3);

		CFTwitterFollowingRecommender cfr4 = new CFTwitterFollowingRecommender(targetTableName);
		recMethodsToExperiment.add(cfr4);

		CFBlogCatalogFollowingRecommender cfr5 = new CFBlogCatalogFollowingRecommender(targetTableName);
		recMethodsToExperiment.add(cfr5);*/

		/*
		MOGrpCommonContactRecommender cfr6 = new MOGrpCommonContactRecommender(targetTableName);
		recMethodsToExperiment.add(cfr6);

		MOGrpCommonContactTwitterFollowingRecommender cfr7 = new MOGrpCommonContactTwitterFollowingRecommender(targetTableName);
		recMethodsToExperiment.add(cfr7);

		MOGrpCommonContactBlogCatalogFollowingRecommender cfr8 = new MOGrpCommonContactBlogCatalogFollowingRecommender(targetTableName);
		recMethodsToExperiment.add(cfr8);
		
		MOGrpCommonContactTwitterFollowingBlogCatalogFollowingRecommender cfr9 = new MOGrpCommonContactTwitterFollowingBlogCatalogFollowingRecommender(targetTableName);
		recMethodsToExperiment.add(cfr9);
		*/
		
		
		/*
		HybridFlickrGrpCommonContactRecommender cfr10 = new HybridFlickrGrpCommonContactRecommender(targetTableName);	
		recMethodsToExperiment.add(cfr10);

		HybridFlickrGrpCommonContactTwitterFollowingRecommender cfr11 = new HybridFlickrGrpCommonContactTwitterFollowingRecommender(targetTableName);
		recMethodsToExperiment.add(cfr11);

		HybridFlickrGrpCommonContactBlogCatalogFollowingRecommender cfr12 = new HybridFlickrGrpCommonContactBlogCatalogFollowingRecommender(targetTableName);
		recMethodsToExperiment.add(cfr12);
		

		HybridFlickrGrpCommonContactTwitterFollowingBlogCatalogFollowingRecommender cfr13 = new HybridFlickrGrpCommonContactTwitterFollowingBlogCatalogFollowingRecommender(targetTableName);
		recMethodsToExperiment.add(cfr13);
*/		
/*
		HybridItemsFlickrGrpCommonContactRecommender cfr14 = new HybridItemsFlickrGrpCommonContactRecommender(targetTableName);
		recMethodsToExperiment.add(cfr14);

		HybridItemsFlickrGrpCommonContactTwitterFollowingRecommender cfr15 = new HybridItemsFlickrGrpCommonContactTwitterFollowingRecommender(targetTableName);
		recMethodsToExperiment.add(cfr15);

		HybridItemsFlickrGrpCommonContactBlogCatalogFollowingRecommender cfr16 = new HybridItemsFlickrGrpCommonContactBlogCatalogFollowingRecommender(targetTableName);
		recMethodsToExperiment.add(cfr16);
		

		HybridItemsFlickrGrpCommonContactTwitterFollowingBlogCatalogFollowingRecommender cfr17 = new HybridItemsFlickrGrpCommonContactTwitterFollowingBlogCatalogFollowingRecommender(targetTableName);
		recMethodsToExperiment.add(cfr17);
*/	 
		// recommendation methods - implemented after Asonam submission
		/*CFFacebookActivityLevelRecommender cfr18 = new CFFacebookActivityLevelRecommender(targetTableName);
		recMethodsToExperiment.add(cfr18);

		CFLastFmCmnFriendsRecommender cfr19 = new CFLastFmCmnFriendsRecommender(targetTableName);
		recMethodsToExperiment.add(cfr19);

		CFLastFmFriendsRecommender cfr20 = new CFLastFmFriendsRecommender(targetTableName);
		recMethodsToExperiment.add(cfr20);

		CFLastFmTopAlbumsRecommender cfr21 = new CFLastFmTopAlbumsRecommender(targetTableName);
		recMethodsToExperiment.add(cfr21);

		CFLastFmTopArtistsRecommender cfr22 = new CFLastFmTopArtistsRecommender(targetTableName);
		recMethodsToExperiment.add(cfr22);

		CFLastFmUserGenderRecommender cfr23 = new CFLastFmUserGenderRecommender(targetTableName);
		recMethodsToExperiment.add(cfr23);

		CFLastFmUserCountryRecommender cfr24 = new CFLastFmUserCountryRecommender(targetTableName);
		recMethodsToExperiment.add(cfr24);

		CFYoutubeCmnSubscriptionsRecommender cfr25 = new CFYoutubeCmnSubscriptionsRecommender(targetTableName);
		recMethodsToExperiment.add(cfr25);
		
		
		CFYoutubeSubscriptionsRecommender cfr26 = new CFYoutubeSubscriptionsRecommender(targetTableName);
		recMethodsToExperiment.add(cfr26);

		CFYoutubeActivityTypeRecommender cfr27 = new CFYoutubeActivityTypeRecommender(targetTableName);
		recMethodsToExperiment.add(cfr27);

		CFYoutubeFreebaseUserTopicsRecommender cfr28 = new CFYoutubeFreebaseUserTopicsRecommender(targetTableName);
		recMethodsToExperiment.add(cfr28);

		CFYoutubeUserVideoCountRecommender cfr29 = new CFYoutubeUserVideoCountRecommender(targetTableName);
		recMethodsToExperiment.add(cfr29);
*/
	
		/*HybridFlickrGrpCmnContactTwitterFollowingBCFollowingFBActivityLevelRecommender cfr30 = new HybridFlickrGrpCmnContactTwitterFollowingBCFollowingFBActivityLevelRecommender(targetTableName);
		recMethodsToExperiment.add(cfr30);
		

		HybridFlickrGrpCmnContactTwitterFollowingBCFollowingLastFmTopArtistsRecommender cfr31 = new HybridFlickrGrpCmnContactTwitterFollowingBCFollowingLastFmTopArtistsRecommender(targetTableName);
		recMethodsToExperiment.add(cfr31);
		
		
		HybridFlickrGrpCmnContactTwitterFollowingBCFollowingLastFmGenderRecommender cfr32 = new HybridFlickrGrpCmnContactTwitterFollowingBCFollowingLastFmGenderRecommender(targetTableName);
		recMethodsToExperiment.add(cfr32);
		
		
		HybridFlickrGrpCmnContactTwitterFollowingBCFollowingLastFmCountryRecommender cfr33 = new HybridFlickrGrpCmnContactTwitterFollowingBCFollowingLastFmCountryRecommender(targetTableName);
		recMethodsToExperiment.add(cfr33);
		
		

		HybridFlickrGrpCmnContactTwitterFollowingBCFollowingYoutubeTopicsRecommender cfr34 = new HybridFlickrGrpCmnContactTwitterFollowingBCFollowingYoutubeTopicsRecommender(targetTableName);
		recMethodsToExperiment.add(cfr34);
		
		
		HybridItemsFlickrGrpCmnContactTwitterFollowingBCFollowingFBActivityLevelRecommender cfr35 = new HybridItemsFlickrGrpCmnContactTwitterFollowingBCFollowingFBActivityLevelRecommender(targetTableName);
		recMethodsToExperiment.add(cfr35);
		
		
		HybridItemsFlickrGrpCmnContactTwitterFollowingBCFollowingLastFmTopArtistsRecommender cfr36 = new HybridItemsFlickrGrpCmnContactTwitterFollowingBCFollowingLastFmTopArtistsRecommender(targetTableName);
		recMethodsToExperiment.add(cfr36);
		
		
		HybridItemsFlickrGrpCmnContactTwitterFollowingBCFollowingLastFmGenderRecommender cfr37 = new HybridItemsFlickrGrpCmnContactTwitterFollowingBCFollowingLastFmGenderRecommender(targetTableName);
		recMethodsToExperiment.add(cfr37);
	
		
		HybridItemsFlickrGrpCmnContactTwitterFollowingBCFollowingLastFmCountryRecommender cfr38 = new HybridItemsFlickrGrpCmnContactTwitterFollowingBCFollowingLastFmCountryRecommender(targetTableName);
		recMethodsToExperiment.add(cfr38);
			
		
		HybridItemsFlickrGrpCmnContactTwitterFollowingBCFollowingYoutubeTopicsRecommender cfr39 = new HybridItemsFlickrGrpCmnContactTwitterFollowingBCFollowingYoutubeTopicsRecommender(targetTableName);
		recMethodsToExperiment.add(cfr39);
*/
		
		/*MOGrpCmnContactTwitterFollowingBCFollowingFBActivityLevelRecommender cfr40 = new MOGrpCmnContactTwitterFollowingBCFollowingFBActivityLevelRecommender(targetTableName);
		recMethodsToExperiment.add(cfr40);
		
		MOGrpCmnContactTwitterFollowingBCFollowingLastFmTopArtistsRecommender cfr41 = new MOGrpCmnContactTwitterFollowingBCFollowingLastFmTopArtistsRecommender(targetTableName);
		recMethodsToExperiment.add(cfr41);
		
		
		MOGrpCmnContactTwitterFollowingBCFollowingLastFmGenderRecommender cfr42 = new MOGrpCmnContactTwitterFollowingBCFollowingLastFmGenderRecommender(targetTableName);
		recMethodsToExperiment.add(cfr42);
	
		
		MOGrpCmnContactTwitterFollowingBCFollowingLastFmCountryRecommender cfr43 = new MOGrpCmnContactTwitterFollowingBCFollowingLastFmCountryRecommender(targetTableName);
		recMethodsToExperiment.add(cfr43);
		
	
		MOGrpCmnContactTwitterFollowingBCFollowingYoutubeTopicsRecommender cfr44 = new MOGrpCmnContactTwitterFollowingBCFollowingYoutubeTopicsRecommender(targetTableName);
		recMethodsToExperiment.add(cfr44);		 
*/

		// recommendation methods - implemented for LastFm Artists recommendation
		/*HybridFlickrCmnContactTwitterFollowingBCFollowingLastFmTopArtistsYoutubeTopicsRecommender cfr45 = new HybridFlickrCmnContactTwitterFollowingBCFollowingLastFmTopArtistsYoutubeTopicsRecommender(targetTableName);
		recMethodsToExperiment.add(cfr45);

		
		HybridLastFmTopArtistsGenderRecommender cfr46 = new HybridLastFmTopArtistsGenderRecommender(targetTableName);
		recMethodsToExperiment.add(cfr46);

		HybridLastFmTopArtistsCmnFriendsRecommender cfr47 = new HybridLastFmTopArtistsCmnFriendsRecommender(targetTableName);
		recMethodsToExperiment.add(cfr47);
	 */
		/*
		HybridItemsFlickrCmnContactTwitterFollowingBCFollowingLastFmTopArtistsYoutubeTopicsRecommender cfr48 = new HybridItemsFlickrCmnContactTwitterFollowingBCFollowingLastFmTopArtistsYoutubeTopicsRecommender(targetTableName);
		recMethodsToExperiment.add(cfr48);
		
		HybridItemsLastFmTopArtistsGenderRecommender cfr49 = new HybridItemsLastFmTopArtistsGenderRecommender(targetTableName);
		recMethodsToExperiment.add(cfr49);

		HybridItemsLastFmTopArtistsCmnFriendsRecommender cfr50 = new HybridItemsLastFmTopArtistsCmnFriendsRecommender(targetTableName);
		recMethodsToExperiment.add(cfr50);
		 */
		
		MOFlickrCmnContactTwitterFollowingBCFollowingLastFmTopArtistsYoutubeTopicsRecommender cfr51 = new MOFlickrCmnContactTwitterFollowingBCFollowingLastFmTopArtistsYoutubeTopicsRecommender(targetTableName);
		recMethodsToExperiment.add(cfr51);
		
		/*MOLastFmTopArtistsGenderRecommender cfr52 = new MOLastFmTopArtistsGenderRecommender(targetTableName);
		recMethodsToExperiment.add(cfr52);

		MOLastFmTopArtistsCmnFriendsRecommender cfr53 = new MOLastFmTopArtistsCmnFriendsRecommender(targetTableName);
		recMethodsToExperiment.add(cfr53);*/
		

		return recMethodsToExperiment;
	}

	private static void writeToFile(String path,
			ArrayList<Recommendation> resultRecs, Integer target) {
		try {
			FilePrinter printer = new FilePrinter();
			// create output string
			StringBuffer sb = new StringBuffer();

			sb.append(target.toString());
			sb.append(",");
			if(resultRecs != null){
				for(Recommendation rec:resultRecs){
					sb.append(rec.getRecommended());
					sb.append(",");
				}
			}

			// print to file
			printer.printString(path, sb.toString());
			// close?
			sb.setLength(0);
			printer = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

}
