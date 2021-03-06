package com.melnick.java8;

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
 *
 * If you are writing a public method that may not return a result, you should consider Optionals.
 */
public class OptionalExample {
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

        entityOptional.ifPresent(OptionalExample::someOtherAction);

        Entity entity = entityOptional.orElse(() -> "SOCIAL_SECURITY");

        if (entityOptional.isPresent() && entityOptional.get().getKey().contains("NAME")) {
            processNameEntity(entityOptional.get());
        }

        entityOptional.filter(entity1 -> entity1.getKey().contains("NAME"))
                .ifPresent(OptionalExample::processNameEntity);



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






    public static void main(String[] args) {
      //  nullConfusion();
        //lessConfusion();
        String s = Integer.toBinaryString(~(1 << 20));
        System.out.println(s + " and " + s.length());
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
