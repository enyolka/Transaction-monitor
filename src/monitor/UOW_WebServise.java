package monitor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.InputStreamReader;
import java.io.BufferedReader;
//import com.google.*;
//import com.google.gson.JsonObject;
//import netscape.javascript.JSObject;
import jdk.internal.org.jline.terminal.spi.JansiSupport;
import netscape.javascript.JSObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.*;
public class UOW_WebServise extends UOW{

     Caretaker_WS caretaker;

     WebService webService;

    HttpURLConnection connection;

     int status;


    public UOW_WebServise (WebService webService){
        this.webService = webService;
        this.caretaker = new Caretaker_WS(this.webService.url);
    }

    public int getStatus (  ){return 0;}

    public int  startTransaction (){
        int failed = 10;
        try{
            // wywoływanie
            reverse();
            executeStatement();
        }catch(Exception e){
            e.printStackTrace();
            failed = -10;
        }
        return failed;
    }

    public int finalizeTransaction (  ){return 50;}

//    @Override
//    Integer executeStatement(List<String> statementsList, Boolean save) throws SQLException {
//        return null;


    Integer reverse () throws MalformedURLException {
        Integer status = 0;
        for (String statement: this.webService.statementsList) {
            if (statement.startsWith("add")) {
                String id = statement.substring(statement.indexOf("?"));
                String[] r = id.split("&");
                List<Object> values = new ArrayList<Object>(Arrays.asList(r));
                register_insert(values);
            }
            else {
                String id = statement.substring(statement.indexOf("?"), statement.indexOf("&"));
                String new_url = this.webService.getURL() + "getId?" + id;
                URL statement_url = new URL(new_url);
                try {
                    connection = (HttpURLConnection) statement_url.openConnection();
                    connection.setRequestMethod("post");
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        String data = response.toString();
                        data = data.replace("[", "");
                        data = data.replace("]", "");
                        data = data.replace("\"", "");

                        String[] data2 = data.split("}");

                        for (String row : data2) {
                            row = row.replace("{", "");
                            row = row.replace("}", "");
                            row = row.replace(":", "=");

                            String[] r = row.split(",");

                            List<Object> values = new ArrayList<Object>(Arrays.asList(r));

                            if (statement.startsWith("delete")) {
                                register_delete(values);
                            } else if (statement.startsWith("update")) {
                                register_delete(values);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            }
        return status;
    }

    void executeStatement(){
        for (String statement: this.webService.statementsList) {
            try {
                String new_url = this.webService.getURL() + statement;
                URL statement_url = new URL(new_url);
                connection = (HttpURLConnection) statement_url.openConnection();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    void executeStatement(List<String> statementsList){
        for (String statement: statementsList) {
            try {
                String new_url = this.webService.getURL() + statement;
                URL statement_url = new URL(new_url);
                connection = (HttpURLConnection) statement_url.openConnection();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    int register_insert(List<Object> tmp) {
        caretaker.register(tmp,"add");
        return 0;
    }

    @Override
    int register_update(List<Object> tmp) {
        caretaker.register(tmp,"update");
        return 0;
    }

    @Override
    int register_delete(List<Object> tmp) {
        caretaker.register(tmp,"delete");
        return 0;
    }

    @Override
    int register_insert (Map<String, Object> tmp, String table_name){
        return 0;
    }


    @Override
    int register_update(Map<String, Object> tmp, String table_name){
        return 0;
    }


    @Override
    int register_delete(Map<String, Object> tmp, String table_name){
        return 0;
    }

    public int commit (){
        int failed = 20;
//        System.out.println("nie dziala");
        try{
            System.out.println("COMMIT");
            executeStatement();
        } catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
            failed = -20;
        }
        return failed;
    }

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

//    @Override
//    public Integer executeStatement(Boolean aFalse) throws SQLException {
//        return null;
//    }
}

