package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParseStart;
import com.github.javaparser.Problem;
import com.github.javaparser.ast.Node;

import org.cqfn.patternika.parser.Parser;
import org.cqfn.patternika.parser.ParserException;
import org.cqfn.patternika.source.Source;
import org.cqfn.patternika.source.SourceIterator;

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
    private final JavaParser parser;
    /** Adapts the AST build by JavaParser to the Patternika format. */
    private final Adapter adapter;
    /** Variants of parse starts in the order of likelihood (needed to parse snippets). */
    private final List<ParseStart<? extends Node>> parseStarts;

    /**
     * Constructor.
     *
     * @param adapter the adapter to convert the AST build by JavaParser to the Patternika format.
     */
    public ParserJava(final Adapter adapter) {
        this.parser = new JavaParser();
        this.adapter = Objects.requireNonNull(adapter);
        this.parseStarts = Arrays.asList(
            ParseStart.COMPILATION_UNIT,
            ParseStart.CLASS_BODY,
            ParseStart.BLOCK,
            ParseStart.EXPRESSION
        );
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
        final SourceIterator iterator = source.getIterator();
        final ParseResult<? extends Node> result = parse(ParseStart.COMPILATION_UNIT, iterator);
        if (!result.isSuccessful()) {
            throw new JavaParserException(result.getProblems());
        }
        return adapt(source, result);
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
            final SourceIterator iterator = source.getIterator();
            result = parse(start, iterator);
            if (result.isSuccessful()) {
                break;
            }
            problems.addAll(result.getProblems());
        }
        Objects.requireNonNull(result);
        if (!result.isSuccessful()) {
            throw new JavaParserException("JavaParser failed to parse the snippet.", problems);
        }
        return adapt(source, result);
    }

    /**
     * Parses the specified source with JavaParser from the specified start.
     *
     * @param start specifies what piece of code is to be parsed.
     * @param sourceIterator iterator for the source to be parsed.
     * @param <N> the root node type for the constructed AST (depends on start).
     * @return a parsing result.
     */
    public <N extends Node> ParseResult<N> parse(
            final ParseStart<N> start,
            final SourceIterator sourceIterator) {
        try (SourceProvider provider = new SourceProvider(sourceIterator)) {
            return parser.parse(start, provider);
        }
    }

    /**
     * Converts the JavaParser result to the Patternika AST.
     *
     * @param source the source.
     * @param result the result of JavaParser.
     * @return the AST.
     */
    protected JavaNode adapt(final Source source, final ParseResult<? extends Node> result) {
        final Optional<? extends Node> root = result.getResult();
        if (!root.isPresent()) {
            throw new IllegalArgumentException("No AST in the parser result!");
        }
        return adapter.adapt(source, root.get());
    }

}
