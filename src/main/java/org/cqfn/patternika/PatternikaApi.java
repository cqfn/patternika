package org.cqfn.patternika;

import org.cqfn.patternika.handlers.ParseHandler;
import org.cqfn.patternika.parser.Parsers;
import org.cqfn.patternika.util.cmdline.Action;
import org.cqfn.patternika.util.cmdline.CmdLineApi;
import org.cqfn.patternika.util.cmdline.Option;

import java.util.Arrays;
import java.util.Collections;

/**
 * Describes the Patternika command-line API.
 *
 * @since 2021/03/05
 */
public class PatternikaApi {
    /** Command-line API. */
    private final CmdLineApi api = new CmdLineApi();

    /** Parsers. */
    private final Parsers parsers = new PatternikaParsers().getParsers();

    /** Option for external DOT visualizer tool. */
    private final Option dot = new Option("dot", 1, true);

    /** Option that specifies that the AST must be saved to an image. */
    private final Option dumpAst = new Option("dump-ast", 0);

    /** Option that specifies that the AST must be saved to a JSON file. */
    private final Option dumpJson = new Option("dump-json", 0);

    /**
     * Constructor.
     */
    public PatternikaApi() {
        api.registerOption(dot);
        api.registerOption(dumpAst);
        api.registerOption(dumpJson);
        registerParse();
    }

    /**
     * Gets the command-line API.
     *
     * @return the command-line API.
     */
    public CmdLineApi getCmdLineApi() {
        return api;
    }

    private void registerParse() {
        final Action action = new Action(
                "parse",
                "Parse a file or a folder",
                Collections.singletonList("source"),
                Collections.emptyList(),
                Arrays.asList(dumpAst, dumpJson)
            );
        api.registerAction(action, new ParseHandler(parsers));
    }

}
