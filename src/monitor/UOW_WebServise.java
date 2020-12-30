package monitor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import com.google.json.Gson;

import com.mysql.cj.xdevapi.JsonParser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class UOW_WebServise extends UOW{

     Caretaker_WS caretaker;

     WebService webService;

    HttpURLConnection connection;

     int status;


    public UOW_WebServise (WebService webService){
        this.webService = webService;
        this.caretaker = new Caretaker_WS();
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
            if (statement.toUpperCase().startsWith("ADD")) {
                String id = statement.substring(statement.indexOf("?") , statement.indexOf("&"));
                String new_url = this.webService.getURL() + "getId?" + id;
                URL statement_url = new URL(new_url);
                try {
                    connection = (HttpURLConnection) statement_url.openConnection();
                    connection.setRequestMethod("POST");
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        } in .close();
                        String output = response.toString();
                        JsonParser parser = new JsonParser();
                        JSONArray array = new JSONArray();// new JsonParser().parse(output).getAsJsonArray();
                        array = parser.parse(output);



                        System.out.println(response.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String data = ".";

//                data = data.replace("[", "");
//                data = data.replace("{", "");
//                data = data.replace("}", "");
//                data = data.replace("]", "");
//                data = data.replace("\"", "");


//                String state = statement.substring(statement.indexOf("[") , statement.indexOf("]"));
                String[] tab = data.split(",");

                List<Object> values = new ArrayList<Object>(Arrays.asList(tab));
                register_insert(values);

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
        caretaker.register(tmp,"ADD");
        return 0;
    }

    @Override
    int register_update(List<Object> tmp) {
        caretaker.register(tmp,"PUT");
        return 0;
    }

    @Override
    int register_delete(List<Object> tmp) {
        caretaker.register(tmp,"DELETE");
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

    public int commit (  ){return 0;}

    public int rollback (  ){return 0;}

    @Override
    int rollback2phase (  ){return 0;}

//    @Override
//    public Integer executeStatement(Boolean aFalse) throws SQLException {
//        return null;
//    }
}

