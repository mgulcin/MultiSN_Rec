package recommender.cf;


public class CFLastFmUserGenderRecommender extends CFRecommenderBase{

	public String getRecName(){
		return "CF_LastFmUserGender";
	}
	
	public CFLastFmUserGenderRecommender(String targetTableName){
		super(targetTableName);
		// which table is used for similarity comparisons
		this.simTableName = "UserSimLastFmGender";
	}

}
