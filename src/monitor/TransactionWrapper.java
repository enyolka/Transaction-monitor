package monitor;//import monitor.UOW;
//import monitor.Callable;

import java.sql.SQLException;
import java.util.concurrent.Callable;

public class TransactionWrapper implements Callable<Integer>
{
    UOW uow;
    Integer mode = 1;
    Boolean hasCommit = Boolean.FALSE;

    public TransactionWrapper (DataBase base) throws ClassNotFoundException {
            uow = new UOW_DataBase(base);
    }

    public TransactionWrapper (WebService base) throws ClassNotFoundException {
            uow = new UOW_WebServise(base);
    }

    Integer startTransaction (  ){
//        Integer status = 10;
//        uow.startTransaction();
//        try {
//            uow.executeStatement(Boolean.TRUE);
//        } catch (SQLException throwables) {
//            status = -10;
//            throwables.printStackTrace();
//        }
//        return status;
        return uow.startTransaction();
    }

    Integer commit (  ){
        return uow.commit();
    }

    Integer rollback (  ){
        return uow.rollback();
    }

    Integer rollback2phase (  ){
        return uow.rollback2phase();
    }

    Integer finalizeTransaction (  ){
        return uow.finalizeTransaction();
    }
//    void getStatus (  ){}

    public Integer call() throws SQLException {
        Integer status = 0;
        if(this.mode == 1){
            status = this.startTransaction();
        }else if (this.mode == 2){
            status = this.commit();
            hasCommit = Boolean.TRUE;
        }else if (this.mode == 3){
            status = this.rollback();
        }else if (this.mode == 4){
            status = this.rollback2phase();
        }else if (this.mode == 5){
            status = this.finalizeTransaction();
        }else if (this.mode == 6){
            throw new java.lang.UnsupportedOperationException("Not supported yet.");
        }else if (this.mode == 7){
            throw new java.lang.UnsupportedOperationException("Not supported yet.");
        }
        return status;
    }
}

