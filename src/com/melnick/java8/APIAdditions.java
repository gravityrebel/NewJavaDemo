package com.melnick.java8;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ZMelnic on 12/28/2015.
 */
public class APIAdditions {
    private List<Integer> integerList = new Random().ints(100).boxed().collect(toList());

    /**
     * NOTE: List.sort() actually modifies the underlying data structure. The list is actually sorted.
     * @return
     */
    @Test
    public List<Integer> newListSort() {
        List<Integer> listCopy = new ArrayList<>(integerList);

        listCopy.sort(null); //sorts by natural ordering
        System.out.println(listCopy);

        listCopy.sort(Comparator.reverseOrder()); //sorts by comparator
        System.out.println(listCopy);
        return listCopy;
    }

    /**
     * NOTE: Stream does not modify the underlying datasource! After this method returns integerList
     * is still unsorted!
     * @return a new list.
     */
    public List<Integer> streamSort() {
        return integerList.stream().sorted().collect(toList());
    }

    public List<Integer> forEach() {
        integerList.forEach(APIAdditions::performAction);
        return integerList;
    }

    public Collection<Integer> removeIf() {
        integerList.removeIf(APIAdditions::isEven); //remove all even numbers from Collection
        return  integerList;
    }

    public List<Integer> replaceAll() {
        integerList.replaceAll(this::plusOne);
        return integerList;
    }

    public String stringJoiners() {
        String alias1 = "Alias 1";
        String alias2 = "Alias 2";
        String alias3 = "Alias 3";
        String[] aliasArray = { alias1, alias2, alias3 };
        StringJoiner joiner =  new StringJoiner(", ");
        for (String alias : aliasArray) {
            joiner.add(alias);
        }
        return joiner.toString(); //returns "Alias 1, Alias 2, Alias 3"
    }

    public String stringJoinersStream() {
        String alias1 = "Alias 1";
        String alias2 = "Alias 2";
        String alias3 = "Alias 3";

        return Stream.of(alias1, alias2, alias3)
                .collect(Collectors.joining(", ", "{", "}")); //returns "{Alias 1, Alias 2, Alias 3}"
    }

    /*These methods do a lot of null checking! If you are explicitly using nulls for "absent" values,
    * it could screw things up! Consider using Optional.empty() instead to represent absent cases*/

    public void newMapMethods() {
        Map<Integer, String> map = new HashMap<>();

        map.getOrDefault(1, "Zero"); //returns a default value if the value is not found.

        map.forEach((integer, string) -> performAction(integer)); //performs an action

        map.replaceAll((integer, s) -> map.get(integer).concat(" is proabably a number"));

        map.putIfAbsent(1, "One"); //will only perform a put if get(key) == null

        map.remove(1, "Two"); //Remove the entry if the current key/value pairing exists.

        map.replace(1, "One", "ONE"); //if the current key/value pairing exists, replace with a new value

        map.replace(1, "TWO"); //replace the current value for the key with the specified value

    }
    public void newObjectsClass(String value) {
        //technically addedd in Java 7, expanded in Java 8
        String one = "ONE";
        String two = "TWO";

        Objects.equals(one, two); //automatically preforms the correct null check
        Objects.requireNonNull(one); //call to put at the beginning of a method. throws NullPointerException
        Objects.requireNonNull(two, "String is null"); //message is not created unless null

        integerList.stream().filter(Objects::nonNull); //some Objects methods are useful for filter operations
        integerList.stream().filter(Objects::isNull);
    }

    private int plusOne(int number) {
        return ++number;
    }

    private static void performAction(Integer integer) {
    }

    public static void performAction(Object o1, Object o2) {

    }

    private static boolean isEven(Integer integer) {
        return integer % 2 == 0;
    }
}
