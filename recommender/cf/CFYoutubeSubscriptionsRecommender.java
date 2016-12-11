package recommender.cf;


public class CFYoutubeSubscriptionsRecommender extends CFRecommenderBase{

	public String getRecName(){
		return "CF_YoutubeSubscriptions";
	}
	
	public CFYoutubeSubscriptionsRecommender(String targetTableName){
		super(targetTableName);
		// which table is used for similarity comparisons
		this.simTableName = "UserSimYoutubeSubscriptions";

	}
}
