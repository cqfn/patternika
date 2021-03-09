package org.cqfn.patternika.util.cmdline;

import java.util.List;
import java.util.Map;

/**
 * Handler for a specific command-line action.
 *
 * @since 2021/02/25
 */
public interface Handler {
    /**
     * Handles a command-line action with specified arguments and options.
     *
     * @param arguments the argument by names.
     * @param options the options by names (one option can have multiple values).
     * @throws CmdLineException if arguments or options are invalid.
     * @throws HandlerException if the handler failed does to some error.
     */
    void handle(
            Map<String, String> arguments,
            Map<String, List<String>> options) throws CmdLineException, HandlerException;
}
