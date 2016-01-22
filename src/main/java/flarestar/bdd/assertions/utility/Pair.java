package flarestar.bdd.assertions.utility;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;

/**
 * TODO
 */
public class Pair<T> implements SelfDescribing {
    public T first;
    public T second;

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public static <V> Pair<V> make(V first, V second) {
        return new Pair<V>(first, second);
    }

    public void describeTo(Description description) {
        description.appendValue(first).appendText(" => ").appendValue(second);
    }
}
