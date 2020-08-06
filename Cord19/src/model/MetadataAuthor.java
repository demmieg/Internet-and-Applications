package model;

import java.util.List;

public class MetadataAuthor {
	private String first;
	private List<String> middle;
	private String last;
	private String suffix;
	private Affiliation affiliation;
	private String email;

	public String getFirst() {
		return first;
	}

	public void setFirst(String value) {
		this.first = value;
	}

	public List<String> getMiddle() {
		return middle;
	}

	public void setMiddle(List<String> value) {
		this.middle = value;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String value) {
		this.last = value;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String value) {
		this.suffix = value;
	}

	public Affiliation getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(Affiliation value) {
		this.affiliation = value;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String value) {
		this.email = value;
	}
}
