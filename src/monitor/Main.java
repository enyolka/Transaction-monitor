package monitor;

public class Main {

    public static void main(String[] args) {
        DataBase test1 = new DataBase("danwarlo","ESibZTBDZSXKKrPy", "jdbc:mysql://mysql.agh.edu.pl:3306/danwarlo");
        DataBase test2 = new DataBase("danwarl2","u8YNn32Zr5F8VLUx", "jdbc:mysql://mysql.agh.edu.pl:3306/danwarl2");

        WebService testw = new WebService("url");

        //URL urlForGetRequest = new URL("http://localhost:8080/");
        //HttpURLConnection myConnection = (HttpURLConnection) urlForGetRequest.openConnection();

        int test =  ;

        if (test == 1) {
            for (int i = 2; i < 6; i++) {
                test1.addStatement("INSERT INTO users VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
                test2.addStatement("INSERT INTO users2 VALUES(" + i + ",'user" + i + "','login" + i + "','pass" + i + "');");
            }
        }
        else if(test == 2){
            for (int i = 2; i < 6; i++) {
                test1.addStatement("DELETE FROM users WHERE ID = " + i + ";");
                test2.addStatement("DELETE FROM users2 WHERE ID = " + i + ";");
            }
        }
        else if(test == 3){
            for (int i = 2; i < 5; i++) {
                test1.addStatement("UPDATE users SET name = 'user" + 5 +"' WHERE ID = " + i + ";");
                test2.addStatement("UPDATE users2 SET user2 = 'user" + 5 +"' WHERE ID = " + i + ";");
            }
        }

        testw.addStatement("polecenie?name=WART");



        try {
            TransactionMonitor transactionMonitor = new TransactionMonitor();
            transactionMonitor.addDataBase(test1);
            transactionMonitor.addDataBase(test2);
            transactionMonitor.startTransaction();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        System.out.println("Pozdrawiam wszystko OK!");
    }


}
