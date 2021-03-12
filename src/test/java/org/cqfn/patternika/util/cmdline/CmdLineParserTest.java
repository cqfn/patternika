package org.cqfn.patternika.util.cmdline;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tests for the {@link CmdLineParser} class.
 *
 * @since 2021/03/02
 */
@SuppressWarnings("PMD.TooManyMethods")
public class CmdLineParserTest {

    /**
     * Class to hold a boolean value that can be set from an lambda (required for some tests).
     */
    private static class Bool {
        /** Boolean value. */
        private boolean value;
    }

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
        api.registerAction(action, cmdLine -> { });
        final CmdLineParser parser = new CmdLineParser(api);
        parser.parse("hello");
    }

    /**
     * Test that checks that an exception is generated when there is not enough arguments.
     *
     * @throws CmdLineException because not enough arguments for an action.
     */
    @Test(expected = CmdLineException.class)
    public void testNotEnoughArguments() throws CmdLineException {
        final CmdLineApi api = new CmdLineApi();
        final Action action = new Action(
                "test",
                "test",
                Collections.singletonList("arg1"),
                Collections.emptyList()
            );
        api.registerAction(action, cmdLine -> { });
        final CmdLineParser parser = new CmdLineParser(api);
        parser.parse("test");
    }

    /**
     * Test that checks that an exception is generated when there is too many arguments.
     *
     * @throws CmdLineException because too many arguments for an action.
     */
    @Test(expected = CmdLineException.class)
    public void testTooManyArguments() throws CmdLineException {
        final CmdLineApi api = new CmdLineApi();
        final Action action = new Action(
                "test",
                "test",
                Collections.singletonList("arg1"),
                Collections.emptyList()
            );
        api.registerAction(action, cmdLine -> { });
        final CmdLineParser parser = new CmdLineParser(api);
        parser.parse("test", "arg1", "arg2");
    }

    /**
     * Tests that a command line with an unlimited number of arguments is correctly parsed.
     *
     * @throws CmdLineException must not happen in this test.
     * @throws HandlerException must not happen in this test.
     */
    @Test
    public void testUnlimitedArguments() throws CmdLineException, HandlerException {
        final Bool handlerExecuted = new Bool();
        final CmdLineApi api = new CmdLineApi();
        final Action action = new Action(
                "test",
                "test",
                2,
                -1,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
            );
        api.registerAction(action, cmdLine -> {
            Assert.assertEquals("zero", cmdLine.getArgument("0"));
            Assert.assertEquals("one", cmdLine.getArgument("1"));
            Assert.assertEquals("two", cmdLine.getArgument("2"));
            Assert.assertEquals("three", cmdLine.getArgument("3"));
            Assert.assertEquals("four", cmdLine.getArgument("4"));
            Assert.assertEquals("five", cmdLine.getArgument("5"));
            Assert.assertEquals("six", cmdLine.getArgument("6"));
            final List<String> arguments =
                    Arrays.asList("zero", "one", "two", "three", "four", "five", "six");
            Assert.assertEquals(arguments, cmdLine.getArguments());
            handlerExecuted.value = true;
        });
        final CmdLineParser parser = new CmdLineParser(api);
        final CmdLine commandLine = parser.parse(
                "test", "zero", "one", "two", "three", "four", "five", "six"
            );
        commandLine.execute();
        Assert.assertTrue(handlerExecuted.value);
    }

    /**
     * Test that checks that an exception is generated on an unknown option.
     *
     * @throws CmdLineException because the option is unknown.
     */
    @Test(expected = CmdLineException.class)
    public void testUnknownOption() throws CmdLineException {
        final CmdLineApi api = new CmdLineApi();
        final Action action = new Action(
                "test",
                "test",
                Collections.emptyList(),
                Collections.emptyList()
            );
        api.registerAction(action, cmdLine -> { });
        final CmdLineParser parser = new CmdLineParser(api);
        parser.parse("test", "--opt1");
    }

    /**
     * Test that checks that an exception is generated on a duplicated option.
     *
     * @throws CmdLineException because the option is duplicated.
     */
    @Test(expected = CmdLineException.class)
    public void testDuplicatedOption() throws CmdLineException {
        final CmdLineApi api = new CmdLineApi();
        final Option option1 = new Option("opt1", 1);
        final Action action = new Action(
                "test",
                "test",
                Collections.emptyList(),
                Collections.singletonList(option1)
            );
        api.registerOption(option1);
        api.registerAction(action, cmdLine -> { });
        final CmdLineParser parser = new CmdLineParser(api);
        parser.parse("test", "--opt1", "val1", "--opt1", "val2");
    }

    /**
     * Test that checks that an exception is generated on a missing option.
     *
     * @throws CmdLineException because the option is missing.
     */
    @Test(expected = CmdLineException.class)
    public void testMissingOption() throws CmdLineException {
        final CmdLineApi api = new CmdLineApi();
        final Option option1 = new Option("opt1", 1);
        final Action action = new Action(
                "test",
                "test",
                Collections.emptyList(),
                Collections.singletonList(option1)
        );
        api.registerOption(option1);
        api.registerAction(action, cmdLine -> { });
        final CmdLineParser parser = new CmdLineParser(api);
        parser.parse("test");
    }

    /**
     * Test that checks that an exception is generated when there is not enough option arguments.
     *
     * @throws CmdLineException because not enough option arguments.
     */
    @Test(expected = CmdLineException.class)
    public void testNotEnoughOptionArgs() throws CmdLineException {
        final CmdLineApi api = new CmdLineApi();
        final Option option1 = new Option("opt1", 1);
        final Action action = new Action(
                "test",
                "test",
                Collections.emptyList(),
                Collections.singletonList(option1)
            );
        api.registerOption(option1);
        api.registerAction(action, cmdLine -> { });
        final CmdLineParser parser = new CmdLineParser(api);
        parser.parse("test", "--opt1");
    }

    /**
     * Test that a simple command line (action + several options) is correctly parsed.
     *
     * @throws CmdLineException must not happen in this test.
     * @throws HandlerException must not happen in this test.
     */
    @Test
    public void testSimpleAction() throws CmdLineException, HandlerException {
        final Bool handlerExecuted = new Bool();
        final CmdLineApi api = new CmdLineApi();
        final Option option1 = new Option("opt1", 1);
        final Option option2 = new Option("opt2", 2);
        final Option option3 = new Option("opt3", 0);
        final Option option4 = new Option("opt4", 0, true);
        final Option option5 = new Option("opt5", 0);
        api.registerOption(option1);
        api.registerOption(option2);
        api.registerOption(option3);
        api.registerOption(option4);
        api.registerOption(option5);
        final Action action = new Action(
                "test",
                "test",
                Arrays.asList("first", "second"),
                Arrays.asList(option1, option2)
            );
        api.registerAction(action, cmdLine -> {
            Assert.assertEquals(Arrays.asList("hello", "bye"), cmdLine.getArguments());
            Assert.assertEquals("hello", cmdLine.getArgument("first"));
            Assert.assertEquals("bye", cmdLine.getArgument("second"));
            Assert.assertEquals(Collections.emptyList(), cmdLine.getOption("opt4"));
            Assert.assertEquals(Collections.singletonList("aaa"), cmdLine.getOption("opt1"));
            Assert.assertEquals(Arrays.asList("bbb", "ccc"), cmdLine.getOption("opt2"));
            handlerExecuted.value = true;
        });
        final CmdLineParser parser = new CmdLineParser(api);
        final CmdLine commandLine = parser.parse(
                "test",
                "hello",
                "bye",
                "--opt1", "aaa",
                "--opt2", "bbb",
                "ccc", "--opt3",
                "--opt4", "--opt5"
            );
        commandLine.execute();
        Assert.assertTrue(handlerExecuted.value);
        Assert.assertEquals("hello", commandLine.getArgument("first"));
        Assert.assertEquals("bye", commandLine.getArgument("second"));
        Assert.assertTrue(commandLine.hasOption("opt1"));
        Assert.assertTrue(commandLine.hasOption("opt2"));
        Assert.assertFalse(commandLine.hasOption("opt3"));
        Assert.assertEquals(Collections.singletonList("aaa"), commandLine.getOption("opt1"));
        Assert.assertEquals(Arrays.asList("bbb", "ccc"), commandLine.getOption("opt2"));
        Assert.assertEquals(
                "The following options are ignored: '--opt3', '--opt5'",
                commandLine.getIgnoredOptions()
            );
        Assert.assertEquals(
                "test: test\n"
                        + "  test <first> <second> <--opt1 ...> <--opt2 ...>\n", api.getReadme()
            );
    }

    /**
     * Tests that a command-line for an API, which contains dependent options, is correctly parsed.
     *
     * @throws CmdLineException must not happen in this test.
     * @throws HandlerException must not happen in this test.
     */
    @Test
    public void testRelatedRequiredOptions() throws CmdLineException, HandlerException {
        final Bool handlerExecuted = new Bool();
        final CmdLineApi api = new CmdLineApi();
        final Option option1 =
                new Option("opt1", 1);
        final Option option2 =
                new Option("opt2", 1, false, Collections.singletonList(option1));
        final Option option3 =
                new Option("opt3", 1,  false, Collections.singletonList(option1));
        final Option option4 =
                new Option("opt4", 1,  false, Collections.singletonList(option1));
        api.registerOption(option1);
        api.registerOption(option2);
        api.registerOption(option3);
        api.registerOption(option4);
        final Action action = new Action(
                "test",
                "test",
                Collections.singletonList("arg"),
                Collections.singletonList(option2),
                Arrays.asList(option3, option4)
            );
        api.registerAction(action, cmdLine -> {
            handlerExecuted.value = true;
        });
        final CmdLineParser parser = new CmdLineParser(api);
        final CmdLine commandLine = parser.parse(
                "test", "value", "--opt2", "two", "--opt3", "three", "--opt1", "one"
            );
        commandLine.execute();
        Assert.assertTrue(handlerExecuted.value);
        Assert.assertEquals("", commandLine.getIgnoredOptions());
        Assert.assertEquals("value", commandLine.getArgument("arg"));
        Assert.assertEquals(Collections.singletonList("one"), commandLine.getOption("opt1"));
        Assert.assertEquals(Collections.singletonList("two"), commandLine.getOption("opt2"));
        Assert.assertEquals(Collections.singletonList("three"), commandLine.getOption("opt3"));
    }

}
