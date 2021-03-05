package org.cqfn.patternika;

import org.cqfn.patternika.handlers.ParseFileHandler;
import org.cqfn.patternika.handlers.ParseHandler;
import org.cqfn.patternika.util.cmdline.Action;
import org.cqfn.patternika.util.cmdline.CmdLineApi;

import java.util.Collections;

/**
 * Describes the Patternika command-line API.
 *
 * @since 2021/03/05
 */
public class PatternikaApi {
    /** Command-line API. */
    private final CmdLineApi api = new CmdLineApi();

    /**
     * Constructor.
     */
    public PatternikaApi() {
        registerParse();
        registerParseFile();
        registerParseFolder();
    }

    /**
     * Returns the command-line API for Patternika.
     *
     * @return the command-line API for Patternika.
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
        api.registerAction(action, new ParseFileHandler());
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
