package flarestar.bdd.assertions;

import flarestar.bdd.annotations.Assertion;
import flarestar.bdd.annotations.ChainableMethod;

/**
 * TODO
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
    AssertionInvoker same();

    // chainable methods that alter asserts
    @ChainableMethod(flag = AssertionFlags.NEGATE, value = "true")
    AssertionInvoker not();

    // assertions
    @Assertion(klass = Asserts.class, method = "assertSame", args = {Object.class})
    void a(Object expectedValue);

    @Assertion(klass = Asserts.class, method = "assertSame", args = {Object.class})
    void an(Object expectedValue);

    @Assertion(klass = Asserts.class, method = "assertEquals", args = {Object.class})
    void equal(Object expectedValue);
}
