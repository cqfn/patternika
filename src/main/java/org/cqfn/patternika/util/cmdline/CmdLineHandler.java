package org.cqfn.patternika.util.cmdline;

/**
 * Handler for a specific command-line action.
 *
 * @since 2021/02/25
 */
public interface CmdLineHandler {
    /**
     * Handles a command-line action with specified arguments and options.
     *
     * @param cmdLine the parsed command line.
     * @throws CmdLineException if arguments or options are invalid.
     * @throws HandlerException if the handler failed does to some error.
     */
    void handle(CmdLine cmdLine) throws CmdLineException, HandlerException;
}
