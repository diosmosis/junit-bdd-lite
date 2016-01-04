package flarestar.bdd.assertions;

import flarestar.bdd.annotations.Assertion;
import flarestar.bdd.annotations.ChainableMethod;
import flarestar.bdd.assertions.exceptions.InvalidAssertionInvokerMethod;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * TODO
 */
public class AssertionInvocationHandler implements InvocationHandler {

    public static final Object NO_VALUE = new Object();

    private Object value;

    public AssertionInvocationHandler(Object value) {
        this.value = value;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isChainable(method)) {
            return proxy;
        } else if (isAssertion(method)) {
            invokeAssert(method, args);
            return null;
        } else {
            throw new InvalidAssertionInvokerMethod("Don't know how to implement method " + method.getName()
                + ", it's missing @ChainableMethod/@Assertion annotations.");
        }
    }

    private void invokeAssert(Method method, Object[] args) throws Throwable {
        args = addActualValueArgument(args);

        Assertion annotation = method.getAnnotation(Assertion.class);

        Method assertionMethod;
        try {
            assertionMethod = annotation.klass().getMethod(annotation.method(), annotation.args());
        } catch (NoSuchMethodException e) {
            throw new InvalidAssertionInvokerMethod("Cannot find @Assertion method '" + annotation.method() + "' in "
                + annotation.klass().getName() + ".", e);
        }

        if (!Modifier.isStatic(assertionMethod.getModifiers())) {
            throw new InvalidAssertionInvokerMethod("Invalid @Assertion proxy method " + annotation.method()
                + ": method must be static.");
        }

        try {
            assertionMethod.invoke(null, args);
        } catch (IllegalAccessException e) {
            e.fillInStackTrace();
            throw e;
        } catch (InvocationTargetException e) {
            e.fillInStackTrace();
            throw e.getTargetException();
        }
    }

    private Object[] addActualValueArgument(Object[] args) {
        if (value == NO_VALUE) {
            return args;
        }

        Object[] result = new Object[args.length + 1];
        if (args.length == 0) {
            result[0] = value;
        } else {
            // we assume the expected value is the second argument in the proxied assert method
            result[0] = args[0];
            result[1] = value;

            System.arraycopy(args, 1, result, 2, args.length - 1);
        }

        return result;
    }

    private boolean isAssertion(Method method) {
        return method.getAnnotation(Assertion.class) != null;
    }

    private boolean isChainable(Method method) {
        return method.getAnnotation(ChainableMethod.class) != null;
    }
}
