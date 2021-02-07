package tests;



import monitor.DataBase;
import monitor.TransactionMonitor;
import monitor.WebService;

import java.util.Random;


/**
 * The type Main.
 */
public class Main {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

      MainTest test = new MainTest();
      test.setUp();
      test.testSuccessful();
    }
}
