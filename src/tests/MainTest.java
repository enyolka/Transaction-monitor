package tests;

import monitor.DataBase;
import monitor.TransactionMonitor;
import monitor.WebService;
import java.util.Scanner;

class MainTest {
    TransactionMonitor transactionMonitor;
    DataBase db1;
    DataBase db2;
    DataBase db_failed;
    WebService ws;
    WebService ws_failed;

    void setUp() {
        this.transactionMonitor = new TransactionMonitor();
        this.db1 = new DataBase("danwarlo", "ESibZTBDZSXKKrPy", "jdbc:mysql://mysql.agh.edu.pl:3306/danwarlo");
        this.db2 = new DataBase("danwarl2", "u8YNn32Zr5F8VLUx", "jdbc:mysql://mysql.agh.edu.pl:3306/danwarl2");
        this.db_failed = new DataBase("danwarl2", "u8YNn32Zr5F8VLUx", "jdbc:mysql://mysql.agh.edu.pl:3306/danwarl2");
        this.ws = new WebService("http://127.0.0.1:8080/");
        this.ws_failed = new WebService("http://127.0.0.1:8090/");
    }

    void testSuccessful() {
        this.setUp();
        this.transactionMonitor.addDataBase(this.db1);
        this.transactionMonitor.addDataBase(this.db2);
        this.transactionMonitor.addWebService(this.ws);

        for (int i = 1; i < 10; i++) {
            this.db1.addStatement("INSERT INTO users VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
            this.db2.addStatement("INSERT INTO users2 VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
        }
        for (int i = 5; i < 10; i++) {
            this.ws.addStatement("add?id=" + i + "&name=Shrek&address=Zasiedmiogrod");
        }

        this.transactionMonitor.startTransaction();
    }

    void testEmptyStatementDataBase() {
        this.setUp();
        this.transactionMonitor.addDataBase(this.db1);
        this.transactionMonitor.addDataBase(this.db2);
        this.transactionMonitor.addWebService(this.ws);

        for (int i = 10; i < 20; i++) {
            this.db1.addStatement("INSERT INTO users VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
        }
        for (int i = 10; i < 15; i++) {
            this.ws.addStatement("add?id=" + i + "&name=Shrek&address=Zasiedmiogrod");
        }

        this.transactionMonitor.startTransaction();
    }

    void testEmptyStatementWebService() {
        this.setUp();
        this.transactionMonitor.addDataBase(this.db1);
        this.transactionMonitor.addDataBase(this.db2);
        this.transactionMonitor.addWebService(this.ws);

        for (int i = 100; i < 105; i++) {
            this.db1.addStatement("INSERT INTO users VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
            this.db2.addStatement("INSERT INTO users2 VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
        }

        this.transactionMonitor.startTransaction();
    }

    void testReverseOrder() {
        this.setUp();
        this.transactionMonitor.addWebService(this.ws);
        this.transactionMonitor.addDataBase(this.db1);
        this.transactionMonitor.addDataBase(this.db2);
        for (int i = 5; i < 7; i++) {
            this.ws.addStatement("delete?id=" + i);
        }
        //Same Key for DataBase 1 and 2
        for (int i = 1; i < 15; i++) {
            this.db1.addStatement("INSERT INTO users VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
            this.db2.addStatement("INSERT INTO users2 VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
        }
        transactionMonitor.startTransaction();
    }

    void testWrongDataBasePassword() {
        this.setUp();
        this.transactionMonitor.addDataBase(this.db1);
        this.transactionMonitor.addDataBase(this.db_failed);
        this.transactionMonitor.addWebService(this.ws);

        for (int i = 1; i < 10; i++) {
            this.db1.addStatement("UPDATE users SET name = 'user_change" + i + "'WHERE ID = '" + i + "';");
            this.db_failed.addStatement("UPDATE users2 SET user2 = 'user" + 6 + "' WHERE ID '" + i + "';");
        }
        for (int i = 5; i < 10; i++) {
            this.ws.addStatement("update?id=" + i + "&name=Shrek_change&address=Zasiedmiogrod_change");
        }

        transactionMonitor.startTransaction();
    }

    void testWrongWebServiceURL() {
        this.setUp();
        this.transactionMonitor.addDataBase(this.db1);
        this.transactionMonitor.addDataBase(this.db2);
        this.transactionMonitor.addWebService(this.ws_failed);

        for (int i = 20; i < 25; i++) {
            this.db1.addStatement("INSERT INTO users VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
            this.db2.addStatement("INSERT INTO users2 VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
        }
        //Wrong WebService URL
        this.ws_failed.addStatement("add?id=" + 100 + "&name=Shrek&address=Zasiedmiogrod");

        transactionMonitor.startTransaction();
    }

    void testSameKeyDataBase() {
        this.setUp();
        this.transactionMonitor.addDataBase(this.db1);
        this.transactionMonitor.addDataBase(this.db2);
        this.transactionMonitor.addWebService(this.ws);

        //Same keys on DataBase 1
        for (int i = 7; i < 12; i++) {
            this.db1.addStatement("INSERT INTO users VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
        }
        for (int i = 1; i < 10; i++) {
            this.db2.addStatement("DELETE FROM users2 WHERE ID = " + i + ";");
        }
        for (int i = 25; i < 30; i++) {
            this.ws.addStatement("add?id=" + i + "&name=Shrek&address=Zasiedmiogrod");
        }

        transactionMonitor.startTransaction();
    }

    void testSameKeyWebService() {
        this.setUp();
        this.transactionMonitor.addDataBase(this.db1);
        this.transactionMonitor.addDataBase(this.db2);
        this.transactionMonitor.addWebService(this.ws);

        this.ws.addStatement("add?id=" + 120 + "&name=Shrek&address=Zasiedmiogrod");
        for (int i = 20; i < 25; i++) {
            this.db1.addStatement("INSERT INTO users VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
            this.db2.addStatement("INSERT INTO users2 VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
        }
        //Same Key on WebService
        for (int i = 110; i < 125; i++) {
            this.ws.addStatement("add?id=" + i + "&name=Shrek&address=Zasiedmiogrod");
        }

        transactionMonitor.startTransaction();
    }

    void testWrongStatementDataBase() {
        this.setUp();
        this.transactionMonitor.addDataBase(this.db1);
        this.transactionMonitor.addDataBase(this.db2);
        this.transactionMonitor.addWebService(this.ws);

        for (int i = 20; i < 25; i++) {
            this.db1.addStatement("INSERT INTO users VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
            //Wrong Statement on Database 2
            this.db2.addStatement("INSERT INTOO users2 VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
        }
        for (int i = 30; i < 35; i++) {
            this.ws.addStatement("add?id=" + i + "&name=Shrek&address=Zasiedmiogrod");
        }

        transactionMonitor.startTransaction();
    }

    public static void main(String[] args) {
        MainTest test = new MainTest();
        System.out.println("Testing successful - adding objects to databases and web service");
        test.testSuccessful();
        System.out.println("Select what you want to test: \n 1 - testEmptyStatementDataBase \n 2 - testReverseOrder \n 3 - testWrongDataBasePassword \n " +
                "4 - testWrongWebServiceURL \n 5 - testSameKeyDataBase \n 6 - testSameKeyWebService \n 7 - testWrongStatementDataBase \n " +
                "8 - testEmptyStatementWebService \n 0 - exit");
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            switch (sc.nextInt()) {
                case 0:
                    return;
                case 1:
                    System.out.println("Testing empty statement database");
                    test.testEmptyStatementDataBase();
                    break;
                case 2:
                    System.out.println("Testing reverse order");
                    test.testReverseOrder();
                    break;
                case 3:
                    System.out.println("Testing wrong database password");
                    test.testWrongDataBasePassword();
                    break;
                case 4:
                    System.out.println("Testing wrong web service URL");
                    test.testWrongWebServiceURL();
                    break;
                case 5:
                    System.out.println("Testing same key on database");
                    test.testSameKeyDataBase();
                    break;
                case 6:
                    System.out.println("Testing same key on web service");
                    test.testSameKeyWebService();
                    break;
                case 7:
                    System.out.println("Testing wrong statement on database");
                    test.testWrongStatementDataBase();
                    break;
                case 8:
                    System.out.println("Testing empty statement on web service");
                    test.testEmptyStatementWebService();
                    break;
            }
            System.out.println("Select what you want to test: \n 1 - testEmptyStatementDataBase \n 2 - testReverseOrder \n 3 - testWrongDataBasePassword \n " +
                    "4 - testWrongWebServiceURL \n 5 - testSameKeyDataBase \n 6 - testSameKeyWebService \n 7 - testWrongStatementDataBase \n " +
                    "8 - testEmptyStatementWebService \n 0 - exit");
        }
    }

}