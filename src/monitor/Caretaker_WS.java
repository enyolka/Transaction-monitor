package monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Caretaker_WS{

     List<Memento_WS> list = new ArrayList<Memento_WS>();

     String url;

    public Caretaker_WS (String url){
        this.url = url;
    }

    List<String> restore(){
        List<String> WEB_S = new ArrayList<String>();
        for(Memento_WS element: list){
            if (element.type.equals("add")){
                WEB_S.add(url+"/delete?"+ element.values.get(0));
            }else if (element.type.equals("update")){
                StringBuffer str = new StringBuffer();
                for (Object object: element.values) {
                    str.append(object + "&");
                }
                WEB_S.add(url+"/update?"+ str.toString());
            }else if (element.type.equals("delete")){
                StringBuffer str = new StringBuffer();
                for (Object object: element.values) {
                    str.append(object + "&");
                }
                WEB_S.add(url+"/add?"+ str.toString());
            }
        }
        return WEB_S;
    }


    void register (List<Object> values, String type){
        list.add(new Memento_WS(values, type));
    }

    void clear (){
        list.clear();
    }
}

