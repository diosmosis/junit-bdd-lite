package flarestar.bdd.assertions.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.Map;

/**
 * TODO
 */
public class ContainsAllKeysMatcher extends BaseMatcher<Map<?, ?>> {

    private Object[] expectedKeys;
    private boolean checkExact;

    public ContainsAllKeysMatcher(Object[] expectedKeys, boolean checkExact) {
        this.expectedKeys = expectedKeys;
        this.checkExact = checkExact;
    }

    public boolean matches(Object o) {
        Map<?, ?> map = (Map<?, ?>) o;
        for (int i = 0; i != expectedKeys.length; ++i) {
            if (!map.containsKey(expectedKeys[i])) {
                return false;
            }
        }

        if (checkExact) {
            return map.size() == expectedKeys.length;
        } else {
            return true;
        }
    }

    public void describeTo(Description description) {
        description.appendText("map contains all keys in ");
        description.appendValueList("[", ", ", "]", expectedKeys);
    }
}
