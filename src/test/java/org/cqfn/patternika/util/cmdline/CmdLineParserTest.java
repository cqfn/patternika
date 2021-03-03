package org.cqfn.patternika.util.cmdline;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests for the {@link CmdLineParser} class.
 *
 * @since 2021/03/02
 */
public class CmdLineParserTest {

    /**
     * Test that checks that an exception is generated on an empty command line.
     *
     * @throws CmdLineException because the command line is empty.
     */
    @Test(expected = CmdLineException.class)
    public void testEmpty() throws CmdLineException {
        final CmdLineApi api = new CmdLineApi();
        final CmdLineParser parser = new CmdLineParser(api);
        parser.parse();
    }

    /**
     * Test that checks that an exception is generated on an unknown action.
     *
     * @throws CmdLineException because the action is unknown.
     */
    @Test(expected = CmdLineException.class)
    public void testUnknownAction() throws CmdLineException {
        final CmdLineApi api = new CmdLineApi();
        final Action action =
                new Action("test", "test", Collections.emptyList(), Collections.emptyList());
        api.registerAction(action, (arguments, options) -> { });
        final CmdLineParser parser = new CmdLineParser(api);
        parser.parse("hello");
    }

    /**
     * Test that a trivial command line (action without options) is correctly parsed.
     *
     * @throws CmdLineException must not happen in this test.
     */
    @Test
    public void testTrivialAction() throws CmdLineException {
        final CmdLineApi api = new CmdLineApi();
        final Action action = new Action(
                "test",
                "test",
                Collections.singletonList("arg1"),
                Collections.emptyList()
            );
        api.registerAction(action, (arguments, options) -> { });
        final CmdLineParser parser = new CmdLineParser(api);
        final CmdLine commandLine = parser.parse("test", "hello");
        Assert.assertEquals("hello", commandLine.getArgument("arg1"));
        Assert.assertEquals("", commandLine.getIgnoredOptions());
    }

    /**
     * Test that a simple command line (action + several options) is correctly parsed.
     *
     * @throws CmdLineException must not happen in this test.
     */
    @Test
    public void testSimpleAction() throws CmdLineException {
        final CmdLineApi api = new CmdLineApi();
        final Option option1 = new Option("opt1", 1);
        final Option option2 = new Option("opt2", 2);
        final Option option3 = new Option("opt3", 0);
        final Option option4 = new Option("opt4", 0);
        api.registerOption(option1);
        api.registerOption(option2);
        api.registerOption(option3);
        api.registerOption(option4);
        final Action action = new Action(
            "test",
            "test",
            Arrays.asList("first", "second"),
            Arrays.asList(option1, option2)
        );
        final Map<String, String> expectedArguments = new HashMap<>();
        expectedArguments.put("first", "hello");
        expectedArguments.put("second", "bye");
        final Map<String, List<String>> expectedOptions = new HashMap<>();
        expectedOptions.put("opt1", Collections.singletonList("aaa"));
        expectedOptions.put("opt2", Arrays.asList("bbb", "ccc"));
        api.registerAction(action, (arguments, options) -> {
            Assert.assertEquals(expectedArguments, arguments);
            Assert.assertEquals(expectedOptions, options);
        });
        final CmdLineParser parser = new CmdLineParser(api);
        final CmdLine commandLine = parser.parse(
                "test", "hello", "bye", "--opt1", "aaa", "--opt2", "bbb", "ccc", "--opt3", "--opt4"
            );
        commandLine.execute();
        Assert.assertEquals("hello", commandLine.getArgument("first"));
        Assert.assertEquals("bye", commandLine.getArgument("second"));
        Assert.assertTrue(commandLine.hasOption("opt1"));
        Assert.assertTrue(commandLine.hasOption("opt2"));
        Assert.assertFalse(commandLine.hasOption("opt3"));
        Assert.assertEquals(Collections.singletonList("aaa"), commandLine.getOption("opt1"));
        Assert.assertEquals(Arrays.asList("bbb", "ccc"), commandLine.getOption("opt2"));
        Assert.assertEquals(
                "The following options are ignored: '--opt3', '--opt4'",
                commandLine.getIgnoredOptions()
            );
        Assert.assertEquals(
                "test: test\n"
                        + "  test <first> <second> <--opt1 ...> <--opt2 ...>\n", api.getReadme()
            );
    }

}
