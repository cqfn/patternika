package org.cqfn.patternika.source;

/**
 * Describes a position in a string.
 *
 * @since 2019/10/28
 */
public class SourceStringPosition implements Position {
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
        return String.valueOf(index);
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
        if (!(obj instanceof SourceStringPosition)) {
            return false;
        }
        final SourceStringPosition other = (SourceStringPosition) obj;
        return this.index == other.index;
    }

    /**
     * Returns the hash code.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return index;
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
