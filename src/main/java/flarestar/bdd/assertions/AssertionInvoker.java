package flarestar.bdd.assertions;

import flarestar.bdd.annotations.AssertionMethod;
import flarestar.bdd.annotations.ChainableMethod;
import flarestar.bdd.assertions.manipulators.GetLengthOf;

import java.util.regex.Pattern;

/**
 * TODO
 */
public interface AssertionInvoker {

    /*
     * TODO: try to use generics more, ie, use type passed as actual value to help determine what params
     *       methods should accept.
     * TODO: try to reimplement by only using hamcrest matchers. maybe pass Matcher classes to @AssertionMethods & @ChainableMethods
     * TODO: following chai.js methods are not currently implemented, but probably should be
     * - deep()
     * - throw()
     */

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

    @ChainableMethod(manipulator = GetLengthOf.class)
    AssertionInvoker length();

    // assertions
    @AssertionMethod(klass = Asserts.class, method = "assertSame")
    void same(Object expectedValue);

    @AssertionMethod(klass = Asserts.class, method = "assertEquals")
    void equal(Object expectedValue);

    @AssertionMethod(klass = Asserts.class, method = "assertContainsKeys")
    void keys(Object... expectedKeys);

    @AssertionMethod(klass = Asserts.class, method = "assertContainsValues")
    void values(Object... expectedValues);

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

    @AssertionMethod(klass = Asserts.class, method = "assertGreaterThan")
    void above(Comparable expectedValue);

    @AssertionMethod(klass = Asserts.class, method = "assertGreaterThanOrEqual")
    void least(Comparable expectedValue);

    @AssertionMethod(klass = Asserts.class, method = "assertLessThan")
    void below(Comparable expectedValue);

    @AssertionMethod(klass = Asserts.class, method = "assertLessThanOrEqual")
    void most(Comparable expectedValue);

    @AssertionMethod(klass = Asserts.class, method = "assertWithin")
    void within(Comparable start, Comparable finish);

    @AssertionMethod(klass = Asserts.class, method = "assertLengthIs")
    void length(long expectedLength);

    @AssertionMethod(klass = Asserts.class, method = "assertMatchesPattern")
    void match(Pattern pattern);

    @AssertionMethod(klass = Asserts.class, method = "assertContainsString")
    void string(String substring);

    @AssertionMethod(klass = Asserts.class,method = "assertEqualsWithPrecision")
    void closeTo(double expectedValue, double precision);
}
