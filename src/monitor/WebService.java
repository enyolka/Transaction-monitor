package monitor;

import java.util.ArrayList;
import java.util.List;

public class WebService extends Base{
    final Integer type = 2;
    String url;
    List<String> statementsList = new ArrayList<String>();

    public WebService (String  url ){
        this.url = url;
    }

    public void addStatement  (String statement){
        statementsList.add(statement);
    }
    public String getURL(){return this.url;}
}

