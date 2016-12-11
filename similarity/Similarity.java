package similarity;

import java.util.Comparator;
import java.util.HashMap;

public class Similarity<T> {
	private T neighbor;
	private HashMap<SimType, Double> similarityMap; // <simType,simVal> 

	// sim types
	public enum SimType { FLICKR_GRP, FLICKR_CMN_CONTACTS, TWITTER_FOLLOWING, 
		BLOGCATALOG_FOLLOWING, FACEBOOK_ACTIVITY_LEVEL, LASTFM_CMN_FRIENDS,
		LASTFM_FRIENDS, LASTFM_TOP_ALBUMS, LASTFM_TOP_ARTISTS, LASTFM_COUNTRY, 
		LASTFM_GENDER, YOUTUBE_CMN_SUBSCRIPTIONS, YOUTUBE_SUBSCRIPTIONS,
		YOUTUBE_ACTIVITY_TYPE, YOUTUBE_VIDEIO_COUNT, YOUTUBE_FREEBASE_TOPICS;}

	public Similarity(T neighbour, HashMap<SimType, Double> similarityMap) {
		super();
		this.neighbor = neighbour;
		this.similarityMap = similarityMap;
	}

	public Similarity(T neighbour, Double simVal, SimType fName) {
		super();
		this.neighbor = neighbour;
		if(similarityMap == null)
		{
			similarityMap = new HashMap<Similarity.SimType, Double>();
		}
		
		similarityMap.put(fName, simVal);
	}

	public T getNeighbor() {
		return neighbor;
	}

	public void setNeighbor(T val) {
		this.neighbor = val;
	}

	public HashMap<SimType, Double> getSimilarityMap() {
		return similarityMap;
	}

	public void setSimilarityMap(HashMap<SimType, Double> similarityMap) {
		this.similarityMap = similarityMap;
	}
	
	public static Comparator<Similarity<Integer>> getComparatorType(Similarity.SimType type)
	{
		Comparator<Similarity<Integer>> retType = null;
		switch(type)
		{
		case FLICKR_GRP: retType = Similarity.SimilarityComparatorFlickrGrpBasedIncreasing; break;
        case FLICKR_CMN_CONTACTS: retType = Similarity.SimilarityComparatorFlickrCmnContactsBasedIncreasing; break;
        case TWITTER_FOLLOWING: retType = Similarity.SimilarityComparatorTwitterFollowingBasedIncreasing; break;
        case BLOGCATALOG_FOLLOWING: retType = Similarity.SimilarityComparatorBlogCatalogFollowingBasedIncreasing; break;
        case FACEBOOK_ACTIVITY_LEVEL: retType = Similarity.SimilarityComparatorFacebookActivityLevelIncreasing; break;
        case LASTFM_CMN_FRIENDS:retType = Similarity.SimilarityComparatorLastFmCommonFriendsIncreasing; break;
        case LASTFM_FRIENDS:retType = Similarity.SimilarityComparatorLastFmFriendsIncreasing; break;
        case LASTFM_TOP_ALBUMS: retType = Similarity.SimilarityComparatorLastFmTopAlbumsIncreasing; break;
        case LASTFM_TOP_ARTISTS: retType = Similarity.SimilarityComparatorLastFmTopArtistsIncreasing; break;
        case LASTFM_COUNTRY: retType = Similarity.SimilarityComparatorLastFmCountryIncreasing; break;
        case LASTFM_GENDER:retType = Similarity.SimilarityComparatorLastFmGenderIncreasing; break;
        case YOUTUBE_CMN_SUBSCRIPTIONS:retType = Similarity.SimilarityComparatorYoutubeCommonSubscriptionsIncreasing; break;
        case YOUTUBE_SUBSCRIPTIONS:retType = Similarity.SimilarityComparatorYoutubeSubscriptionsIncreasing; break;
        case YOUTUBE_ACTIVITY_TYPE: retType = Similarity.SimilarityComparatorYoutubeActivityTypeIncreasing; break;
        case YOUTUBE_VIDEIO_COUNT: retType = Similarity.SimilarityComparatorYoutubeVideoCountIncreasing; break;
        case YOUTUBE_FREEBASE_TOPICS:retType = Similarity.SimilarityComparatorYoutubeFreebaseTopicsIncreasing; break;
        default: System.out.println("Wrong type of sim"); System.exit(-1); break;
		}		
		return retType;
	}
	
	public static Comparator<Similarity<Integer>> SimilarityComparatorYoutubeFreebaseTopicsIncreasing = 
			 new Comparator<Similarity<Integer> >() {

	        public int compare(Similarity<Integer> o1, Similarity<Integer> o2) {
	        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.YOUTUBE_FREEBASE_TOPICS);
				Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.YOUTUBE_FREEBASE_TOPICS);
				
				if(o2Sim == null || o1Sim == null){
					return 0;
				}
				
