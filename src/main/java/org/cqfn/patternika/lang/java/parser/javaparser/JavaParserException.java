package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.Problem;
import com.github.javaparser.Range;
import com.github.javaparser.TokenRange;
import org.cqfn.patternika.parser.ParserException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Exception describing errors that have occurred in JavaParser.
 *
 * @since 2021/01/21
 */
public class JavaParserException extends ParserException {
    private static final long serialVersionUID = -7054849309442589653L;
    /** The list of problems JavaParser encountered during parsing. */
    private final List<Problem> problems;

    /**
     * Main constructor.
     *
     * @param message the exception message.
     * @param problems the list of problems JavaParser encountered during parsing.
     */
    public JavaParserException(final String message, final List<Problem> problems) {
        super(message);
        this.problems = Objects.requireNonNull(problems);
    }

    /**
     * Additional constructor.
     *
     * @param problems the list of problems JavaParser encountered during parsing.
     */
    public JavaParserException(final List<Problem> problems) {
        this("JavaParser failed to parse the document.", problems);
    }

    /**
     * Returns the description that contains the descriptions
     * of all problems JavaParser has encountered when parsing a document.
     *
     * @return the problem description.
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getMessage());
        for (final Problem problem : problems) {
            builder.append(System.lineSeparator());
            final Optional<TokenRange> location = problem.getLocation();
            if (location.isPresent()) {
                final Optional<Range> range = location.get().toRange();
                range.ifPresent(value -> builder.append(String.format("[%s]", value)));
            }
            builder.append(problem.getMessage());
        }
        return builder.toString();
    }
}
