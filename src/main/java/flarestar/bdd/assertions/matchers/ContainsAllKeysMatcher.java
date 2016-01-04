package flarestar.bdd.assertions.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.Map;

/**
 * TODO
 */
public class ContainsAllKeysMatcher extends BaseMatcher<Map<?, ?>> {

    private Object[] expectedKeys;

    public ContainsAllKeysMatcher(Object[] expectedKeys) {
        this.expectedKeys = expectedKeys;
    }

    public boolean matches(Object o) {
        Map<?, ?> map = (Map<?, ?>) o;
        for (int i = 0; i != expectedKeys.length; ++i) {
            if (!map.containsKey(expectedKeys[i])) {
                return false;
            }
        }
        return true;
    }

    public void describeTo(Description description) {
        description.appendText("map contains all keys in ");
        description.appendValueList("[", ", ", "]", expectedKeys);
    }
}
