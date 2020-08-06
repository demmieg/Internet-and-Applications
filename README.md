# Internet-and-Applications
## COVID-04: Εισαγωγή Άρθρων σε μία Σχεσιακή Βάση.

Χρησιμοποιώντας το dataset https://ai2-semanticscholar-cord-19.s3-us-west-2.amazonaws.com/historical_releases.html θα φτιάξω μια Desktop εφαρμογή η οποία θα εισάγει τα δεδομένα που υπάρχουν για κάθε άρθρο σε μία σχεσιακή βάση και θα κάνω συνοπτική παρουσίαση των δεδομένων της βάσης.

## Περιορισμοί
* Το αρχείο μεταδεδομένων csv των άρθρων δεν μπορεί να περιέχει \\"" , το οποίο αντιστοιχεί λανθασμένα αντί για umlaut, καθώς δεν είναι εφικτή η σωστή ανάγνωση και αναγνώριση του αρχείου. Οι χαρακτήρες αυτοί πρέπει να αφαιρεθούν από το αρχείο metadata.csv
* Το αρχείο document_parses που βρίσκεται μέσα στο φάκελο του dataset που έχουμε κατεβάσει πρέπει να αποσυμπιεστεί στην ίδια τοποθεσία ώστε να λειτουργήσει η εφαρμογή.

## Bάση Δεδομένων
 Η βάση Δεδομένων αποτελείται από τρεις πίνακες:
 Article: Έχει στήλες που προέρχονται από τα μεταδεδομένα που βρίσκονται στο csv αρχείο, με primary key το cordID
 FullArticle: Έχει στήλες που προέρχονται από τα .json αρχεία
 ArticleToFullArticle: Περιγράφει τη σχέση του Article και του FullArticle με τη χρήση foreign keys.
 
 Περισσότερα μπορείτε να δείτε στο dump της βάσης που υπαρχει στο project.

## Βασική Ροή προγράμματος
1. Ξεκινά με την εμφάνιση του γραφικού περιβάλλλοντος καλώντας τη μέθοδο GUIApp.show();. 
2. Στη συνέχεια καλείται η μέθοδος processFiles. Η συγκεκριμένη μέθοδος παίρνει ως όρισμα το path που αντιστοιχεί στο metadata.csv και το αν θέλουμε να επεξεργαστούμε και τα json αρχεία.
3. Καλεί στη συνέχεια την csvParser.parseFile(filePath); η οποία διαβάζει το metadata.csv και επιστρέφει μία λίστα με αντικείμενα τύπου Articles.java
4. Δημιουργεί ένα στιγμιότυπο του DBmanager, ο οποίος εγκαθιστά μια σύνδεση στη βάση που έχει περιγραφεί στο configuration αρχείο της εφαρμογής.
5. Καλεί τη dbManager.writeArticlesMetadata(List<Article> articles); η οποία αποθηκεύει όλη τη λίστα που επέστρεψε ο csvParser στον πίνακα Article της βάσης.
6. Στη συνέχεια αν έχει επιλεγεί στο γραφικό ότι θέλω πλήρη μεταδεδομένα καλείται η dbManager.getArticlesWithFiles(); η οποία βρίσκει από τη βάση όλες τις εγγραφές του πίνακα Article οι οποίες έχουν τιμές στις στήλες pathToPMC ή pathToPDF και επιστρέφει μια λίστα από αντικείμενα τύπου Articles.java ένα για κάθε εγγραφή.
7. Για κάθε ένα από αυτά τα αντικείμενα καλείται έπειτα η jsonParser.parseFile(Article article, String folder); η οποία διαβάζει το αντίστοιχο json αρχείο τους και επιστρέφει μια λίστα από αντικείμενα τύπου FullArticle.java (μπορεί να είναι πολλαπλά καθώς ένα άρθρο μπορεί να έχει πολλαπλά json αρχεία) . 
8. Για κάθε ένα από τα FullArticles καλείτε η dbManager.writeFullArticle(FullArticle fullArticle, String cordID); αποθηκεύει στη βάση το FullArticle, και τη συσχέτιση του Article με ένα FullArticle
9.Τέλος καλέι τη dbManager.close(); η οποία κλείνει την σύνδεση στη βάση.


