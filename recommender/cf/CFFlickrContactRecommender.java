package recommender.cf;

public class CFFlickrContactRecommender extends CFRecommenderBase {

	public String getRecName(){
		return "CF_Contacts";
	}
	public CFFlickrContactRecommender(String targetTableName){
		super(targetTableName);
		// which table is used for similarity comparisons
		this.simTableName = "UserSimFlickrContacts";
	}

}
