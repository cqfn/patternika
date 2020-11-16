package org.cqfn.patternika.source;

import java.util.Objects;

/**
 * Represents source code from a string.
 *
 * @since 2019/10/28
 */
public class SourceString implements Source {
    /** Source code represented as a string. */
    private final String data;

    /**
     * Constructor.
     *
     * @param string Source code represented as a string, not {@code null}.
     */
    public SourceString(final String string) {
        this.data = Objects.requireNonNull(string);
    }

    /**
     * Returns iterator over the string.
     *
     * @return source iterator.
     */
    @Override
    public SourceIterator getIterator() {
        return new SourceStringIterator(data);
    }

    /**
     * Return a fragment of the string in the specified position range.
     *
     * @param start start position.
     * @param end end position.
     * @return fragment of the string.
     */
    @Override
    public String getFragmentAsString(final Position start, final Position end) {
        checkPositionType(start);
        checkPositionType(end);
        return data.substring(start.getIndex(), end.getIndex());
    }

    /**
     * Checks that position type is {@link SourceStringPosition}.
     *
     * @param position position.
     * @throws IllegalArgumentException if position is not {@link SourceStringPosition}.
     */
    private static void checkPositionType(final Position position) {
        if (!(position instanceof SourceStringPosition)) {
            throw new IllegalArgumentException(
                    "Illegal position type: " + position.getClass().getName()
                );
        }
    }

    /**
     * Describes a position in a string.
     */
    private static class SourceStringPosition implements Position {
        /** Index of the position in source code. */
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
            checkPositionType(other);
            return Integer.compare(this.index, other.getIndex());
        }

        @Override
        public String toString() {
            return String.valueOf(index);
        }

    }

    /**
     * Iterator over a string.
     */
    private static class SourceStringIterator implements SourceIterator {
        /** Source code represented as a string. */
        private final String data;
        /** Current position in source code. */
        private int index;

        /**
         * Constructor.
         *
         * @param data Source code represented as a string, not {@code null}.
         */
        SourceStringIterator(final String data) {
            this.data = Objects.requireNonNull(data);
            this.index = 0;
        }

        @Override
        public char getChar() {
            return index < data.length() ? data.charAt(index) : 0;
        }

        @Override
        public char getChar(final int offset) {
            final int pos = index + offset;
            return pos < data.length() ? data.charAt(pos) : 0;
        }

        @Override
        public char nextChar() {
            return index < data.length() ? data.charAt(index++) : 0;
        }

        @Override
        public Position getPosition() {
            return new SourceStringPosition(index);
        }

        @Override
        public Position getPosition(final int offset) {
            return new SourceStringPosition(index + offset);
        }

    }

}
