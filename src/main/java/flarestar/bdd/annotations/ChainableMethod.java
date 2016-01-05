package flarestar.bdd.annotations;

import flarestar.bdd.assertions.ValueManipulator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ChainableMethod {
    String flag() default "";
    String value() default "";
    Class<? extends ValueManipulator> manipulator() default ValueManipulator.class;
}
