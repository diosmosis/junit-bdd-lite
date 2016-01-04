package flarestar.bdd.assertions.exceptions;

/**
 * TODO
 */
public class InvalidAssertionInvokerMethod extends RuntimeException {
    public InvalidAssertionInvokerMethod() {
        super();
    }

    public InvalidAssertionInvokerMethod(String message) {
        super(message);
    }

    public InvalidAssertionInvokerMethod(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAssertionInvokerMethod(Throwable cause) {
        super(cause);
    }

    protected InvalidAssertionInvokerMethod(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
