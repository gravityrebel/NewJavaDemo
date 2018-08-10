package com.melnick.java8;

import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Think of streams as creating a pipeline which is acted upon by any number of intermediate
 * operations, and ending with a single terminal operation.
 *
 * Lots of the things in the Streams class are variants or special cases of map/reduce.
 *
 * Map is basically running some function upon any number of inputs,
 * and reduce is the act of taking all the inputs and outputting a single result. This could be
 * a number, or a collection, or a boolean, or really anything that is defined.
 *
 * There are two kinds of reduce operations, reduce and collect. Reduce returns a unique result
 * each time, and collect can use mutable operations and objects.
 *
 * From Java's documentation:
 * Collections and streams, while bearing some superficial similarities, have different goals.
 * Collections are primarily concerned with the efficient management of, and access to, their
 * elements. By contrast, streams do not provide a means to directly access or manipulate their
 * elements, and are instead concerned with declaratively describing their source and the
 * computational operations which will be performed in aggregate on that source. However,
 * if the provided stream operations do not offer the desired functionality, the
 * BaseStream.iterator() and BaseStream.spliterator() operations can be used to perform a
 * controlled traversal.
 *
 * What this says to me:
 * Streams are there for when accessing elements is not important, and you do not want to modify the
 * data in the collection. The actual access of the * elements is a JDK/compiler concern that is
 * offloaded from the programmer. The stream allows the programmer to be clear and declarative about
 * the exact operations they are performing, making intent more clear.
 *
 * Because the access of the elements is taken care of by the language, it makes things like
 * parallel processing trivial.
 */
public class StreamExample {


    // Lets define some class called Transaction for use.
    static class Transaction {

        private TransactionStatus status;
        private TransactionType type;
        private Integer timeInSeconds;
        private Record parentRecord;

        Transaction(TransactionStatus status, TransactionType type, Integer timeInSeconds) {
            this.status = status;
            this.type = type;
            this.timeInSeconds = timeInSeconds;
        }

        Integer getTimeInSeconds() {
            return timeInSeconds;
        }
        Record getRecord() {
            return parentRecord;
        }
        void setRecord(Record parentRecord) {
            this.parentRecord = parentRecord;
        }
        TransactionType getType() {
            return type;
        }
        TransactionStatus getStatus() {
            return status;
        }
    }

















    @Test
    public void basicStream() {
        Collection<Transaction> collection = createCollection();
        long count = 0;
        for (Transaction streamExample : collection) {
            if (streamExample.type == TransactionType.IAFIS) {
                count++;
            }
        }

        long streamCount = collection.stream()
                .filter(transaction -> transaction.type == TransactionType.IAFIS)
                .count();

        Assert.assertEquals(count, streamCount);
    }

















    /**
     *  Streams have two types of operations, intermediate, and terminal. You can string any
     *  number of intermediate operations together, as they produce new streams.
     *  Terminal operations produce a result.
    */
    public void streamOperations() {
        Collection<Transaction> collection = createCollection();

        /*the desired behavior: from all the items in the collection, get the parent record,
        then get a list of all the "DONE" transactions for all the records.*/
        //traditional
        Set<Transaction> noDuplicates = new HashSet<>(collection);
        List<Transaction> traditionalDoneList = new ArrayList<>();
        for (Transaction transaction : noDuplicates) {
            for (Transaction transaction1 : transaction.getRecord().getTransactions()) {
                if (transaction.getStatus() == TransactionStatus.DONE) {
                        traditionalDoneList.add(transaction1);
                }
            }
        }

        /*
          This is a pipeline of work done on a source. It is clear that everything is
          performed on the source.

          Reading a stream pipeline like this is sort of like containing all the logic
          in it's own method. It is clear that this is a pipeline of work intended to
          do a single unit of work.
        */
        List<Transaction> streamedDoneList = collection.stream()
                .map(transaction -> transaction.getRecord().getTransactions())
                .flatMap(Collection::stream)
                .filter(transaction -> transaction.getStatus() == TransactionStatus.DONE)
                .distinct()
                .collect(Collectors.toList());

        Assert.assertEquals(traditionalDoneList, streamedDoneList);

        streamedDoneList = collection.stream()
                .flatMap(transactions -> transactions.getRecord().getTransactions().stream())
                .filter(transaction -> transaction.getStatus() == TransactionStatus.DONE)
                .distinct()
                .collect(Collectors.toList());

        Assert.assertEquals(traditionalDoneList, streamedDoneList);
    }
















