package flarestar.bdd.runner.statements;

import flarestar.bdd.runner.Runner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.lang.reflect.Method;

/**
 * TODO
 */
public class InvokeBeforeEachChain extends Statement {
    private Runner runner;
    private Statement next;

    public InvokeBeforeEachChain(Runner runner, Statement next) {
        this.runner = runner;
        this.next = next;
    }

    @Override
    public void evaluate() throws Throwable {
        executeBeforeEachChain(runner);

        next.evaluate();
    }

    private void executeBeforeEachChain(Runner runner) throws Throwable {
        Runner parentRunner = runner.getParentRunner();
        if (parentRunner != null) {
            executeBeforeEachChain(parentRunner);
        }

        executeBeforeEach(runner);
    }

    private void executeBeforeEach(Runner runner) throws Throwable {
        Object testObject = runner.getTestObject();

        Method beforeEachMethod = getBeforeEachMethod(testObject);
        if (beforeEachMethod == null) {
            return;
        }

        FrameworkMethod method = new FrameworkMethod(beforeEachMethod);
        method.invokeExplosively(testObject);
    }

    private Method getBeforeEachMethod(Object testObject) {
        try {
            return testObject.getClass().getMethod("beforeEach");
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
