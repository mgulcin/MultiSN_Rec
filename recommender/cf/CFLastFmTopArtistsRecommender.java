package recommender.cf;


public class CFLastFmTopArtistsRecommender extends CFRecommenderBase{

	public String getRecName(){
		return "CF_LastFmTopArtists";
	}

	public CFLastFmTopArtistsRecommender(String targetTableName){
		super(targetTableName);
		// which table is used for similarity comparisons
		this.simTableName = "UserSimLastFmTopArtists";
	}
	
}
