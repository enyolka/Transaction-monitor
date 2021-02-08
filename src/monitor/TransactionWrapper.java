package monitor;//import monitor.UOW;
//import monitor.Callable;

import java.sql.SQLException;
import java.util.concurrent.Callable;

/**
 * The type Transaction wrapper.
 * The class responsible for wrapping DataBases and WebService objects.
 * It helps in correctly execute commands on corresponding objects in separate threads (one for each database and web service).
 */
public class TransactionWrapper implements Callable<Integer> {

    /**
     * The Uow.
     */
    UOW uow;
    /**
     * The Mode.
     */
    Integer mode = 1;
    /**
     * The Has commit.
     */
    Boolean hasCommit = Boolean.FALSE;

    /**
     * Instantiates a new Transaction wrapper.
     *
     * @param base the base
     */
    public TransactionWrapper (DataBase base){
            uow = new UOW_DataBase(base);
    }

    /**
     * Instantiates a new Transaction wrapper.
     *
     * @param base the base
     */
    public TransactionWrapper (WebService base) {
            uow = new UOW_WebService(base);
    }

    /**
     * Start the transaction, decide: commit or rollback and check if transaction was committed
     *
     * @return the status as integer value, 10 for success, -10 for fail
     */
    Integer startTransaction (){
        int status1phase;
        int status = uow.startTransaction();

        if (status == -10){
            status1phase = this.rollback();
        }
        else{
            status1phase = this.commit();
        }
        if (status1phase == 20){
            hasCommit = Boolean.TRUE;
        }
        return status1phase;
    }

    /**
     * Execute function commit from class UOW and returns returned value.
     *
     * @return the status as integer value, 20 for success, -20 for fail
     */
    Integer commit (){
        return uow.commit();
    }

    /**
     * Execute function rollback from class UOW and returns returned value.
     *
     * @return the status as integer value, 30 for success, -30 for fail
     */
    Integer rollback (){
        return uow.rollback();
    }

    /**
     * Execute function rollback2phase from class UOW and returns returned value.
     *
     * @return the status as integer value, 40 for success, -40 for fail
     * @throws SQLException the sql exception
     */
    Integer rollback2phase () throws SQLException {
        return uow.rollback2phase();
    }

    /**
     * Execute function finalizeTransaction from class UOW and returns returned value.
     *
     * @return the status as integer value, 50 for success, -50 for fail
     *
     */
    Integer finalizeTransaction (){
        return uow.finalizeTransaction();
    }

    /**
     * Call function is responsible for executing a specific operation in concurrent mode
     *
     * @return status of current operation on transaction component (based on mode)
     * @throws SQLException SQLException
     */
    public Integer call() throws SQLException {
        Integer status = 0;
        if(this.mode == 1){
            status = this.startTransaction();
        }else if (this.mode == 4){
            status = this.rollback2phase();
        }else if (this.mode == 5){
            status = this.finalizeTransaction();
        }
        return status;
    }
}

