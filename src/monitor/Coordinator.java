package monitor;

import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Coordinator {

     List<TransactionWrapper> TransactionWrapperList = new ArrayList<TransactionWrapper>();
     ExecutorService executor;
     List<Future<Integer>> resultList = null;

     Coordinator() throws InterruptedException {

     }

     void main(){
          while (true) {
               execute();
               status();
               int sum = 0;
               for (TransactionWrapper status : TransactionWrapperList) {
                    sum += Math.abs(status.mode);
               }
               System.out.println("Sum " + sum);
               if (sum == 0) break;
          }
     }
     void execute() {
          //Execute all tasks and get reference to Future objects
          executor = (ExecutorService) Executors.newFixedThreadPool(2);
          try {
               resultList = executor.invokeAll(TransactionWrapperList); //new ArrayList<TransactionWrapper>(TransactionWrapperModeMap.keySet())
          } catch (InterruptedException e) {
               e.printStackTrace();
          }
          executor.shutdown();
     }


     void status() {
          int i = 0;
          for (Future<Integer> future : resultList) {
               try {
                    Integer result = future.get();
                    System.out.println("Result " + result);
                    if(result == 10){
                         TransactionWrapperList.get(i).mode = 2;
                    }
                    else if(result == -10){
                         for (TransactionWrapper transactionWrapper : TransactionWrapperList) {
                              if (transactionWrapper.hasCommit == Boolean.TRUE) {
                                   transactionWrapper.mode = 3;
                              } else {
                                   transactionWrapper.mode = 5;
                              }
                         }
                         break;
                    }
                    else if(result == 20){
                         TransactionWrapperList.get(i).mode = 5;
                    }
                    else if(result == -20){
                         for (TransactionWrapper transactionWrapper : TransactionWrapperList) {
                              if (transactionWrapper.hasCommit == Boolean.TRUE) {
                                   transactionWrapper.mode = 3;
                              } else {
                                   transactionWrapper.mode = 5;
                              }
                         }
                         break;
                    }
                    else if(result == 30){
                         TransactionWrapperList.get(i).mode = 4;
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


     void clearResults(){
          resultList = null;
     }


}

