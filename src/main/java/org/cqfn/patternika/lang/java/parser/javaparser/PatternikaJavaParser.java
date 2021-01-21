package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParseStart;

import com.github.javaparser.Provider;
import com.github.javaparser.ast.Node;
import org.cqfn.patternika.parser.Parser;
import org.cqfn.patternika.source.Source;

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

    /**
     * Parses the specified source and returns an AST for it.
     *
     * @param source the source to be parsed.
     * @return an AST.
     */
    @Override
    public org.cqfn.patternika.ast.Node parse(final Source source) {
        final Provider provider = new SourceProvider(source.getIterator());
        final ParseResult<? extends Node> result =
                parser.parse(ParseStart.COMPILATION_UNIT, provider);
        final Optional<? extends Node> root = result.getResult();
        return convertAst(root.get());
    }

    private org.cqfn.patternika.ast.Node convertAst(final Node root) {
        return null;
    }

}
