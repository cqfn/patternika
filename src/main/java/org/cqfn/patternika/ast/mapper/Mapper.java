package org.cqfn.patternika.ast.mapper;

/**
 * Interface responsible for building a mapping between two node trees.
 *
 * @param <T> the type of mapped elements.
 */
public interface Mapper<T> {
    /**
     * Builds and returns a mapping.
     *
     * @return container with mappings between two node trees.
     */
    Mapping<T> buildMapping();
}
