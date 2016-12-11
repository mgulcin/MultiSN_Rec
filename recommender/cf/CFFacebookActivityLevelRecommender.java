package recommender.cf;


public class CFFacebookActivityLevelRecommender extends CFRecommenderBase{


	public String getRecName(){
		return "CF_FacebookActivityLevel";
	}

	public CFFacebookActivityLevelRecommender(String targetTableName){
		super(targetTableName);
		// which table is used for similarity comparisons
		this.simTableName = "UserSimFbActivityLevel";
	
	}
	
	

}
