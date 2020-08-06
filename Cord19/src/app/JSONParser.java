package app;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import model.FullArticle;

public class JSONParser {

	/*
	 * Αυτή η μέθοδος δέχεται ως όρισμα ένα Article, και επιστρέφει μία λίστα με όλα
	 * τα FullArticles που έχει το Article αυτό.
	 */
	public List<FullArticle> parseFile(Article article, String folder) {

		List<FullArticle> results = null;

		String filepath = null;
		if (!article.getPathToPMCJson().isEmpty()) {
			filepath = article.getPathToPMCJson();
		} else if (!article.getPathToPDFJson().isEmpty()) {
			filepath = article.getPathToPDFJson();
		}

		if (filepath != null) {
			String pathToFile;
			String[] paths = filepath.split(";");

			results = new ArrayList<FullArticle>();

			for (String path : paths) {
				pathToFile = folder + "/" + path.trim();

				try {
					Gson gson = new Gson();
					Reader reader = (new FileReader(pathToFile));
					FullArticle fullArticle = gson.fromJson(reader, FullArticle.class);
					results.add(fullArticle);
				} catch (FileNotFoundException e) {
					GUIApp.appendLog("Δε βρέθηκε το αρχείο " + pathToFile);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		return results;
	}
}
