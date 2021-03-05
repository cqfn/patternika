package org.cqfn.patternika.handlers;

import org.cqfn.patternika.util.cmdline.Handler;

import java.util.List;
import java.util.Map;

/**
 * Handler for the "parse-folder" command-line action.
 *
 * @since 2021/03/05
 */
public class ParseFolderHandler implements Handler {
    /**
     * Handles a command-line action with specified arguments and options.
     *
     * @param arguments the argument by names.
     * @param options   the options by names (one option can have multiple values).
     */
    @Override
    public void handle(
            final Map<String, String> arguments,
            final Map<String, List<String>> options) {
        // ToDo
    }
}
