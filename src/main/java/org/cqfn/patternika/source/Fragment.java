package org.cqfn.patternika.source;

import java.util.Objects;

/**
 * Describes a fragment of source code.
 *
 * @since 2020/11/2
 */
public class Fragment {
    /** Fragment source. */
    private final Source source;
    /** Fragment start position. */
    private final Position start;
    /** Fragment end position. */
    private final Position end;

    /**
     * Main constructor.
     *
     * @param source source, not {@code null}.
     * @param start start position, can be {@code null} meaning no start.
     * @param end end position, can be {@code null} meaning no end.
     */
    public Fragment(final Source source, final Position start, final Position end) {
        this.source = Objects.requireNonNull(source);
        // Start is always a lesser existing position.
        if (start == null || start.compareTo(end) < 0) {
            this.start = start;
            this.end = end;
        } else {
            this.start = end;
            this.end = start;
        }
    }

    /**
     * Copy constructor.
     *
     * @param other fragment to be copied, not {@code null}.
     */
    public Fragment(final Fragment other) {
        this(other.source, other.start, other.end);
    }

    /**
     * Additional constructor.
     * <p>
     * Constructs a fragment that matches the whole specified source.
     *
     * @param source source, not {@code null}.
     */
    public Fragment(final Source source) {
        this(source, getStart(source), getEnd(source));
    }

    /**
     * Gets the start position for the specified source.
     *
     * @param source source, not {@code null}.
     * @return the start position.
     */
    private static Position getStart(final Source source) {
        final SourceIterator iterator = source.getIterator();
        return iterator.getPosition();
    }

    /**
     * Gets the end position for the specified source.
     *
     * @param source source, not {@code null}.
     * @return the end position.
     */
    private static Position getEnd(final Source source) {
        final SourceIterator iterator = source.getIterator();
        while (iterator.getChar() != 0) {
            iterator.nextChar();
        }
        return iterator.getPosition();
    }

    /**
     * Returns a source for the fragment.
     *
     * @return source.
     */
    public Source getSource() {
        return source;
    }

    /**
     * Returns the start position for the fragment.
     *
     * @return start position or {@code null} if there is no start.
     */
    public Position getStart() {
        return start;
    }

    /**
     * Returns the end position for the fragment.
     *
     * @return end position or {@code null} if there is no end.
     */
    public Position getEnd() {
        return end;
    }

    /**
     * Returns a fragment for the end position of the current fragment.
     *
     * @return start fragment or {@code null} if there is no start.
     */
    public Fragment getStartFragment() {
        return start != null ? new Fragment(source, start, start) : null;
    }

    /**
     *
     *
     * @return end fragment or {@code null} if there is no end.
     */
    public Fragment getEndFragment() {
        return end != null ? new Fragment(source, end, end) : null;
    }

    /**
     * Checks whether the current fragment contains the other fragment.
     *
     * @param other other fragment.
     * @return {@code true} if this contains other or {@code false} otherwise.
     */
    public boolean contains(final Fragment other) {
        return this.source.equals(other.source)
            && this.start.compareTo(other.start) <= 0
            && this.end.compareTo(other.end) >= 0;
    }

    /**
     * Returns a copy of the fragment with a discarded start.
     *
     * @return a copy of the fragment with a discarded start.
     */
    public Fragment withDiscardedStart() {
        return new Fragment(source, null, end);
    }

    /**
     * Returns a copy of the fragment with a discarded end.
     *
     * @return a copy of the fragment with a discarded end.
     */
    public Fragment withDiscardedEnd() {
        return new Fragment(source, start, null);
    }

}