## Κλάσεις και μέθοδοι της εφαρμογής

### Article.java
Κλάση που μοντελοποιεί τα Άρθρα με πεδία που αντιστοιχούν στα μεταδεομένα που περιγράφονται στο metadata.csv αρχείο. 
Η αντιστοίχηση είναι:

 csv File: cord_uid, sha ,source_x, title, abstract, publish_time, authors ,pdf_json_files, pmc_json_files, url, s2_id
 
 Article Object: ID, sha, source, title, articleAbstract, publishTime, authors, journal, pathToPDFJson, pathToPMCJson, url, corpusID
 
 
### FullArticle.java
Κλάση που μοντελοποιεί τα Άρθρα με πεδία που αντιστοιχούν στα μεταδεομένα που περιγράφονται στο .json αρχείο.
Οι υπόλοιπες κλάσεις που υπάρχουν στο πακέτο model είναι για τη σωστή περιγραφή του FullArtcle.java

### GUIApp.java
Υλοποιεί το γραφικό περιβάλλον της εφαρμογής. Έχει τις μεθόδους
  - show(): Αρχικοποιεί το παράθυρο και καλεί τις υπόλοιπες μεθόδους του γραφικού
  - createmenu(JFrame frame): Παίρνει ως όρισμα το αρχικό παράθυρο της εφαρμογής, δημιουργεί ένα μενού μέσα σε αυτό και ορίζει και τις λειτουργίες που θα εκτελεστούν όταν ο χρήστης διαδράσει με το μενού.
  - createPieChart(): Δημιουργεί μια σύνδεση στη βάση, καλεί τις μεδόθους DBmanager.getNumberOfArticlesWithFullText(); και int numberOfAllArticles = manager.getNumberOfAllArticles(); για να πάρει τα δεδομένα που χρειάζεται από τη βάση και στη συνέχεια σχεδιάζει ένα διάγραμμα πίτα με τα άρθρα που έχουν πλήρες κείμενο σε σχέση με αυτά που δεν έχουν.
  - createXYLineChart(): Δημιουργεί μια σύνδεση στη βάση, καλεί τις μεδόθους DBmanager.getDatasetForPublishYear(); για να πάρει τον αριθμό των Άρθρων που δημοσιεύτηκαν κάθε χρονιά και στη συνέχει σχεδιάζει ένα διάγραμμα ΧΥ με βάση αυτά.
  - createBarChart(): Δημιουργεί μια σύνδεση στη βάση, καλεί τις μεδόθους DBmanager.getDatasetForAythors(); για να πάρει από τη βάση μία λίστα με τους συγγραφείς κάθε άρθρου. Διαβάσει κάθε string που του επέστρεψε η βάση το κάνει split και μετράει πόσους συγγραφείς έχει κάθε άρθρο και στη συνέχεια υπολογίζει πόσα άρθρα έχουν τον ίδιο αριθμό συγγραφέων. Τέλος παρουσιάζει τα αποτελέσματα σε ένα γράφημα με μπάρες.
  - viewSettings(): Διαβάζει το αρχείο config.properties του project και τυπώνει το περιεχόμενό του στην text area του γραφικού.
  - processSettings(): Ανοίγει ένα παράθυρο διαλόγου για να επιλέξεις ένα αρχείο και αποθηκεύει το περιεχόμενό του στο αρχείο config.properties του project.
  - createOpenScreen(JPanel panel): Παίρνει ως όρισμα ένα JPanel και φτιάχνει την αρχική οθόνη της εφαρμογής προσθέτοντας ένα TextArea, κουμπιά και checkbox καθώς και τις λειτουργίες τους
  - String selectFile(): Ανοίγει ένα παράθυρο διαλόγου για να επιλέξεις ένα αρχείο και επιστρέφει το path του.
  - appendLog(String text): Παίρνει ως όρισμα ένα String και το τυπώνει στο Text area του γραφικού.