	            return -1 * o2.getSimilarityMap().get(Similarity.SimType.YOUTUBE_FREEBASE_TOPICS).
	            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.YOUTUBE_FREEBASE_TOPICS));
	        }

	    };

	public static Comparator<Similarity<Integer>> SimilarityComparatorYoutubeVideoCountIncreasing = 
			 new Comparator<Similarity<Integer> >() {

	        public int compare(Similarity<Integer> o1, Similarity<Integer> o2) {
	        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.YOUTUBE_VIDEIO_COUNT);
				Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.YOUTUBE_VIDEIO_COUNT);
				
				if(o2Sim == null || o1Sim == null){
					return 0;
				}
				
	            return -1 * o2.getSimilarityMap().get(Similarity.SimType.YOUTUBE_VIDEIO_COUNT).
	            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.YOUTUBE_VIDEIO_COUNT));
	        }

	    };
	    
	public static Comparator<Similarity<Integer>> SimilarityComparatorYoutubeActivityTypeIncreasing = 
			 new Comparator<Similarity<Integer> >() {

	        public int compare(Similarity<Integer> o1, Similarity<Integer> o2) {
	        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.YOUTUBE_ACTIVITY_TYPE);
				Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.YOUTUBE_ACTIVITY_TYPE);
				
				if(o2Sim == null || o1Sim == null){
					return 0;
				}
				
	            return -1 * o2.getSimilarityMap().get(Similarity.SimType.YOUTUBE_ACTIVITY_TYPE).
	            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.YOUTUBE_ACTIVITY_TYPE));
	        }

	    };

	public static Comparator<Similarity<Integer>> SimilarityComparatorYoutubeSubscriptionsIncreasing = 
			 new Comparator<Similarity<Integer> >() {

	        public int compare(Similarity<Integer> o1, Similarity<Integer> o2) {
	        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.YOUTUBE_SUBSCRIPTIONS);
				Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.YOUTUBE_SUBSCRIPTIONS);
				
				if(o2Sim == null || o1Sim == null){
					return 0;
				}
				
	            return -1 * o2.getSimilarityMap().get(Similarity.SimType.YOUTUBE_SUBSCRIPTIONS).
	            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.YOUTUBE_SUBSCRIPTIONS));
	        }

	    };
	    
	public static Comparator<Similarity<Integer>> SimilarityComparatorYoutubeCommonSubscriptionsIncreasing = 
			 new Comparator<Similarity<Integer> >() {

	        public int compare(Similarity<Integer> o1, Similarity<Integer> o2) {
	        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.YOUTUBE_CMN_SUBSCRIPTIONS);
				Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.YOUTUBE_CMN_SUBSCRIPTIONS);
				
				if(o2Sim == null || o1Sim == null){
					return 0;
				}
				
	            return -1 * o2.getSimilarityMap().get(Similarity.SimType.YOUTUBE_CMN_SUBSCRIPTIONS).
	            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.YOUTUBE_CMN_SUBSCRIPTIONS));
	        }

	    };
	public static Comparator<Similarity<Integer>> SimilarityComparatorLastFmGenderIncreasing = 
			 new Comparator<Similarity<Integer> >() {

	        public int compare(Similarity<Integer> o1, Similarity<Integer> o2) {
	        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.LASTFM_GENDER);
				Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.LASTFM_GENDER);
				
				if(o2Sim == null || o1Sim == null){
					return 0;
				}
				
	            return -1 * o2.getSimilarityMap().get(Similarity.SimType.LASTFM_GENDER).
	            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.LASTFM_GENDER));
	        }

	    };
	    
	public static Comparator<Similarity<Integer>> SimilarityComparatorLastFmCountryIncreasing = 
			 new Comparator<Similarity<Integer> >() {

	        public int compare(Similarity<Integer> o1, Similarity<Integer> o2) {
	        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.LASTFM_GENDER);
				Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.LASTFM_GENDER);
				
				if(o2Sim == null || o1Sim == null){
					return 0;
				}
				
	            return -1 * o2.getSimilarityMap().get(Similarity.SimType.LASTFM_GENDER).
	            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.LASTFM_GENDER));
	        }

	    };
	    
	public static Comparator<Similarity<Integer>> similarityComparatorLastFmCountryIncreasing = 
			 new Comparator<Similarity<Integer> >() {

	        public int compare(Similarity<Integer> o1, Similarity<Integer> o2) {
	        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.LASTFM_COUNTRY);
				Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.LASTFM_COUNTRY);
				
				if(o2Sim == null || o1Sim == null){
					return 0;
				}
				
	            return -1 * o2.getSimilarityMap().get(Similarity.SimType.LASTFM_COUNTRY).
	            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.LASTFM_COUNTRY));
	        }

	    };
	    
	public static Comparator<Similarity<Integer>> SimilarityComparatorLastFmTopArtistsIncreasing = 
			 new Comparator<Similarity<Integer> >() {

	        public int compare(Similarity<Integer> o1, Similarity<Integer> o2) {
	        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.LASTFM_TOP_ARTISTS);
				Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.LASTFM_TOP_ARTISTS);
				
				if(o2Sim == null || o1Sim == null){
					return 0;
				}
				
	            return -1 * o2.getSimilarityMap().get(Similarity.SimType.LASTFM_TOP_ARTISTS).
	            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.LASTFM_TOP_ARTISTS));
	        }

	    };
	    
	public static Comparator<Similarity<Integer>> SimilarityComparatorLastFmTopAlbumsIncreasing = 
			 new Comparator<Similarity<Integer> >() {

	        public int compare(Similarity<Integer> o1, Similarity<Integer> o2) {
	        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.LASTFM_TOP_ALBUMS);
				Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.LASTFM_TOP_ALBUMS);
				
				if(o2Sim == null || o1Sim == null){
					return 0;
				}
				
	            return -1 * o2.getSimilarityMap().get(Similarity.SimType.LASTFM_TOP_ALBUMS).
	            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.LASTFM_TOP_ALBUMS));
	        }

	    };
	    
	    
	public static Comparator<Similarity<Integer>> SimilarityComparatorLastFmFriendsIncreasing = 
			 new Comparator<Similarity<Integer> >() {

	        public int compare(Similarity<Integer> o1, Similarity<Integer> o2) {
	        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.LASTFM_FRIENDS);
				Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.LASTFM_FRIENDS);
				
				if(o2Sim == null || o1Sim == null){
					return 0;
				}
				
	            return -1 * o2.getSimilarityMap().get(Similarity.SimType.LASTFM_FRIENDS).
	            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.LASTFM_FRIENDS));
	        }

	    };
	    
	public static Comparator<Similarity<Integer>> SimilarityComparatorLastFmCommonFriendsIncreasing = 
			 new Comparator<Similarity<Integer> >() {

	        public int compare(Similarity<Integer> o1, Similarity<Integer> o2) {
	        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.LASTFM_CMN_FRIENDS);
				Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.LASTFM_CMN_FRIENDS);
				
				if(o2Sim == null || o1Sim == null){
					return 0;
				}
				
	            return -1 * o2.getSimilarityMap().get(Similarity.SimType.LASTFM_CMN_FRIENDS).
	            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.LASTFM_CMN_FRIENDS));
	        }

	    };
 
	public static Comparator<Similarity<Integer>> SimilarityComparatorFacebookActivityLevelIncreasing = 
			 new Comparator<Similarity<Integer> >() {

	        public int compare(Similarity<Integer> o1, Similarity<Integer> o2) {
	        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.FACEBOOK_ACTIVITY_LEVEL);
				Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.FACEBOOK_ACTIVITY_LEVEL);
				
				if(o2Sim == null || o1Sim == null){
					return 0;
				}
				
	            return -1 * o2.getSimilarityMap().get(Similarity.SimType.FACEBOOK_ACTIVITY_LEVEL).
	            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.FACEBOOK_ACTIVITY_LEVEL));
	        }

	    };
	    
	public static Comparator<Similarity<Integer>> SimilarityComparatorBlogCatalogFollowingBasedIncreasing = 
			 new Comparator<Similarity<Integer> >() {

	        public int compare(Similarity<Integer> o1, Similarity<Integer> o2) {
	        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.BLOGCATALOG_FOLLOWING);
				Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.BLOGCATALOG_FOLLOWING);
				
				if(o2Sim == null || o1Sim == null){
					return 0;
				}
				
	            return -1 * o2.getSimilarityMap().get(Similarity.SimType.BLOGCATALOG_FOLLOWING).
	            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.BLOGCATALOG_FOLLOWING));
	        }

	    };
	    
	public static Comparator<Similarity<Integer>> SimilarityComparatorTwitterFollowingBasedIncreasing = 
			 new Comparator<Similarity<Integer> >() {

	        public int compare(Similarity<Integer> o1, Similarity<Integer> o2) {
	        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.TWITTER_FOLLOWING);
				Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.TWITTER_FOLLOWING);
				
				if(o2Sim == null || o1Sim == null){
					return 0;
				}
				
	            return -1 * o2.getSimilarityMap().get(Similarity.SimType.TWITTER_FOLLOWING).
	            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.TWITTER_FOLLOWING));
	        }

	    };
	    
	 public static Comparator<Similarity<Integer>> SimilarityComparatorFlickrGrpBasedIncreasing = 
			 new Comparator<Similarity<Integer> >() {

	        public int compare(Similarity<Integer> o1, Similarity<Integer> o2) {
	        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.FLICKR_GRP);
				Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.FLICKR_GRP);
				
				if(o2Sim == null || o1Sim == null){
					return 0;
				}
				
	            return -1 * o2.getSimilarityMap().get(Similarity.SimType.FLICKR_GRP).
	            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.FLICKR_GRP));
	        }

	    };
	    
		 public static Comparator<Similarity<Integer>> SimilarityComparatorFlickrCmnContactsBasedIncreasing = 
				 new Comparator<Similarity<Integer> >() {

		        public int compare(Similarity<Integer> o1, Similarity<Integer> o2) {
		        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.FLICKR_CMN_CONTACTS);
					Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.FLICKR_CMN_CONTACTS);
					
					if(o2Sim == null || o1Sim == null){
						return 0;
					}
					
		            return -1 * o2.getSimilarityMap().get(Similarity.SimType.FLICKR_CMN_CONTACTS).
		            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.FLICKR_CMN_CONTACTS));
		        }

		    };
	    
	   
	    
}
