package com.melnick.java7;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project Coin was Java's project to include "nice to have" features that were fairly simple
 * to implement. These were ideas submitted by the community.
 */
public class ProjectCoin {
    private static final String YES = "Y";
    private static final String TRUE = "T";
    private static final String NO = "N";
    private static final String FALSE = "F";
    private static final String MAYBE = "M";
















    /**
     * Very nice change. The right hand side will always infer the type information from the left.
     * @param oldList oldList
     * @param <E> element
     */
    public <E> void diamondOperators(List<E> oldList) {
        List<String> stringList = new ArrayList<>(); //infers Type.
        Map<Integer, List<Map<String, String>>> map = new HashMap<>(); //please don't


        List<E> newList= oldList;
        newList = new ArrayList<>(); //This infers Type

       // List<String> wontWork = new ArrayList<>(newList);
    }
















    @Test
    public void underscoresInNumericLiterals() {
        int hardToRead = 1000000;
        int easyToRead = 1_000_000;
        Assert.assertEquals("The numbers are not equal", hardToRead, easyToRead);

       // System.out.println("The number are exactly the same: " + (hardToRead == easyToRead));
    }
















    /**
     * Precise Rethrow. Java now allows you to re-throw errors even if they have been caught as
     * a different type. Notice how this method catches ALL exceptions, but throws the specific ones.
     * @param s some String
     * @throws NullPointerException
     * @throws ClassCastException
     * @throws UnsupportedOperationException
     */
    public void preciseRethrow(String s) throws NullPointerException, ClassCastException, UnsupportedOperationException{
        try {
            throwsError(s);
        } catch (Exception e) {
            System.out.println("Caught generic Exception. Re-throwing to calling class");
            throw e;
        }
    }





    @Test
    public void multiCatchTest() {
        multiCatch(null);
        multiCatch("");
        multiCatch("0");
        multiCatch("Hello World");
    }










    /**
     * Notice how the first catch has multiple exceptions that it is catching. Makes the need to
     * cast up much less when throwing exceptions.
     * @param string input
     */
    public void multiCatch(String string) {
        try {
            preciseRethrow(string);
        } catch (NullPointerException | ClassCastException e) {
            System.out.println("String was null or Empty. " + e.getMessage() + "\n");
        } catch (UnsupportedOperationException e) {
            System.out.println("The number 0 is not supported. " + e.getMessage() + "\n");
        }
    }













    /**
     * This is a typical coding paradigm. It is also ugly and hard to read.
     * @param input
     * @return
     */
    public String ifElseStrings(String input) {
        String result;
        if (YES.equals(input) || TRUE.equals(input)) {
            someMethod();
            result = TRUE;
        } else if (NO.equals(input) || FALSE.equals(input)) {
            someOtherMethod();
            result = FALSE;
        } else if (MAYBE.equals(input)) {
            someThirdMethod();
            result = MAYBE;
        }
        else {
            result = "UNKNOWN";
        }
        return result;
    }
















    /**
     * Switch should be pretty easy for everybody to understand. But something to note here is that
     * you can take advantage of the "fall through" that occurs if you don't add a break statement
     * to replicate OR statements.
     * Case in point: if YES, it will immediately start falling through the switch block and run all
     * code until it reaches a break. In this case, both YES and TRUE will run someMethod(), but
     * nothing else will.
     *
     * The actual implementation of switch with strings is basically the same as a lookup in a
     * HashSet.
     *
     * First the hashCode() is checked, which is used to implement an int based switch statement.
     * then in each case block, .equals is used to ensure there were no hash collisions.
     * @param input input
     * @return output
     */
    public String switchStrings(String input) {
        String result;
        switch (input) {
        case YES:
        case TRUE:
            someMethod();
            result = TRUE;
            break;
        case NO:
        case FALSE:
            someOtherMethod();
            result = FALSE;
            break;
        case MAYBE:
            someThirdMethod();
            result = MAYBE;
            break;
        default:
            result = "UNKNOWN";
        }
        return result;
    }


    private String throwsError(String string) throws NullPointerException, ClassCastException, UnsupportedOperationException {
        if (string == null) {
            throw new NullPointerException("String is null");
        } else if (string.isEmpty()) {
            throw new ClassCastException("String is empty");
        } else if ("0".equals(string)) {
            throw new UnsupportedOperationException("Unsupported Operation");
        }
        return string;
    }
















    private void someMethod(){}

    private void someOtherMethod(){}
    private void someThirdMethod(){}
    public static void main(String[] args) {
        ProjectCoin sample = new ProjectCoin();
        sample.multiCatch(null);
        sample.multiCatch("");
        sample.multiCatch("0");
        sample.multiCatch("Hello World");
        sample.underscoresInNumericLiterals();
    }
}