### MainApp.java
Η βασική κλάση του προγράμματος
  - processFiles(String filePath, boolean processFullMetadata) παίρνει ως όρισμα το path που αντιστοιχεί στο metadata.csv και το αν θέλουμε να επεξεργαστούμε και τα json αρχεία. Είναι υπεύθυνη για τη βασική ροή του προγράμματος όπως αναλύθηκε παραπάνω και έχω επιλέξει να τρέχει σε ξεχωριστό Tread έτσι ώστε να μην εμποδίζεται η ταυτόχρονη λειτουργία του γραφικού..
  
### CSVParser.java
Η κλάση για την επεξεργασία του .csv αρχείου
 - List<Article> parseFile(String filePath): Αυτή η μέθοδος δέχεται ως όρισμα ένα path για ένα αρχείο, και επιστρέφει μία λίστα με όλα τα αντικείμενα τύπου Article που έχει το αρχείο αυτό.
 
### JSONParser.java
Η κλάση για την επεξεργασία του .json αρχείου
 - List<FullArticle> parseFile(Article article, String folder): Αυτή η μέθοδος δέχεται ως όρισμα ένα αντικείμενο τύπου Article καθώς το path για τον φάκελο που περιέχει το φάκελο document_parses με τα .json αρχεία, και επιστρέφει μία λίστα με όλα τα αντικείμενα τύπου FullArticles που αντιστοιχούν στο Article αυτό.
  
### DBManager.java
Η κλάση που διαχειρίζεται τη σύνδεση με τη Βάση δεδομένων και την αποστολή ερωτημάτων σε αυτήν. Έχει τις εξής μεθόδους:
 - Properties loadSettings(): Διαβάζει το αρχείο config.properties του project και επιστρέφει ένα αντικείμενο τύπου Properties με τις παραμέτρους της βάσης.
 - writeArticlesMetadata(List<Article> articles): Παίρνει ως όρισμα μία λίστα από αντικείμενα Articles και αποθηκεύει στον πίνακα Article της βάσης όλα τα πεδία τους.
   Πρώτα φτιάχνει τα παραμετροποιημένα qweries και με τα τα εκτελει όλα μαζλι στη βάση ως batch με σκοπό τη μικρότερη καθυστέρηση του προγράμματος στην επικοινωνία.
 - List<Article> getArticlesWithFiles(): Επιλέγει από τη βάση τις εγγραφές εκείνες που έχουν τιμή στα πεδία pathToPDF ή pathToPMC και επιστρέφει μία λίστα με αντικείμενα Articles με πεδία το cordID, το pathToPDF και pathToPMC της εγγραφής.
 - writeFullArticle(FullArticle fullArticle, String cordID): Αυτή η μέθοδος λαμβάνει ως ορίσματα ένα αντικείμενο τύπου FullArticle και το ID ενός Article, και αποθηκεύει στον πίνακα FullArticle της βάσης το FullArticle, και στον πίνακα της βάσης ArticleToFullArticle τη συσχέτιση του Article με ένα FullArticle.
 - int getNumberOfArticlesWithFullText(): Επιστρέφει από τη βάση τον αριθμό όλων FullArticles τα οποία έχουν πλήρες κείμενο.
 - int getNumberOfAllArticles(): Επιστρέφει από τη βάση τον αριθμό όλων των Articles.
 - ArrayList<String> getDatasetForAuthors(): Επιστρέφει από τη βάση σε πίνακα τους συγγραφείς όλων των Articles.
 - HashMap<Integer, Integer> getDatasetForPublishYear(): Επιστρέφει από τη βάση τον αριθμό των Articles ανά έτος και το έτος που δημοσιεύτηκαν.
 - close(): Κλείνει τη σύνδεση στη βάση
 
## Βιβλιοθήκες που χρησιμοποιήθηκαν
 - opencsv-5.2.jar: Για την μετατροπή των εγγραφών του .csv αρχείου σε αντικείμενα.
 - gson-2.8.6.jar: Για την μετατροπή των αρχείων JSON σε αντικείμενα.
 - jfreechart-1.0.19.jar: Για την δημιουργία στατιστικών γραφηματων.
 - mysql-connector-java-8.0.21.jar: Για τη σύνδεση στη βάση και την εκτέλση queries.
 
