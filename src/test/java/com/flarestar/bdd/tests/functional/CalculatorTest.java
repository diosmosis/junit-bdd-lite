package com.flarestar.bdd.tests.functional;

import flarestar.bdd.annotations.Describe;
import flarestar.bdd.annotations.It;
import flarestar.bdd.runner.Runner;
import org.junit.Assert;
import org.junit.runner.RunWith;

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

    @It("should not throw an exception")
    public void testThrowException() throws Exception {
        this.calculator.failAtNotThrowing();
    }

    @It("should not fail this assertion")
    public void testAssertionFail() {
        Assert.assertTrue(false);
    }

    @It("should pass")
    public void testPassing() {
        // empty
    }
}
