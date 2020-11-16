package org.cqfn.patternika.source;

/**
 * Represents source code.
 *
 * @since 2019/10/28
**/
public interface Source {

    /**
     * Returns iterator over this source code.
     *
     * @return source iterator.
     */
    SourceIterator getIterator();

    /**
     * Return a string for a source code fragment within the specified range.
     *
     * @param start start position.
     * @param end end position.
     * @return text for the fragment.
     */
    String getFragmentAsString(Position start, Position end);

}
