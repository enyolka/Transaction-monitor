package main;


import monitor.DataBase;
import monitor.TransactionMonitor;
import monitor.WebService;

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


        DataBase test1 = new DataBase("danwarlo", "ESibZTBDZSXKKrPy", "jdbc:mysql://mysql.agh.edu.pl:3306/danwarlo");
        DataBase test2 = new DataBase("danwarl2", "u8YNn32Zr5F8VLUx", "jdbc:mysql://mysql.agh.edu.pl:3306/danwarl2");

        WebService testw = new WebService("http://127.0.0.1:8080/");

        int test = 2;

        if (test == 1) {
            for (int i = 2; i < 6; i++) {
                test1.addStatement("INSERT INTO users VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
                test2.addStatement("INSERT INTO users2 VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
            }
        } else if (test == 2) {
            for (int i = 2; i < 6; i++) {
                test1.addStatement("DELETE FROM users WHERE ID = " + i + ";");
                test2.addStatement("DELETE FROM users2 WHERE ID = " + i + ";");
            }
        } else if (test == 3) {
            for (int i = 2; i < 5; i++) {
                test1.addStatement("UPDATE users SET name = 'user" + 6 + "' WHERE ID = '" + i + "';");
                test2.addStatement("UPDATE users2 SET user2 = 'user" + 6 + "' WHERE ID = '" + i + "';");
            }
        } else if (test == 4) {
            test1.addStatement("INSERT INTO users VALUES(" + 99 + ",'user" + 99 + "','login" + 99 + "','pass" + 99 + "');");
            test2.addStatement("INSERT INTO users2 VALUES(" + 99 + ",'user" + 99 + "','login" + 99 + "','pass" + 99 + "');");
        }

        testw.addStatement("add?id=67&name=Shrek&address=Polska");

        TransactionMonitor transactionMonitor = new TransactionMonitor();
        transactionMonitor.addDataBase(test1);
        transactionMonitor.addDataBase(test2);
        transactionMonitor.addWebService(testw);
        transactionMonitor.startTransaction();
    }
}
