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

        int operation = 7;
        int id = 149;

        switch(operation){
            case 0:
                test1.addStatement("INSERT INTO users VALUES(" + 99 + ",'user" + 99 + "','login" + 99 + "','pass" + 99 + "');");
                test2.addStatement("INSERT INTO users2 VALUES(" + 99 + ",'user" + 99 + "','login" + 99 + "','pass" + 99 + "');");
                break;
            case 1:
                for (int i = 2; i < 6; i++) {
                    test1.addStatement("INSERT INTO users VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
                    test2.addStatement("INSERT INTO users2 VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
                }
                testw.addStatement("add?id="+id+"&name=Shrek&address=Zasiedmiogrod");
                break;
            case 2:
                for (int i = 2; i < 6; i++) {
                    test1.addStatement("DELETE FROM users WHERE ID = " + i + ";");
                    test2.addStatement("DELETE FROM users2 WHERE ID = " + i + ";");
                }
                testw.addStatement("delete?id="+id);
                break;
            case 3:
                for (int i = 2; i < 6; i++) {
                    test1.addStatement("UPDATE users SET name = 'user" + 6 + "' WHERE ID = '" + i + "';");
                    test2.addStatement("UPDATE users2 SET user2 = 'user" + 6 + "' WHERE ID = '" + i + "';");
                }
                testw.addStatement("update?id="+id+"&name=Fiona&address=Siedmiogrod");
                break;
            case 4:
                for (int i = 2; i < 6; i++) {
                    test1.addStatement("UPDATE users SET name = 'user" + 6 + "' WHERE ID = '" + i + "';");
                    test2.addStatement("DELETE FROM users2 WHERE ID = " + i + ";");
                }
                testw.addStatement("add?id=18&name=Piotrus&address=Nibylandia");
                for (int i = 10; i < 20; i++) {
                    testw.addStatement("add?id="+i+"&name=Piotrus&address=Nibylandia");
                }
                break;
            case 5:
                for (int i = 2; i < 6; i++) {
                    test1.addStatement("UPDATE users SET name = 'user" + 6 + "' WHERE ID = '" + i + "';");
                    test2.addStatement("DELETE FROM users2 WHERE ID = " + i + ";");
                }
                testw.addStatement("add?id="+id+"&name=Shrek&address=Polska");
                break;
            case 6:
                for (int i = 2; i < 6; i++)
                    test1.addStatement("INSERT INTO users VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
                for (int i = 4; i < 6; i++)
                    test1.addStatement("UPDATE users SET name = 'user" + 11 + "' WHERE ID = '" + i + "';");
                testw.addStatement("add?id="+id+"&name=Shrek&address=Polska");
                break;
            case 7:
                test2.addStatement("INSERT INTO users2 VALUES(" + 25 + ",'user" + 25 + "','login" + 25 + "','pass" + 25 + "');");
                test2.addStatement("INSERT INTO users2 VALUES(" + 35 + ",'user" + 35 + "','login" + 35 + "','pass" + 35 + "');");
                test1.addStatement("INSERT INTO users VALUES(" + 29 + ",'user" + 29 + "','login" + 29 + "','pass" + 29 + "');");
                for (int i = 25; i < 32; i++)
                    test1.addStatement("INSERT INTO users VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
                testw.addStatement("add?id="+id+"&name=Shrek&address=Polska");
                break;
        }


        TransactionMonitor transactionMonitor = new TransactionMonitor();
        transactionMonitor.addDataBase(test1);
        transactionMonitor.addDataBase(test2);
        transactionMonitor.addWebService(testw);
        transactionMonitor.startTransaction();
    }
}
