package flarestar.bdd.model;

import flarestar.bdd.annotations.It;
import junit.framework.Protectable;
import junit.framework.Test;
import junit.framework.TestResult;
import org.junit.runner.Describable;
import org.junit.runner.Description;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * TODO
 */
public class BddTest implements Test, Describable, BddTestInterface {

    private Method method;
    private Object testSuiteInstance;
    private String testDescription;

    public BddTest(Method method) {
        this.method = method;

        It annotation = method.getAnnotation(It.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Invalid BddTest method '" + method.getDeclaringClass().getName() + "."
                + method.getName() + "', @It annotation is missing.");
        }

        testDescription = "it " + annotation.value();
    }

    public int countTestCases() {
        return 1;
    }

    public void run(final TestResult testResult) {
        testResult.startTest(this);
        testResult.runProtected(this, new Protectable() {
            public void protect() throws Throwable {
                runTest();
            }
        });
        testResult.endTest(this);
    }

    private void runTest() throws Throwable {
        try {
            method.invoke(testSuiteInstance, new Object[0]);
        } catch (InvocationTargetException e) {
            e.fillInStackTrace();
            throw e.getTargetException();
        } catch (IllegalAccessException e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    public Object getTestSuiteInstance() {
        return testSuiteInstance;
    }

    public void setTestSuiteInstance(Object testSuiteInstance) {
        this.testSuiteInstance = testSuiteInstance;
    }

    public Description getDescription() {
        return Description.createTestDescription(method.getDeclaringClass().getName(), testDescription);
    }
}
