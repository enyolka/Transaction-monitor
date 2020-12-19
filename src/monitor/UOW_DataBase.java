package monitor;

// Import required packages
import java.sql.*;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;


public class UOW_DataBase extends UOW{

     Caretaker_DB caretaker;

     DataBase dataBase;
     int status;

    final boolean autoCommit = false;
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    public UOW_DataBase ( DataBase database ) throws ClassNotFoundException {
        this.dataBase = database;
        this.caretaker = new Caretaker_DB();
        // Register JDBC driver
//        Class.forName(dataBase.getJdbcDriver());
    }

    public int getStatus (  ){return 0;}

    int startTransaction(){
        int failed = 10;
        try{
            Class.forName(dataBase.getJdbcDriver());
//            Driver myDriver = new com.mysql.jdbc.Driver();
//            DriverManager.registerDriver( myDriver );
            this.conn = DriverManager.getConnection(dataBase.getDbUrl(),dataBase.getUSER(),dataBase.getPASS());
            this.conn.setAutoCommit(autoCommit);
            stmt = this.conn.createStatement();
            reverse();
            executeStatement();

        }catch(SQLException se){
            se.printStackTrace();
            failed = -10;
        }catch(Exception e){
            e.printStackTrace();
            failed = -10;
        }
        return failed;
    }

    int finalizeTransaction(){
        // Close connection to database
        int failed = 50;
        try{
            System.out.println("FINALIZE");
//            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se2) {
            se2.printStackTrace();
            failed = -50;
        }
        return failed;
    }

