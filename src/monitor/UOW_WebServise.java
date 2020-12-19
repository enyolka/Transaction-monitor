package monitor;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UOW_WebServise extends UOW
{

     Caretaker_WS carataker;

     WebService WebService;

     int status;


    public UOW_WebServise ( WebService WebServise ){}

    public int getStatus (  ){return 0;}

    public int  startTransaction (  ){return 0;}

    public int finalizeTransaction (  ){return 0;}

//    @Override
//    Integer executeStatement(List<String> statementsList, Boolean save) throws SQLException {
//        return null;
//    }

    @Override
    int register_insert(Map<String, Object> tmp, String table_name) {
        return 0;
    }

    @Override
    int register_update(Map<String, Object> tmp, String table_name) {
        return 0;
    }

    @Override
    int register_delete(Map<String, Object> tmp, String table_name) {
        return 0;
    }

    public int commit (  ){return 0;}

    public int rollback (  ){return 0;}

    @Override
    int rollback2phase (  ){return 0;}

//    @Override
//    public Integer executeStatement(Boolean aFalse) throws SQLException {
//        return null;
//    }
}

