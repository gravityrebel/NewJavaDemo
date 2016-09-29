package com.devtech.java8;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The Optional construct has made it's way into Java 8 without third party library support.
 * <p>
 * I still suggest that everyone read Google excellent write up on the Optional class at
 * https://github.com/google/guava/wiki/UsingAndAvoidingNullExplained#optional
 * <p>
 * At it's core, an Optional is just a wrapper around a value. That's it. It's purpose is to help
 * avoid null pointer exceptions, especially in collections and when interfacing with api's whose
 * behavior the caller cannot be sure of.
 * <p>
 * Null is cheap and often the correct thing to return. But sometimes conceptually null is not
 * correct. Null is ambiguous. Optional exists to give the ABSENT case of null a name.
 * <p>
 * Many of the operations in the Stream class returns optionals, they are a big part of the new
 * java API. They force you to check for the absent case and help make code safer and easier to
 * read.
 */
public class OptionalExample {
    private static Map<Integer, String> valueMap = createValueMap();
    private static Map<Integer, Optional<String>> optionalMap = createOptionalMap();
    private static Entity someEntity = () -> "GENDER"; //sidenote, created an on the fly instance


















    public static void creatingOptionals() {
        Optional<Entity> entityOptional = Optional.of(someEntity); //returns not empty
        Optional<Entity> emptyOptional = Optional.empty(); //returns empty
        Optional<Entity> nullableOptional = Optional.ofNullable(null); //returns empty
        Optional<Entity> willThrowError = Optional.of(null); //throws null pointer
    }


















    public static void usingOptionals() {
        Optional<Entity> entityOptional = methodThatReturnsSomeValue("FIRST_NAME");
        //basic usage
        if (entityOptional.isPresent()) {
            someOtherAction(entityOptional.get());
        }

        //Other Usage
        entityOptional.ifPresent(entity -> someOtherAction(entity));
        Entity entity = entityOptional.orElse(() -> "SOCIAL_SECURITY");
        entityOptional.filter(entity1 -> entity1.getKey().contains("NAME"))
                .ifPresent(OptionalExample::processNameEntity);

        if (entityOptional.isPresent() && entityOptional.get().getKey().contains("NAME")) {
            processNameEntity(entityOptional.get());
        }
    }


















    /**
     * Here is a good example in our code of using the Optional type to write clear code.
     * We have some iterable that we iterate over looking for a value. In the event that we do not
     * find the value what should we do? Return null? The way that the method is written, every
     * single caller of this method MUST check for null values, as "nothing was found" is a valid
     * return type. But we have to have a way to FORCE the caller to check for these cases.
     * <p>
     * Optional answers this call by wrapping the value and basically saying "you have to check me
     * before you get the value". Anybody who sees this api method will KNOW that the absent case is
     * valid and expected, just by seeing it returns an optional.
     */
    public <T extends Entity> Optional<T> findEntity(EntityType entityType, Iterable<T> entityIterable) {
        for (T entity : entityIterable) {
            if (entityType.getKey().equals(entity.getKey())) {
                return Optional.of(entity);
            }
        }
        return Optional.empty();
    }



    public void foo() {
        @SuppressWarnings("unchecked")
        Optional<Entity> bar = findEntity(() -> "foobar", Collections.EMPTY_LIST);
        if (bar.isPresent()) {
            someOtherAction(bar.get());
        }
    }


















    /**
     * To give an example of where nulls suck and Optional is a good choice
     */
    @Test
    public void nullConfusion() {
        String passedInNull = valueMap.get(null);
        System.out.println("The retrieved value is: " + passedInNull);
        //The above code prints out as expected! Though this could be improved

        valueMap.put(null, "Null Key Value");
        String nullKey = valueMap.get(null);
        System.out.println("The retrieved value is: " + nullKey);
        //This is a good example of wanting to special case some null value, and not use null itself.

        String zero = valueMap.get(0);
        System.out.println("The retrieved value is: " + zero);
        //BAD! Null here is the most ambiguous. What does this mean?
    }

















    @Test
    public void lessConfusion() {
        Optional<String> passedInNull = optionalMap.get(null);
       if (passedInNull.isPresent()) {//This will throw null pointer!
            System.out.println("The retrieved value : " + passedInNull.get());
        }
        //The above code prints out as expected! Though this could be improved

        optionalMap.put(null, Optional.of("Null Key Value"));
        Optional<String> nullKey = optionalMap.get(null);
        if (nullKey.isPresent()) { //will evaluate to false
            System.out.println("The retrieved value is: " + nullKey.get()); //This will print
        }
        //This is a good example of wanting to special case some null value, and not use null itself.

        Optional<String> zero = optionalMap.get(0);
        if (zero.isPresent()) { //skips
            System.out.println("The retrieved value is: " + zero.get());
        }
        //BAD! Null here is the most ambiguous. What does this mean?
    }















    public static void main(String[] args) {
      //  nullConfusion();
        //lessConfusion();
        String s = Integer.toBinaryString(~(1 << 20));
        System.out.println(s + " and " + s.length());
    }

    private static Map<Integer, String> createValueMap() {
        Map<Integer, String> valueMap = new HashMap<>();
        valueMap.put(1, "One");
        valueMap.put(2, "Two");
        valueMap.put(3, "Three");
        valueMap.put(0, null);
        return valueMap;
    }

    private static Map<Integer, Optional<String>> createOptionalMap() {
        Map<Integer, Optional<String>> valueMap = new HashMap<>();
        valueMap.put(1, Optional.of("One"));
        valueMap.put(2, Optional.of("Two"));
        valueMap.put(3, Optional.of("Three"));
        valueMap.put(0, Optional.empty());
        return valueMap;
    }

    private static Optional<Entity> methodThatReturnsSomeValue(String s) {
        return s == null || s.isEmpty() ? Optional.ofNullable(null) : Optional.of(someEntity);
    }

    private static void someOtherAction(Entity entity) {

    }

    private static void processNameEntity(Entity entity2) {

    }

    interface Entity {
        String getKey();
    }

    interface EntityType {
        String getKey();
    }

}
