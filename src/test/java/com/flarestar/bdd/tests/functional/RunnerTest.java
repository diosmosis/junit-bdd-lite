package com.flarestar.bdd.tests.functional;

import com.flarestar.bdd.tests.functional.tests.ExampleTest;
import flarestar.bdd.annotations.Describe;
import flarestar.bdd.annotations.It;
import flarestar.bdd.runner.Runner;
import org.junit.Assert;
import org.junit.internal.TextListener;
import org.junit.runner.Computer;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

/**
 * Functional test that tests the whole system, including the JUnit output that is generated.
 */
@RunWith(Runner.class)
@Describe(desc = "system")
public class RunnerTest {

    private final static boolean REMOVE_STACKTRACE_FROM_OUTPUT = true;

    @It("should correctly run @Describe tests")
    public void testSystem() throws Throwable {
        Computer computer = new Computer();
        JUnitCore jUnitCore = new JUnitCore();

        ByteArrayOutputStream capture = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(capture);
        TextListener listener = new TextListener(printStream) {
            // override so we don't print out exception backtraces for this test
            @Override
            protected void printFailure(Failure each, String prefix) {
                printStream.println(prefix + ") " + each.getTestHeader());

                String trace = each.getTrace();

                if (REMOVE_STACKTRACE_FROM_OUTPUT) {
                    trace = trace.substring(0, trace.indexOf("\n\tat"));
                    trace = trace.replaceAll("@[a-zA-Z0-9]+>", "@...>");
                    trace = trace.replaceAll("[ \t]+(\r\n?|\n)", "\n");
                }

                printStream.println(trace);
            }
        };
        jUnitCore.addListener(listener);

        PrintStream systemOut = System.out;
        System.setOut(printStream);
        try {
            jUnitCore.run(computer, ExampleTest.class);
        } finally {
            System.setOut(systemOut);
        }

        String output = capture.toString("UTF-8");
        output = normalizeOutput(output);

        String expectedOutput = getResource("/RunnerTest.testSystem.expected.txt");
        Assert.assertEquals(expectedOutput, output);
    }

    private String normalizeOutput(String output) {
        output = output.replaceAll("Time:[^\n]+", "Time:");
        return output;
    }

    private String getResource(String path) throws IOException, URISyntaxException {
        URL resourceUrl = getClass().getResource(path);
        if (resourceUrl == null) {
            throw new RuntimeException("Cannot find resource: " + path);
        }

        return (new Scanner(new File(resourceUrl.getFile()))).useDelimiter("\\Z").next() + "\n";
    }
}
