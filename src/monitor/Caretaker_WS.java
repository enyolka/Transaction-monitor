package monitor;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Caretaker_WS
 * Stores a list of Memento WS objects, collect all data that has been changed
 * and make available mechanisms for their recovery in case of rollback
 */
public class Caretaker_WS{

    /**
     * The List of Memento_WS corresponding to modified data in web service.
     */
    List<Memento_WS> list = new ArrayList<Memento_WS>();

    /**
     * The Url.
     */
    String url;

    /**
     * Instantiates a new Caretaker ws.
     *
     * @param url the url
     */
    public Caretaker_WS (String url){
        this.url = url;
    }

    /**
     * Restore all statements recuired to get rollback.
     *
     * @return the list of url's that need to by use to rollback the changes
     */
    List<String> restore(){
        List<String> WEB_S = new ArrayList<String>();
        for(Memento_WS element: list){
            if (element.type.equals("add")){
                WEB_S.add(url+"delete?"+ element.values.get(0));
            }else if (element.type.equals("update")){
                StringBuilder str = new StringBuilder();
                for (Object object: element.values) {
                    str.append(object).append("&");
                }
                WEB_S.add(url+"update?"+ str.toString());
            }else if (element.type.equals("delete")){
                StringBuilder str = new StringBuilder();
                for (Object object: element.values) {
                    str.append(object).append("&");
                }
                WEB_S.add(url+"add?"+ str.toString());
            }
        }
        return WEB_S;
    }


    /**
     * Register appends Memento objects to list to remember changed data in web service in case of rollback
     *
     * @param values the values
     * @param type   the type (ADD, UPDATE, DELETE)
     */
    void register (List<Object> values, String type){
        list.add(new Memento_WS(values, type));
    }


    /**
     * Clear the list of stored memento objects.
     */
    void clear (){
        list.clear();
    }
}

