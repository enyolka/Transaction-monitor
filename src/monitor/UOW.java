package monitor;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * The type UOW.
 * Abstract class responsible for the definition of the general form of objects
 */
public abstract class UOW{

    /**
     * Start transaction int.
     *
     * @return the int
     */
    abstract int startTransaction();

    /**
     * Finalize transaction int.
     *
     * @return the int
     */
    abstract int finalizeTransaction();

    /**
     * Register insert.
     *
     * @param tmp        the tmp
     * @param table_name the table name
     */
    abstract void register_insert(Map<String, Object> tmp, String table_name);

    /**
     * Register insert.
     *
     * @param tmp the tmp
     */
    abstract void register_insert(List<Object> tmp);

    /**
     * Register update.
     *
     * @param tmp        the tmp
     * @param table_name the table name
     */
    abstract void register_update(Map<String, Object> tmp, String table_name);

    /**
     * Register update.
     *
     * @param tmp the tmp
     */
    abstract void register_update(List<Object> tmp);

    /**
     * Register delete.
     *
     * @param tmp        the tmp
     * @param table_name the table name
     */
    abstract void register_delete(Map<String, Object> tmp, String table_name);

    /**
     * Register delete.
     *
     * @param tmp the tmp
     */
    abstract void register_delete(List<Object> tmp);

    /**
     * Commit all changes
     *
     * @return the int; ; positive value for success, negative value for fail
     */
    abstract int commit();

    /**
     * Rollback all changes or catch exception (handle errors for JDBC or class.forName)
     *
     * @return the int; positive value for success, negative value for fail
     */
    abstract int rollback();

    /**
     * Second phase rollback for all changes if first phase returns different codes (even one hasn't been committed)
     *
     * @return the int; positive value for success, negative value for fail
     * @throws SQLException the sql exception
     */
    abstract int rollback2phase() throws SQLException;

}

