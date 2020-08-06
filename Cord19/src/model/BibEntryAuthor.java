package model;

import java.util.List;

public class BibEntryAuthor {
	private String first;
	private List<String> middle;
	private String last;
	private String suffix;

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
}
