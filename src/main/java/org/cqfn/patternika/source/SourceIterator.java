package org.cqfn.patternika.source;

/**
 * An iterator for iterating over characters in source code.
 *
 * @since 2019/10/28
 */
public interface SourceIterator {

    /**
     * Returns the next character and moves current position forward by one step.
     *
     * @return next character or {@code '\0'} if the end is reached.
     */
    char nextChar();

    /**
     * Gets a character at the current position.
     *
     * @return current character or {@code '\0'} if the current position is out of range.
     */
    default char getChar() {
        return getChar(0);
    }

    /**
     * Gets a character at the specified offset from the current position.
     *
     * @param offset the offset from the current position.
     * @return character or {@code '\0'} if the offset is out of range.
     */
    char getChar(int offset);

    /**
     * Returns the current position.
     *
     * @return position.
     */
    default Position getPosition() {
        return getPosition(0);
    }

    /**
     * Returns a position at the given offset from the current position.
     *
     * @param offset the offset from the current position.
     * @return position.
     */
    Position getPosition(int offset);

}
