package com.flarestar.bdd.tests.functional.tests;

import flarestar.bdd.annotations.Describe;
import flarestar.bdd.annotations.It;
import flarestar.bdd.runner.Runner;
import org.junit.Assert;
import org.junit.runner.RunWith;

import java.util.*;
import java.util.regex.Pattern;

import static flarestar.bdd.Assert.*;

/**
 * TODO
 */
@RunWith(Runner.class)
@Describe(Calculator.class)
public class ExampleTest {
    private Calculator calculator;

    public static void before() {
        System.out.println("in before");
    }

    public static void after() {
        System.out.println("in after");
    }

    public void beforeEach() {
        System.out.println("in before each");

        this.calculator = new Calculator();
    }

    public void afterEach() {
        System.out.println("in after each");
    }

    @Describe(desc = "inner 1")
    public class InnerTest1 {
        public void beforeEach() {
            System.out.println("[inner] in before each");
        }

        public void afterEach() {
            System.out.println("[inner] in after each");
        }

        @It("should run this test (fail)")
        public void testOnly() {
            expect(false).to().be().true_();
        }

        @It("should not fail this test")
        public void testPass() {
            expect(calculator).to().be().instanceOf(Calculator.class);
        }

        @Describe(desc = "inner 1.1")
        public class InnerTest2 {
            public void beforeEach() {
                System.out.println("[inner 1.1] in before each");
            }

            public void afterEach() {
                System.out.println("[inner 1.1] in after each");
            }

            @It("should run this test also (fail)")
            public void testOnly() {
                expect(false).to().be().true_();
            }

            @It("should not fail this test also")
            public void testPass() {
                expect(calculator).to().be().instanceOf(Calculator.class);
            }
        }
    }

    @Describe(desc = "inner 2")
    public static class InnerTest2 {
        public static void before() {
            System.out.println("[inner 2] in before");
        }

        public static void after() {
            System.out.println("[inner 2] in after");
        }

        public void beforeEach() {
            System.out.println("[inner 2] in before each");
        }

        public void afterEach() {
            System.out.println("[inner 2] in after each");
        }

        @It("should run this test as well (fail)")
        public void testOnly() {
            expect(false).to().be().true_();
        }
    }

    @It("should not throw an exception (fail)")
    public void testThrowException() throws Exception {
        this.calculator.failAtNotThrowing();
    }

    @It("should not fail this assertion (fail)")
    public void testAssertionFail() {
        Assert.assertTrue(false);
    }

    @It("should pass (pass)")
    public void testPassing() {
        // empty
    }

    @It("should add two values correctly (pass)")
    public void testAdd() {
        int result = calculator.add(8, 12);
        expect(result).to().equal(20);
    }

    @It("should subtract two values correctly (fail)")
    public void testBrokenMethodSubtract() {
        int result = calculator.subtract(8, 12);
        expect(result).to().equal(-4);
    }

    @It("should pass when value not equal to something (pass)")
    public void testNotChainable() {
        expect(4).to().not().equal(10);
    }

    @It("should fail when value not equal to something (fail)")
    public void testNotChainableFailure() {
        expect(4).to().not().equal(4);
    }

    @It("should pass when all().keys() used and map contains keys (pass)")
    public void testKeysSuccess() {
        Map<String, String> values = makeTestMap();

        expect(values).to().include().all().keys("key1", "key2", "key3");
    }

    @It("should pass when all().keys() used and map contains keys (fail)")
    public void testKeysFailure() {
        Map<String, String> values = makeTestMap();
        expect(values).to().contain().keys("key1", "notkey2");
    }

    @It("should pass when have().all().keys() used and map contains all keys (pass)")
    public void testHaveAllKeysSuccess() {
        Map<String, String> values = makeTestMap();
        expect(values).to().have().keys("key1", "key2", "key3");
    }

    @It("should pass when have().all().keys() used and map contains all keys (fail)")
    public void testHaveAllKeysFailure() {
        Map<String, String> values = makeTestMap();
        expect(values).to().have().keys("key1", "key3");
    }

    @It("should pass when any().keys() used and map contains keys (pass)")
    public void testAnyKeysSuccess() {
        Map<String, String> values = makeTestMap();

        expect(values).to().contain().any().keys("notkey1", "key2");
    }

    @It("should pass when any().keys() used and map contains keys (fail)")
    public void testAnyKeysFailure() {
        Map<String, String> values = makeTestMap();
        expect(values).to().include().any().keys("notkey1", "notkey2");
    }

    @It("should pass when instanceOf() used (pass)")
    public void testInstanceOfSuccess() {
        expect(calculator).to().be().instanceOf(Calculator.class);
    }

    @It("should pass when instanceOf() used (fail)")
    public void testInstanceOfFailure() {
        expect(new Object()).to().be().instanceOf(Calculator.class);
    }

    @It("should pass w/ .ok() when a value is ok (pass)")
    public void testOkSuccess() {
        expect(5).to().be().ok();
    }

    @It("should pass w/ .ok() when a value is ok (fail)")
    public void testOkFailure() {
        expect("").to().be().ok();
    }

    @It("should pass w/ .null() when the value is null (pass)")
    public void testNullSuccess() {
        expect(null).to().be().null_();
    }

    @It("should pass w/ .null() when the value is null (fail)")
    public void testNullFailure() {
        expect("").to().be().null_();
    }

