package flarestar.bdd;

import flarestar.bdd.assertions.AssertionInvocationHandler;
import flarestar.bdd.assertions.AssertionInvoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * TODO
 */
public class Assert {
    public static AssertionInvoker expect(Object value) {
        return expect(AssertionInvoker.class, value);
    }

    public static AssertionInvoker expect() {
        return expect(AssertionInvoker.class, AssertionInvocationHandler.NO_VALUE);
    }

    public static AssertionInvoker expect(Class<? extends AssertionInvoker> assertionInvokerClass, Object value) {
        InvocationHandler handler = new AssertionInvocationHandler(value);
        Class<?>[] implementedInterfaces = new Class[] { assertionInvokerClass };
        return (AssertionInvoker)Proxy.newProxyInstance(Assert.class.getClassLoader(), implementedInterfaces, handler);
    }
}
