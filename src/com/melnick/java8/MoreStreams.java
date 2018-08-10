package com.melnick.java8;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

public enum MoreStreams {
    STREAM_A("Stream A"),
    STREAM_B("Stream B");

    private final String value;

    MoreStreams(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }




















    public static MoreStreams valueFrom(String value) throws Exception {
        if (value != null) {
            for (MoreStreams moreStreams : values()) {
                if (value.equals(moreStreams.getValue())) {
                    return moreStreams;
                }
            }
        }
        throw new Exception();
    }


















    /**
     * Streams work in aggregate, not on individual members.'
     * @param value
     * @return
     * @throws Exception
     */
    public static MoreStreams okValueFrom(String value) throws Exception {
        if (value != null) {
            Optional<MoreStreams> returnValue = Arrays.stream(values())
                    .filter(moreStreams -> value.equals(moreStreams.getValue()))
                    .findFirst();
            if (returnValue.isPresent()) {
                return returnValue.get();
            }
        }
        throw new NoSuchElementException("value not found");
    }


















    /**
     * Streams work in aggregate, not on individual members.'
     * @param value
     * @return
     * @throws Exception
     */
    public static MoreStreams betterValueFrom(final String value) throws Exception {
        Objects.requireNonNull(value, "Passed in value was null. Provide a non-null value") ;
        return Arrays.stream(values())
                .filter(moreStreams -> value.equals(moreStreams.getValue()))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

}
