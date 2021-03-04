package org.cqfn.patternika.source;

import java.util.Objects;

/**
 * Represents a position in source code file (multiline text).
 *
 * @since 2019/12/09
 **/
public class SourceFilePosition implements Position {
    /** Character index. */
    private final int index;

    /** Row index. */
    private final int row;

    /** Column index. */
    private final int column;

    /**
     * Constructor.
     *
     * @param index character index.
     * @param row row index
     * @param column column index.
     */
    SourceFilePosition(final int index, final int row, final int column) {
        this.index = index;
        this.row = row;
        this.column = column;
    }

    /**
     * Returns the row index.
     *
     * @return the row index.
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column index.
     *
     * @return the column index.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Index of the position (character index).
     *
     * @return index.
     */
    @Override
    public int getIndex() {
        return index;
    }

    /**
     * Compares this position with the other position.
     *
     * @param other other position to be compared.
     * @return a negative integer, zero, or a positive integer as this position
     *         is less than, equal to, or greater than the other position.
     */
    @Override
    public int compareTo(final Position other) {
        checkType(other);
        return Integer.compare(this.index, other.getIndex());
    }

    /**
     * Returns textual representation of this position.
     *
     * @return text.
     */
    @Override
    public String toString() {
        return String.valueOf(row) + '.' + column;
    }

    /**
     * Checks this and the specified position for equality.
     *
     * @param obj other position.
     * @return {@code true} or {@code false}.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SourceFilePosition)) {
            return false;
        }
        final SourceFilePosition other = (SourceFilePosition) obj;
        return this.index == other.index
            && this.row == other.row
            && this.column == other.column;
    }

    /**
     * Returns the hash code.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(index, row, column);
    }

    /**
     * Checks that position type is {@link SourceFilePosition}.
     *
     * @param position position.
     * @throws IllegalArgumentException if position is not {@link SourceFilePosition}.
     */
    public static void checkType(final Position position) {
        if (!(position instanceof SourceFilePosition)) {
            final Class<?> clazz = position.getClass();
            throw new IllegalArgumentException("Illegal position type: " + clazz);
        }
    }

}
