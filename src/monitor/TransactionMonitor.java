package monitor;

/**
 * The type TransactionMonitor
 * Class available for the user. Allows to create transactions on selected databases/web services
 * with selected set of commands for each of them.
 */
public class TransactionMonitor{

    /**
     * The Coordinator.
     */
    Coordinator coordinator;

    /**
     * Start transaction.
     */
    public void startTransaction (  ){
        coordinator.main();
    }

    /**
     * Add data base.
     *
     * @param dataBase the data base
     */
    public void addDataBase(DataBase dataBase){
        this.coordinator.TransactionWrapperList.add(new TransactionWrapper(dataBase));
    }

    /**
     * Add web service.
     *
     * @param webService the web service
     */
    public void addWebService(WebService webService){
        this.coordinator.TransactionWrapperList.add(new TransactionWrapper(webService));
    }

    /**
     * Instantiates a new Transaction monitor.
     *
     */
    public TransactionMonitor(){
        coordinator = new Coordinator();
    }
}

