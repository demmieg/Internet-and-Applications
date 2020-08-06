package model;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class FullArticle {
	@SerializedName("paper_id")
	private String paperID;
	private Metadata metadata;
	@SerializedName("abstract")
	private List<TextEntry> fullArticleAbstract;
	@SerializedName("body_text")
	private List<TextEntry> bodyText;
	@SerializedName("bib_entries")
	private Map<String, BibEntry> bibEntries;
	@SerializedName("ref_entries")
	private Map<String, RefEntry> refEntries;
	@SerializedName("back_matter")
	private List<TextEntry> backMatter;

	public String getPaperID() {
		return paperID;
	}

	public void setPaperID(String value) {
		this.paperID = value;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata value) {
		this.metadata = value;
	}

	public List<TextEntry> getWelcomeAbstract() {
		return fullArticleAbstract;
	}

	public void setWelcomeAbstract(List<TextEntry> value) {
		this.fullArticleAbstract = value;
	}

	public List<TextEntry> getBodyText() {
		return bodyText;
	}

	public void setBodyText(List<TextEntry> value) {
		this.bodyText = value;
	}

	public Map<String, BibEntry> getBibEntries() {
		return bibEntries;
	}

	public void setBibEntries(Map<String, BibEntry> value) {
		this.bibEntries = value;
	}

	public Map<String, RefEntry> getRefEntries() {
		return refEntries;
	}

	public void setRefEntries(Map<String, RefEntry> value) {
		this.refEntries = value;
	}

	public List<TextEntry> getBackMatter() {
		return backMatter;
	}

	public void setBackMatter(List<TextEntry> value) {
		this.backMatter = value;
	}
}
