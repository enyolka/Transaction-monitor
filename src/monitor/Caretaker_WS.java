package monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Caretaker_WS{

     List<Memento_WS> list = new ArrayList<Memento_WS>();

    public Caretaker_WS (){}

    void restore (  ){}

    void register (List<Object> values, String type){
        list.add(new Memento_WS(values, type));
    }

    void clear (){
        list.clear();
    }
}

