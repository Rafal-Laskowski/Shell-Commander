package io.github.laskowski.shell.utils;

import io.github.laskowski.shell.exceptions.TimeoutException;

import java.util.function.Function;
import java.util.function.Supplier;

public class Wait {

    public static void until(int timeout, Supplier<Boolean> supplier) {
        long timeoutms = timeout*1000;
        long start = System.currentTimeMillis();
        while (!supplier.get()) {
            if (System.currentTimeMillis() - start > timeoutms) {
                throw new TimeoutException(String.format("Condition not meet within %s ms", timeoutms));
            }
        }
    }

    public static <T> void until(int timeout, Function<T, Boolean> readyFunction, T functionArgument) {
        long timeoutms = timeout*1000;
        long start = System.currentTimeMillis();
        while (!readyFunction.apply(functionArgument)) {
            if (System.currentTimeMillis() - start > timeoutms) {
                throw new TimeoutException(String.format("Condition not meet within %s ms", timeoutms));
            }
        }
    }
}
