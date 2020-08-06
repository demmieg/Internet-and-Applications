package model;

import java.util.List;

public class TextEntry {
	private String text;
	private List<Span> citeSpans;
	private List<Span> refSpans;
	private String section;

	public String getText() {
		return text;
	}

	public void setText(String value) {
		this.text = value;
	}

	public List<Span> getCiteSpans() {
		return citeSpans;
	}

	public void setCiteSpans(List<Span> value) {
		this.citeSpans = value;
	}

	public List<Span> getRefSpans() {
		return refSpans;
	}

	public void setRefSpans(List<Span> value) {
		this.refSpans = value;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String value) {
		this.section = value;
	}
}
