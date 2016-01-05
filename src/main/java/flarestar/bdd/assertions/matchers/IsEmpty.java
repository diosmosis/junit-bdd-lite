package flarestar.bdd.assertions.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.Collection;
import java.util.Map;

/**
 * TODO
 */
public class IsEmpty extends BaseMatcher<Object> {
    public boolean matches(Object o) {
        return isEmptyString(o) || isEmptyCollection(o) || isEmptyMap(o);
    }

    private boolean isEmptyMap(Object o) {
        return o instanceof Map && ((Map) o).isEmpty();
    }

    private boolean isEmptyCollection(Object o) {
        return o instanceof Collection && ((Collection) o).isEmpty();
    }

    private boolean isEmptyString(Object o) {
        return o instanceof String && ((String) o).isEmpty();
    }

    public void describeTo(Description description) {
        description.appendText("is empty collection or empty string");
    }
}
