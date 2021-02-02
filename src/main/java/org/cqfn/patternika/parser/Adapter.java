package org.cqfn.patternika.parser;

import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.source.Source;

/**
 * Interface for adapters that convert the AST built by a third-party
 * parser to the Patternika format.
 *
 * @param <T> the type of the root node of the AST built by a third-party parser.
 * @since 2021/01/23
 */
public interface Adapter<T> {
    /**
     * Adapts a AST built by a third-party parser to the Patternika format.
     *
     * @param source the source.
     * @param root the root of the AST to be adapted.
     * @return the AST in the Patternika format.
     */
    Node adapt(Source source, T root);
}
