package org.cqfn.patternika.parser;

/**
 * Exception that describes errors that occur during parsing causing parser failures.
 *
 * @since 2021/01/22
 */
public class ParserException extends Exception {
    private static final long serialVersionUID = -1817059177987958147L;

    /**
     * Overridden constructor of a base class.
     */
    public ParserException() {
        super();
    }

    /**
     * Overridden constructor of a base class.
     *
     * @param message the exception message.
     */
    public ParserException(final String message) {
        super(message);
    }

    /**
     * Overridden constructor of a base class.
     *
     * @param message the exception message.
     * @param cause the cause of the problem.
     */
    public ParserException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Overridden constructor of a base class.
     *
     * @param cause the cause of the problem.
     */
    public ParserException(final Throwable cause) {
        super(cause);
    }
}
