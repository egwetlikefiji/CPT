import java.util.*;

/**
 * @author Erik Georgiev
 * @date 01/16/2025
 * money manager 
 */

/**
 * transaction class, adding the attributes to a thingy and creating a string output
 */
class Transaction {
    protected double amount;
    protected String name;
    protected String category;
    protected String type;
    protected String date;

    public Transaction(double amount, String name, String category, String type, String date) {
        this.amount = amount;
        this.name = name;
        this.category = category;
        this.type = type;
        this.date = date;
    }

    public String toString() {
        // https://www.w3schools.com/java/ref_string_format.asp
        return String.format("%s - %s: CA$%,.2f for %s on %s", type, category, amount, name, date);
    }
}

/**
 * recurring transaction class. extends the transaction class. used for transactions like rent (e.g. monthly), 
 * paychecks (e.g. biweekly), etc.
 */
class RecurringTransaction extends Transaction {
    protected String frequency;

    public RecurringTransaction(double amount, String name, String category, String type, String date, String frequency) {
        super(amount, name, category, type, date);
        this.frequency = frequency;
    }

    public String toString() {
        // added the [Recurring: frequency] to the end of the string from transaction class
        return super.toString() + String.format(" [Recurring: %s]", frequency);
    }
}

/**
 * frequent transaction class. extends the transaction class. used for transactions that occur often, allowing you to 
 * use a shortcut instead of retyping all of the values over and over again
 */
class FrequentTransaction extends Transaction {
    protected String shortcut;

    /**
     * adds the values to the shortcut
     * @param amount
     * @param name
     * @param category
     * @param type
     * @param date
     * @param shortcut
     */
    public FrequentTransaction(double amount, String name, String category, String type, String date, String shortcut) {
        super(amount, name, category, type, date);

        /**
         * the "|" is used for seperation, i will use .split to seperate the different values into parts, this will allow
         * me to use the shortcuts properly
         */
        this.shortcut = String.format("%s|%s|%s|%s|%s", amount, name, category, type, date);
    }

    public String toString() {
        return super.toString() + String.format(" [Shortcut: %s]", shortcut);
    }
    /**
     * getter
     * @return
     */
    public String getShortcut() {
        return shortcut;
    }
}

/**
 * template class, shows the user a template to use as a goal, i dont really know how to "use" this one
 */
class Template {
    protected String templateName;
    protected double wantsPercentage;
    protected double needsPercentage;
    protected double savingsPercentage;

    public Template(String templateName, double wantsPercentage, double needsPercentage, double savingsPercentage) {
        this.templateName = templateName;
        this.wantsPercentage = wantsPercentage;
        this.needsPercentage = needsPercentage;
        this.savingsPercentage = savingsPercentage;
    }

    /**
     * prints out the template based on the values
     * @param manager
     */
    public void applyRule(prompts manager) {
        System.out.printf("Template '%s' applied: Wants %.2f%%, Needs %.2f%%, Savings %.2f%%\n",
            templateName, wantsPercentage, needsPercentage, savingsPercentage);
    }
}

/**
 * prompts class, adds the transaction to the transaction, recurring transaction, and templates arraylist, allowing you to see it 
 * later. i would like to make it so that it creates a file, and it writes to that file, then reads it when you run the code so 
 * that your transactions save.
 */
class prompts {
    protected List<Transaction> transactions;
    protected List<RecurringTransaction> recurringTransactions;
    protected Map<String, FrequentTransaction> shortcuts;
    protected List<Template> templates;

    public prompts() {
        transactions = new ArrayList<>();
        recurringTransactions = new ArrayList<>();
        shortcuts = new HashMap<>();
        templates = new ArrayList<>();
    }

    /**
     * allows the user to add a normal transaction to the transactions array
     * @param transaction
     */
    public void addTransaction(Transaction transaction) {
        // add the transactions and send a confirmation
        transactions.add(transaction);
        System.out.println("Transaction added: " + transaction);
    }

    /**
     * allows the user to add a recurring transaction to the transactions array
     * @param transaction
     */
    public void addRecurringTransaction(RecurringTransaction transaction) {
        // add the recurring transactions and send a confirmation
        recurringTransactions.add(transaction);
        System.out.println("Recurring transaction added: " + transaction);
    }

