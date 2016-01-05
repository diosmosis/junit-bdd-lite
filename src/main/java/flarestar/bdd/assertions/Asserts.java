package flarestar.bdd.assertions;

import flarestar.bdd.assertions.matchers.ContainsAllKeysMatcher;
import flarestar.bdd.assertions.matchers.ContainsAnyKeysMatcher;
import flarestar.bdd.assertions.matchers.IsEmpty;
import flarestar.bdd.assertions.matchers.IsTruthyMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Assert;

import java.util.Map;

/**
 * TODO
 */
public class Asserts {
    public static void assertSame(Map<String, String> flags, Object actualValue, Object expectedValue) {
        if (hasNegate(flags)) {
            Assert.assertNotSame(expectedValue, actualValue);
        } else {
            Assert.assertSame(expectedValue, actualValue);
        }
    }

    public static void assertEquals(Map<String, String> flags, Object actualValue, Object expectedValue) {
        if (hasNegate(flags)) {
            Assert.assertNotEquals(expectedValue, actualValue);
        } else {
            Assert.assertEquals(expectedValue, actualValue);
        }
    }

    public static void assertContainsKeys(Map<String, String> flags, Object actualValue, Object[] expectedKeys) {
        if (!(actualValue instanceof Map<?, ?>)) {
            throw new IllegalStateException("Can only use .keys() assertion w/ Map instance (got '"
                + actualValue.getClass().getName() + "').");
        }

        if (hasBooleanFlag(flags, AssertionFlags.ANY)) {
            Assert.assertThat((Map<?,?>)actualValue, new ContainsAnyKeysMatcher(expectedKeys));
            return;
        }

        // for below, assume all flag

        if (hasBooleanFlag(flags, AssertionFlags.HAVE)) {
            Assert.assertThat((Map<?, ?>)actualValue, new ContainsAllKeysMatcher(expectedKeys, true));
            return;
        }

        // assume all + contains
        Assert.assertThat((Map<?, ?>) actualValue, new ContainsAllKeysMatcher(expectedKeys, false));
    }

    public static void assertInstanceOf(Map<String, String> flags, Object actualValue, Class<?> expectedType) {
        if (hasNegate(flags)) {
            Assert.assertThat(actualValue, CoreMatchers.not(CoreMatchers.instanceOf(expectedType)));
        } else {
            Assert.assertThat(actualValue, CoreMatchers.instanceOf(expectedType));
        }
    }

    public static void assertTruthy(Map<String, String> flags, Object actualValue) {
        Matcher<? super Object> matcher = new IsTruthyMatcher();
        if (hasNegate(flags)) {
            matcher = CoreMatchers.not(matcher);
        }
        Assert.assertThat(actualValue, matcher);
    }

    public static void assertTrue(Map<String, String> flags, Object actualValue) {
        assertEquals(flags, actualValue, true);
    }

    public static void assertFalse(Map<String, String> flags, Object actualValue) {
        assertEquals(flags, actualValue, false);
    }

    public static void assertNull(Map<String, String> flags, Object actualValue) {
        if (hasNegate(flags)) {
            Assert.assertNotNull(actualValue);
        } else {
            Assert.assertNull(actualValue);
        }
    }

    public static void assertEmpty(Map<String, String> flags, Object actualValue) {
        Matcher<? super Object> matcher = new IsEmpty();
        if (hasNegate(flags)) {
            matcher = CoreMatchers.not(matcher);
        }
        Assert.assertThat(actualValue, matcher);
    }

    private static boolean hasNegate(Map<String, String> flags) {
        return hasBooleanFlag(flags, AssertionFlags.NEGATE);
    }

    private static boolean hasBooleanFlag(Map<String, String> flags, String name) {
        String flag = flags.get(name);
        return flag != null && flag.equals("true");
    }
}
