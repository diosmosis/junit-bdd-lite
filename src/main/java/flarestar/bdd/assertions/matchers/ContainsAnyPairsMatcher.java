package flarestar.bdd.assertions.matchers;

import flarestar.bdd.assertions.utility.ComparisonHelper;
import flarestar.bdd.assertions.utility.Pair;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * TODO
 */
public class ContainsAnyPairsMatcher extends BaseMatcher<Map<?, ?>> {
    private List<Pair<?>> expectedMappings;

    public ContainsAnyPairsMatcher(Pair<?>[] expectedMappings) {
        this.expectedMappings = Arrays.asList(expectedMappings);
    }

    public boolean matches(Object o) {
        Map<?, ?> map = (Map<?, ?>) o;
        for (Pair<?> mapping : expectedMappings) {
            if (map.containsKey(mapping.first)
                && ComparisonHelper.equals(map.get(mapping.first), mapping.second)
            ) {
                return true;
            }
        }
        return false;
    }

    public void describeTo(Description description) {
        description.appendText("map contains any of the following mappings ");
        description.appendList("[", ", ", "]", expectedMappings);
    }
}
