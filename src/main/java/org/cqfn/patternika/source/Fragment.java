package org.cqfn.patternika.source;

/**
 * Describes a fragment of source code.
 *
 * @since 2020/11/2
 */
public interface Fragment {

    /**
     * Returns a source for the given fragment.
     *
     * @return source.
     */
    Source getSource();

}

