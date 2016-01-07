package flarestar.bdd.assertions.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.concurrent.Callable;

/**
 * TODO
 */
public class CheckThrowsMatcher extends BaseMatcher<Object> {
    private Class<? extends Throwable> throwableClass;

    public CheckThrowsMatcher(Class<? extends Throwable> throwableClass) {
        this.throwableClass = throwableClass;
    }

    public boolean matches(Object o) {
        try {
            execute(o);
            return false;
        } catch (Throwable caught) {
            if (throwableClass.isInstance(caught)) {
                return true;
            }

            throw new RuntimeException(caught); // TODO: would be nice if we didn't have to use a RuntimeException in this case
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
    }
}
