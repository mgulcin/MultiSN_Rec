package similarity;

public class SimilarityVal {
	Integer user1id;
	Integer user2id;
	Double simVal;
	
	
	public SimilarityVal(Integer user1id, Integer user2id, Double simVal) {
		super();
		this.user1id = user1id;
		this.user2id = user2id;
		this.simVal = simVal;
	}
	public Integer getUser1id() {
		return user1id;
	}
	public void setUser1id(Integer user1id) {
		this.user1id = user1id;
	}
	public Integer getUser2id() {
		return user2id;
	}
	public void setUser2id(Integer user2id) {
		this.user2id = user2id;
	}
	public Double getSimVal() {
		return simVal;
	}
	public void setSimVal(Double simVal) {
		this.simVal = simVal;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(user1id);
		builder.append(",");
		builder.append(user2id);
		builder.append(",");
		builder.append(simVal);
		return builder.toString();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user1id == null) ? 0 : user1id.hashCode());
		result = prime * result + ((user2id == null) ? 0 : user2id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimilarityVal other = (SimilarityVal) obj;
		if (user1id == null) {
			if (other.user1id != null)
				return false;
		} else if (!user1id.equals(other.user1id))
			return false;
		if (user2id == null) {
			if (other.user2id != null)
				return false;
		} else if (!user2id.equals(other.user2id))
			return false;
		return true;
	}
	
	
}
