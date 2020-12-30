package monitor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Caretaker_DB{

     List<Memento_DB> list = new ArrayList<Memento_DB>();

    public Caretaker_DB(){}

    List<String> restore(){
        List<String> SQL_R = new ArrayList<String>();
        for(Memento_DB element: list){
            if (element.type.equals("INSERT")){
                SQL_R.add("DELETE FROM " + element.table_name + " WHERE " +
                        element.values.get(element.values.keySet().toArray()[0]) + " = " + element.values.values().toArray()[0]);
            }else if (element.type.equals("UPDATE")){
                SQL_R.add("UPDATE " + element.table_name + " SET " + element.values + " WHERE " +
                        element.values.get(element.values.keySet().toArray()[0]) + " = " + element.values.values().toArray()[0]);
            }else if (element.type.equals("DELETE")){
                SQL_R.add("INSERT INTO " + element.table_name + " VALUES " + element.values);
            }
        }
        return SQL_R;
    }

    void register (Map<String, Object> values, String type, String table_name){
        list.add(new Memento_DB(values, type, table_name));
    }

    void clear (){
        list.clear();
    }
}

