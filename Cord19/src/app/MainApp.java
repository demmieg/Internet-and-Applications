package app;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import model.FullArticle;

public class MainApp {

	public static void main(String[] args) {
		GUIApp app = new GUIApp();
		app.show();
	}

	/*
	 * Αυτή η μέθοδος λαμβάνει ως όρισμα το αρχείο για .csv αρχείο και την επιλογή
	 * για πλήρη επεξργασία των αρχείων ή μη και εκτελεί τη βασική ροή του
	 * προγράμματος
	 */
	public static void processFiles(String filePath, boolean processFullMetadata) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				if (filePath == null) {
					GUIApp.appendLog("Δεν έχετε ορίσει CSV αρχείο. Παρακαλώ ορίστε το αρχείο των μεταδεδομένων");
				} else {
					
					GUIApp.appendLog("Η διαδικασία ξεκίνησε. Παρακαλώ περιμένετε... \n");

					File f = new File(filePath);
					String folder = f.getParent();

					CSVParser csvParser = new CSVParser();
					List<Article> articles = csvParser.parseFile(filePath);

					DBManager dbManager = null;
					try {
						dbManager = new DBManager();
					} catch (SQLException e) {
						GUIApp.appendLog(
								"Έξοδος... Δεν ήταν δυνατή η σύνδεση στη βάση. Παρακαλώ ελέγξτε τις ρυθμίσεις.");
						e.printStackTrace();
						return;
					}

					dbManager.writeArticlesMetadata(articles);

					if (processFullMetadata) {
						if (folder == null) {
							GUIApp.appendLog("Δεν υπάρχει ο φάκελος για τα πλήρη μεταδεδομένα.");
						} else {
							File documentParsesFolder = new File(folder + "/document_parses");

							if (documentParsesFolder.exists() && documentParsesFolder.isDirectory()) {

								List<Article> articlesWithFiles = dbManager.getArticlesWithFiles();

								JSONParser jsonParser = new JSONParser();

								long start = System.currentTimeMillis();

								for (Article article : articlesWithFiles) {
									List<FullArticle> fullArticles = jsonParser.parseFile(article, folder);

									if (fullArticles != null) {
										for (FullArticle fullArticle : fullArticles) {
											dbManager.writeFullArticle(fullArticle, article.getID());
										}
									}
								}

								long end = System.currentTimeMillis();
								GUIApp.appendLog(
										"Χρόνος για την επεξεργασία των FullArticles: " + (end - start) + " ms.\n");
							} else {
								GUIApp.appendLog("Δεν υπάρχει ο φάκελος " + documentParsesFolder.toString()
										+ " με τα πλήρη μεταδεδομένα.");
							}
						}
					}

					dbManager.close();
					GUIApp.appendLog("Η διαδικασία ολοκληρώθηκε. Μπορείτε αν θέλετε να δείτε τα στατιστικά της βάσης.\n");

				}

			}

		}).start();

	}
}
