package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.Position;
import org.cqfn.patternika.source.Source;
import org.cqfn.patternika.source.SourceIterator;

import java.util.Objects;
import java.util.function.Function;

/**
 * A function that converts {@link Position} from JavaParser
 * to a position in the Patternika format.
 *
 * @since 2021/01/28
 */
public class PositionConverter implements Function<Position, org.cqfn.patternika.source.Position> {
    /** Source for positions. */
    private final Source source;
    /** Iterator for the source. */
    private SourceIterator iterator;
    /** Current line. */
    private int line;
    /** Current column. */
    private int column;

    /**
     * Constructor.
     *
     * @param source the source for positions.
     */
    public PositionConverter(final Source source) {
        this.source = Objects.requireNonNull(source);
        reset();
    }

    /**
     * Resets the iterator to the start of the source.
     */
    private void reset() {
        this.iterator = source.getIterator();
        this.line = 1;
        this.column = 1;
    }

    /**
     * Applies this function to the given argument.
     *
     * @param pos the original position the function argument
     * @return the converted position that points to source.
     */
    @Override
    public org.cqfn.patternika.source.Position apply(final Position pos) {
        if (pos.line < line || pos.line == line && pos.column < column) {
            // Need to start from the beginning.
            reset();
        }
        // Scroll the current position until it becomes equal to the target position.
        while (line < pos.line || line == pos.line && column < pos.column) {
            final char val = iterator.getChar();
            if (val == 0) {
                throw new IllegalStateException(
                        "Failed to find position " + pos + " in the source!");
            }
            if (val == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
            iterator.nextChar();
        }
        return iterator.getPosition();
    }

}
