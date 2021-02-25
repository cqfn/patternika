package org.cqfn.patternika.launcher;

/**
 * Exception caused by an issue in command line parsing.
 *
 * @since 2021/02/25
 */
public class CmdLineException extends Exception {
    private static final long serialVersionUID = 3753636909754503692L;

    /**
     * Constructor.
     *
     * @param message the exception message.
     */
    public CmdLineException(final String message) {
        super(message);
    }
}
