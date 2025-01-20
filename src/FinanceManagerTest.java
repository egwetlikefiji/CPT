import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// https://www.tutorialspoint.com/java/java_bytearrayoutputstream.htm
// https://stackoverflow.com/questions/8751553/how-to-write-a-unit-test
// https://docs.oracle.com/javase/8/docs/api/java/io/PrintStream.html
/**
 * unit tests for the FinanceManager program
 */
public class FinanceManagerTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalSystemOut = System.out;
    
    // sets up a test environment so that it doesnt mess with anything
    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    // cleans up the test environment
    @AfterEach
    public void tearDown() {
        System.setOut(originalSystemOut);
    }
    /**
     * test adding a one-time transaction
     */
    @Test
    void testAddTransaction() {
        // set
        prompts manager = new prompts();
        manager.addTransaction(new Transaction(4000, "Rent", "Housing", "Expense", "2025-01-31"));

        // validate
        assertTrue(outputStream.toString().contains("Transaction added: Expense - Housing: CA$4,000.00 for Rent on 2025-01-31"));
    }

    /**
     * test adding a frequent transactions (shortcut)
     */
    @Test
    void testAddFrequentTransaction() {
        // set
        prompts manager = new prompts();
        manager.addFrequentTransaction(new FrequentTransaction(6, "Coffee", "Food", "Expense", "c1"));

        // validate
        assertTrue(outputStream.toString().contains("Frequent transaction added: Shortcut 'c1': Expense - Food: CA$6.00 for Coffee"));
    }

    /**
     * test using a frequent transaction shortcut
     */
    @Test
    void testUseFrequentTransaction() {
        // set
        prompts manager = new prompts();
        manager.addFrequentTransaction(new FrequentTransaction(6, "Coffee", "Food", "Expense", "c1"));

        // use
        manager.useFrequentTransaction("c1", "2025-01-19");

        // validate
        assertTrue(outputStream.toString().contains("Transaction added: Expense - Food: CA$6.00 for Coffee on 2025-01-19"));
    }

    /**
     * test viewing transactions
     */
    @Test
    void testViewTransactions() throws IOException {
        // create files
        File transactionsFile = new File("transactions.txt");
        File shortcutsFile = new File("shortcuts.txt");
        
        // clear files
        transactionsFile.delete();
        shortcutsFile.delete();

        // set
        prompts manager = new prompts();
        manager.addTransaction(new Transaction(4000, "Rent", "Housing", "Expense", "2025-01-31"));
        manager.addFrequentTransaction(new FrequentTransaction(6, "Coffee", "Food", "Expense", "c1"));
        manager.useFrequentTransaction("c1", "2025-01-19");

        // call
        manager.viewTransactions();

        // validate
        assertTrue(outputStream.toString().contains("All Transactions:"));
        assertTrue(outputStream.toString().contains("Expense - Housing: CA$4,000.00 for Rent on 2025-01-31"));
        assertTrue(outputStream.toString().contains("Expense - Food: CA$6.00 for Coffee on 2025-01-19"));
    }

    /**
     * test clearing all transacations
     */
    @Test
    void testClearAllTransactions() {
        // set
        prompts manager = new prompts();
        manager.addTransaction(new Transaction(4000, "Rent", "Housing", "Expense", "2025-01-31"));

        // user input
        // https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        String simulatedUserInput = "yes i do\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        // clear transactions
        manager.clearAllTransactions();

        // validate
        assertTrue(outputStream.toString().contains("All transactions and shortcuts cleared."));
    }
}
