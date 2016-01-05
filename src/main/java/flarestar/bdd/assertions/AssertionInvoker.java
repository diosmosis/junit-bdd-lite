package flarestar.bdd.assertions;

import flarestar.bdd.annotations.AssertionMethod;
import flarestar.bdd.annotations.ChainableMethod;

/**
 * TODO
 *
 * TODO: following chai.js methods are not currently implemented, but probably should be
 * - deep()
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

    @ChainableMethod(flag = AssertionFlags.CONTAINS, value = "true")
    AssertionInvoker contain();

    @ChainableMethod(flag = AssertionFlags.CONTAINS, value = "true")
    AssertionInvoker include();

    @ChainableMethod(flag = AssertionFlags.HAVE, value = "true")
    AssertionInvoker has();

    @ChainableMethod(flag = AssertionFlags.HAVE, value = "true")
    AssertionInvoker have();

    // assertions
    @AssertionMethod(klass = Asserts.class, method = "assertSame")
    void same(Object expectedValue);

    @AssertionMethod(klass = Asserts.class, method = "assertEquals")
    void equal(Object expectedValue);

    @AssertionMethod(klass = Asserts.class, method = "assertContainsKeys")
    void keys(Object... expectedKeys);

    @AssertionMethod(klass = Asserts.class, method = "assertInstanceOf")
    void a(Class<?> klass);

    @AssertionMethod(klass = Asserts.class, method = "assertInstanceOf")
    void an(Class<?> klass);

    @AssertionMethod(klass = Asserts.class, method = "assertInstanceOf")
    void instanceOf(Class<?> klass);

    @AssertionMethod(klass = Asserts.class, method = "assertTruthy")
    void ok();

    @AssertionMethod(klass = Asserts.class, method = "assertTrue")
    void true_();

    @AssertionMethod(klass = Asserts.class, method = "assertFalse")
    void false_();

    @AssertionMethod(klass = Asserts.class, method = "assertNull")
    void null_();

    @AssertionMethod(klass = Asserts.class, method = "assertEmpty")
    void empty();
}
