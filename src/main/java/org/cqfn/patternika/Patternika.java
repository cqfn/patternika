package org.cqfn.patternika;

import org.cqfn.patternika.launcher.CmdLine;
import org.cqfn.patternika.launcher.CmdLineApi;
import org.cqfn.patternika.launcher.CmdLineException;
import org.cqfn.patternika.launcher.CmdLineParser;

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
     * The entry point.
     *
     * @param args the command-line arguments.
     */
    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(final String[] args) {
        final CmdLineApi api = createApi();
        final CmdLineParser parser = new CmdLineParser(api);
        try {
            final CmdLine cmdLine = parser.parse(args);
            cmdLine.execute();
        } catch (final CmdLineException ex) {
            System.err.print("Invalid command-line format. Error: ");
            System.err.println(ex.getMessage());
            System.out.println(api.getReadme());
        }
    }

    /**
     * Creates the command-line API for Patternika.
     *
     * @return the command-line API for Patternika.
     */
    private static CmdLineApi createApi() {
        return new CmdLineApi();
    }

}
