package model;

public class Span {
	private long start;
	private long end;
	private String text;
	private String refID;

	public long getStart() {
		return start;
	}

	public void setStart(long value) {
		this.start = value;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long value) {
		this.end = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String value) {
		this.text = value;
	}

	public String getRefID() {
		return refID;
	}

	public void setRefID(String value) {
		this.refID = value;
	}
}
