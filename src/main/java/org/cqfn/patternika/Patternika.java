package org.cqfn.patternika;

import org.cqfn.patternika.util.cmdline.CmdLine;
import org.cqfn.patternika.util.cmdline.CmdLineApi;
import org.cqfn.patternika.util.cmdline.CmdLineException;
import org.cqfn.patternika.util.cmdline.CmdLineParser;
import org.cqfn.patternika.util.cmdline.HandlerException;

/**
 * The main class (entry point) for the tool.
 *
 * @since 2021/02/18
 */
public final class Patternika {
    /**
     * Disabled constructor: creating instances is forbidden.
     */
    private Patternika() { }

    /**
     * The entry point. Parses the command line and runs actions.
     * <p>
     * Error handling policy:
     * <p>
     * 1. Exceptions caused by programming errors (invariant violations) to be fixed
     * must cause the tool to fail and must not be suppressed.
     * <p>
     * 2. Exceptions caused by unrecoverable errors (especially related to invalid user input)
     * must be propagated to the main function.
     * <p>
     * 3. Exceptions caused by recoverable errors must be reported to the log and
     * should not be propagated.
     *
     * @param args the command-line arguments.
     */
    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(final String[] args) {
        final CmdLineApi api = new PatternikaApi().getCmdLineApi();
        final CmdLineParser parser = new CmdLineParser(api);
        try {
            final CmdLine cmdLine = parser.parse(args);
            cmdLine.execute();
        } catch (final CmdLineException ex) {
            System.err.print("Invalid command-line format. Error: ");
            System.err.println(ex.getMessage());
            System.out.println("Try one of:");
            System.out.println(api.getReadme());
        } catch (final HandlerException ex) {
            System.err.println("Action failed due to an error.");
            System.err.println(ex.getMessage());
        }
    }

}
