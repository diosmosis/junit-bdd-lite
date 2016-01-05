package flarestar.bdd.assertions.matchers;

import flarestar.bdd.assertions.manipulators.GetLengthOf;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * TODO
 */
public class LengthIsMatcher extends BaseMatcher<Object> {
    private long expectedLength;

    public LengthIsMatcher(long expectedLength) {
        this.expectedLength = expectedLength;
    }

    public boolean matches(Object o) {
        return expectedLength == GetLengthOf.getLength(o);
    }

    public void describeTo(Description description) {
        description.appendText("collection, array or string with length ").appendValue(expectedLength);
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        description.appendText("was ").appendValue(GetLengthOf.getLength(item));
    }
}
