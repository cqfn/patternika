package org.cqfn.patternika.source;

/**
 * Describes a fragment of source code.
 *
 * @since 2020/11/2
 */
public interface Fragment {

    /**
     * Returns a source for the fragment.
     *
     * @return source.
     */
    Source getSource();

    /**
     * Returns the start position for the fragment.
     *
     * @return start position or {@code null} if there is no start.
     */
    Position getStart();

    /**
     * Returns the end position for the fragment.
     *
     * @return end position or {@code null} if there is no end.
     */
    Position getEnd();

    /**
     * Checks whether the current fragment contains the other fragment.
     *
     * @param other other fragment.
     * @return {@code true} if this contains other or {@code false} otherwise.
     */
    boolean contains(Fragment other);

}
