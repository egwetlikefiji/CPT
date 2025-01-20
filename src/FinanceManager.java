import java.util.*;
import java.io.*;

/**
 * @author Erik Georgiev
 * @date 01/17/2025
 * money manager 
 */

/**
 * gives the user options tho select and allows the use to add, view, remove, and use frequent transactions with the 
 * ability to save data to a file, aswell as read it
 */
public class FinanceManager {
    /**
     * initializes the manager
     */
    public FinanceManager() {
        // Default constructor
    }
    /**
     * the main method for the program.
     * @param args not used1
     */
    public static void main(String[] args) {
        // i used ChatGPT to do this part becuase i know how to do it but it would take up a lot of time and it would
        // be a lot of repetetive typing. i added the comments myself
        prompts manager = new prompts();
        // load frequent transactions right away so that they can be used
        manager.loadFrequentTransactions();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            // prints out the options for the user to select
            System.out.println("\nChoose an action by number:");
            System.out.println("1: Add Transaction");
            System.out.println("2: Add Frequent Transaction");
            System.out.println("3: Use Frequent Transaction");
            System.out.println("4: View Transactions");
            System.out.println("5: Clear All Transactions");
            System.out.println("6: Exit");

            // adds the input into a variable to use for the switch
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    // add a one-time transaction
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter type (Income/Expense): ");
                    String type = scanner.nextLine();
                    System.out.print("Enter date (YYYY-MM-DD): ");
                    String date = scanner.nextLine();
                    manager.addTransaction(new Transaction(amount, name, category, type, date));
                    break;
                case 2:
                    // add a frequent transaction to a shortcut
                    // notice there is no date, because the date will be added when the shortcut is used
                    System.out.print("Enter amount: ");
                    amount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter name: ");
                    name = scanner.nextLine();
                    System.out.print("Enter category: ");
                    category = scanner.nextLine();
                    System.out.print("Enter type (Income/Expense): ");
                    type = scanner.nextLine();
                    System.out.print("Enter shortcut name: ");
                    String shortcut = scanner.nextLine();
                    manager.addFrequentTransaction(new FrequentTransaction(amount, name, category, type, shortcut));
                    break;
                case 3:
                    // use a frequent transaction shortcut 
                    // here is where the date is added
                    System.out.print("Enter shortcut name: ");
                    shortcut = scanner.nextLine();
                    System.out.print("Enter date (YYYY-MM-DD): ");
                    date = scanner.nextLine();
                    manager.useFrequentTransaction(shortcut, date);
                    break;
                case 4:
                    // view all transactions
                    manager.viewTransactions();
                    break;
                case 5:
                    // clear all saved transactions
                    manager.clearAllTransactions();
                    break;
                case 6:
                    // exit the program
                    running = false;
                    System.out.println("Exiting program. Goodbye!");
                    break;
                default:
                    // incase the user inputs something other than the numbers 1-6
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}

/**
 * represents a single transaction. a transaction consists of an amount, a name, a category, a type, and a date
 */
class Transaction {
    protected double amount;
    protected String name;
    protected String category;
    protected String type;
    protected String date;

    /**
     * constructs a new transaction by adding the values to the attributes
     * 
     * @param amount     the transaction amount (in CA$)
     * @param name       the name of the transaction
     * @param category   the category of the transaction
     * @param type       the type (Income or Expense) 
     * @param date       the date which the transaction occured
     */
    public Transaction(double amount, String name, String category, String type, String date) {
        this.amount = amount;
        this.name = name;
        this.category = category;
        this.type = type;
        this.date = date;
    }

    /**
     * @return a string representation of the transaction
     */
    public String toString() {
        // https://www.w3schools.com/java/ref_string_format.asp
        return String.format("%s - %s: CA$%,.2f for %s on %s", type, category, amount, name, date);
    }
}

/**
 * represents a frequently used transaction. this allows users to define shortcuts for transactions that occur regularly to
 * make it so that the user does not need to re-type a transaction a lot of times
 */
class FrequentTransaction extends Transaction {
    protected String shortcut;

    /**
     * constructs a frequent transaction by adding the values to the attributes. this allows the user to use the shortcut
     * 
     * @param amount     the transaction amount (in CA$)
     * @param name       the name of the transaction
     * @param category   the category of the transaction
     * @param type       the type (Income or Expense) 
     * @param shortcut   the name of the shortcut
     */
    public FrequentTransaction(double amount, String name, String category, String type, String shortcut) {
        // No date needed for frequent transactions. it will be added when used
        super(amount, name, category, type, null); 
        this.shortcut = shortcut;
    }

     /**
     * takes the values and adds a date in order to add to add a transaction
     *
     * @param date   the date of the transaction
     * @return       return a new transaction
     */
    public Transaction toTransaction(String date) {
        return new Transaction(amount, name, category, type, date);
    }

