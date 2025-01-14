import java.util.*;

public class FinanceManager {
    
}

//transaction class, adding the attributes
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
        return String.format("%s - %s: CA$%,.2f for %s on %s", type, category, amount, name, date);
    }
}


