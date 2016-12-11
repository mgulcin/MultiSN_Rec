package recommender.cf;


public class CFLastFmFriendsRecommender extends CFRecommenderBase{

	public String getRecName(){
		return "CF_LastFmFriends";
	}
	
	public CFLastFmFriendsRecommender(String targetTableName){
		super(targetTableName);
		// which table is used for similarity comparisons
		this.simTableName = "UserSimLastFmFriends";
	}
}
