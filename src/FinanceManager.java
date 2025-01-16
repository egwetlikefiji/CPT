import java.util.*;

/**
 * @author Erik Georgiev
 * @date 01/16/2025
 * money manager 
 */

 public class FinanceManager {
    
}

//transaction class, adding the attributes to one
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

class RecurringTransaction extends Transaction {
    private String frequency;

    public RecurringTransaction(double amount, String name, String category, String type, String date, String frequency) {
        super(amount, name, category, type, date);
        this.frequency = frequency;
    }

    public String toString() {
        return super.toString() + String.format(" [Recurring: %s]", frequency);
    }
}

class FrequentTransaction extends Transaction {
    private String shortcut;

    public FrequentTransaction(double amount, String name, String category, String type, String date, String shortcut) {
        super(amount, name, category, type, date);
        this.shortcut = String.format("%s|%s|%s|%s|%s", amount, name, category, type, date);
    }

    public String toString() {
        return super.toString() + String.format(" [Shortcut: %s]", shortcut);
    }

    public String getShortcut() {
        return shortcut;
    }
}


