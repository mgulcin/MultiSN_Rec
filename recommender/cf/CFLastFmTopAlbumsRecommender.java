package recommender.cf;


public class CFLastFmTopAlbumsRecommender extends CFRecommenderBase{

	public String getRecName(){
		return "CF_LastFmTopAlbums";
	}

	public CFLastFmTopAlbumsRecommender(String targetTableName){
		super(targetTableName);
		// which table is used for similarity comparisons
		this.simTableName = "UserSimLastFmTopAlbums";
	}
	
}