    /**
     * @return a string representation of the frequent transaction
     */
    public String toString() {
        return String.format("Shortcut '%s': %s - %s: CA$%,.2f for %s", shortcut, type, category, amount, name);
    }

    /**
     * @return the shortcut name for this frequent transaction
     */
    public String getShortcut() {
        return shortcut;
    }

    /**
     * returns a string representation to store in a file
     */
    public String toFileString() {
        return String.format("%s,%s,%s,%s,%s", shortcut, type, category, amount, name);
    }

    /**
     * creates a frequent transaction object from the file string
     */
    public static FrequentTransaction fromFileString(String fileString) {
        // splits the string into 5 parts (0-4) to use the values for the toString
        String[] parts = fileString.split(",");
        return new FrequentTransaction(
            Double.parseDouble(parts[3]),
            parts[4],
            parts[2],
            parts[1],
            parts[0]
        );
    }
}

/**
 * handles user interaction and manages transactions. this class stores, retrieves, and deletes transactions from a file.
 */
class prompts {
    // https://www.tutorialspoint.com/java/java_files_io.htm
    // https://docs.oracle.com/javase/tutorial/essential/io/file.html

    // creates 2 files, one to store transactions, and another to store shortcuts. this allows the user to use shortcuts even
    // after they stop running the code and start it again. also allows the user to view their old transactions that they didnt
    // add in that instance
    protected static final String TRANSACTIONS_FILE = "transactions.txt";
    protected static final String SHORTCUTS_FILE = "shortcuts.txt";
    // List to store frequent transactions
    protected List<FrequentTransaction> shortcuts = new ArrayList<>();

     /**
     * adds a new transaction to the transactions file
     * 
     * @param transaction the transaction to add
     */
    // https://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java
    public void addTransaction(Transaction transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE, true))) {
            writer.write(transaction.toString());
            writer.newLine();
            System.out.println("Transaction added: " + transaction);
        } catch (IOException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }

    /**
     * adds a frequent transaction shortut and saves it to the shortcuts file
     *
     * @param frequentTransaction the frequent transaction to add
     */
    public void addFrequentTransaction(FrequentTransaction frequentTransaction) {
        // add to the array list
        shortcuts.add(frequentTransaction);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SHORTCUTS_FILE, true))) {
            writer.write(frequentTransaction.toFileString());
            writer.newLine();
            System.out.println("Frequent transaction added: " + frequentTransaction);
        } catch (IOException e) {
            System.out.println("Error saving frequent transaction: " + e.getMessage());
        }
    }

     /**
     * uses a frequent transaction shortcut to create a new transaction
     *
     * @param shortcut   the name of the shortcut
     * @param date       the date of the transaction
     */
    public void useFrequentTransaction(String shortcut, String date) {
        // search for the FrequentTransaction by shortcut
        for (FrequentTransaction ft : shortcuts) {
            if (ft.getShortcut().equals(shortcut)) {
                // if found, create a new Transaction and add it
                Transaction transaction = ft.toTransaction(date);
                addTransaction(transaction);
                // exit after finding the transaction
                return; 
            }
        }
        System.out.println("Shortcut not found: " + shortcut);
    }

    /**
     * loads frequent transactions from the shortcuts file. allowing it to be used
     */
    public void loadFrequentTransactions() {
        File file = new File(SHORTCUTS_FILE);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                FrequentTransaction ft = FrequentTransaction.fromFileString(line);
                // add to the ArrayList
                shortcuts.add(ft);  
            }
        } catch (IOException e) {
            System.out.println("Error loading frequent transactions: " + e.getMessage());
        }
    }
    /**
     * displays all transactions saved in the transactions file.
     */
    public void viewTransactions() {
        File file = new File(TRANSACTIONS_FILE);
        if (!file.exists()) {
            System.out.println("No transactions found.");
            return;
        }
        System.out.println("\nAll Transactions:");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading transactions: " + e.getMessage());
        }
    }

    /**
     * deletes all transactions and shortcuts
     */
    // closing scanner causes errors, so i suppressed the warning.
    @SuppressWarnings("resource")
    public void clearAllTransactions() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nAre you sure you want to clear all transactions and shortcuts? Type 'yes i do' to confirm:");
        String confirmation = scanner.nextLine();
        if ("yes i do".equals(confirmation)) {
            File transactionsFile = new File(TRANSACTIONS_FILE);
            File shortcutsFile = new File(SHORTCUTS_FILE);

            // deletes the files and clears the arraylist
            boolean transactionsCleared = !transactionsFile.exists() || transactionsFile.delete();
            boolean shortcutsCleared = !shortcutsFile.exists() || shortcutsFile.delete();

            if (transactionsCleared && shortcutsCleared) {
                shortcuts.clear();
                System.out.println("All transactions and shortcuts cleared.");
            } else {
                System.out.println("Error clearing files.");
            }
        } else {
            System.out.println("Transactions and shortcuts clear canceled.");
        }
    }
}