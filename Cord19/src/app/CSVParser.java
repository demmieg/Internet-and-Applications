package app;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;

public class CSVParser {

	/*
	 * Αυτή η μέθοδος δέχεται ως όρισμα ένα path για ένα αρχείο, και επιστρέφει μία
	 * λίστα με όλα τα Articles που έχει το αρχείο.
	 */
	public List<Article> parseFile(String filePath) {
		List<Article> articles = null;

		try {
			CSVReader reader = new CSVReader(new FileReader(filePath));
			articles = new CsvToBeanBuilder<Article>(reader).withType(Article.class).build().parse();
			reader.close();
		} catch (FileNotFoundException e) {
			GUIApp.appendLog("Το path για το αρχείο είναι λάθος: " + filePath);
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			GUIApp.appendLog("Πρόβλημα στην επεξεργασία του αρχείου: " + filePath);
			e.printStackTrace();
			return null;
		}

		GUIApp.appendLog("Βρέθηκαν " + articles.size() + " άρθρα.");

		return articles;
	}

}
