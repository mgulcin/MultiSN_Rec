package recommender.cf;


public class CFYoutubeActivityTypeRecommender extends CFRecommenderBase {

	public String getRecName(){
		return "CF_YoutubeActivityType";
	}
	
	public CFYoutubeActivityTypeRecommender(String targetTableName){
		super(targetTableName);
		// which table is used for similarity comparisons
		this.simTableName = "UserSimYoutubeActivityType";
	}

}