    //TODO: sprawdzić czy nie trzeba executeUpdate(statement) do commit
    // oraz oddzielić do innej funkcji, gdy XD = 0 trzeba zatrzymać commit/zrobić roll_back!
    // plus potem połączyć wspólne cechy

//    public Integer executeStatement(Boolean save) throws SQLException {
//        Integer status = 10;
//        int re;
//        for (String statement: this.dataBase.statementsList) {
//            System.out.println(statement);
//            if (statement.toUpperCase().startsWith("INSERT")) {
//                String table_name = statement.substring(statement.indexOf("INTO") + 5, statement.toUpperCase().indexOf("VALUES") - 1);
//                String first = "SELECT * FROM " + table_name + " LIMIT 1;";
//                ResultSet rf = this.stmt.executeQuery(first);
//                ResultSetMetaData rfmd = rf.getMetaData();
//                String col = rfmd.getColumnName(1);
//                String state = "SELECT * FROM " + table_name + " WHERE " + col + " = " + statement.substring(statement.indexOf('(') + 1, statement.indexOf(','));
//                rs = this.stmt.executeQuery(state);
//                ResultSetMetaData rsmd = rs.getMetaData();
//                while (rs.next()) {
//                    int numColumns = rsmd.getColumnCount();
//                    Map<String, Object> tmp = new HashMap<String, Object>();
//                    for (int i = 1; i <= numColumns; i++) {
//                        String column_name = rsmd.getColumnName(i);
//                        tmp.put(column_name, rs.getObject(column_name));
//                    }
//                    if (save)register_insert(tmp, table_name);
//                }
//                this.stmt.executeUpdate(statement);
//            } else if (statement.toUpperCase().startsWith("UPDATE")) {
//                String table_name = statement.substring(statement.indexOf("UPDATE") + 7, statement.toUpperCase().indexOf("SET") - 1);
//                String state = "SELECT * FROM " + table_name + statement.substring(statement.toUpperCase().indexOf(" WHERE"));
//                System.out.println(state);
//                rs = this.stmt.executeQuery(state);
//                ResultSetMetaData rsmd = rs.getMetaData();
//                while (rs.next()) {
//                    int numColumns = rsmd.getColumnCount();
//                    Map<String, Object> tmp = new HashMap<String, Object>();
//                    for (int i = 1; i <= numColumns; i++) {
//                        String column_name = rsmd.getColumnName(i);
//                        tmp.put(column_name, rs.getObject(column_name));
//                    }
//                    if (save){register_update(tmp, table_name);}
//                }
//                this.stmt.executeUpdate(statement);
//            } else if (statement.toUpperCase().startsWith("DELETE")) {
//                String table_name = statement.substring(statement.indexOf("FROM") + 5, statement.toUpperCase().indexOf("WHERE") - 1);
//                String state = "SELECT * FROM " + table_name + statement.substring(statement.toUpperCase().indexOf(" WHERE"));
////                System.out.println(state);
//                rs = this.stmt.executeQuery(state);
//                ResultSetMetaData rsmd = rs.getMetaData();
//                while (rs.next()) {
//                    int numColumns = rsmd.getColumnCount();
//
//                    Map<String, Object> tmp = new HashMap<String, Object>();
//                    for (int i = 1; i <= numColumns; i++) {
////                        System.out.println(rsmd.getColumnName(i));
//                        String column_name = rsmd.getColumnName(i);
////                        System.out.println(rs.getObject(column_name));
//                        tmp.put(column_name, rs.getObject(column_name));
//                    }
//                    if (save){
//                        register_delete(tmp, table_name);
//                    }
//                }
////                System.out.println(statement);
//                int xd = this.stmt.executeUpdate(statement);
//                System.out.println("XD = "+xd);
//            }
//        }
//        return status;
//    }
//    Integer executeStatement (List<String> statementsList, Boolean save) throws SQLException {
//        Integer status = 0;
//        for (String statement: statementsList) {
//            if (statement.toUpperCase().startsWith("INSERT")) {
//                String table_name = statement.substring(statement.indexOf("INTO") + 5, statement.toUpperCase().indexOf("VALUES") - 1);
//                String first = "SELECT * FROM " + table_name + " LIMIT 1;";
//                ResultSet rf = this.stmt.executeQuery(first);
//                ResultSetMetaData rfmd = rf.getMetaData();
//                String col = rfmd.getColumnName(1);
//                String state = "SELECT * FROM " + table_name + " WHERE " + col + " = " + statement.substring(statement.indexOf('(') + 1, statement.indexOf(','));
//                rs = this.stmt.executeQuery(state);
//                ResultSetMetaData rsmd = rs.getMetaData();
//                while (rs.next()) {
//                    int numColumns = rsmd.getColumnCount();
//                    Map<String, Object> tmp = new HashMap<String, Object>();
//                    for (int i = 1; i <= numColumns; i++) {
//                        String column_name = rsmd.getColumnName(i);
//                        tmp.put(column_name, rs.getObject(column_name));
//                    }
//                    if (save) register_insert(tmp, table_name);
//                }
//                this.stmt.executeUpdate(statement);
//            } else if (statement.toUpperCase().startsWith("UPDATE")) {
//                String table_name = statement.substring(statement.indexOf("UPDATE") + 7, statement.toUpperCase().indexOf("SET") - 1);
//                String state = "SELECT * FROM " + table_name + statement.substring(statement.toUpperCase().indexOf("WHERE"));
//                rs = this.stmt.executeQuery(state);
//                ResultSetMetaData rsmd = rs.getMetaData();
//                while (rs.next()) {
//                    int numColumns = rsmd.getColumnCount();
//                    Map<String, Object> tmp = new HashMap<String, Object>();
//                    for (int i = 1; i <= numColumns; i++) {
//                        String column_name = rsmd.getColumnName(i);
//                        tmp.put(column_name, rs.getObject(column_name));
//                    }
//                    if (save) register_update(tmp, table_name);
//                }
//                this.stmt.executeUpdate(statement);
//            } else if (statement.toUpperCase().startsWith("DELETE")) {
//                String table_name = statement.substring(statement.indexOf("FROM") + 5, statement.toUpperCase().indexOf("WHERE") - 1);
//                String state = "SELECT * FROM " + table_name + statement.substring(statement.toUpperCase().indexOf("WHERE"));
//                rs = this.stmt.executeQuery(state);
//                ResultSetMetaData rsmd = rs.getMetaData();
//                while (rs.next()) {
//                    int numColumns = rsmd.getColumnCount();
//                    Map<String, Object> tmp = new HashMap<String, Object>();
//                    for (int i = 1; i <= numColumns; i++) {
////                        System.out.println(rsmd.getColumnName(i));
//                        String column_name = rsmd.getColumnName(i);
////                        System.out.println(rs.getObject(column_name));
//                        tmp.put(column_name, rs.getObject(column_name));
//                    }
//                    if (save){
//                        register_delete(tmp, table_name);
//                    }
//                }
////                System.out.println(statement);
//                this.stmt.executeUpdate(statement);
//            }
//        }
//        return status;
//    }

