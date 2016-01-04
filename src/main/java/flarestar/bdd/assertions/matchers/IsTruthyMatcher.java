package flarestar.bdd.assertions.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * TODO
 *
 * TODO: should we check for empty arrays/collections/maps?
 */
public class IsTruthyMatcher extends BaseMatcher<Object> {
    public boolean matches(Object o) {
        if (o == null || o.equals(false) || o.equals(0)) {
            return false;
        }

        if (o instanceof String && ((String)o).isEmpty()) {
            return false;
        }

        return true;
    }

    public void describeTo(Description description) {
        description.appendText("is ok (not null, not empty string, not false and not 0)");
    }
}
