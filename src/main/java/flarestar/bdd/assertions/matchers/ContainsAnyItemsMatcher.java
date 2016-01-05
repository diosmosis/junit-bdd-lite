package flarestar.bdd.assertions.matchers;

import org.hamcrest.Description;

import java.util.Iterator;

/**
 * TODO
 */
public class ContainsAnyItemsMatcher extends BaseItemMatcher {

    public ContainsAnyItemsMatcher(Object[] expectedItems) {
        super(expectedItems);
    }

    public boolean matches(Object o) {
        Iterator<?> it = getIterator(o);
        while (it.hasNext()) {
            Object value = it.next();
            if (isItemInExpectedList(value)) {
                return true;
            }
        }
        return false;
    }

    public void describeTo(Description description) {
        description.appendText("array or collection containing any value in ");
        description.appendValueList("[", ", ", "]", expectedItems);
    }
}
