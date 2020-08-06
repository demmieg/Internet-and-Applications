package model;

import java.util.List;

public class BibEntry {
	private String refID;
	private String title;
	private List<BibEntryAuthor> authors;
	private Long year;
	private String venue;
	private String volume;
	private String issn;
	private String pages;
	private OtherIDS otherIDS;

	public String getRefID() {
		return refID;
	}

	public void setRefID(String value) {
		this.refID = value;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String value) {
		this.title = value;
	}

	public List<BibEntryAuthor> getAuthors() {
		return authors;
	}

	public void setAuthors(List<BibEntryAuthor> value) {
		this.authors = value;
	}

	public Long getYear() {
		return year;
	}

	public void setYear(Long value) {
		this.year = value;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String value) {
		this.venue = value;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String value) {
		this.volume = value;
	}

	public String getIssn() {
		return issn;
	}

	public void setIssn(String value) {
		this.issn = value;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String value) {
		this.pages = value;
	}

	public OtherIDS getOtherIDS() {
		return otherIDS;
	}

	public void setOtherIDS(OtherIDS value) {
		this.otherIDS = value;
	}
}
