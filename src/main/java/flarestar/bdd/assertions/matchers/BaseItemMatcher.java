package flarestar.bdd.assertions.matchers;

import org.hamcrest.BaseMatcher;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * TODO
 */
public abstract class BaseItemMatcher extends BaseMatcher<Object> {

    private static class ReflectiveArrayIterable implements Iterator<Object> {
        private Object array;
        private int index;

        public ReflectiveArrayIterable(Object array) {
            this.array = array;
            this.index = -1;
        }

        public boolean hasNext() {
            return index + 1 < Array.getLength(array);
        }

        public Object next() {
            ++this.index;
            return Array.get(array, index);
        }

        public void remove() {
            throw new UnsupportedOperationException("remove not supported");
        }
    }

    protected Object[] expectedItems;

    public BaseItemMatcher(Object[] expectedItems) {
        this.expectedItems = expectedItems;
    }

    protected Iterator<?> getIterator(Object o) {
        if (o.getClass().isArray()) {
            return new ReflectiveArrayIterable(o);
        }

        if (o instanceof Collection) {
            return ((Collection) o).iterator();
        }

        if (o instanceof Map) {
            return ((Map) o).values().iterator();
        }

        // TODO: should use a special exception (ie, InvalidExpectTarget)
        throw new IllegalArgumentException("Don't know how to get an Iterator from a " + o.getClass().getName());
    }

    protected boolean isItemInExpectedList(Object value) {
        for (int i = 0; i != expectedItems.length; ++i) {
            if (expectedItems[i] == value
                || (expectedItems[i] != null && expectedItems[i].equals(value))
            ) {
                return true;
            }
        }
        return false;
    }

    protected int getSize(Object o) {
        if (o.getClass().isArray()) {
            return Array.getLength(o);
        }

        if (o instanceof Collection) {
            return ((Collection) o).size();
        }

        if (o instanceof Map) {
            return ((Map) o).size();
        }

        throw new IllegalArgumentException("Don't know how to get an size from a " + o.getClass().getName());
    }

    protected boolean hasItem(Object o, Object expectedItem) {
        Iterator<?> it = getIterator(o);
        while (it.hasNext()) {
            Object value = it.next();
            if (value == expectedItem || (value != null && value.equals(expectedItem))) {
                return true;
            }
        }
        return false;
    }
}
