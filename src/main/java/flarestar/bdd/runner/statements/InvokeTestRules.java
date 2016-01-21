package flarestar.bdd.runner.statements;

import flarestar.bdd.model.BddMethod;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class InvokeTestRules extends Statement {
    private Object testObject;
    private TestClass testClass;
    private BddMethod method;
    private Description childDescription;
    private Statement computedStatement;

    public InvokeTestRules(Object testObject, TestClass testClass, BddMethod method, Description childDescription,
                           Statement statement) {
        this.testObject = testObject;
        this.testClass = testClass;
        this.method = method;
        this.childDescription = childDescription;
        this.computedStatement = computeStatement(statement);
    }

    @Override
    public void evaluate() throws Throwable {
        this.computedStatement.evaluate();
    }

    private Statement computeStatement(Statement statement) {
        for (MethodRule rule : getMethodRules()) {
            statement = rule.apply(statement, method, testObject);
        }

        for (TestRule rule : getTestRules()) {
            statement = rule.apply(statement, childDescription);
        }

        return statement;
    }

    private Iterable<MethodRule> getMethodRules() {
        List<MethodRule> result = new ArrayList<MethodRule>();
        result.addAll(testClass.getAnnotatedMethodValues(testObject, Rule.class, MethodRule.class));
        result.addAll(testClass.getAnnotatedFieldValues(testObject, Rule.class, MethodRule.class));
        return result;
    }

    private Iterable<TestRule> getTestRules() {
        List<TestRule> result = new ArrayList<TestRule>();
        result.addAll(testClass.getAnnotatedMethodValues(testObject, Rule.class, TestRule.class));
        result.addAll(testClass.getAnnotatedFieldValues(testObject, Rule.class, TestRule.class));
        return result;
    }
}
