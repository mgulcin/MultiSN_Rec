package recommender.cf;


public class CFYoutubeFreebaseUserTopicsRecommender extends CFRecommenderBase{

	public String getRecName(){
		return "CF_YoutubeFreebaseUserTopics";
	}
	
	public CFYoutubeFreebaseUserTopicsRecommender(String targetTableName){
		super(targetTableName);
		// which table is used for similarity comparisons
		this.simTableName = "UserSimYoutubeFreebaseUserTopics";
	}

}
