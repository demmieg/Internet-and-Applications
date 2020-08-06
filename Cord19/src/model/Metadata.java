package model;

import java.util.List;

public class Metadata {
	private String title;
	private List<MetadataAuthor> authors;

	public String getTitle() {
		return title;
	}

	public void setTitle(String value) {
		this.title = value;
	}

	public List<MetadataAuthor> getAuthors() {
		return authors;
	}

	public void setAuthors(List<MetadataAuthor> value) {
		this.authors = value;
	}
}
