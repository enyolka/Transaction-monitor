package monitor;

import java.util.Dictionary;
import java.util.Map;

public class Memento_DB

{

    // zamienić na listę
     Map<String, Object> values;

     String type;

     String table_name;

    public Memento_DB (Map<String, Object> values, String type, String table_name){
        this.values = values;
        this.type = type;
        this.table_name = table_name;
    }
}

