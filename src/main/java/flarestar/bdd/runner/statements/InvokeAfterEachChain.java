package flarestar.bdd.runner.statements;

import flarestar.bdd.model.BoundFrameworkMethod;
import flarestar.bdd.runner.Runner;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class InvokeAfterEachChain extends RunAfters {
    public InvokeAfterEachChain(Runner runner, Statement previous) {
        super(previous, getAfterEachChain(runner), null);
    }

    private static List<FrameworkMethod> getAfterEachChain(Runner runner) {
        List<FrameworkMethod> result = new ArrayList<FrameworkMethod>();
        getAfterEachChain(runner, result);
        return result;
    }

    private static void getAfterEachChain(Runner runner, List<FrameworkMethod> result) {
        FrameworkMethod afterEachMethod = getAfterEachMethod(runner);
        if (afterEachMethod != null) {
            result.add(afterEachMethod);
        }

        Runner parentRunner = runner.getParentRunner();
        if (parentRunner != null) {
            getAfterEachChain(parentRunner, result);
        }
    }

    private static FrameworkMethod getAfterEachMethod(Runner runner) {
        try {
            Method method = runner.getTestClass().getJavaClass().getMethod("afterEach");
            return new BoundFrameworkMethod(method, runner);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
