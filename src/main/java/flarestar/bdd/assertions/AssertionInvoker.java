package flarestar.bdd.assertions;

import flarestar.bdd.annotations.Assertion;
import flarestar.bdd.annotations.ChainableMethod;
import org.junit.Assert;

/**
 * TODO
 */
public interface AssertionInvoker {
    @ChainableMethod
    AssertionInvoker to();

    @Assertion(klass = Assert.class, method = "assertSame", args = {Object.class, Object.class})
    void be(Object expectedValue);

    @Assertion(klass = Assert.class, method = "assertEquals", args = {Object.class, Object.class})
    void equal(Object expectedValue);
}
