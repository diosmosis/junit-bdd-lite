package flarestar.bdd.assertions;

import flarestar.bdd.annotations.Assertion;
import flarestar.bdd.annotations.ChainableMethod;

/**
 * TODO
 *
 * TODO: following chai.js methods are not currently implemented, but probably should be
 * -
 */
public interface AssertionInvoker {
    // chainable no-op methods
    @ChainableMethod
    AssertionInvoker to();

    @ChainableMethod
    AssertionInvoker be();

    @ChainableMethod
    AssertionInvoker been();

    @ChainableMethod
    AssertionInvoker is();

    @ChainableMethod
    AssertionInvoker that();

    @ChainableMethod
    AssertionInvoker which();

    @ChainableMethod
    AssertionInvoker and();

    @ChainableMethod
    AssertionInvoker has();

    @ChainableMethod
    AssertionInvoker have();

    @ChainableMethod
    AssertionInvoker with();

    @ChainableMethod
    AssertionInvoker at();

    @ChainableMethod
    AssertionInvoker of();

    @ChainableMethod
    AssertionInvoker an();

    @ChainableMethod
    AssertionInvoker a();

    @ChainableMethod
    AssertionInvoker same();

    // chainable methods that alter asserts
    @ChainableMethod(flag = AssertionFlags.NEGATE, value = "true")
    AssertionInvoker not();

    @ChainableMethod(flag = AssertionFlags.ANY, value = "true")
    AssertionInvoker any();

    @ChainableMethod(flag = AssertionFlags.ALL, value = "true")
    AssertionInvoker all();

    // assertions
    @Assertion(klass = Asserts.class, method = "assertSame", args = {Object.class})
    void same(Object expectedValue);

    @Assertion(klass = Asserts.class, method = "assertEquals", args = {Object.class})
    void equal(Object expectedValue);

    @Assertion(klass = Asserts.class, method = "assertContainsKeys", args = {Object[].class})
    void keys(Object... expectedKeys);

    @Assertion(klass = Asserts.class, method = "assertInstanceOf", args = {Class.class})
    void a(Class<?> klass);

    @Assertion(klass = Asserts.class, method = "assertInstanceOf", args = {Class.class})
    void an(Class<?> klass);

    @Assertion(klass = Asserts.class, method = "assertInstanceOf", args = {Class.class})
    void instanceOf(Class<?> klass);
}
