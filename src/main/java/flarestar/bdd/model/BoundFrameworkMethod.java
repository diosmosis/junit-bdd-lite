package flarestar.bdd.model;

import flarestar.bdd.runner.Runner;
import org.junit.runners.model.FrameworkMethod;

import java.lang.reflect.Method;

/**
 * TODO
 */
public class BoundFrameworkMethod extends FrameworkMethod {
    private Runner boundRunner;

    public BoundFrameworkMethod(Method method, Runner boundRunner) {
        super(method);
        this.boundRunner = boundRunner;
    }

    @Override
    public Object invokeExplosively(Object target, Object... params) throws Throwable {
        return super.invokeExplosively(boundRunner.getTestObject(), params);
    }
}
