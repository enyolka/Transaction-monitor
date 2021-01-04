package monitor;

import java.util.ArrayList;
import java.util.List;

/**
 * The type WebService
 * Class available for the user. User sends here information needed to connect with selected web service,
 * add the commands he wants to execute and adds it to the Transaction Monitor
 */
public class WebService{
    /**
     * The Url.
     */
    String url;
    /**
     * The Statements list.
     */
    List<String> statementsList = new ArrayList<String>();

    /**
     * Instantiates a new Web service.
     *
     * @param url the url
     */
    public WebService(String  url ){
        this.url = url;
    }

    /**
     * Function adds statement to the statements list.
     *
     * @param statement the statement
     */
    public void addStatement(String statement){
        statementsList.add(statement);
    }

    /**
     * Get url string.
     *
     * @return the string
     */
    public String getURL(){return this.url;}
}

