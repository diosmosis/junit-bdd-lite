package flarestar.bdd.runner;

import flarestar.bdd.model.BddMethod;
import flarestar.bdd.model.BddSuite;
import flarestar.bdd.runner.statements.*;
import org.junit.*;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.*;
import org.junit.runner.Describable;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * NOTE: this runner can contain Runner instances & FrameworkMethod instances, so we have to inherit
 * from ParentRunner&lt;Object&gt;
 */
public class Runner extends ParentRunner<Object> {

    private Runner parentRunner;
    private BddSuite suite;

    private List<Object> children = new ArrayList<Object>();
    private Object testObject;

    public Runner(Class<?> testClass) throws InitializationError {
        this(testClass, null);
    }

    public Runner(Class<?> testClass, Runner parentRunner) throws InitializationError {
        super(testClass);

        this.parentRunner = parentRunner;
        this.suite = new BddSuite(testClass);

        for (Class<?> innerSuite : suite.getInnerSuites()) {
            children.add(new Runner(innerSuite, this));
        }

        for (Method method : suite.getTestMethods()) {
            children.add(new BddMethod(method));
        }
    }

    @Override
    protected List<Object> getChildren() { return children; }

    @Override
    protected Description describeChild(Object child) {
        if (child instanceof Describable) {
            return ((Describable) child).getDescription();
        }

        throw new IllegalArgumentException("Invalid child for BDD Runner: " + child.getClass().getName());
    }

    @Override
    protected void runChild(Object child, RunNotifier runNotifier) {
        Description description = describeChild(child);
        if (isIgnored(child)) {
            runNotifier.fireTestIgnored(description);
            return;
        }

        Throwable caught = null;
        try {
            testObject = new ReflectiveCallable() {
                @Override
                protected Object runReflectiveCall() throws Throwable {
                    return createTest();
                }
            }.run();
        } catch (Throwable throwable) {
            caught = throwable;
        }

        if (caught != null) {
            runLeaf(new Fail(caught), description, runNotifier);
        } else if (child instanceof BddMethod) {
            runLeaf(methodBlock((BddMethod)child), description, runNotifier);
        } else if (child instanceof Runner) {
            try {
                invokeNestedRunner((Runner)child, runNotifier).evaluate();
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable); // should never occur since exceptions are dealt w/ by runLeaf
            }
        } else {
            throw new RuntimeException("Don't know how to run instance of " + child.getClass().getName());
        }
    }

    @Override
    protected String getName() {
        return suite.getDescriptionText();
    }

    @Override
    protected Statement withBeforeClasses(Statement statement) {
        List<FrameworkMethod> befores = new ArrayList<FrameworkMethod>();
        befores.addAll(getTestClass().getAnnotatedMethods(BeforeClass.class));

        try {
            Method before = getTestClass().getJavaClass().getMethod("before");
            if (!Modifier.isStatic(before.getModifiers())) {
                return new Fail(new RuntimeException("'" + getTestClass().getJavaClass().getName() + "::before' must be static."));
            }

            befores.add(new FrameworkMethod(before));
        } catch (NoSuchMethodException e) {
            // ignore
        }

        return new RunBefores(statement, befores, null);
    }

    @Override
    protected Statement withAfterClasses(Statement statement) {
        List<FrameworkMethod> afters = new ArrayList<FrameworkMethod>();
        afters.addAll(getTestClass().getAnnotatedMethods(AfterClass.class));

        try {
            Method after = getTestClass().getJavaClass().getMethod("after");
            if (!Modifier.isStatic(after.getModifiers())) {
                return new Fail(new RuntimeException("'" + getTestClass().getJavaClass().getName() + "::after' must be static."));
            }

            afters.add(new FrameworkMethod(after));
        } catch (NoSuchMethodException e) {
            // ignore
        }

        return new RunAfters(statement, afters, null);
    }

    @Override
    protected boolean isIgnored(Object child) {
        if (child instanceof FrameworkMethod) {
            return ((FrameworkMethod) child).getMethod().getAnnotation(Ignore.class) != null;
        } else {
            return ((Runner)child).getTestClass().getAnnotation(Ignore.class) != null;
        }
    }

    public Runner getParentRunner() {
        return parentRunner;
    }

    private Statement methodBlock(BddMethod method) {
        // logic basically copied from BlockJUnit4ClassRunner since we want to support normal JUnit annotations/behavior,
        // but can't inherit from ParentRunner since we need to be able to nest Runners.
        Description childDescription = describeChild(method);

        Statement statement = new InvokeMethod(method, testObject);
        statement = new PossiblyExpectExceptions(method, statement);
        statement = new InvokeBeforeEachChain(this, statement);
        statement = new InvokeAfterEachChain(this, statement);
        statement = new InvokeTestRules(testObject, getTestClass(), method, childDescription, statement);
        return statement;
    }

    private Statement invokeNestedRunner(Runner child, RunNotifier notifier) {
        return child.classBlock(notifier);
    }

    public Object getTestObject() {
        return testObject;
    }

    private Object createTest() throws Exception {
        Class<?> testKlass = getTestClass().getJavaClass();
        Constructor<?> constructor = getTestClass().getOnlyConstructor();

        if (!testKlass.isMemberClass() || Modifier.isStatic(testKlass.getModifiers())) {
            return constructor.newInstance();
        } else {
            return constructor.newInstance(parentRunner.getTestObject());
        }
    }
}
