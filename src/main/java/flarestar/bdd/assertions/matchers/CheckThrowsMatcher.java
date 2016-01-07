package flarestar.bdd.assertions.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.concurrent.Callable;
import java.util.regex.Pattern;

/**
 * TODO
 */
public class CheckThrowsMatcher extends BaseMatcher<Object> {
    private Class<? extends Throwable> throwableClass;
    private String expectedMessageContains;
    private Pattern expectedMessagePattern;
    private Throwable caughtException;

    public CheckThrowsMatcher(Class<? extends Throwable> throwableClass, String expectedMessageContains,
                              Pattern expectedMessagePattern) {
        this.throwableClass = throwableClass;
        this.expectedMessageContains = expectedMessageContains;
        this.expectedMessagePattern = expectedMessagePattern;
    }

    public boolean matches(Object o) {
        try {
            execute(o);
            return false;
        } catch (Throwable caught) {
            caughtException = caught;

            if (!throwableClass.isInstance(caught)) {
                throw new RuntimeException(caught); // TODO: would be nice if we didn't have to use a RuntimeException in this case
            }

            if (expectedMessageContains != null
                && !caught.getMessage().contains(expectedMessageContains)
            ) {
                return false;
            }

            if (expectedMessagePattern != null
                && !expectedMessagePattern.matcher(caught.getMessage()).find()
            ) {
                return false;
            }

            return true;
        }
    }

    private void execute(Object o) throws Throwable {
        // TODO: support java 8 functional programming interface
        if (o instanceof Runnable) {
            ((Runnable) o).run();
        } else if (o instanceof Callable) {
            ((Callable) o).call();
        } else {
            throw new IllegalArgumentException("Don't know how to run '" + o.getClass().getName() + "'.");
        }
    }

    public void describeTo(Description description) {
        description.appendText("throws ").appendText(throwableClass.getName());

        if (expectedMessageContains != null) {
            description.appendText(" with message containing ").appendValue(expectedMessageContains);
        }

        if (expectedMessagePattern != null) {
            description.appendText(" with pattern containg ").appendValue(expectedMessagePattern);
        }
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        description.appendText("was ").appendValue(caughtException);
    }
}
