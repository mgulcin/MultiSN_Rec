package recommender.cf;


public class CFLastFmCmnFriendsRecommender extends CFRecommenderBase {

	public String getRecName(){
		return "CF_LastFmCmnFriends";
	}
	public CFLastFmCmnFriendsRecommender(String targetTableName){
		super(targetTableName);
		// which table is used for similarity comparisons
		this.simTableName = "UserSimLastFmCmnFriends";

	}
}
