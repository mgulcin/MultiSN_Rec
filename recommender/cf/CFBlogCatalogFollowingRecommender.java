package recommender.cf;


public class CFBlogCatalogFollowingRecommender extends CFRecommenderBase{

	public String getRecName(){
		return "CF_BlogCatalogFollowing";
	}
	
	public CFBlogCatalogFollowingRecommender(String targetTableName){
		super(targetTableName);
		// which table is used for similarity comparisons
		this.simTableName = "UserSimBlogCatalogFollowing";
	}
	
}
