package recommender.cf;


public class CFYoutubeCmnSubscriptionsRecommender extends CFRecommenderBase{

	public String getRecName(){
		return "CF_YoutubeCmnSubscriptions";
	}
	
	public CFYoutubeCmnSubscriptionsRecommender(String targetTableName){
		super(targetTableName);
		// which table is used for similarity comparisons
		this.simTableName = "UserSimYoutubeCmnSubscriptions";

	}
}
