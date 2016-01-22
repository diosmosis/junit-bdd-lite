package flarestar.bdd.assertions.utility;

/**
 * TODO
 */
public class ComparisonHelper {
    public static boolean equals(Object lhs, Object rhs) {
        return lhs == rhs || (lhs != null && lhs.equals(rhs));
    }
}
