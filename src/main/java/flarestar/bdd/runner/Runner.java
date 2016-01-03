package flarestar.bdd.runner;

import flarestar.bdd.model.BddSuite;
import junit.framework.*;
import org.junit.runner.Describable;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

/**
 * TODO
 */
public class Runner extends org.junit.runner.Runner {

    private static final class TestClassListener implements TestListener {
        private final RunNotifier notifier;

        private TestClassListener(RunNotifier notifier) {
            this.notifier = notifier;
        }

        public void endTest(Test test) {
            notifier.fireTestFinished(asDescription(test));
        }

        public void startTest(Test test) {
            notifier.fireTestStarted(asDescription(test));
        }

        public void addError(Test test, Throwable e) {
            Failure failure = new Failure(asDescription(test), e);
            notifier.fireTestFailure(failure);
        }

        private Description asDescription(Test test) {
            return ((Describable)test).getDescription();
        }

        public void addFailure(Test test, AssertionFailedError t) {
            addError(test, t);
        }
    }

    private volatile BddSuite testSuite;

    public Runner(Class<?> testKlass) {
        this.testSuite = new BddSuite(testKlass);
    }

    @Override
    public Description getDescription() {
        return testSuite.getDescription();
    }

    @Override
    public void run(RunNotifier runNotifier) {
        TestResult result = new TestResult();
        result.addListener(createListener(runNotifier));
        testSuite.run(result);
    }

    private TestListener createListener(RunNotifier runNotifier) {
        return new TestClassListener(runNotifier);
    }
}
