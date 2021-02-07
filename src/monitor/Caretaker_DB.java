package monitor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Caretaker_DB
 * Stores a list of Memento DB objects, collect all data that has been changed
 * and make available mechanisms for their recovery in case of rollback
 */
public class Caretaker_DB{

    /**
     * The List of Memento_DB corresponding to modified rows over statement.
     */
    List<Memento_DB> list = new ArrayList<Memento_DB>();

    /**
     * Instantiates a new Caretaker db.
     */
    public Caretaker_DB(){}

    /**
     * Restore list.
     *
     * @return the list of statements that need to by use to rollback the changes
     */
    List<String> restore(){
        List<String> SQL_R = new ArrayList<String>();
        for(Memento_DB element: list){
            if (element.type.equals("INSERT")){

                StringBuilder text = new StringBuilder();

                for (int i = 0; i < element.values.size()-1; i++) {
                    text.append(element.values.keySet().toArray()[i]);
                    text.append("=");
                    text.append(element.values.values().toArray()[i]);
                    text.append(" AND ");
                }

                text.append(element.values.keySet().toArray()[element.values.size()-1]);
                text.append("=");
                text.append(element.values.values().toArray()[element.values.size()-1]);
                text.append(" ");

                SQL_R.add("DELETE FROM " + element.table_name + " WHERE " + text + ";");

            }else if (element.type.equals("UPDATE")){
                StringBuilder text = new StringBuilder();

                for (int i = 0; i < element.values.size()-1; i++) {
                    text.append(element.values.keySet().toArray()[i]);
                    text.append("='");
                    text.append(element.values.values().toArray()[i]);
                    text.append("', ");
                }

                text.append(element.values.keySet().toArray()[element.values.size()-1]);
                text.append("='");
                text.append(element.values.values().toArray()[element.values.size()-1]);
                text.append("' ");
                SQL_R.add("UPDATE " + element.table_name + " SET " + text + " WHERE " +
                        element.values.keySet().toArray()[0] + " = '" + element.values.values().toArray()[0] + "';");
            }
            else if (element.type.equals("DELETE")){
                StringBuilder text1 = new StringBuilder("(");
                StringBuilder text2 = new StringBuilder("(");

                for (int i = 0; i < element.values.size()-1; i++) {
                    text1.append(element.values.keySet().toArray()[i]);
                    text1.append(", ");
                    text2.append("'");
                    text2.append(element.values.values().toArray()[i]);
                    text2.append("', ");
                }

                text2.append("'");
                text1.append(element.values.keySet().toArray()[element.values.size()-1]);
                text2.append(element.values.values().toArray()[element.values.size()-1]);
                text1.append(")");
                text2.append("')");

                SQL_R.add("INSERT INTO " + element.table_name + " " + text1 + " VALUES " + text2 + ";");
            }
        }
        return SQL_R;
    }

    /**
     * Register new Memento_DB objects in list to remember changed rows in database tables in case of rollback
     * @param values     the values
     * @param type       the type (INSERT, UPDATE, DELETE)
     * @param table_name the table name
     */
    void register (Map<String, Object> values, String type, String table_name){
        list.add(new Memento_DB(values, type, table_name));
    }

    /**
     * Clear the list of stored memento objects.
     */
    void clear (){
        list.clear();
    }
}

