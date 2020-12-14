package monitor;

public class TransactionMonitor{

    Coordinator coordinator;

    public void startTransaction (  ){
        coordinator.main();
    }

    public void addDataBase(DataBase dataBase) throws ClassNotFoundException {
        this.coordinator.TransactionWrapperList.add(new TransactionWrapper(dataBase));
    }

    public void addWebService(WebService webService) throws ClassNotFoundException {
        this.coordinator.TransactionWrapperList.add(new TransactionWrapper(webService));
    }

    public TransactionMonitor() throws InterruptedException {
        coordinator = new Coordinator();
    }
}

