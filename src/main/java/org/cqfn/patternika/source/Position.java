package org.cqfn.patternika.source;

/**
 * Represents a position in a source code.
 *
 * @since 2019/10/28
 **/
public interface Position extends Comparable<Position> {

    /**
     * Index of the position (character index).
     *
     * @return index.
     */
    int getIndex();

    /**
     * Compares this position with the other position.
     *
     * @param other other position to be compared.
     * @return a negative integer, zero, or a positive integer as this position
     *         is less than, equal to, or greater than the other position.
     */
    @Override
    int compareTo(Position other);

    /**
     * Returns a textual representation of the position.
     *
     * @return textual representation.
     */
    @Override
    String toString();

}