    @It("should pass w/ .empty() when collection is empty (pass)")
    public void testEmptySuccess() {
        expect(new HashMap<String, String>()).to().be().empty();
    }

    @It("should pass w/ .empty() when collection is empty (fail)")
    public void testEmptyFailure() {
        expect(makeTestMap()).to().be().empty();
    }

    @It("should pass w/ .above() when value is greater than expected (pass)")
    public void testAboveSuccess() {
        expect(5).to().be().above(2);
    }

    @It("should pass w/ .above() when value is greater than expected (fail)")
    public void testAboveFailre() {
        expect(1L).to().be().above(2);
    }

    @It("should pass w/ .least() when value is >= expected (pass)")
    public void testLeastSuccess() {
        expect(3.1).to().be().at().least(3);
    }

    @It("should pass w/ .least() when value is >= expected (fail)")
    public void testLeastFailure() {
        expect(2.9).to().be().at().least(3);
    }

    @It("should pass w/ .below() when value is < expected (pass)")
    public void testBelowSuccess() {
        expect(3.1f).to().be().below(3.2f);
    }

    @It("should pass w/ .below() when value is < expected (fail)")
    public void testBelowFailure() {
        expect(3.1f).to().be().below(3.0);
    }

    @It("should pass w/ .most() when value is <= expected (pass)")
    public void testMostSuccess() {
        expect(3.1f).to().be().at().most(3.1f);
    }

    @It("should pass w/ .most() when value is <= expected (fail)")
    public void testMostFailure() {
        expect(3.1).to().be().at().most(3.09);
    }

    @It("should pass w/ .assertWithin() when value is within range (pass)")
    public void testWithinSuccess() {
        expect(3.1).to().be().within(3.09f, 4L);
    }

    @It("should pass w/ .assertWithin() when value is within range (fail)")
    public void testWithinFailure() {
        expect(2).to().be().within(2.9f, 4.1);
    }

    @It("should pass w/ .length(...) when length of collection is equal (pass)")
    public void testLengthSuccess() {
        expect(new int[]{1, 2, 3}).to().have().length(3);
    }

    @It("should pass w/ .length(...) when length of collection is equal (fail)")
    public void testLengthFailure() {
        expect(makeTestMap()).to().have().length(1);
    }

    @It("should change the value passed to expect() to the length when .length() is used in a chain (pass)")
    public void testLengthChainableSuccess() {
        expect(new int[]{1, 2, 3}).to().have().length().below(5);
    }

    @It("should change the value passed to expect() to the length when .length() is used in a chain (fail)")
    public void testLengthChainableFailure() {
        expect(makeTestMap()).to().have().length().above(10);
    }

    @It("should pass w/ .matches() when string matches pattern (pass)")
    public void testMatchesSuccess() {
        expect("ababbbabab").to().match(Pattern.compile("ab"));
    }

    @It("should pass w/ .matches() when string matches pattern (fail)")
    public void testMatchesFailure() {
        expect("ababbbabab").to().match(Pattern.compile("^ab$"));
    }

    @It("should pass w/ .string() when string contains string (pass)")
    public void testHasSubstringSuccess() {
        expect("here is a string").to().have().string("is a");
    }

    @It("should pass w/ .string() when string contains string (fail)")
    public void testHasSubstringFailure() {
        expect("here is a string").to().have().string("ooga");
    }

    @It("should pass w/ .closeTo() when value within delta (pass)")
    public void testCloseToSuccess() {
        expect(24).to().be().closeTo(23.0, 2.0);
    }

    @It("should pass w/ .closeTo() when value within delta (fail)")
    public void testCloseToFailure() {
        expect(2.0f).to().be().closeTo(1.0, 0.99);
    }

    @It("should pass when all().values() used and collection contains values (pass)")
    public void testValuesSuccess() {
        List<String> values = makeTestList();

        expect(values).to().include().all().values("item1", "item2");
    }

    @It("should pass when all().values() used and collection contains values (fail)")
    public void testValuesFailure() {
        expect(new String[] {"a", "b", "c"}).to().contain().values("a", "d");
    }

    @It("should pass when have().values() used and collection contains all values (pass)")
    public void testHaveAllValuesSuccess() {
        List<String> values = makeTestList();

        expect(values).to().have().values("item1", "item2", "item3");
    }

    @It("should pass when have().values() used and map contains all values (fail)")
    public void testHaveAllValuesFailure() {
        Map<String, String> values = makeTestMap();
        expect(values).to().have().values("val1", "val2");
    }

    @It("should pass when any().values() used and map contains values (pass)")
    public void testAnyValuesSuccess() {
        Map<String, String> values = makeTestMap();

        expect(values).to().contain().any().values("notkey1", "val2");
    }

    @It("should pass when any().values() used and map contains values (fail)")
    public void testAnyValuesFailure() {
        expect(new int[] {1, 2, 3}).to().include().any().values(9, 10);
    }

    private Map<String,String> makeTestMap() {
        Map<String, String> values = new TreeMap<String, String>();
        values.put("key1", "val1");
        values.put("key2", "val2");
        values.put("key3", "val3");
        return values;
    }

    private List<String> makeTestList() {
        List<String> result = new ArrayList<String>();
        result.add("item1");
        result.add("item2");
        result.add("item3");
        return result;
    }
}
