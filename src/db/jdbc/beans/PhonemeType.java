package db.jdbc.beans;

public class PhonemeType {
	
	private String id;
	private String Vowel;
	private String Consonant;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVowel() {
		return Vowel;
	}
	public void setVowel(String vowel) {
		Vowel = vowel;
	}
	public String getConsonant() {
		return Consonant;
	}
	public void setConsonant(String consonant) {
		Consonant = consonant;
	}
	@Override
	public String toString() {
		return "Phoneme [id=" + id + ", Vowel=" + Vowel + ", Consonant="
				+ Consonant + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((Consonant == null) ? 0 : Consonant.hashCode());
		result = prime * result + ((Vowel == null) ? 0 : Vowel.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		PhonemeType other = (PhonemeType) obj;
		if (Consonant == null) {
			if (other.Consonant != null)
				return false;
		} else if (!Consonant.equals(other.Consonant))
			return false;
		if (Vowel == null) {
			if (other.Vowel != null)
				return false;
		} else if (!Vowel.equals(other.Vowel))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
