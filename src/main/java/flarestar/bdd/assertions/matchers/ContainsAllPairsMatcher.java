package flarestar.bdd.assertions.matchers;

import flarestar.bdd.assertions.utility.Pair;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * TODO
 */
public class ContainsAllPairsMatcher extends BaseMatcher<Map<?, ?>> {
    private List<Pair<?>> expectedMappings;
    private boolean checkExact;

    public ContainsAllPairsMatcher(Pair<?>[] expectedMappings, boolean checkExact) {
        this.expectedMappings = Arrays.asList(expectedMappings);
        this.checkExact = checkExact;
    }

    public boolean matches(Object o) {
        Map<?, ?> map = (Map<?, ?>) o;
        for (Pair<?> mapping : expectedMappings) {
            if (!map.containsKey(mapping.first)
                || !Objects.equals(map.get(mapping.first), mapping.second)
            ) {
                return false;
            }
        }

        if (checkExact) {
            return map.size() == expectedMappings.size();
        } else {
            return true;
        }
    }

    public void describeTo(Description description) {
        description.appendText("map contains following mappings ");
        description.appendList("[", ", ", "]", expectedMappings);
    }
}
