package recommender;

import java.util.Comparator;

public class Recommendation {

	Integer recommendedId;
	Double score;
	
	
	
	public Recommendation(Integer recommended, Double score) {
		super();
		this.recommendedId = recommended;
		this.score = score;
	}

	public Integer getRecommended() {
		return recommendedId;
	}



	public void setRecommended(Integer recommended) {
		this.recommendedId = recommended;
	}



	public Double getScore() {
		return score;
	}



	public void setScore(Double score) {
		this.score = score;
	}

   


	public static Comparator<Recommendation> ScoreComparator = new Comparator<Recommendation>() {

		public int compare(Recommendation o1, Recommendation o2) {
			int resVal = o2.getScore().compareTo(o1.getScore());
			// may change this to alpha numeric order in case of equality
			// if(resVal == 0){
			//resVal = o1.getRecommended().compareTo(o2.getRecommended());
			//}
			return resVal;
		}

	};
}
