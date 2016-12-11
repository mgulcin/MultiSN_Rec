package recommender;


public class Rating<T> {
	T recommender;
	T recommended; 
	Double ratingScore;
	
	public Rating(T recommender, T recommended,
			Double ratingScore) {
		super();
		this.recommender = recommender;
		this.recommended = recommended;
		this.ratingScore = ratingScore;
	}

	public T getRecommender() {
		return recommender;
	}

	public void setRecommenderGeneName(T recommender) {
		this.recommender = recommender;
	}

	public T getRecommended() {
		return recommended;
	}

	public void setRecommendedGeneName(T recommended) {
		this.recommended = recommended;
	}

	public Double getRatingScore() {
		return ratingScore;
	}

	public void setRatingScore(Double ratingScore) {
		this.ratingScore = ratingScore;
	}
	
	
	
}
