package org.cqfn.patternika.source;

/**
 * Describes a position in a string.
 *
 * @since 2019/10/28
 */
class SourceStringPosition implements Position {
    /** Index of the position (character index). */
    private final int index;

    /**
     * Constructor.
     *
     * @param index index.
     */
    SourceStringPosition(final int index) {
        this.index = index;
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
        return String.valueOf(index);
    }

    /**
     * Checks that position type is {@link SourceStringPosition}.
     *
     * @param position position.
     * @throws IllegalArgumentException if position is not {@link SourceStringPosition}.
     */
    public static void checkType(final Position position) {
        if (!(position instanceof SourceStringPosition)) {
            final Class<?> clazz = position.getClass();
            throw new IllegalArgumentException("Illegal position type: " + clazz);
        }
    }

}