## Παραδοχές 
* Στο συγκεκριμένο dataset υπάρχουν εγγραφές με το ίδιο cord_uid. Δεδομένου πως οι εγγραφές αυτές αναφέρονται στο ίδιο άρθρο που μπορεί να προέρχεται από διαφορετικές πηγές (όπως αναφέρεται στο link https://github.com/allenai/cord19/blob/master/README.md) θεωρώ το cord_uid ως μοναδικό και κρατώ τη πρώτη(πιο πρόσφατη) εγγραφή.
* Θεωρώ πως τα PMC JSONs είναι καλύτερα από PDF JSONs (όπως αναφέρεται στο link https://github.com/allenai/cord19/blob/master/README.md) και για αυτό όπου υπάρχει PMC Json επεξεργάζομαι αυτό και όχι το PDF.
* Στο συγκεκριμένο dataset υπάρχουν εγγραφές με διαφορετικό cord_uid αλλά με ίδιο sha το οποίο αντιστοιχεί και σε ίδια json files όπου υπάρχουν. Δεδομένου πως οι εγγραφές αυτές αναφέρονται στο ίδιο άρθρο που από λάθος έχει περάσει πολλαπλές φορές στο dataset (όπως αναφέρεται στο link https://github.com/allenai/cord19/blob/master/README.md) δεν επεξεργάζομαι δύο φορές το ίδιο json και κρατώ την πρώτη (πιο πρόσφατη) εγγραφή.

## Οδηγίες εφαρμογής
Καλώς ήρθατε στην εφαρμογή που αφορά τα άρθρα του Cord-19!

Επιλέξτε απο το μενού Ρυθμίσεις --> Τρέχουσες Ρυθμίσεις για να δείτε τις τρέχουσες ρυθμίσεις για τη βάση σας.
Αν ρυθμίσεις αυτές δεν είναι σωστές ή αν μπαίνετε πρώτη φορά στην εφαρμογή επιλέξτε Αλλαγή Ρυθμίσεων.

Επιλέξτε μέσα από το παράθυρο που σας άνοιξε το αρχείο με τις ρυθμίσεις της βάσης που αποθηκευτούν τα δεδομένα. 
Το αρχείο θα πρέπει ναέχει φτιαχτεί σύμφωνα με το πρώτυπο που υπάρχει στον φάκελο της εφαρμογής.

Πατήστε το κουμπί "Επιλογή CSV Αρχείου..." για να επιλέξετε το αρχείο μεταδεδομένων που θέλετε.
Επιλέξτε το "Θέλω πλήρη μεταδεδομένα" αν εκτός από το αρχείο μεταδεδομένων θέλετε να αντλήσετε δεδομένα και από τα.json αρχεια.
Όταν είστε έτοιμοι πατήστε "Εκτέλεση".

Στατιστικά
-> Άρθρα με πλήρες κείμενο: Θα εμφανιστεί ένα διάγραμμα πίτα με το ποσοστό των άρθρων στη βάση για τα οποία υπάρχει πλήρες κείμενο.
-> Άρθρα ανά έτος έκδοσης: Θα εμφανιστεί ένα ΧΥ διάγραμμα με τον αριθμό των άρθρων που εκδόθηκαν ανά έτος.
-> Συγγραφείς ανά άρθρο: Θα εμφανιστεί ένα διάγραμμα με μπάρες με τον αριθμό των άρθρων που υπάρχουν στη βάση ανάλογα με τον αριθμό των συγγραφέων που έχουν.

ΠΡΟΣΟΧΗ!!!
Πρέπει να βεβαιωθείτε πως το αρχείο μεταδεδομένων .csv δεν περιέχει \"" και πως ο αποσυμπιεσμένος φάκελος document_parses βρίσκεται στον ίδιο φάκελο με το .csv αρχείο.
