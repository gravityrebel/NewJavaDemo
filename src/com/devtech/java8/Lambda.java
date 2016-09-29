package com.devtech.java8;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * WELCOME TO THE WORLD OF LAMBDA'S
 * FUNCTIONAL PROGRAMING PARADIGMS HAVE BEEN ADDED TO JAVA!
 *
 * Lambda's are a functional notation. The concepts here are different than typical programing
 * that we are all used to, but they are very useful and powerful.
 *
 *
 * Functional programing is based on the idea of passing functions AS PARAMETERS.
 * That is to say methods have generic USES, and the specific behavior you want done is passed
 * dynamically at runtime.
 *
 */
public class Lambda {


    /**
     * Java introduced the concept of a FunctionalInterface.
     * A FunctionalInterface is an interface with only ONE abstract method.
     *
     * For our first example we will look at the interface Iterable, which has added a new default
     * method forEach(Consumer<? super T> action).
     *
     * The consumer is a FunctionalInterface, and it's behavior is undefined.
     * @see Consumer and note how it has one method accept().
     *
     * Now lets look at the implementation of forEach, which is defined by the IterableInterface
     * @see Iterable#forEach(Consumer)
     *
     *    default void forEach(Consumer<? super T> action) {
     *      Objects.requireNonNull(action);
     *      for (T t : this) {
     *          action.accept(t);
     *      }
     *    }
     * You can see here that the forEach method is literally running a forEach loop, but since action
     * is an interface, the behavior of it's method accept(t) is undefined. This is the underlying
     * concept of lambda expressions: we can define the behavior of accept(t) at runtime by passing
     * in an instance of an anonymous class. The lamda notation exists as a shorthand for defining
     * instances of an anonymous class when that class only has one abstract method.
     *
     * NOTE: This is not actually how lambdas are implemented under the hood, at all. There are
     * differences that are important, such as who {@code this} refers to, and exactly when the
     * instance is created. Stating that it is a shorthand for anonymous class creation is a decent
     * approximation though.
     *
     * As a note, we will go over default methods and static interface methods later.
     */
    public void simpleLambda() {
        List<String> stoogeList = Arrays.asList("Larry", "Curly", "Moe");

        //A traditional For Loop. The iteration is done by the programmer.
        for (String stooge : stoogeList) {
            System.out.println(stooge);
        }

        /** An implementation of the class Consumer. See how we define the behavior of accept()
          explicitly here, then look at the implementation of forEach() above. Note how now the
          traditional for loop and the below code are identical. Except that the choice of how to
          do the iteration is done by the class, not the programmer.*/
        stoogeList.forEach(new Consumer<String>() {
            @Override public void accept(String stooge) {
                System.out.println(stooge);
            }
        });


        /* Below is the lambda syntax.
         This is a SHORTHAND! Lambda's are shorthands for their functional interfaces.
         This is the same code as above. The way I have grown to read this code is:
         for each "stooge" such that: System.out.println("stooge")*/
        stoogeList.forEach(stooge -> System.out.println(stooge));

        /*This is a method reference. It is a shorthand for calling a single method that takes
          all of the parameters getting passed in. It is the same as all the above code.*/
        stoogeList.forEach(System.out::println);
    }

















    /**
     * Functional programing allows classes to implement the best solution for each "type" of operation.
     *
     * Below we have another simple case of lambda expression being used. First we will see
     * the traditional way of writing code, followed by defining the FunctionalInterface, ending
     * with the lambda syntax.
     *
     * Above we did a simple for loop. Below we will do something a bit more complicated, but the
     * end result is cleaner, more readable code.
     *
     * Lets say we want to replace or append all the values in a List. We can't actually do this
     * in a forEach style loop. It's a bit more complicated than that.
     *
     * @see List#replaceAll(UnaryOperator)
     *  default void replaceAll(UnaryOperator<E> operator) {
     *      Objects.requireNonNull(operator);
     *      final ListIterator<E> li = this.listIterator();
     *      while (li.hasNext()) {
     *          li.set(operator.apply(li.next()));
     *      }
     *  }
     *
     *  But this is the GENERIC solution to the problem. Each class gets to define it's own
     *  more efficient implementation.
     */
    public void anotherSimpleCase() {
        List<String> stoogeList = Arrays.asList("Larry", "Curly", "Moe");

        /* In order to do a global replace on a List, below is the Java 7 and below way of doing it.
         What's more is that every time you want to replicate this functionality, you need to
         re-write this paradigm (or include it in a utility class).*/
        ListIterator<String> stringListIterator = stoogeList.listIterator();
        while (stringListIterator.hasNext()) {
            String replacementString = stringListIterator.next() + " is a stooge";
            stringListIterator.set(replacementString);
        }

        /** Look at the above implementation of
         * @see List#replaceAll(UnaryOperator)
         * Below is an implementation of the apply() method getting used by List when replaceAll()
         * is called.*/

        stoogeList.replaceAll(new UnaryOperator<String>() {
            @Override public String apply(String stooge) {
                return stooge + " is a stooge";
            }
        });

        /**Below is the lambda syntax. Again this is a SHORTHAND for the above code.
         We have already seen. This can actually be more efficient than the above solution if an
         ArrayList was used.
         @see java.util.ArrayList#replaceAll(UnaryOperator)
         It uses the array directly, never invoking an iterator, and takes care of
         ConcurrentModification
         */

        stoogeList.replaceAll(stooge -> stooge + " is a stooge");
    }











   public void aSwingExample() {
        JButton importBtn = new JButton();
        importBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                Object[] options = { "Yes", "No" };
                String messageDisplay =
                        "This action will overwrite any information you have already entered for the current subject.\n" +
                                "Do you still want to import?\n";
                int resp =
                        JOptionPane.showOptionDialog(null, messageDisplay, "Import", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (resp == JOptionPane.YES_OPTION) {
                    BiometricAction transaction = getTransaction();
                    if (transaction.getExternalId(ExternalIDType.FIN) != null) {
                        // extract FINS from response
                        String fins = transaction.getExternalId(ExternalIDType.FIN);
                    }
                }
            }
        });
    }
















    public void aSwingExampleLambda() {
        JButton importBtn = new JButton();
        importBtn.addActionListener(event -> {
            Object[] options = { "Yes", "No" };
            String messageDisplay =
                    "This action will overwrite any information you have already entered for the current subject.\n" +
                            "Do you still want to import?\n";
            int resp =
                    JOptionPane.showOptionDialog(null, messageDisplay, "Import", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (resp == JOptionPane.YES_OPTION) {
                BiometricAction transaction = getTransaction();
                if (transaction.getExternalId(ExternalIDType.FIN) != null) {
                    // extract FINS from response
                    String fins = transaction.getExternalId(ExternalIDType.FIN);
                }
            }
        });
    }













    public static void main(String[] args) {
        Lambda lambda = new Lambda();
        lambda.simpleLambda();
    }
    private BiometricAction getTransaction() {
        return (externalIDType) -> "fooBar";
    }

    interface BiometricAction{String getExternalId(ExternalIDType type); }
    enum ExternalIDType{FIN}
}
