package monitor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/**
 * The type UOW_WebService.
 * Class is responsible for the correct execution of operations on the web service.
 * It implements the web service methods and provides mechanism fos saving changes
 */
public class UOW_WebService extends UOW{

    /**
     * The Caretaker.
     */
    Caretaker_WS caretaker;

    /**
     * The Web service.
     */
    WebService webService;

    /**
     * The Connection.
     */
    HttpURLConnection connection;

    /**
     * The Index.
     */
    int index;

    /**
     * The Statement status.
     */
    boolean statement_status = true;


    /**
     * Instantiates a new Uow web service.
     *
     * @param webService the web service
     */
    public UOW_WebService(WebService webService){
        this.webService = webService;
        this.caretaker = new Caretaker_WS(this.webService.url);
    }

    /**
     * Start the transaction at web service, if the transaction fails, variable failed is set to -10
     * @return the status as integer value positive value for success, negative value for fail
     */
    public int  startTransaction (){
        int failed = 10;
        try{
            System.out.println("START WS");
            reverse();
            this.index = executeStatement();
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
     * Finalize Web Service
     *
     * @return the status as integer value positive value for success, negative value for fail
     */
    public int finalizeTransaction (  ){
        System.out.println("FINALIZE WS");
        caretaker.clear();
        return 50;
    }

    /**
     * Reverse statement from statementsList, put them to map in Caretaker class (Memento Patter)
     * This allows to make rollback for all database from reversed queries which were already executed
     *
     * @throws MalformedURLException the malformed url exception
     */
    void reverse () throws MalformedURLException {
        for (String statement: this.webService.statementsList) {
            if (statement.startsWith("add")) {
                String id = statement.substring(statement.indexOf("?")+1);
                String[] r = id.split("&");

                List<Object> values = new ArrayList<Object>(Arrays.asList(r));
                register_insert(values);
            }
            else {
                String id;
                if (statement.contains("&")){
                    id = statement.substring(statement.indexOf("?"), statement.indexOf("&"));
                }
                else{
                    id = statement.substring(statement.indexOf("?"));
                }

                String new_url = this.webService.getURL() + "getId" + id;
                URL statement_url = new URL(new_url);
                System.out.println(new_url);
                try {
                    connection = (HttpURLConnection) statement_url.openConnection();
                    connection.setRequestMethod("POST");
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        String data = response.toString();
                        data = data.replace("[", "");
                        data = data.replace("]", "");
                        data = data.replace("\"", "");

                        String[] data2 = data.split("},");

                        for (String row : data2) {
                            row = row.replace("{", "");
                            row = row.replace("}", "");
                            row = row.replace(":", "=");

                            String[] r = row.split(",");

                            List<Object> values = new ArrayList<Object>(Arrays.asList(r));
                            System.out.println(values);

                            if (statement.startsWith("delete")) {
                                register_delete(values);
                            } else if (statement.startsWith("update")) {
                                register_update(values);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * Create new url by concatenation of base url and executed statement.
     * Open connection and post the request. If response status is 200 execute statement.
     * In case of Bad Request, Service Unavailable or other response status different from OK (200) set statement status to false and return index
     *
     * @return the count of executed statements
     */
    int executeStatement(){
        int i = 0;
        for (String statement: this.webService.statementsList) {
            try {
                String new_url = this.webService.getURL() + statement;
                URL statement_url = new URL(new_url);
                connection = (HttpURLConnection) statement_url.openConnection();
                connection.setRequestMethod("POST");
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK){
                    statement_status = false;
                    return i;
                }
                i += 1;
            } catch (MalformedURLException e) {
                //e.printStackTrace();
                statement_status = false;
                return i;
            } catch (IOException e) {
                //e.printStackTrace();
                statement_status = false;
                return i;
            }
        }
        return i;
    }

    /**
     * Execute statement.
     *
     * @param statementsList the statements list
     */
    void executeStatement(List<String> statementsList){
        for (String statement: statementsList.subList(0,getIndex())) {
            try {
                URL statement_url = new URL(statement);
                connection = (HttpURLConnection) statement_url.openConnection();
                connection.setRequestMethod("POST");
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK){
                    System.out.println(responseCode);
                }
            } catch (MalformedURLException e) {
                //e.printStackTrace();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }

    @Override
    void register_insert(List<Object> tmp) {
        caretaker.register(tmp,"add");
    }

    @Override
    void register_update(List<Object> tmp) {
        caretaker.register(tmp,"update");
    }

    @Override
    void register_delete(List<Object> tmp) {
        caretaker.register(tmp,"delete");
    }

    @Override
    void register_insert (Map<String, Object> tmp, String table_name){
    }

    @Override
    void register_update(Map<String, Object> tmp, String table_name){
    }

    @Override
    void register_delete(Map<String, Object> tmp, String table_name){
    }

    /**
     * Commit changes in Web Service
     *
     * @return the status as integer value, 20 value for success, -20 value for fail
     */
    @Override
    public int commit(){
        int failed = 20;
        try{
            System.out.println("COMMIT WS");

            URL statement_url = new URL(webService.getURL() + "commit");
            connection = (HttpURLConnection) statement_url.openConnection();
            connection.setRequestMethod("POST");
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK){
                failed = -20;
            }
        } catch(Exception e){
            System.out.println("COMMIT WS failed");
            e.printStackTrace();
            failed = -20;
        }
        return failed;
    }

    /**
     * Rollback
     *
     * @return the status as integer value 30 for success, -30 value for fail
     */
    @Override
    int rollback (){
        int failed = 30;
        try{
            System.out.println("ROLLBACK WS");
            executeStatement(caretaker.restore());
            setIndex(0);
            URL statement_url = new URL(webService.getURL() + "rollback");
            connection = (HttpURLConnection) statement_url.openConnection();
            connection.setRequestMethod("POST");
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK){
                failed = -30;
            }
        }catch(Exception e){
            System.out.println("ROLLBACK WS failed");
            e.printStackTrace();
            failed = -30;
        }
        return failed;
    }


    @Override
    int rollback2phase (  ){
        int failed = 40;

        System.out.println("ROLLBACK 2 WS");
        executeStatement(caretaker.restore());

        URL statement_url;
        try {
            statement_url = new URL(webService.getURL() + "rollback");
            connection = (HttpURLConnection) statement_url.openConnection();
            connection.setRequestMethod("POST");
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK){
                failed = -40;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return failed;
    }

    /**
     * Gets index.
     *
     * @return index index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets index.
     *
     * @param index the index
     */
    public void setIndex(int index) {
        this.index = index;
    }


}

