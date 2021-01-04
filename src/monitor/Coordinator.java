package monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * The type Coordinator.
 * Class responsible for the proper conduct of the transaction.
 * It has a list of objects included in transaction and after execute user statements it check the operation status.
 * According to status coordinator send commit or rollback request on each transaction object.
 */
public class Coordinator {

     /**
      * The Transaction wrapper list.
      */
     List<TransactionWrapper> TransactionWrapperList = new ArrayList<TransactionWrapper>();
     /**
      * list of the transaction components
      */
     ExecutorService executor;
     /**
      * The Result list.
      */
     List<Future<Integer>> resultList = null;

     /**
      * Instantiates a new Coordinator.
      */
     Coordinator() {}

     /**
      * Function responsible for the operations of the program.
      */
     void main(){
          long startTime = System.currentTimeMillis();
          while (true) {
               execute();
               status();
               int sum = 0;
               for (TransactionWrapper status : TransactionWrapperList) {
                    sum += Math.abs(status.mode);
               }
               if (sum == 0) break;
               if (System.currentTimeMillis() - startTime >= 480000){
                    for (TransactionWrapper transactionWrapper : TransactionWrapperList) {
                         if (transactionWrapper.hasCommit == Boolean.TRUE) {
                              transactionWrapper.mode = 4;
                              transactionWrapper.hasCommit = Boolean.FALSE;
                         } else if (transactionWrapper.mode != 5){
                              transactionWrapper.mode = 5;
                         }
                    }
               }
          }
     }

     /**
      * Execute program using threads.
      */
     void execute(){
          executor = Executors.newFixedThreadPool(2);
          try {
               resultList = executor.invokeAll(TransactionWrapperList);
          } catch (InterruptedException e) {
               e.printStackTrace();
          }
          executor.shutdown();
     }


     /**
      * Iterates over all statements from list and checks which result of transaction is returned.
      * Every result leads to corresponding mode from TransactionWrapper class
      */
     void status() {
          int i = 0;
          for (Future<Integer> future : resultList) {
               try {
                    Integer result = future.get();

                    if(result == 20){
                         TransactionWrapperList.get(i).mode = 5;
                    }
                    else if(result == -20){
                         for (TransactionWrapper transactionWrapper : TransactionWrapperList) {
                              if (transactionWrapper.hasCommit == Boolean.TRUE) {
                                   transactionWrapper.mode = 4;
                              } else {
                                   transactionWrapper.mode = 5;
                              }
                         }
                         break;
                    }
                    else if(result == 30){
                         for (TransactionWrapper transactionWrapper : TransactionWrapperList) {
                              if (transactionWrapper.hasCommit == Boolean.TRUE) {
                                   transactionWrapper.mode = 4;
                              } else {
                                   transactionWrapper.mode = 5;
                              }
                         }
                         break;
                    }
                    else if(result == -30){
                         for (TransactionWrapper transactionWrapper : TransactionWrapperList) {
                              if (transactionWrapper.hasCommit == Boolean.TRUE) {
                                   transactionWrapper.mode = 4;
                              } else {
                                   transactionWrapper.mode = 5;
                              }
                         }
                         TransactionWrapperList.get(i).mode = 4;
                         break;
                    }
                    else if(result == 50 || result == -50){
                         TransactionWrapperList.get(i).mode = 0;
                    }
                    else if(result == 0){
                         TransactionWrapperList.get(i).mode = 0;
                    }
                    else{
                         TransactionWrapperList.get(i).mode = 5;
                    }
               } catch (InterruptedException e) {
                    e.printStackTrace();
               } catch (ExecutionException e) {
                    e.printStackTrace();
               }
               i++;
          }

          clearResults();
     }

     /**
      * Clear list of transaction components status results
      */
     void clearResults(){
          resultList = null;
     }

}