    @Test
    public void aggregateWaitTime() {
        Collection<Transaction> collection = createCollection();
        //traditional
        int waitTime = 0;
        for (Transaction streamExample : collection) {
            waitTime += streamExample.getTimeInSeconds();
        }
        System.out.println("Aggregate wait time is: " + waitTime);

        //pipeline
        int aggregateWaitTime = collection.stream()
                .mapToInt(Transaction::getTimeInSeconds)
                .sum();
        System.out.println("Aggregate wait time is: " + aggregateWaitTime);
    }

















    public static void filteredWaitTimes() {
        Collection<Transaction> collection = createCollection();

        //same tradtional code
        int waitTime = 0;
        for (Transaction streamExample : collection) {
            if (streamExample.status == TransactionStatus.DONE) {
                waitTime += streamExample.getTimeInSeconds();
            }
        }


        int aggregateWaitTime = collection.stream()
                .filter(e -> e.status == TransactionStatus.DONE)
                .mapToInt(Transaction::getTimeInSeconds)
                .sum();
        System.out.println("Aggregate wait time is: " + aggregateWaitTime);


        System.out.println("Aggregate wait time is: " + waitTime);
    }











    public static void modifyingOperations() {
        List<Integer> intList = new Random().ints(100).boxed().collect(Collectors.toList());

        intList.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList()); //This does not sort the list!

