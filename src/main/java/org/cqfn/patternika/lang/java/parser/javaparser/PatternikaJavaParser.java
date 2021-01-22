package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParseStart;
import com.github.javaparser.Provider;
import com.github.javaparser.ast.Node;

import org.cqfn.patternika.parser.Parser;
import org.cqfn.patternika.source.Source;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Parser for the Java language that adapts
 * an AST built by {@link JavaParser} to the Patternika AST.
 *
 * @since 2021/01/21
 */
@SuppressWarnings("PMD")
public class PatternikaJavaParser implements Parser {
    /** Java parser. */
    private final JavaParser parser = new JavaParser();
    /** Variants of parse starts in the order of likelihood (needed to parse snippets). */
    private final List<ParseStart<? extends Node>> parseStarts = Arrays.asList(
            ParseStart.COMPILATION_UNIT,
            ParseStart.CLASS_OR_INTERFACE_TYPE,
            ParseStart.CLASS_BODY,
            ParseStart.BLOCK
    );

    /**
     * Parses the specified source and returns an AST for it.
     *
     * @param source the source to be parsed.
     * @return an AST.
     */
    @Override
    public org.cqfn.patternika.ast.Node parse(final Source source) {
        final ParseResult<? extends Node> result = parse(ParseStart.COMPILATION_UNIT, source);
        if (!result.isSuccessful()) {
            return null;
        }
        final Optional<? extends Node> root = result.getResult();
        return convertAst(root.get());
    }

    /**
     * Parses a code snippet specified as source and returns an AST for it.
     *
     * @param source the source to be parsed.
     * @return an AST.
     */
    @Override
    public org.cqfn.patternika.ast.Node parseSnippet(final Source source) {
        ParseResult<? extends Node> result = null;
        for (final ParseStart<? extends Node> start : parseStarts) {
            result = parse(start, source);
            if (result.isSuccessful()) {
                break;
            }
        }
        if (result == null || !result.isSuccessful()) {
            return null;
        }
        final Optional<? extends Node> root = result.getResult();
        return convertAst(root.get());
    }

    /**
     * Parses the specified source with JavaParser from the specified start.
     *
     * @param start specifies what piece of code is to be parsed.
     * @param source the source to be parsed.
     * @param <N> the root node type for the constructed AST (depends on start).
     * @return a parsing result.
     */
    protected <N extends Node> ParseResult<N> parse(
            final ParseStart<N> start,
            final Source source) {
        final Provider provider = new SourceProvider(source.getIterator());
        return parser.parse(start, provider);
    }

    private org.cqfn.patternika.ast.Node convertAst(final Node root) {
        return null;
    }

}
