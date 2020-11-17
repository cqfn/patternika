package org.cqfn.patternika.source;

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

    /**
     * Checks that position type is {@link SourceFilePosition}.
     *
     * @param position position.
     * @throws IllegalArgumentException if position is not {@link SourceFilePosition}.
     */
    public static void checkType(final Position position) {
        if (!(position instanceof SourceFilePosition)) {
            throw new IllegalArgumentException(
                    "Illegal position type: " + position.getClass().getName()
            );
        }
    }

}
