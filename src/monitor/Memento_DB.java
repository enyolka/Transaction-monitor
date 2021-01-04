package monitor;

import java.util.Dictionary;
import java.util.Map;

/**
 * The type Memento_DB
 * Class stores data that was in the database before making changes and data newly added to database.
 */
public class Memento_DB

{

    /**
     * The Values of changed rows in adequate columns.
     */
     Map<String, Object> values;

    /**
     * The Type (INSERT, UPDATE, DELETE).
     */
    String type;

    /**
     * The Table name.
     */
    String table_name;

    /**
     * Instantiates a new Memento db.
     *
     * @param values     the values
     * @param type       the type
     * @param table_name the table name
     */
    public Memento_DB (Map<String, Object> values, String type, String table_name){
        this.values = values;
        this.type = type;
        this.table_name = table_name;
    }
}

