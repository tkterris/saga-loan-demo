package com.acme.saga.loan.helpers;

import java.util.List;
import java.util.stream.StreamSupport;

public class Utils {
    public static <T> List<T> toList(final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                            .toList();
    }
}