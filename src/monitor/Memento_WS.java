package monitor;

import java.util.List;

public class Memento_WS

{

     List<Object> values;

     String type;

     String url;

     String table_name;

     Memento_WS (List<Object> values, String type, String url, String table_name){
          this.values = values;
          this.type = type;
          this.url = url;
          this.table_name = table_name;
     }
}

