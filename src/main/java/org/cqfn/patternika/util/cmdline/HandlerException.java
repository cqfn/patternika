package org.cqfn.patternika.util.cmdline;

/**
 * Exception causes by an error that occurred in a command-line handler.
 *
 * @since 2021/03/09
 */
public class HandlerException extends Exception {
    private static final long serialVersionUID = 1784562746745653412L;

    /**
     * Constructor.
     *
     * @param message the exception message.
     */
    public HandlerException(final String message) {
        super(message);
    }

    /**
     * Constructor with exception cause.
     *
     * @param message the exception message.
     * @param cause the exception cause.
     */
    public HandlerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
