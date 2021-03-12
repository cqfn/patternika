package org.cqfn.patternika.util.cmdline;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * Tests for the {@link CmdLineApi} class.
 *
 * @since 2021/03/03
 */
public class CmdLineApiTest {

    /**
     * Tests that and exception is thrown if no handler is registered for an action.
     */
    @Test(expected = IllegalStateException.class)
    public void testNoHandler() {
        final CmdLineApi api = new CmdLineApi();
        final Action action = new Action(
                "test",
                "test",
                Collections.emptyList(),
                Collections.emptyList()
            );
        api.getHandler(action);
    }

    /**
     * Tests the generated readme text with various actions and options.
     */
    @Test
    public void testActionsAndOptions() {
        final CmdLineApi api = new CmdLineApi();
        final Option opt0 = new Option("opt0", 0);
        final Option opt1 = new Option("opt1", 1);
        final Option opt2 = new Option("opt2", 2);
        api.registerOption(opt0);
        api.registerOption(opt1);
        api.registerOption(opt2);
        final Action action1 = new Action(
                "test", "test",
                2,
                4,
                Arrays.asList("arg1", "arg2", "arg3", "arg4"),
                Arrays.asList(opt0, opt1),
                Collections.singletonList(opt2)
            );
        final Action action2 = new Action(
                "test1", "test1",
                Collections.singletonList("arg1"),
                Collections.singletonList(opt0),
                Collections.singletonList(opt1)
            );
        api.registerAction(action1, (cmdLine) -> { });
        api.registerAction(action2, (cmdLine) -> { });
        Assert.assertSame(action1, api.getAction("test"));
        Assert.assertSame(action2, api.getAction("test1"));
        Assert.assertSame(opt0, api.getOption("opt0"));
        Assert.assertSame(opt1, api.getOption("opt1"));
        Assert.assertSame(opt2, api.getOption("opt2"));
        final String expectedReadme =
                "test: test\n"
              + "  test <arg1> <arg2> [arg3] [arg4] <--opt0> <--opt1 ...> [--opt2 ...]\n"
              + "test1: test1\n"
              + "  test1 <arg1> <--opt0> [--opt1 ...]\n";
        Assert.assertEquals(expectedReadme, api.getReadme());
    }

}