        intList.sort(null); //This sorts the list!

    }



















    public static void parallelOperations() {
        LocalTime before = LocalTime.now();
        System.out.println(new Random().ints(5_000_000).parallel().sorted().count());
        LocalTime after = LocalTime.now();
        System.out.println("Duration was: " + Duration.between(before, after).toString());

        before = LocalTime.now();
        System.out.println(new Random().ints(10_000_000).sorted().count());
        after = LocalTime.now();
        System.out.println("Duration was: " + Duration.between(before, after).toString());

        LocalTime before1 = LocalTime.now();

        System.out.println(new Random().ints(10_000_000).parallel().sorted().count());
        LocalTime after1 = LocalTime.now();
        System.out.println("Duration was: " + Duration.between(before1, after1).toString());
    }











    /*
    * Map is a fundamental operation. The map operation takes in the value from the stream,
    * performs an operation on it, and returns a value.
    *
    * To show this, lets take a list of integers, and return a list of String
    * representations of the integers.
    * */
    public void mapOperation() {
        List<Integer> integerList = new Random().ints(100).boxed().collect(Collectors.toList());

        //The MAP operation in work.
        List<String> intsAsStrings = integerList.stream()
                .map(integer -> integer.toString())
                .collect(Collectors.toList());

        //same code with method reference
        List<String> intAsString = integerList.stream()
                .map(Objects::toString)
                .collect(Collectors.toList());
    }














    /*flatMap() is an important operation for dealing with nested objects.
    * Essentially what it does is take in a stream of streamable objects, and flattens it into a
    * single stream. Lets take a look*/
    @Test
    public void flatMapOperation() {
        List<Image> subjectA = Arrays.asList(ImageType.FINGERPRINT, ImageType.MUGSHOT);
        List<Image> subjectB = Arrays.asList(ImageType.MUGSHOT, ImageType.FINGERPRINT);

        List<List<Image>> subjectImagesList = Arrays.asList(subjectA, subjectB);

        /*OK, at this point we have a List of Lists. What would happen if we called .stream() on
        subjectImageList? It would return a stream with two items in it: subjectA (a list), and
        subjectB (a list). We want to work on all the Images though! Not the lists!

        Lets try and get a list of all the status's from this master list.
        */

        //traditional
        List<CaptureStatus> captureStatusList = new ArrayList<>();
        for (List<Image> imageList : subjectImagesList) {
            for (Image image : imageList) {
                captureStatusList.add(image.getStatus());
            }
        }


        /*flatMap. We map the incoming List<Image> to a stream. FlatMap will then combine the
        results of the multiple streams into a single stream on which to work.*/
        List<CaptureStatus> captureStatusListFlatMap = subjectImagesList.stream()
                .flatMap(subjectList -> subjectList.stream()) //for each List<Image>, get it's stream. the function then combines them
                .map(image -> image.getStatus())
                .collect(Collectors.toList());

        Assert.assertEquals(captureStatusList, captureStatusListFlatMap);

        //same using method references
        captureStatusListFlatMap = subjectImagesList.stream()
                .flatMap(Collection::stream)
                .map(Image::getStatus)
                .collect(Collectors.toList());

        Assert.assertEquals(captureStatusList, captureStatusListFlatMap);
    }












    private List<String> getReceiptNumbersIterative(Collection<BenefitRequest> benefitRequests) {
        List<String> receiptNumbers = new ArrayList<>();
        for (BenefitRequest benefitRequest : benefitRequests) {
            receiptNumbers.add(benefitRequest.getUscisReceiptNumber());
        }
        return receiptNumbers;
    }











    // Returns List instead of Array for reuse in completeCasePaymentTasks in next example
    private List<String> getReceiptNumbers(Collection<BenefitRequest> benefitRequests) {
        return benefitRequests
            .stream()
            .map(BenefitRequest::getUscisReceiptNumber)
            .collect(Collectors.toList());
    }














    private void completeCasePaymentTasksIterative(Collection<BenefitRequest> benefitRequests) {
        List<String> receiptNumbers = getReceiptNumbers(benefitRequests);
        List<Case> caseList = caseDAO.findCasesByReceiptList(receiptNumbers);
        for (Case cisCase : caseList) {
            if (shouldUpdateCasePaymentTask(cisCase)) {
                workflowEventService.updateTaskAfterEvent(
                    new TaskEventKeyHolder(TaskType.CASE_PAYMENT_VALIDATION, TaskStatus.COMPLETED),
                    cisCase.getCaseIdentifier(), null);
            }
        }
    }










    private void completeCasePaymentTasks(Collection<BenefitRequest> benefitRequests) {
        List<String> receiptNumbers = getReceiptNumbers(benefitRequests);
        TaskEventKeyHolder taskEvent = new TaskEventKeyHolder(TaskType.CASE_PAYMENT_VALIDATION, TaskStatus.COMPLETED);
        caseDAO
            .findCasesByReceiptList(receiptNumbers)
            .stream()
            .filter(this::shouldUpdateCasePaymentTask)
            .map(Case::getCaseIdentifier)
            .forEach(cisCaseId ->
                workflowEventService.updateTaskAfterEvent(
                    taskEvent, cisCaseId, null));
    }






    public static Collection<Transaction>  createCollection() {
        List<Transaction> collection = new ArrayList<>();
        collection.add(new Transaction(TransactionStatus.DONE, TransactionType.IAFIS, 30));
        collection.add(new Transaction(TransactionStatus.DONE, TransactionType.ABIS, 60));
        collection.add(new Transaction(TransactionStatus.IN_PROGRESS, TransactionType.IAFIS, 30));
        collection.add(new Transaction(TransactionStatus.IN_PROGRESS, TransactionType.IDENT, 50));
        collection.add(new Transaction(TransactionStatus.SENDING, TransactionType.IDENT, 20));
        collection.add(new Transaction(TransactionStatus.SENDING, TransactionType.ABIS, 60));

        return collection;
    }

    public static void main(String[] args) {
        //basicStream();
        //aggregateWaitTime();
        //filteredWaitTimes();
        parallelOperations();
    }

    interface Image {CaptureStatus getStatus(); CaptureType getType();}
    enum ImageType implements Image {
        MUGSHOT,
        FINGERPRINT;

        @Override public CaptureStatus getStatus() {
            return null;
        }

        @Override public CaptureType getType() {
            return null;
        }
    }
    interface CaptureType{boolean isFinger();}
    interface Record{
        List<Transaction> getTransactions();
    }
    enum CaptureStatus {C;}
    enum CaptureMode{TWO_PRINT(2), TEN_PRINT(10);

        public int getPrints() {
            return prints;
        }

        public void setPrints(int prints) {
            this.prints = prints;
        }

        int prints;
        CaptureMode(int i) {
            this.prints  = i;
        }
    }
    enum TransactionStatus {SENDING, IN_PROGRESS, DONE}
    enum TransactionType {IDENT, IAFIS, ABIS;}



    interface BenefitRequest { String getUscisReceiptNumber();}
    interface Case{String getCaseIdentifier();}

    class TaskEventKeyHolder{

        public TaskEventKeyHolder(TaskType type, TaskStatus status) {

        }
    }

    class SomeDAO{

        List<Case> findCasesByReceiptList(Object whatever) {
            return null;
        }
    }

    class SomeWorkflow{
        void updateTaskAfterEvent(TaskEventKeyHolder event, String foo, Object bar) {}
    }
    private SomeDAO caseDAO = new SomeDAO();
    private SomeWorkflow workflowEventService = new SomeWorkflow();
    enum TaskType{CASE_PAYMENT_VALIDATION}
    enum TaskStatus{COMPLETED}
    boolean shouldUpdateCasePaymentTask(Case theCase) {
        return true;
    }
}
