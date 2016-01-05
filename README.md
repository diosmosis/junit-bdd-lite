# junit-bdd-lite

Lightweight BDD for Java 5 and above.

## Description

This repo contains a junit Runner that lets you create unit tests using an
interface similar to interfaces provided by JavaScript BDD testing libraries
like Jasmine, Mocha or Chai.

Instead of either using undescriptive test method names like `testFeature` or
descriptive but excessively long method names like `testFeatureDoesNotThrowWhenSuppliedCertainParameters`,
you can use `@Describe`/`@It` annotations to create more readable tests and
test output:

```
@Describe(MyTestSubject.class)
public class MyTestSubjectTest {
    private MyTestSubject instance;

    public void beforeEach() {
        this.instance = new MyTestSubject();
    }

    @It("should not throw when doSomething() is called")
    public void testDoSomething() {
        this.instance.doSomething();
    }
}
```

And instead of using junit asserts or hamcrest matchers to assert conditions in
tests, you can use the more readable `expect()` based interface:

```
@It("should add two values correctly when .add(...) is called")
public void testAddMethod() {
    int result = this.calculator.add(10, 20);
    expect(result).to().be().equal(30);
}
```

junit-bdd-lite mimics the behavior of Mocha and Chai whenever possible. Regarding
Mocha, this means you can nest `@Describe` tests just as you can in Mocha:

```
@Describe(desc = "some system")
public class MySomeFeatureTest {

    @Describe(desc = "some subsystem")
    public class MySubsystemTest {
        // ...
    }

    // ...
}
```

With regards to Chai, it means almost every assertion in Chai that isn't strictly related
to JavaScript exists in junit-bdd-lite as well.

## Limitations

- Some assertions that are provided by Chai and could exist in Java are currently missing,
  eg, asserting deep equality of objects.
- The runner is not well integrated into Junit, so it's not possible to use annotations
  that are meant to be used in normal junit tests.

## Custom Assertions

You can add your own assertions to the assertion DSL by extending the `AssertionInvoker`
interface. Add methods to the interface and annotate each with either the `@ChainableMethod`
or `@AssertionMethod` annotations, eg:

```
interface MyCustomAssertionInvoker extends AssertionInvoker {
    @ChainableMethod
    AssertionInvoker with();

    @AssertionMethod(klass = MyAsserts.class, method = "assertWithStyle")
    void style(String razzmatazz);
}

// somewhere else
class MyAsserts {
    public void assertWithStyle(Map<String, String> flags, Object actualValue, String razzmatazz) {
        // ... perform your assert here ...
    }
}
```

Then supply your `flarestar.bdd.Assert.expect()` like so:

```
@It("should be stylin")
public void testStyle() {
    Style style = ...;
    expect(MyCustomAssertionInvoker.class, style).to().be().with().style("sizzle");
}
```
