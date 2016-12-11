package recommender.cf;



public class CFTwitterFollowingRecommender extends CFRecommenderBase{

	public String getRecName(){
		return "CF_TwitterFollowing";
	}
	
	public CFTwitterFollowingRecommender(String targetTableName){
		super(targetTableName);
		// which table is used for similarity comparisons
		this.simTableName = "UserSimTwitterFollowing";
	}

}
