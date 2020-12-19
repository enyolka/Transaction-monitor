package monitor;

import java.util.ArrayList;
import java.util.List;


public class DataBase extends Base {
    final Integer type = 1;
    // JDBC driver name and database URL
    final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
//    final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    // C:\Program Files (x86)\MySQL\Connector J 8.0
    String DB_URL;

    //  Database credentials
    String USER;
    String PASS;

    List<String> statementsList = new ArrayList<String>();

    public String getJdbcDriver() {
        return JDBC_DRIVER;
    }

    public String getDbUrl() {
        return DB_URL;
    }

    public String getUSER() {
        return USER;
    }

    public String getPASS() {
        return PASS;
    }

    public List<String> getStatementsList() {
        return statementsList;
    }

    public DataBase(String user, String password, String url){
        this.USER = user;
        this.PASS = password;
        this.DB_URL = url;

    }

    public void addStatement (String statement){
        statementsList.add(statement);
    }

//    protected void connect (  ){}

    public void setStatementsList(List<String> statementsList) {
        this.statementsList = statementsList;
    }

    public Integer getType() {
        return type;
    }
}

