package model;

public class RefEntry {
	private String text;
	private Object latex;
	private String type;
	private String html;

	public String getText() {
		return text;
	}

	public void setText(String value) {
		this.text = value;
	}

	public Object getLatex() {
		return latex;
	}

	public void setLatex(Object value) {
		this.latex = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String value) {
		this.type = value;
	}

	public String getHTML() {
		return html;
	}

	public void setHTML(String value) {
		this.html = value;
	}
}
