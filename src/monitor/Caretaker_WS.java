package monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Caretaker_WS

{

     List<Memento_WS> list = new ArrayList<Memento_WS>();

    public Caretaker_WS (  ){}

    void restore (  ){}

    void register (List<Object> values, String type, String url, String table_name){
        list.add(new Memento_WS(values, type, url, table_name));
    }

    void clear (){
        list.clear();
    }
}

