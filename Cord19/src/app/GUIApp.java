package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class GUIApp {

	String metadataPath = null;
	JButton findButton;
	JButton processButton;
	JCheckBox checkbox;
	static JTextArea log;

	/*
	 * Αρχικοποιεί το παράθυρο και καλεί τις υπόλοιπες μεθόδους του γραφικού.
	 */
	public void show() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				JFrame frame = new JFrame("Cord-19 ");

				frame.setSize(800, 600);
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);

				createΜenu(frame);

				JPanel panel = new JPanel();
				panel.setOpaque(true);
				panel.setLayout(new BorderLayout());
				createOpenScreen(panel);

				frame.setContentPane(panel);
				frame.setVisible(true);
				appendLog("Καλως ήρθατε στην εφαρμογή Cord-19!\n");

				appendLog("Επιλέξτε Βοήθεια στο μενού για να δείτε τις διαθέσιμες επιλογές.\n");
			}
		});
	}

	/*
	 * Δημιουργεί ένα μενού και ορίζει τις λειτουργίες που θα εκτελεστούν όταν ο
	 * χρήστης αλληλεπιδράσει με το μενού.
	 */
	void createΜenu(JFrame frame) {
		JMenuBar menu = new JMenuBar();
		JMenu m1 = new JMenu("Ρυθμίσεις");
		JMenu m2 = new JMenu("Στατιστικά");
		JMenu m3 = new JMenu("Βοήθεια");

		m3.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				Desktop dt = Desktop.getDesktop();
				try {
					dt.open(new File("help.txt"));
				} catch (IOException ioe) {
				}

			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		menu.add(m1);
		menu.add(m2);
		menu.add(m3);

		JMenuItem m11 = new JMenuItem("Τρέχουσες Ρυθμίσεις");
		m11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				viewSettings();
			}
		});

		JMenuItem m12 = new JMenuItem("Αλλαγή Ρυθμίσεων");
		m12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				processSettings();
			}
		});

		m1.add(m11);
		m1.add(m12);

		JMenuItem m21 = new JMenuItem("Άρθρα με πλήρες κείμενο");
		m21.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				createPieChart();
			}

		});

		JMenuItem m22 = new JMenuItem("Άρθρα ανά Έτος Έκδοσης");
		m22.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				createXYLineChart();
			}

		});

		JMenuItem m23 = new JMenuItem("Συγγραφείς ανά Άρθρο");
		m23.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				createBarChart();
			}

		});

		m2.add(m21);
		m2.add(m22);
		m2.add(m23);

		frame.setJMenuBar(menu);
	}

	/*
	 * Δημιουργεί την αρχική οθόνη της εφαρμογής
	 */
	public void createOpenScreen(JPanel panel) {

		log = new JTextArea(5, 20);
		log.setMargin(new Insets(10, 10, 10, 10));
		log.setEditable(false);

		JScrollPane logScrollPane = new JScrollPane(log);

		findButton = new JButton("Επιλογή CSV αρχείου...");
		findButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				metadataPath = selectFile();
				if (metadataPath != null) {
					appendLog("Επιλέξατε το αρχείο " + metadataPath + "\n");
				}
			}
		});

		checkbox = new JCheckBox("Θέλω πλήρη στοιχεία");

		processButton = new JButton("Εκτέλεση");
		processButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				appendLog("Επιλέξατε επεξεργασία δεδομένων του αρχείου " + metadataPath + ",");
				if (checkbox.isSelected()) {
					appendLog("με επιλογή για αποθήκευση όλων των μεταδεδομένων των άρθρων. \n");
				} else {
					appendLog("με επιλογή για σύντομη αποθήκευση μεταδεδομένων των άρθρων. \n");
				}
				MainApp.processFiles(metadataPath, checkbox.isSelected());
			}
		});

		JPanel buttonPanel = new JPanel(); // Νέο JPanel για να είναι οι επιλογές σε οριζόντια στοίχιση
		buttonPanel.add(findButton);
		buttonPanel.add(checkbox);
		buttonPanel.add(processButton);

		panel.add(logScrollPane, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.PAGE_END);
	}

	/*
	 * Διαβάζει το αρχείο config.properties του project και τυπώνει το περιεχόμενό
	 * του στο text area του γραφικού.
	 */
	protected void viewSettings() {
		String file = "config.properties";

		Reader reader;
		Properties settings = new Properties();

		try {
			reader = new FileReader(new File(file));
			settings.load(reader);

			GUIApp.appendLog("\nΟι τρέχουσες ρυθμίσεις είναι οι εξής:");

			GUIApp.appendLog("URL: " + settings.getProperty("databaseUrl"));
			GUIApp.appendLog("PORT: " + settings.getProperty("databasePort"));
			GUIApp.appendLog("DB NAME: " + settings.getProperty("databaseName"));
			GUIApp.appendLog("DB USERNAME: " + settings.getProperty("databaseUsername"));

			GUIApp.appendLog("\n");

			reader.close();
		} catch (FileNotFoundException e) {
			GUIApp.appendLog("Δεν υπάρχουν αποθηκευμένες ρυθμίσεις. Παρακαλώ κάντε αλλαγή ρυθμίσεων.\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Ανοίγει ένα παράθυρο διαλόγου για να επιλέξεις ένα αρχείο και αποθηκεύει το
	 * περιεχόμενό του στο αρχείο config.properties του project.
	 */
	protected void processSettings() {
		String file = selectFile();

		if (file != null) {
			Reader reader;
			Properties settings = new Properties();

			try {
				reader = new FileReader(new File(file));
				settings.load(reader);

				File configFile = new File("config.properties");
				FileWriter writer = new FileWriter(configFile);
				settings.store(writer, "Database settings");

				GUIApp.appendLog("Οι ρυθμίσεις σας αποθηκεύτηκαν επιτυχώς");

				GUIApp.appendLog("\nΟι τρέχουσες ρυθμίσεις είναι οι εξής:");

				GUIApp.appendLog("URL: " + settings.getProperty("databaseUrl"));
				GUIApp.appendLog("PORT: " + settings.getProperty("databasePort"));
				GUIApp.appendLog("DB NAME: " + settings.getProperty("databaseName"));
				GUIApp.appendLog("DB USERNAME: " + settings.getProperty("databaseUsername"));

				GUIApp.appendLog("\n");

				reader.close();
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/*
	 * Ανοίγει ένα παράθυρο διαλόγου για να επιλέξεις ένα αρχείο από τον υπολογιστή
	 * και επιστρέφει το path του.
	 */
	public String selectFile() {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home") + "/Desktop");
		fileChooser.setDialogTitle("Επιλέξτε το αρχείο των μεταδεδομένων");

		int response = fileChooser.showOpenDialog(null);
		if (response != JFileChooser.APPROVE_OPTION)
			return null;
		return fileChooser.getSelectedFile().toString();

	}

	/*
	 * Σχεδιάζει ένα διάγραμμα πίτα με τα άρθρα που έχουν πλήρες κείμενο σε σχέση με
	 * αυτά που δεν έχουν.
	 */
	private void createPieChart() {
		DBManager manager = null;
		try {
			manager = new DBManager();
		} catch (SQLException e) {
			GUIApp.appendLog("Έξοδος... Δεν ήταν δυνατή η σύνδεση στη βάση. Παρακαλώ ελέγξτε τις ρυθμίσεις.");
			return;
		}

		int numberOfArticlesWithFullText = manager.getNumberOfArticlesWithFullText();
		int numberOfAllArticles = manager.getNumberOfAllArticles();

		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("Άρθρα με πλήρες κείμενο", numberOfArticlesWithFullText);
		dataset.setValue("Άρθρα χωρίς πλήρες κείμενο", numberOfAllArticles - numberOfArticlesWithFullText);

		JFreeChart chart = ChartFactory.createPieChart("", dataset, false, true, false);
		ChartFrame frame = new ChartFrame("Άρθρα με πλήρες κείμενο", chart);
		chart.getPlot().setBackgroundPaint(Color.WHITE);

		frame.pack();
		frame.setSize(800, 500);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	/*
	 * Σχεδιάζει ένα διάγραμμα ΧΥ με τον αριθμό των Άρθρων που δημοσιεύτηκαν κάθε
	 * χρονιά
	 */
	private void createXYLineChart() {
		DBManager manager = null;
		try {
			manager = new DBManager();
		} catch (SQLException e) {
			GUIApp.appendLog("Έξοδος... Δεν ήταν δυνατή η σύνδεση στη βάση. Παρακαλώ ελέγξτε τις ρυθμίσεις.\n");
			return;
		}

		HashMap<Integer, Integer> results = manager.getDatasetForPublishYear();
		XYSeries series = new XYSeries("");

		for (Integer i : results.keySet()) {
			if (i > 0) {
				series.add(i, results.get(i));
			}
		}

		XYSeriesCollection dataset = new XYSeriesCollection(series);
		dataset.setAutoWidth(true);

		JFreeChart chart = ChartFactory.createXYLineChart("", "Έτος", "Άρθρα", dataset, PlotOrientation.VERTICAL, false,
				true, false);
		ChartFrame frame = new ChartFrame("Άρθρα ανά Έτος Έκδοσης", chart);
		chart.getPlot().setBackgroundPaint(Color.WHITE);

		XYPlot xyPlot = (XYPlot) chart.getPlot();
		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setRangeCrosshairVisible(true);

		frame.pack();
		frame.setSize(800, 500);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	/*
	 * Σχεδιάζει ένα γράφημα με μπάρες με τον αριθμό των συγγραφέων ανά άρθρο
	 */
	protected void createBarChart() {
		DBManager manager = null;
		try {
			manager = new DBManager();
		} catch (SQLException e) {
			GUIApp.appendLog("Έξοδος... Δεν ήταν δυνατή η σύνδεση στη βάση. Παρακαλώ ελέγξτε τις ρυθμίσεις.\n");
			return;
		}

		ArrayList<String> results = manager.getDatasetForAuthors();

		HashMap<Integer, Integer> groups = new HashMap<Integer, Integer>();

		for (String author : results) {
			int numberOfAuthors = author.split(";").length;
			if (numberOfAuthors > 20) {
				numberOfAuthors = 20;
			}
			if (!groups.containsKey(numberOfAuthors)) {
				groups.put(numberOfAuthors, 1);
			} else {
				groups.put(numberOfAuthors, groups.get(numberOfAuthors) + 1);
			}
		}

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (Integer i : groups.keySet()) {
			if (i == 20) {
				dataset.addValue(groups.get(i), "# Συγγραφέων", "20+");
			} else {
				dataset.addValue(groups.get(i), "# Συγγραφέων", i);
			}
		}

		JFreeChart chart = ChartFactory.createBarChart("", "# Συγγραφέων", "# Άρθρων", dataset,
				PlotOrientation.VERTICAL, false, true, true);

		ChartFrame frame = new ChartFrame("Συγγραφείς ανά Άρθρο", chart);
		chart.getPlot().setBackgroundPaint(Color.WHITE);

		frame.pack();
		frame.setSize(800, 500);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	/*
	 * Τυπώνει ένα κείμενο στο text area για logging σκοπούς
	 */
	public static void appendLog(String text) {
		log.append(text + "\n");
		log.setCaretPosition(log.getDocument().getLength());
	}

}
