package flarestar.bdd.assertions.manipulators;

import flarestar.bdd.assertions.ValueManipulator;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * TODO
 */
public class GetLengthOf implements ValueManipulator {
    public Object manipulate(Object value) {
        return getLength(value);
    }

    public static long getLength(Object value) {
        if (value instanceof String) {
            return ((String) value).length();
        }

        if (value.getClass().isArray()) {
            return Array.getLength(value);
        }

        if (value instanceof Collection) {
            return ((Collection) value).size();
        }

        if (value instanceof Map){
            return ((Map) value).size();
        }

        throw new IllegalArgumentException("Cannot get size of a " + value.getClass().getName() + " instance.");
    }
}
