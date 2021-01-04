package monitor;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * The type UOW_DataBase.
 * Class is responsible for the correct execution of operations on the database.
 * It implements the database methods and provides mechanism fos saving changes
 */
public class UOW_DataBase extends UOW{

    /**
     * The Caretaker.
     */
    Caretaker_DB caretaker;

    /**
     * The Data base.
     */
    DataBase dataBase;
    /**
     * The Statement status.
     */
    boolean statement_status = true;

    /**
     * The Auto commit.
     */
    final boolean autoCommit = false;
    /**
     * The Conn.
     */
    Connection conn = null;
    /**
     * The Stmt.
     */
    Statement stmt = null;
    /**
     * The Rs.
     */
    ResultSet rs = null;

    /**
     * Instantiates a new Uow data base.
     *
     * @param database the database
     */
    public UOW_DataBase ( DataBase database ){
        this.dataBase = database;
        this.caretaker = new Caretaker_DB();
    }

    /**
     * Start the transaction at data base, if the transaction fails, variable failed is set to -10
     * @return the status as integer value positive value for success, negative value for fail
     */
    int startTransaction(){
        int failed = 10;
        try{
            System.out.println("START DB");
            Class.forName(dataBase.getJdbcDriver());
            this.conn = DriverManager.getConnection(dataBase.getDbUrl(),dataBase.getUSER(),dataBase.getPASS());
            this.conn.setAutoCommit(autoCommit);
            stmt = this.conn.createStatement();
            this.stmt.executeUpdate("SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;");
            reverse();
            executeStatement();

        }catch(SQLException se){
            se.printStackTrace();
            failed = -10;
        }catch(Exception e){
            e.printStackTrace();
            failed = -10;
        }
        if (!statement_status){
            failed = -10;
        }
        return failed;
    }

    /**
     * Close connection to database
     *
     * @return  the status as integer value, 50 for success, -50 for fail
     */
    int finalizeTransaction(){
        int failed = 50;
        try{
            System.out.println("FINALIZE DB");
            stmt.close();
            conn.close();
        }catch(SQLException se2) {
            se2.printStackTrace();
            failed = -50;
        }
        caretaker.clear();
        return failed;
    }

    /**
     * Reverse statement from statementsList, put them to map in Caretaker class. (Memento Patter)
     * This allows to make rollback for all database from reversed queries which were already executed
     *
     * @throws SQLException the sql exception
     */
    void reverse () throws SQLException {
        for (String statement: this.dataBase.statementsList) {
            if (statement.toUpperCase().startsWith("INSERT")) {
            String table_name = statement.substring(statement.indexOf("INTO") + 5, statement.toUpperCase().indexOf("VALUES") - 1);
            String first = "SELECT * FROM " + table_name + " LIMIT 1;";
            ResultSet rf = this.stmt.executeQuery(first);
            ResultSetMetaData rfmd = rf.getMetaData();
            String value = statement.substring(statement.indexOf("(")+1,statement.indexOf(")"));
            String[] values = value.split(",");

            while (rf.next()) {
                int numColumns = rfmd.getColumnCount();
                Map<String, Object> tmp = new LinkedHashMap<String, Object>();
                for (int i = 1; i <= numColumns; i++) {
                    String column_name = rfmd.getColumnName(i);
                    tmp.put(column_name, values[i-1]);
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
                Map<String, Object> tmp = new LinkedHashMap<String, Object>();
                for (int i = 1; i <= numColumns; i++) {
                    String column_name = rsmd.getColumnName(i);
                    tmp.put(column_name, rs.getObject(column_name));
                }
                register_update(tmp, table_name);
            }
        } else if (statement.toUpperCase().startsWith("DELETE")) {
            String table_name = statement.substring(statement.indexOf("FROM") + 5, statement.toUpperCase().indexOf("WHERE") - 1);
            String state = "SELECT * FROM " + table_name + ' ' + statement.substring(statement.toUpperCase().indexOf("WHERE"));

            rs = this.stmt.executeQuery(state);
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                int numColumns = rsmd.getColumnCount();
                Map<String, Object> tmp = new LinkedHashMap<String, Object>();
                for (int i = 1; i <= numColumns; i++) {
                    String column_name = rsmd.getColumnName(i);
                    tmp.put(column_name, rs.getObject(column_name));
                }
                register_delete(tmp, table_name);
            }
        }
    }
}

    /**
     * Execute statement.
     * This allows to execute SQL statements from statementsList param
     *
     * @param statementsList the statements list
     */
    void executeStatement(List<String> statementsList){
        for (String statement: statementsList) {
            try {
                this.stmt.executeUpdate(statement);
            } catch (SQLException throwables) {
                //throwables.printStackTrace();
            }
        }
    }

    /**
     * Execute statement.
     * This allows to execute SQL statement from statementsList in corresponding dataBase
     */
    void executeStatement(){
        for (String statement: this.dataBase.statementsList) {
            try {
                this.stmt.executeUpdate(statement);
            } catch (SQLException throwables) {
                //throwables.printStackTrace();
                statement_status = false;
            }
        }
    }

    @Override
    void register_insert (Map<String, Object> tmp, String table_name){
        caretaker.register(tmp,"INSERT", table_name);
    }


    @Override
    void register_update(Map<String, Object> tmp, String table_name){
        caretaker.register(tmp,"UPDATE", table_name);
    }


    @Override
    void register_delete(Map<String, Object> tmp, String table_name){
        caretaker.register(tmp,"DELETE", table_name);
    }

    @Override
    void register_insert(List<Object> tmp){
    }

    @Override
    void register_update(List<Object> tmp){
    }

    @Override
    void register_delete(List<Object> tmp){
    }

    @Override
    int commit (){
        int failed = 20;
        try{
            System.out.println("COMMIT DB");
            this.conn.commit();
        }catch(SQLException se){
            se.printStackTrace();
            failed = -20;
        }catch(Exception e) {
            e.printStackTrace();
            failed = -20;
        }
        return failed;
    }

    @Override
    int rollback ( ){
        int failed = 30;
        try{
            System.out.println("ROLLBACK DB");
            this.conn.rollback();
        }catch(SQLException se){
            se.printStackTrace();
            failed = -30;
        }catch(Exception e){
            e.printStackTrace();
            failed = -30;
        }
        return failed;
    }

    @Override
    int rollback2phase () throws SQLException {
        int failed = 40;
        System.out.println("ROLLBACK 2 DB");
        executeStatement(caretaker.restore());
        this.conn.commit();
        return failed;
    }
}

