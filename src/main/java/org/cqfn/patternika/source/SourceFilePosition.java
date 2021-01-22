package org.cqfn.patternika.source;

import java.util.Objects;

/**
 * Represents a position in source code file (multiline text).
 *
 * @since 2019/12/09
 **/
class SourceFilePosition implements Position {
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

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public int compareTo(final Position other) {
        checkType(other);
        return Integer.compare(this.index, other.getIndex());
    }

    @Override
    public String toString() {
        return String.valueOf(row) + '.' + column;
    }

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
