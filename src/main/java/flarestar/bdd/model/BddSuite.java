package flarestar.bdd.model;

import flarestar.bdd.annotations.Describe;
import flarestar.bdd.annotations.It;

import java.lang.reflect.Method;
import java.util.*;

/**
 * TODO
 */
public class BddSuite {
    private Class<?> testKlass;
    private Class<?>[] testTargets;
    private String customDescription;

    public BddSuite(Class<?> testKlass) {
        this.testKlass = testKlass;

        Describe annotation = testKlass.getAnnotation(Describe.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Test class " + testKlass + " must be annotated w/ @Describe.");
        }

        testTargets = annotation.value();
        customDescription = annotation.desc();
    }

    public Iterable<Class<?>> getInnerSuites() {
        List<Class<?>> result = new ArrayList<Class<?>>();
        for (Class<?> innerClass : testKlass.getDeclaredClasses()) {
            if (innerClass.getAnnotation(Describe.class) == null) {
                continue;
            }

            result.add(innerClass);
        }
        return result;
    }

    public Iterable<Method> getTestMethods() {
        List<Method> methods = new ArrayList<Method>();
        for (Method method : testKlass.getDeclaredMethods()) {
            if (method.getAnnotation(It.class) == null) {
                continue;
            }

            methods.add(method);
        }

        Collections.sort(methods, new Comparator<Method>() {
            public int compare(Method method1, Method method2) {
                return method1.getName().compareTo(method2.getName());
            }
        });
        return methods;
    }

    public String getDescriptionText() {
        StringBuilder result = new StringBuilder();
        result.append(testKlass.getName());

        if (customDescription != null && !customDescription.isEmpty()) {
            result.append(" [tests ");
            result.append(customDescription);
            result.append("]");
        } else if (testTargets.length > 0) {
            result.append(" [tests ");
            for (int i = 0; i != testTargets.length; ++i) {
                if (i != 0) {
                    result.append(", ");
                }
                result.append(testTargets[i].getName());
            }
            result.append("]");
        }

        return result.toString();
    }
}
