package recommender.cf;


public class CFYoutubeUserVideoCountRecommender extends CFRecommenderBase{

	public String getRecName(){
		return "CF_YoutubeUserVideoCount";
	}
	
	public CFYoutubeUserVideoCountRecommender(String targetTableName){
		super(targetTableName);
		// which table is used for similarity comparisons
		this.simTableName = "UserSimYoutubeUserVideoCount";
	}
}
