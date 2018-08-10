package com.melnick.java8;

import com.melnick.java8.StreamExample.Transaction;
import com.melnick.java8.StreamExample.TransactionStatus;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.melnick.java8.StreamExample.createCollection;

/**
 * Functional programing allows you to design generic functions and provide that functionality
 * at runtime. There have been a few pre-done Predicates and a generic method.
 *
 * There is also an example here of passing in this functionality in an ad-hoc manner.
 * Also to note is that each return of a set is a brand new collection.
 *
 * The source collection has been unmodified.
 *
 */
public class Functional {



    //Let's define some generic behavior. This is something that may occur in many places in a codebase.
    public Set<Transaction> filterTransactions(Collection<Transaction> transactions, Predicate<Transaction> predicate) {
        Set<Transaction> set = new HashSet<>();
        for (Transaction transaction : transactions) {
            if (predicate.test(transaction)) {
                set.add(transaction);
            }
        }
        return set;
    }

    public void functionalPrograming() {
        Collection<Transaction> transactions = createCollection();

        //We want to force a re-send of transactions that have been In Progress
        //for more than 10 minutes
        /*
            We can pre-define different methods that return a Functional Type. This is useful
            if you are potentially performing the same sets of behavior in multiple places.

            In this case, if checking to see if transactions took longer than ten minutes was
            done more than once, it may make sense to turn this into a a method.

         */
        Set<Transaction> transactionsToResend =
                filterTransactions(transactions, tookLongerThan10Minutes());



        /*
            We want all transactions that are Done in order to do reporting

            Here we are passing a method reference. How do we know when a method reference will work?
            Look no further than the method signature.

            The method filterTransactions() takes a Predicate. The signature for a predicate's
            abstract method test() is: boolean test(T t). Any method that takes in an appropriate
            type T and returns a boolean will match up.

            Commonly this is done by calling a method from a class. In this case the method must be
            static.

            You can reference 'this' or specific objects as well. In these cases it can be non-static.

            Look at the signatures!
        */
        Set<Transaction> longRunningTransactions = filterTransactions(transactions, this::tookLongerThanTenMinutes);
        Set<Transaction> doneTransactions = filterTransactions(transactions, Functional::isDone);


        TransactionCollection transactionCollection = new TransactionCollection(transactions);
        filterTransactions(transactionCollection, transactionCollection::isLongerThan10Minutes);


        /*
            Ad-Hoc

            We can also create an anonymous function. We define the body of test(T t) so that it
            returns a boolean value.
         */
        Set<Transaction> identTransactions = filterTransactions(transactions,
                transaction -> transaction.getType() == StreamExample.TransactionType.IDENT);

        Long totalDoneTransactionTime = filterTransactionsAndSum(transactions, Functional::isDone,
            Transaction::getTimeInSeconds);

    }







    /**
     * WHEN WOULD WE CREATE OUR OWN LAMBDA FUNCTIONS?
     *
     * ANYTIME YOU DETERMINE A REPEATABLE PATTERN WITH DIFFERENT IMPLEMENTATIONS
     */

    public void aCommonPatternIsFilterThenMap() {
        List<String> marxBrothers = Arrays.asList("Larry", "Curly", "Moe");
        List<Character> theYBrothers = new ArrayList<>();
        for (String brother : marxBrothers) {
           if (brother.endsWith("Y")) {
                theYBrothers.add(brother.charAt(0));
            }
        }
    }












    public static <T, R> Optional<List<R>> filterThenMap(
        final List<T> inputList,
        final Predicate<T> filter,
        final Function<T, R> function) {

        List<R> returnList = new ArrayList<>();
        for (T item : inputList) {
            if (filter.test(item)) {
                returnList.add(function.apply(item));
            }
        }
        return returnList.isEmpty() ? Optional.empty() : Optional.of(returnList);
    }












    public static <T, R> Optional<List<R>> mapThenFilter(
        final List<T> inputList,
        final Function<T, R> mappingFunction,
        final Predicate<R> filter) {

        List<R> returnList = inputList.stream()
            .map(mappingFunction)
            .filter(filter)
            .collect(Collectors.toList());

        return returnList.isEmpty() ? Optional.empty() : Optional.of(returnList);
    }











    public void usingLambdaMethods() {
        List<String> marxBrothers = Arrays.asList("Larry", "Curly", "Moe");

        Optional<List<Character>> theYBrothers =
            filterThenMap(
                marxBrothers,
                brother -> brother.endsWith("Y"),
                name -> name.charAt(0)
            );

        Optional<List<String>> theLBrothers = mapThenFilter(marxBrothers, Function.identity(),
            name -> name.toLowerCase().contains("l"));
    }







    private Predicate<Transaction> tookLongerThan10Minutes() {
        return transaction ->
            transaction.getTimeInSeconds() > Duration.ofMinutes(10).getSeconds();
    }

    private boolean tookLongerThanTenMinutes(Transaction transaction) {
        return transaction.getTimeInSeconds() > Duration.ofMinutes(10).getSeconds();
    }

    private static boolean isDone(Transaction transaction) {
        return transaction.getStatus() == TransactionStatus.DONE;
    }



    private static Long filterTransactionsAndSum(Collection<Transaction> transactions,
        Predicate<Transaction> predicate, ToLongFunction<Transaction> function) {
        return transactions.stream()
            .filter(predicate)
            .mapToLong(function)
            .sum();
    }








    class TransactionCollection implements Collection<Transaction> {

        private final Collection<Transaction> collection;

        public TransactionCollection(Collection<Transaction> collection) {
            this.collection = collection;
        }


        public boolean isLongerThan10Minutes(Transaction transaction) {
            return transaction.getTimeInSeconds() > Duration.ofMinutes(10).getSeconds();
        }


        @Override
        public int size() {
            return collection.size();
        }

        @Override
        public boolean isEmpty() {
            return collection.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return collection.contains(o);
        }

        @Override
        public Iterator<Transaction> iterator() {
            return collection.iterator();
        }

        @Override
        public void forEach(Consumer<? super Transaction> action) {
            collection.forEach(action);
        }

        @Override
        public Object[] toArray() {
            return collection.toArray();
        }

        @Override
        public <T1> T1[] toArray(T1[] a) {
            return collection.toArray(a);
        }

        @Override
        public boolean add(Transaction t) {
            return collection.add(t);
        }

        @Override
        public boolean remove(Object o) {
            return collection.remove(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return collection.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends Transaction> c) {
            return collection.addAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return collection.removeAll(c);
        }

        @Override
        public boolean removeIf(Predicate<? super Transaction> filter) {
            return collection.removeIf(filter);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return collection.retainAll(c);
        }

        @Override
        public void clear() {
            collection.clear();
        }

        @Override
        public Spliterator<Transaction> spliterator() {
            return collection.spliterator();
        }

        @Override
        public Stream<Transaction> stream() {
            return collection.stream();
        }

        @Override
        public Stream<Transaction> parallelStream() {
            return collection.parallelStream();
        }
    }
}
