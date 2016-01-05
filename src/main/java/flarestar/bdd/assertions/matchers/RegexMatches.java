package flarestar.bdd.assertions.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.regex.Pattern;

/**
 * TODO
 */
public class RegexMatches extends BaseMatcher<Object> {
    private Pattern pattern;

    public RegexMatches(Pattern pattern) {
        this.pattern = pattern;
    }

    public boolean matches(Object o) {
        if (!(o instanceof CharSequence)) {
            throw new IllegalArgumentException("Only CharSequence can be used w/ Regex matching.");
        }

        return pattern.matcher((CharSequence)o).find();
    }

    public void describeTo(Description description) {
        description.appendText("to match ").appendText(pattern.pattern());
    }
}
