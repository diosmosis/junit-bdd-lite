package flarestar.bdd.assertions.matchers;

import org.hamcrest.Description;

import java.util.Iterator;

/**
 * TODO
 */
public class ContainsAllItemsMatcher extends BaseItemMatcher {
    private boolean checkExact;

    public ContainsAllItemsMatcher(Object[] expectedItems, boolean checkExact) {
        super(expectedItems);

        this.checkExact = checkExact;
    }

    public boolean matches(Object o) {
        for (int i = 0; i != expectedItems.length; ++i) {
            if (!hasItem(o, expectedItems[i])) {
                return false;
            }
        }

        if (checkExact) {
            return getSize(o) == expectedItems.length;
        } else {
            return true;
        }
    }

    public void describeTo(Description description) {
        description.appendText("array or collection containing all values in ");
        description.appendValueList("[", ", ", "]", expectedItems);
    }
}
