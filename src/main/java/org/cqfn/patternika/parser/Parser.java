package org.cqfn.patternika.parser;

import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.source.Source;

/**
 * Interface for parsers. Hides implementation details.
 *
 * @since 2021/01/21
 */
public interface Parser {
    /**
     * Parses an entire source file specified as source and returns an AST for it.
     *
     * @param source the source to be parsed.
     * @return an AST.
     * @throws ParserException if the parser failed to parse code in the specified source.
     */
    Node parse(Source source) throws ParserException;

    /**
     * Parses a code snippet specified as source and returns an AST for it.
     * <p>
     * A separate method for parsing code snippets is provided because
     * some parsers are oriented on parsing full source code files and
     * they require additional actions to parse incomplete code fragments.
     *
     * @param source the source to be parsed.
     * @return an AST.
     * @throws ParserException if the parser failed to parse code in the specified source.
     */
    Node parseSnippet(Source source) throws ParserException;
}
