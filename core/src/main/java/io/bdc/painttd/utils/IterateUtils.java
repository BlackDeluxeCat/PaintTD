package io.bdc.painttd.utils;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.utils.func.*;

public class IterateUtils {
    public static <T> @Null T first(Iterable<T> array, Boolf<T> func) {
        for (T t : array) {
            if (func.get(t)) return t;
        }
        return null;
    }

    public static <T> void each(Iterable<T> array, Cons<T> func) {
        for (T t : array) {
            func.get(t);
        }
    }
}