    Integer reverse () throws SQLException {
    Integer status = 0;
    for (String statement: this.dataBase.statementsList) {
        if (statement.toUpperCase().startsWith("INSERT")) {
            String table_name = statement.substring(statement.indexOf("INTO") + 5, statement.toUpperCase().indexOf("VALUES") - 1);
            String first = "SELECT * FROM " + table_name + " LIMIT 1;";
            ResultSet rf = this.stmt.executeQuery(first);
            ResultSetMetaData rfmd = rf.getMetaData();
            String col = rfmd.getColumnName(1);
            String state = "SELECT * FROM " + table_name + " WHERE " + col + " = " + statement.substring(statement.indexOf('(') + 1, statement.indexOf(','));
            rs = this.stmt.executeQuery(state);
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                int numColumns = rsmd.getColumnCount();
                Map<String, Object> tmp = new HashMap<String, Object>();
                for (int i = 1; i <= numColumns; i++) {
                    String column_name = rsmd.getColumnName(i);
                    tmp.put(column_name, rs.getObject(column_name));
                }
                register_insert(tmp, table_name);
            }
        } else if (statement.toUpperCase().startsWith("UPDATE")) {
            String table_name = statement.substring(statement.indexOf("UPDATE") + 7, statement.toUpperCase().indexOf("SET") - 1);
            String state = "SELECT * FROM " + table_name + ' ' + statement.substring(statement.toUpperCase().indexOf("WHERE"));
            rs = this.stmt.executeQuery(state);
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                int numColumns = rsmd.getColumnCount();
                Map<String, Object> tmp = new HashMap<String, Object>();
                for (int i = 1; i <= numColumns; i++) {
                    String column_name = rsmd.getColumnName(i);
                    tmp.put(column_name, rs.getObject(column_name));
                }
                register_update(tmp, table_name);
            }
        } else if (statement.toUpperCase().startsWith("DELETE")) {
            String table_name = statement.substring(statement.indexOf("FROM") + 5, statement.toUpperCase().indexOf("WHERE") - 1);
            String state = "SELECT * FROM " + table_name + ' ' + statement.substring(statement.toUpperCase().indexOf("WHERE"));
//            System.out.println(state);
            rs = this.stmt.executeQuery(state);
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                int numColumns = rsmd.getColumnCount();
                Map<String, Object> tmp = new HashMap<String, Object>();
                for (int i = 1; i <= numColumns; i++) {
//                        System.out.println(rsmd.getColumnName(i));
                    String column_name = rsmd.getColumnName(i);
//                        System.out.println(rs.getObject(column_name));
                    tmp.put(column_name, rs.getObject(column_name));
                }
                register_delete(tmp, table_name);
            }
//                System.out.println(statement);
        }
    }
    return status;
}

    Integer reverse (List<String> statementsList) throws SQLException {
        Integer status = 0;
        for (String statement: statementsList) {
            if (statement.toUpperCase().startsWith("INSERT")) {
                String table_name = statement.substring(statement.indexOf("INTO") + 5, statement.toUpperCase().indexOf("VALUES") - 1);
                String first = "SELECT * FROM " + table_name + " LIMIT 1;";
                ResultSet rf = this.stmt.executeQuery(first);
                ResultSetMetaData rfmd = rf.getMetaData();
                String col = rfmd.getColumnName(1);
                String state = "SELECT * FROM " + table_name + " WHERE " + col + " = " + statement.substring(statement.indexOf('(') + 1, statement.indexOf(','));
                rs = this.stmt.executeQuery(state);
                ResultSetMetaData rsmd = rs.getMetaData();
                while (rs.next()) {
                    int numColumns = rsmd.getColumnCount();
                    Map<String, Object> tmp = new HashMap<String, Object>();
                    for (int i = 1; i <= numColumns; i++) {
                        String column_name = rsmd.getColumnName(i);
                        tmp.put(column_name, rs.getObject(column_name));
                    }
                    register_insert(tmp, table_name);
                }
            } else if (statement.toUpperCase().startsWith("UPDATE")) {
                String table_name = statement.substring(statement.indexOf("UPDATE") + 7, statement.toUpperCase().indexOf("SET") - 1);
                String state = "SELECT * FROM " + table_name  + ' '+ statement.substring(statement.toUpperCase().indexOf("WHERE"));
                rs = this.stmt.executeQuery(state);
                ResultSetMetaData rsmd = rs.getMetaData();
                while (rs.next()) {
                    int numColumns = rsmd.getColumnCount();
                    Map<String, Object> tmp = new HashMap<String, Object>();
                    for (int i = 1; i <= numColumns; i++) {
                        String column_name = rsmd.getColumnName(i);
                        tmp.put(column_name, rs.getObject(column_name));
                    }
                    register_update(tmp, table_name);
                }
            } else if (statement.toUpperCase().startsWith("DELETE")) {
                String table_name = statement.substring(statement.indexOf("FROM") + 5, statement.toUpperCase().indexOf("WHERE") - 1);
                String state = "SELECT * FROM " + table_name + statement.substring(statement.toUpperCase().indexOf("WHERE"));
                rs = this.stmt.executeQuery(state);
                ResultSetMetaData rsmd = rs.getMetaData();
                while (rs.next()) {
                    int numColumns = rsmd.getColumnCount();
                    Map<String, Object> tmp = new HashMap<String, Object>();
                    for (int i = 1; i <= numColumns; i++) {
//                        System.out.println(rsmd.getColumnName(i));
                        String column_name = rsmd.getColumnName(i);
//                        System.out.println(rs.getObject(column_name));
                        tmp.put(column_name, rs.getObject(column_name));
                    }
                    register_delete(tmp, table_name);
                }
//                System.out.println(statement);
            }
        }
        return status;
    }


    void executeStatement(List<String> statementsList){
        for (String statement: statementsList) {
            try {
                this.stmt.executeUpdate(statement);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    void executeStatement(){
        for (String statement: this.dataBase.statementsList) {
            try {
                this.stmt.executeUpdate(statement);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    int register_insert (Map<String, Object> tmp, String table_name){
        caretaker.register(tmp,"INSERT", table_name);
        return 0;
    }


    @Override
    int register_update(Map<String, Object> tmp, String table_name){
        caretaker.register(tmp,"UPDATE", table_name);
        return 0;
    }


    @Override
    int register_delete(Map<String, Object> tmp, String table_name){
        caretaker.register(tmp,"DELETE", table_name);
        return 0;
    }

    int commit (){
        // Commit changes
        int failed = 20;
//        System.out.println("nie dziala");
        try{
            System.out.println("COMMIT");
            this.conn.commit();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
            failed = -20;
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
            failed = -20;
        }
        return failed;
    }

    @Override
    int rollback ( ){
        // Rollback changes
        int failed = 30;
        try{
            System.out.println("ROLLBACK");
            this.conn.rollback();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
            failed = -30;
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
            failed = -30;
        }
        return failed;
    }

    @Override
    int rollback2phase (  ){
        // Rollback changes
        int failed = 40;

        //try {
            System.out.println("ROLLBACK 2");
            executeStatement(caretaker.restore());
       // } catch (SQLException throwables) {
     //       failed = -40;
   //         throwables.printStackTrace();
 //       }
        return failed;
    }
}

