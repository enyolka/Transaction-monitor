package monitor;

import java.util.ArrayList;
import java.util.List;


/**
 * The type DataBase
 * Class available for the user. User sends here information needed to connect with selected database,
 * add the commands he wants to execute and adds it to the Transaction Monitor
 */
public class DataBase{
    /**
     * The Jdbc driver.
     */
    final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    /**
     * The Db url.
     */
    String DB_URL;

    /**
     * The User.
     */
    String USER;
    /**
     * The Pass.
     */
    String PASS;

    /**
     * The Statements list.
     */
    List<String> statementsList = new ArrayList<String>();

    /**
     * Gets jdbc driver.
     *
     * @return the jdbc driver
     */
    public String getJdbcDriver() {
        return JDBC_DRIVER;
    }

    /**
     * Gets db url.
     *
     * @return the db url
     */
    public String getDbUrl() {
        return DB_URL;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public String getUSER() {
        return USER;
    }

    /**
     * Gets pass.
     *
     * @return the pass
     */
    public String getPASS() {
        return PASS;
    }

    /**
     * Gets statements list.
     *
     * @return the statements list
     */
    public List<String> getStatementsList() {
        return statementsList;
    }

    /**
     * Instantiates a new Data base.
     *
     * @param user     the user
     * @param password the password
     * @param url      the url
     */
    public DataBase(String user, String password, String url){
        this.USER = user;
        this.PASS = password;
        this.DB_URL = url;

    }

    /**
     * Function adds statement to the statements list.
     *
     * @param statement the statement
     */
    public void addStatement (String statement){
        statementsList.add(statement);
    }


    /**
     * Sets statements list.
     *
     * @param statementsList the statements list
     */
    public void setStatementsList(List<String> statementsList) {
        this.statementsList = statementsList;
    }

}

