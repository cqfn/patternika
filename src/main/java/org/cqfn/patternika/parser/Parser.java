package org.cqfn.patternika.parser;

import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.source.Source;

/**
 * Interface for parsers. Hides implementation details..
 *
 * @since 2021/01/21
 */
public interface Parser {

    /**
     * Parses the specified source and returns an AST for it.
     *
     * @param source the source to be parsed.
     * @return an AST.
     */
    Node parse(Source source);

}
