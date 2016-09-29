package com.devtech.java8;

import com.devtech.java8.StreamExample.Transaction;
import com.devtech.java8.StreamExample.TransactionStatus;

import java.time.Duration;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.devtech.java8.StreamExample.createCollection;

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

    public void functionalPrograming() {
        Collection<Transaction> transactions = createCollection();

        //We want to force a re-send of transactions that have been In Progress
        //for more than 10 minutes
        Set<Transaction> transactionsToResend =
                filterTransactions(transactions, tookLongerThan10Minutes());



        //We want all transactions that are Done in order to do reporting
        Set<Transaction> doneTransactions = filterTransactions(transactions, isDone());


        //ad-hoc
        Set<Transaction> identTransactions = filterTransactions(transactions,
                transaction -> transaction.getType() == StreamExample.TransactionType.IDENT);

    }




    private Predicate<Transaction> isDone() {
        return transaction -> transaction.getStatus() == TransactionStatus.DONE;
    }


    private Predicate<Transaction> tookLongerThan10Minutes() {
        return transaction ->
                transaction.getTimeInSeconds() > Duration.ofMinutes(10).getSeconds();
    }


    public Set<Transaction> filterTransactions(Collection<Transaction> transactions, Predicate<Transaction> predicate) {
        return transactions.stream()
                .filter(predicate)
                .collect(Collectors.toSet());
    }
}
