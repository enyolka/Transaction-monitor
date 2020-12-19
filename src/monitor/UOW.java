package monitor;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class UOW{

    abstract int startTransaction();

    abstract int finalizeTransaction();

//    abstract Integer executeStatement(List<String> statementsList, Boolean save) throws SQLException;

    abstract int register_insert(Map<String, Object> tmp, String table_name);

    abstract int register_update(Map<String, Object> tmp, String table_name);

    abstract int register_delete(Map<String, Object> tmp, String table_name);

    abstract int commit();

    abstract int rollback();

    abstract int rollback2phase();

//    public abstract Integer executeStatement(Boolean aFalse) throws SQLException;
}

