package flarestar.bdd.runner.statements;

import flarestar.bdd.model.BddMethod;
import org.junit.Test;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.runners.model.Statement;

/**
 * TODO
 */
public class PossiblyExpectExceptions extends Statement {
    private BddMethod method;
    private Statement next;

    public PossiblyExpectExceptions(BddMethod method, Statement next) {
        this.method = method;
        this.next = next;
    }

    @Override
    public void evaluate() throws Throwable {
        Test annotation = method.getAnnotation(Test.class);

        Class<? extends Throwable> expectedException = Test.None.class;
        if (annotation != null) {
            expectedException = annotation.expected();
        }

        if (expectedException != Test.None.class) {
            new ExpectException(next, expectedException).evaluate();
        } else {
            next.evaluate();
        }
    }
}
