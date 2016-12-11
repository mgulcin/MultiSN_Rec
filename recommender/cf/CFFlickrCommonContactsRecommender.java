package recommender.cf;

public class CFFlickrCommonContactsRecommender extends CFRecommenderBase{
		
	public String getRecName(){
		return "CF_CmnContacts";
	}
	
	public CFFlickrCommonContactsRecommender(String targetTableName){
		super(targetTableName);
		
		// which table is used for similarity comparisons
		this.simTableName = "UserSimFlickrCmnContacts";
	}

	
}
