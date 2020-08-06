package app;

import com.opencsv.bean.CsvBindByName;

public class Article {

	public Article() {

	}

	public Article(String id, String pathToPMCJson, String pathToPDFJson) {
		this.ID = id;
		this.pathToPMCJson = pathToPMCJson;
		this.pathToPDFJson = pathToPDFJson;
	}

	@CsvBindByName(column = "cord_uid")
	private String ID;

	@CsvBindByName(column = "sha")
	private String sha;

	@CsvBindByName(column = "source_x")
	private String source;

	@CsvBindByName
	private String title;

	@CsvBindByName(column = "abstract")
	private String articleAbstract;

	@CsvBindByName(column = "publish_time")
	private String publishTime;

	@CsvBindByName
	private String authors;

	@CsvBindByName
	private String journal;

	@CsvBindByName(column = "pdf_json_files")
	private String pathToPDFJson;

	@CsvBindByName(column = "pmc_json_files")
	private String pathToPMCJson;

	@CsvBindByName
	private String url;

	@CsvBindByName(column = "s2_id")
	private String corpusID;

	// Setters and Getters

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArticleAbstract() {
		return articleAbstract;
	}

	public void setArticleAbstract(String articleAbstract) {
		this.articleAbstract = articleAbstract;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getJournal() {
		return journal;
	}

	public void setJournal(String journal) {
		this.journal = journal;
	}

	public String getPathToPDFJson() {
		return pathToPDFJson;
	}

	public void setPathToPDFJson(String pathToPDFJson) {
		this.pathToPDFJson = pathToPDFJson;
	}

	public String getPathToPMCJson() {
		return pathToPMCJson;
	}

	public void setPathToPMCJson(String pathToPMCJson) {
		this.pathToPMCJson = pathToPMCJson;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCorpusID() {
		return corpusID;
	}

	public void setCorpusID(String corpusID) {
		this.corpusID = corpusID;
	}

	public String getArticle() {
		return getID() + " , " + getSha() + " , " + getSource() + " , " + getTitle() + " , " + getArticleAbstract()
				+ " , " + getPublishTime() + " , " + getAuthors() + " , " + getJournal() + " , " + getPathToPDFJson()
				+ " , " + getPathToPMCJson() + " , " + getUrl() + " , " + getCorpusID() + " , ";
	}

}
