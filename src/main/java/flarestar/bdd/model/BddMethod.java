package flarestar.bdd.model;

import flarestar.bdd.annotations.It;
import flarestar.bdd.runner.Runner;
import org.junit.runner.Describable;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;

import java.lang.reflect.Method;

/**
 * TODO
 */
public class BddMethod extends FrameworkMethod implements Describable {
    private String testDescription;

    public BddMethod(Method method) {
        super(method);

        It annotation = method.getAnnotation(It.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Method " + method.toGenericString()
                + " must be annotated w/ @It to be used with BddMethod.");
        }

        testDescription = "it " + annotation.value();
    }

    public Description getDescription() {
        return Description.createTestDescription(getMethod().getDeclaringClass().getName(), testDescription);
    }
}
