package flarestar.bdd.assertions;

import flarestar.bdd.annotations.AssertionMethod;
import flarestar.bdd.annotations.ChainableMethod;
import flarestar.bdd.assertions.exceptions.InvalidAssertionInvokerMethod;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 */
public class AssertionInvocationHandler implements InvocationHandler {

    public static final Object NO_VALUE = new Object();

    private Object value;
    private Map<String, String> flags = new HashMap<String, String>();

    public AssertionInvocationHandler(Object value) {
        this.value = value;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isChainable(method)) {
            invokeChainable(proxy, method, args);
            return proxy;
        } else if (isAssertion(method)) {
            invokeAssert(method, args);
            return null;
        } else {
            throw new InvalidAssertionInvokerMethod("Don't know how to implement method " + method.getName()
                + ", it's missing @ChainableMethod/@AssertionMethod annotations.");
        }
    }

    private void invokeChainable(Object proxy, Method method, Object[] args) throws Throwable {
        ChainableMethod annotation = method.getAnnotation(ChainableMethod.class);

        String flagToAdd = annotation.flag();
        if (flagToAdd != null && !flagToAdd.isEmpty()) {
            flags.put(flagToAdd, annotation.value());
        }

        Class<? extends ValueManipulator> manipulatorClass = annotation.manipulator();
        if (manipulatorClass != ValueManipulator.class) {
            ValueManipulator manipulator = manipulatorClass.newInstance();
            value = manipulator.manipulate(value);
        }
    }

    private void invokeAssert(Method method, Object[] args) throws Throwable {
        args = addExtraArguments(args);

        AssertionMethod annotation = method.getAnnotation(AssertionMethod.class);

        Class<?> argClasses[] = getArgClasses(method);

        Method assertionMethod;
        try {
            assertionMethod = annotation.klass().getMethod(annotation.method(), argClasses);
        } catch (NoSuchMethodException e) {
            throw new InvalidAssertionInvokerMethod("Cannot find @AssertionMethod method '" + annotation.method() + "' in "
                + annotation.klass().getName() + ".", e);
        }

        if (!Modifier.isStatic(assertionMethod.getModifiers())) {
            throw new InvalidAssertionInvokerMethod("Invalid @AssertionMethod proxy method " + annotation.method()
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

    private Class<?>[] getArgClasses(Method method) {
        Class<?>[] methodArgs = method.getParameterTypes();

        Class<?>[] result = new Class<?>[methodArgs.length + 2];
        result[0] = Map.class;
        result[1] = Object.class;
        System.arraycopy(methodArgs, 0, result, 2, methodArgs.length);
        return result;
    }

    private Object[] addExtraArguments(Object[] args) {
        if (value == NO_VALUE) {
            return args;
        }

        if (args == null) {
            return new Object[] {flags, value};
        }

        Object[] result = new Object[args.length + 2];
        result[0] = flags;
        result[1] = value;
        System.arraycopy(args, 0, result, 2, args.length);

        return result;
    }

    private boolean isAssertion(Method method) {
        return method.getAnnotation(AssertionMethod.class) != null;
    }

    private boolean isChainable(Method method) {
        return method.getAnnotation(ChainableMethod.class) != null;
    }
}
