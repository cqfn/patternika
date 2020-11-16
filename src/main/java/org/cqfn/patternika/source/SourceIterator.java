package org.cqfn.patternika.source;

/**
 * An iterator for iterating over source code.
 *
 * @since 2019/10/28
 */
public interface SourceIterator {

    /**
     * Returns the next character.
     *
     * @return next character or {@code '\0'} if the end of the source code is reached.
     */
    char nextChar();

    /**
     * Gets a character at the specified offset.
     *
     * @param offset the offset in the source code.
     * @return character of {@code '\0'} if the offset is out of range.
     */
    char getChar(int offset);

    /**
     * Gets the first character in the source code.
     *
     * @return the first character or {@code '\0'} if the source code is empty.
     */
    default char getChar() {
        return getChar(0);
    }

    /**
     * Returns a position for the given offset.
     *
     * @param offset the offset in the source code.
     * @return position.
     */
    Position getPosition(int offset);

    /**
     * Returns the starting position for the source code.
     *
     * @return position.
     */
    default Position getPosition() {
        return getPosition(0);
    }

}
