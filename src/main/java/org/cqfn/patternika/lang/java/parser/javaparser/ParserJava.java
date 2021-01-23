package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParseStart;
import com.github.javaparser.Problem;
import com.github.javaparser.Provider;
import com.github.javaparser.ast.Node;

import org.cqfn.patternika.parser.Parser;
import org.cqfn.patternika.parser.ParserException;
import org.cqfn.patternika.source.Source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Parses Java code with {@link JavaParser} and adapts the AST to the Patternika format.
 *
 * @since 2021/01/21
 */
public class ParserJava implements Parser {
    /** Java parser. */
    private final JavaParser parser = new JavaParser();
    /** Adapts the AST build by JavaParser to the Patternika format. */
    private final Adapter adapter;
    /** Variants of parse starts in the order of likelihood (needed to parse snippets). */
    private final List<ParseStart<? extends Node>> parseStarts = Arrays.asList(
            ParseStart.COMPILATION_UNIT,
            ParseStart.CLASS_OR_INTERFACE_TYPE,
            ParseStart.CLASS_BODY,
            ParseStart.BLOCK
    );

    /**
     * Constructor.
     *
     * @param adapter the adapter to convert the AST build by JavaParser to the Patternika format.
     */
    public ParserJava(final Adapter adapter) {
        this.adapter = Objects.requireNonNull(adapter);
    }

    /**
     * Parses the specified source and returns an AST for it.
     *
     * @param source the source to be parsed.
     * @return an AST.
     * @throws ParserException if the parser failed to parse code in the specified source.
     */
    @Override
    public JavaNode parse(final Source source) throws ParserException {
        final ParseResult<? extends Node> result = parse(ParseStart.COMPILATION_UNIT, source);
        if (!result.isSuccessful()) {
            throw new JavaParserException(result.getProblems());
        }
        final Optional<? extends Node> root = result.getResult();
        assert root.isPresent() : "No AST in the parser result!";
        return adapter.adapt(source, root.get());
    }

    /**
     * Parses a code snippet specified as source and returns an AST for it.
     *
     * @param source the source to be parsed.
     * @return an AST.
     * @throws ParserException if the parser failed to parse code in the specified source.
     */
    @Override
    public JavaNode parseSnippet(final Source source) throws ParserException {
        ParseResult<? extends Node> result = null;
        final List<Problem> problems = new ArrayList<>();
        for (final ParseStart<? extends Node> start : parseStarts) {
            result = parse(start, source);
            if (result.isSuccessful()) {
                break;
            }
            problems.addAll(result.getProblems());
        }
        assert result != null;
        if (!result.isSuccessful()) {
            throw new JavaParserException("JavaParser failed to parse the snippet.", problems);
        }
        final Optional<? extends Node> root = result.getResult();
        assert root.isPresent() : "No AST in the parser result!";
        return adapter.adapt(source, root.get());
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
        try (Provider provider = new SourceProvider(source.getIterator())) {
            return parser.parse(start, provider);
        } catch (final IOException ex) {
            throw new AssertionError("Unexpected IO error!", ex);
        }
    }

}
