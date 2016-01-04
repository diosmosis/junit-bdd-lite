package com.flarestar.bdd.tests.functional.tests;

/**
 * TODO
 */
public class Calculator {
    public static class TemperTantrum extends Exception {
        public TemperTantrum(String message) {
            super(message);
        }
    }

    public void failAtNotThrowing() throws TemperTantrum {
        throw new TemperTantrum("waaaaaah!");
    }

    public int add(int lhs, int rhs) {
        return lhs + rhs;
    }

    public int subtract(int lhs, int rhs) {
        return lhs - rhs + 42;
    }
}
