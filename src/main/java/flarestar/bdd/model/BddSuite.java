package flarestar.bdd.model;

import flarestar.bdd.annotations.Describe;
import flarestar.bdd.annotations.It;
import junit.framework.Protectable;
import junit.framework.Test;
import junit.framework.TestResult;
import org.junit.runner.Describable;
import org.junit.runner.Description;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * TODO
 */
public class BddSuite implements Test, Describable, BddTestInterface {

    // TODO: allow non-static inner classes (currently we assume inner classes are static)

    private Class<?> testKlass;
    private Class<?>[] testTargets;
    private String customDescription;
    private List<Test> tests;
    private Method beforeMethod;
    private Method afterMethod;
    private Method beforeEachMethod;
    private Method afterEachMethod;

    private BddSuite parentTestSuite;
    private Object testSuiteInstance = null;

    public BddSuite(Class<?> testKlass, BddSuite parentTestSuite) {
        this.testKlass = testKlass;
        this.parentTestSuite = parentTestSuite;

        Describe annotation = testKlass.getAnnotation(Describe.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Test class " + testKlass + " must be annotated w/ @Describe.");
        }

        testTargets = annotation.value();
        customDescription = annotation.desc();

        tests = getDescribeSuiteTests(testKlass);

        beforeMethod = getOptionalMethod("before");
        afterMethod = getOptionalMethod("after");
        beforeEachMethod = getOptionalMethod("beforeEach");
        afterEachMethod = getOptionalMethod("afterEach");
    }

    public int countTestCases() {
        int total = 0;
        for (Test test : tests) {
            total += test.countTestCases();
        }
        return total;
    }

    public void run(final TestResult testResult) {
        testResult.runProtected(this, new Protectable() {
            public void protect() throws Throwable {
                runBare(testResult);
            }
        });
    }

    public void runBare(final TestResult testResult) throws Throwable {
        runSurrounded(null, beforeMethod, afterMethod, new Protectable() {
            public void protect() throws Throwable {
                for (Test test : tests) {
                    if (testResult.shouldStop()) {
                        break;
                    }

                    runTest(test, testResult);
                }
            }
        }, false);
    }

    public void setTestSuiteInstance(Object testSuiteInstance) {
        this.testSuiteInstance = testSuiteInstance;
    }

    private void runTest(final Test test, final TestResult testResult) throws Throwable {
        Object testCase = createTestCaseInstance();
        setTestSuiteInstance(testCase);

        if (test instanceof BddTest) {
            ((BddTest) test).setTestSuiteInstance(testCase);

            runSurrounded(testCase, beforeEachMethod, afterEachMethod, new Protectable() {
                public void protect() throws Throwable {
                    test.run(testResult);
                }
            }, true);
        } else { // assume BddSuite
            test.run(testResult);
        }
    }

    private void runSurrounded(Object testCaseInstance, Method beforeMethod, Method afterMethod, Protectable protectable,
                               boolean isEachMethod) throws Throwable {
        if (isEachMethod) {
            runBeforeEachChain();
        } else {
            runMethod(testCaseInstance, beforeMethod);
        }

        Throwable caught = null;
        try {
            protectable.protect();
        } catch (Throwable throwable) {
            caught = throwable;
        } finally {
            try {
                if (isEachMethod) {
                    runAfterEachChain();
                } else {
                    runMethod(testCaseInstance, afterMethod);
                }
            } catch (Throwable throwable) {
                if (caught == null) {
                    caught = throwable;
                }
            }
        }

        if (caught != null) {
            throw caught;
        }
    }

    private void runAfterEachChain() throws Throwable {
        for (BddSuite suite = this; suite != null; suite = suite.parentTestSuite) {
            suite.runMethod(suite.testSuiteInstance, suite.afterEachMethod);
        }
    }

    private void runBeforeEachChain() throws Throwable {
        if (parentTestSuite != null) {
            parentTestSuite.runBeforeEachChain();
        }

        runMethod(testSuiteInstance, beforeEachMethod);
    }

    private void runMethod(Object testCaseInstance, Method method) throws Throwable {
        if (method == null) {
            return;
        }

        try {
            method.invoke(testCaseInstance, new Object[0]);
        } catch (IllegalAccessException e) {
            e.fillInStackTrace();
            throw e;
        } catch (InvocationTargetException e) {
            e.fillInStackTrace();
            throw e.getTargetException();
        }
    }

    public Description getDescription() {
        return Description.createSuiteDescription(getDescriptionText(), testKlass.getAnnotations());
    }

    public String getDescriptionText() {
        StringBuilder result = new StringBuilder();
        result.append(testKlass.getName());

        if (customDescription != null && !customDescription.isEmpty()) {
            result.append(" [tests ");
            result.append(customDescription);
            result.append("]");
        } else if (testTargets.length > 0) {
            result.append(" [tests ");
            for (int i = 0; i != testTargets.length; ++i) {
                if (i != 0) {
                    result.append(", ");
                }
                result.append(testTargets[i].getName());
            }
            result.append("]");
        }

        return result.toString();
    }

    private List<Test> getDescribeSuiteTests(Class<?> testKlass) {
        List<Test> tests = new ArrayList<Test>();

        // add inner class @Describe suites
        for (Class<?> childClass : testKlass.getDeclaredClasses()) {
            if (childClass.isInterface()) {
                continue;
            }

            Describe annotation = childClass.getAnnotation(Describe.class);
            if (annotation == null) {
                continue;
            }

            tests.add(new BddSuite(childClass, this));
        }

        List<Method> methods = Arrays.asList(testKlass.getDeclaredMethods());
        Collections.sort(methods, new Comparator<Method>() {
            public int compare(Method method1, Method method2) {
                return method1.getName().compareTo(method2.getName());
            }
        });

        // add method tests
        for (Method method : methods) {
            It annotation = method.getAnnotation(It.class);
            if (annotation == null) {
                continue;
            }

            tests.add(new BddTest(method));
        }

        return tests;
    }

    private Method getOptionalMethod(String methodName) {
        try {
            return testKlass.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private Object createTestCaseInstance() throws Throwable {
        try {
            if (!testKlass.isMemberClass() || Modifier.isStatic(testKlass.getModifiers())) {
                return testKlass.newInstance();
            } else {
                return testKlass.getConstructor(parentTestSuite.testSuiteInstance.getClass()).newInstance(parentTestSuite.testSuiteInstance);
            }
        } catch (InstantiationException e) {
            e.fillInStackTrace();
            throw e;
        } catch (IllegalAccessException e) {
            e.fillInStackTrace();
            throw e;
        }
    }
}
