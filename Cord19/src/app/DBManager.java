package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import model.FullArticle;

public class DBManager {

	private Connection connection;
	private Properties settings;

	public DBManager() throws SQLException {
		this.settings = loadSettings();

		String databaseName = this.settings.getProperty("databaseName");
		String databaseURL = this.settings.getProperty("databaseUrl");
		String databasePort = this.settings.getProperty("databasePort");

		String connectionURL = "jdbc:mysql://" + databaseURL + ":" + databasePort + "/" + databaseName
				+ "?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

		String user = this.settings.getProperty("databaseUsername");
		String password = this.settings.getProperty("databasePassword");

		this.connection = DriverManager.getConnection(connectionURL, user, password);
	}

	/*
	 * Αυτή η μέθοδος φορτώνει τις ρυθμίσεις της βάσης.
	 */
	private Properties loadSettings() {
		String file = "config.properties";

		Reader reader;
		Properties settings = new Properties();

		try {
			reader = new FileReader(new File(file));
			settings.load(reader);
			reader.close();
		} catch (FileNotFoundException e) {
			GUIApp.appendLog("Δεν υπάρχουν αποθηκευμένες ρυθμίσεις. Παρακαλώ κάντε αλλαγή ρυθμίσεων.");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return settings;
	}

	/*
	 * Αυτή η μέθοδος αποθηκεύει στη βάση όλα τα Articles.
	 */
	public void writeArticlesMetadata(List<Article> articles) {
		try {
			String query = "INSERT IGNORE into Article (cordID,sha,source,title,abstract,publishTime,authors,journal,pathToPDF,pathToPMC,URL,corpusID)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?)";

			this.connection.setAutoCommit(false);
			PreparedStatement preparedStmt = this.connection.prepareStatement(query);

			for (Article article : articles) {
				preparedStmt.setString(1, article.getID());
				preparedStmt.setString(2, article.getSha());
				preparedStmt.setString(3, article.getSource());
				preparedStmt.setString(4, article.getTitle());
				preparedStmt.setString(5, article.getArticleAbstract());
				preparedStmt.setString(6, article.getPublishTime());
				preparedStmt.setString(7, article.getAuthors());
				preparedStmt.setString(8, article.getJournal());
				preparedStmt.setString(9, article.getPathToPDFJson());
				preparedStmt.setString(10, article.getPathToPMCJson());
				preparedStmt.setString(11, article.getUrl());
				preparedStmt.setString(12, article.getCorpusID());
				preparedStmt.addBatch();
			}

			long start = System.currentTimeMillis();
			int[] articlesInserted = preparedStmt.executeBatch();
			this.connection.commit();
			long end = System.currentTimeMillis();

			GUIApp.appendLog("Αποθηκεύτηκαν " + articlesInserted.length + " άρθρα.");
			GUIApp.appendLog("Χρόνος για την ολοκληρώση της writeArticlesMetadata(): " + (end - start) + " ms.\n");

			preparedStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Αυτή η μέθοδος επιστρέφει όλα τα Articles τα οποία έχουν αρχείο με το PMC ή
	 * το PDF.
	 */
	public List<Article> getArticlesWithFiles() {

		long start = System.currentTimeMillis();

		List<Article> results = null;

		String query = "SELECT cordID, pathToPDF, pathToPMC FROM Article WHERE Article.pathToPDF LIKE \"%document%\" OR Article.pathToPMC LIKE \"%document%\";";

		try {
			this.connection.setAutoCommit(true);
			Statement stmt = this.connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			results = new ArrayList<Article>();
			while (rs.next()) {
				results.add(new Article(rs.getString("cordID"), rs.getString("pathToPMC"), rs.getString("pathToPDF")));
			}

			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();

		GUIApp.appendLog("Βρέθηκαν " + results.size() + " άρθρα με path για πλήρες αρχείο.");
		GUIApp.appendLog("Χρόνος για την ολοκληρώση της getArticlesWithFiles(): " + (end - start) + " ms\n");

		return results;
	}

	/*
	 * Αυτή η μέθοδος λαμβάνει ως ορίσματα ένα FullArticle και το ID ενός Article,
	 * και αποθηκεύει στη βάση το FullArticle, και τη συσχέτιση του Article με ένα
	 * FullArticle
	 */
	public void writeFullArticle(FullArticle fullArticle, String cordID) {

		String query = "INSERT IGNORE into FullArticle (paperID, numberOfRef, numberOfBib, hasText)"
				+ " values (?,?,?,?)";

		try {
			this.connection.setAutoCommit(true);
			PreparedStatement preparedStmt = this.connection.prepareStatement(query);

			preparedStmt.setString(1, fullArticle.getPaperID());
			preparedStmt.setInt(2, fullArticle.getRefEntries().size());
			preparedStmt.setInt(3, fullArticle.getBibEntries().size());
			preparedStmt.setBoolean(4, !fullArticle.getBodyText().get(0).getText().isEmpty());

			int result = preparedStmt.executeUpdate();

			preparedStmt.close();

			if (result < 1) {
				GUIApp.appendLog("Το FullArticle με ID " + fullArticle.getPaperID() + " υπάρχει ήδη.");
			} else {
				this.connection.createStatement()
						.executeUpdate("INSERT INTO ArticleToFullArticle (paperID, cordID) VALUES( \""
								+ fullArticle.getPaperID() + "\" ,\"" + cordID + "\") ;");
			}

			preparedStmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Αυτή η μέθοδος επιστρέφει τον αριθμό όλων των FullArticles τα οποία έχουν
	 * πλήρες κείμενο.
	 */
	public int getNumberOfArticlesWithFullText() {

		int count = 0;

		String query = "SELECT COUNT(*) FROM FullArticle WHERE FullArticle.hasText=1;";

		try {
			this.connection.setAutoCommit(true);
			Statement stmt = this.connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			count = rs.getInt(1);
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	/*
	 * Αυτή η μέθοδος επιστρέφει τον αριθμό όλων των Articles.
	 */
	public int getNumberOfAllArticles() {

		int count = 0;

		String query = "SELECT COUNT(*) FROM Article;";

		try {
			this.connection.setAutoCommit(true);
			Statement stmt = this.connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			count = rs.getInt(1);
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	/*
	 * Αυτή η μέθοδος επιστρέφει τους συγγραφείς όλων των Articles.
	 */
	public ArrayList<String> getDatasetForAuthors() {

		ArrayList<String> results = new ArrayList<String>();
		String query = "SELECT authors FROM Article;";

		try {
			this.connection.setAutoCommit(true);
			Statement stmt = this.connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				results.add(rs.getString(1));
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

	/*
	 * Αυτή η μέθοδος επιστρέφει το έτος έκδοσης για τα Articles.
	 */
	public HashMap<Integer, Integer> getDatasetForPublishYear() {

		HashMap<Integer, Integer> results = new HashMap<Integer, Integer>();
		String query = "SELECT SUBSTRING(publishTime, 1, 4) AS year, COUNT(*) FROM Article GROUP BY SUBSTRING(publishTime, 1, 4);";

		try {
			this.connection.setAutoCommit(true);
			Statement stmt = this.connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				results.put(rs.getInt(1), rs.getInt(2));
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

	/*
	 * Αυτή η μέθοδος κλείνει το connection για τη βάση
	 */
	public void close() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
