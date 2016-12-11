package recommender.cf;

public class CFFlickrGrpRecommender extends CFRecommenderBase{

	public String getRecName(){
		return "CF_Grp";
	}

	public CFFlickrGrpRecommender(String targetTableName){
		super(targetTableName);
		// which table is used for similarity comparisons
		this.simTableName = "UserSimFlickrGroups";
	}


}
