package recommender.cf;


public class CFLastFmUserCountryRecommender extends CFRecommenderBase{

	public String getRecName(){
		return "CF_LastFmUserCountry";
	}

	public CFLastFmUserCountryRecommender(String targetTableName){
		super(targetTableName);
		// which table is used for similarity comparisons
		this.simTableName = "UserSimLastFmCountry";
	}
}
