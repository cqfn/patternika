package org.cqfn.patternika;

import org.cqfn.patternika.handlers.ParseFileHandler;
import org.cqfn.patternika.handlers.ParseHandler;
import org.cqfn.patternika.parser.Parsers;
import org.cqfn.patternika.util.cmdline.Action;
import org.cqfn.patternika.util.cmdline.CmdLineApi;
import org.cqfn.patternika.util.cmdline.Option;

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

    /**
     * Constructor.
     */
    public PatternikaApi() {
        api.registerOption(dot);
        registerParse();
        registerParseFile();
        registerParseFolder();
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
                Collections.emptyList()
        );
        api.registerAction(action, new ParseHandler());
    }

    private void registerParseFile() {
        final Action action = new Action(
                "parse-file",
                "Parse a file",
                Collections.singletonList("file"),
                Collections.emptyList()
        );
        api.registerAction(action, new ParseFileHandler(parsers));
    }

    private void registerParseFolder() {
        final Action action = new Action(
                "parse-folder",
                "Parse a folder",
                Collections.singletonList("folder"),
                Collections.emptyList()
        );
        api.registerAction(action, new ParseHandler());
    }

}
