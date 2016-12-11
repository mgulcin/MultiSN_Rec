package recommender;

import java.io.IOException;
import java.util.ArrayList;

public abstract class  RecommenderBase {

	// abstract methods
	public abstract String getRecName();
	public abstract ArrayList<Recommendation> recommend(Integer target, Integer neighborCount, 
			Integer outputListSize) throws IOException;
	
}