    /**
     * allows the user to add a frequent transaction to the transactions array
     * @param transaction
     */
    public void addFrequentTransaction(FrequentTransaction transaction) {
        // add the shortcut and send a confirmation
        shortcuts.put(transaction.getShortcut(), transaction);
        System.out.println("Frequent transaction added: " + transaction);
    }

    /**
     * seperates the values stores in the shortcut to apply them.
     * @param shortcut
     */
    public void useFrequentTransaction(String shortcut) {

        FrequentTransaction ft = shortcuts.get(shortcut);
        if (ft != null) {
            // to seperate the values of the shortcut
            String[] parts = ft.getShortcut().split("|");
            // add the seperate parts into the transaction
            double amount = Double.parseDouble(parts[0]);
            String name = parts[1];
            String category = parts[2];
            String type = parts[3];
            String date = parts[4];
            addTransaction(new Transaction(amount, name, category, type, date));
        } else {
            // if you dont have a shortcut with that name, it will tell you
            System.out.println("Shortcut not found: " + shortcut);
        }
    }
    /**
     * prints out all transactions in the transactions array
     */
    public void viewTransactions() {
        System.out.println("All Transactions:");
        // for every transaction in transaction print it out
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }
    /**
     * adds the template to the templates array
     * @param template
     */
    public void applyTemplate(Template template) {
        templates.add(template);
        template.applyRule(this);
    }
}

public class FinanceManager {
    public static void main(String[] args) {
        prompts manager = new prompts();
        Scanner scanner = new Scanner(System.in);
        /**
         * i used ChatGPT to do this part becuase i know how to do it but it would be a lot of repetetive typing
         */
        boolean running = true;

        while (running) {
            System.out.println("\nChoose an action by number:");
            System.out.println("1: Add Transaction");
            System.out.println("2: Add Recurring Transaction");
            System.out.println("3: Add Frequent Transaction");
            System.out.println("4: Use Shortcut");
            System.out.println("5: View Transactions");
            System.out.println("6: Apply Template");
            System.out.println("7: Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
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
                    System.out.print("Enter amount: ");
                    amount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter name: ");
                    name = scanner.nextLine();
                    System.out.print("Enter category: ");
                    category = scanner.nextLine();
                    System.out.print("Enter type (Income/Expense): ");
                    type = scanner.nextLine();
                    System.out.print("Enter date (YYYY-MM-DD): ");
                    date = scanner.nextLine();
                    System.out.print("Enter frequency (Daily/Weekly/Monthly): ");
                    String frequency = scanner.nextLine();
                    manager.addRecurringTransaction(new RecurringTransaction(amount, name, category, type, date, frequency));
                    break;
                case 3:
                    System.out.print("Enter amount: ");
                    amount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter name: ");
                    name = scanner.nextLine();
                    System.out.print("Enter category: ");
                    category = scanner.nextLine();
                    System.out.print("Enter type (Income/Expense): ");
                    type = scanner.nextLine();
                    System.out.print("Enter date (YYYY-MM-DD): ");
                    date = scanner.nextLine();
                    System.out.print("Enter shortcut name: ");
                    String shortcut = scanner.nextLine();
                    manager.addFrequentTransaction(new FrequentTransaction(amount, name, category, type, date, shortcut));
                    break;
                case 4:
                    System.out.print("Enter shortcut name: ");
                    shortcut = scanner.nextLine();
                    manager.useFrequentTransaction(shortcut);
                    break;
                case 5:
                    manager.viewTransactions();
                    break;
                case 6:
                    System.out.print("Enter template name: ");
                    String templateName = scanner.nextLine();
                    System.out.print("Enter wants percentage: ");
                    double wantsPercentage = scanner.nextDouble();
                    System.out.print("Enter needs percentage: ");
                    double needsPercentage = scanner.nextDouble();
                    System.out.print("Enter savings percentage: ");
                    double savingsPercentage = scanner.nextDouble();
                    scanner.nextLine();
                    manager.applyTemplate(new Template(templateName, wantsPercentage, needsPercentage, savingsPercentage));
                    break;
                case 7:
                    running = false;
                    System.out.println("Exiting program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }

        scanner.close();
    }
}
