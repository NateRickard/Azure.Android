package com.microsoft.azureandroid.data.util;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * Created by naterickard on 1/5/18.
 */

public class FunctionalUtils {
    public static <T> Function1<T, Unit> onResponse(Consumer<T> callable) {
        return t -> {
            callable.accept(t);
            return Unit.INSTANCE;
        };
    }
}