package com.flarestar.bdd.tests.functional.tests;

import flarestar.bdd.annotations.Describe;
import flarestar.bdd.annotations.It;
import flarestar.bdd.runner.Runner;
import org.junit.Assert;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static flarestar.bdd.Assert.*;

/**
 * TODO
 */
@RunWith(Runner.class)
@Describe(Calculator.class)
public class CalculatorTest {
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

        expect(values).to().have().all().keys("key1", "key2", "key3");
    }

    @It("should pass when all().keys() used and map contains keys (fail)")
    public void  testKeysFailure() {
        Map<String, String> values = makeTestMap();
        expect(values).to().have().keys("key1", "notkey2");
    }

    @It("should pass when any().keys() used and map contains keys (pass)")
    public void testAnyKeysSuccess() {
        Map<String, String> values = makeTestMap();

        expect(values).to().have().any().keys("notkey1", "key2");
    }

    @It("should pass when any().keys() used and map contains keys (fail)")
    public void testAnyKeysFailure() {
        Map<String, String> values = makeTestMap();
        expect(values).to().have().any().keys("notkey1", "notkey2");
    }

    private Map<String,String> makeTestMap() {
        Map<String, String> values = new HashMap<String, String>();
        values.put("key1", "val1");
        values.put("key2", "val2");
        values.put("key3", "val3");
        return values;
    }
}
