package org.cqfn.patternika.parser;

import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.source.Source;
import org.cqfn.patternika.source.SourceFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Parsers for all supported languages. A proper parser is selected for the specified language.
 * <p>
 * Configurable. To support more languages, corresponding parsers need to be registered.
 *
 * @since 2021/03/09
 */
public class Parsers {
    /** Parsers for specific languages. */
    private final Map<String, Parser> parsersByLang = new HashMap<>();

    /**
     * Parses the specified source code file and returns an AST for it.
     *
     * @param lang the language of the file.
     * @param path the path to the source code file.
     * @return the AST.
     * @throws ParserException if the parser failed to parse the specified file.
     */
    public Node parseFile(final String lang, final String path) throws ParserException {
        final Parser parser = getParser(lang);
        try {
            final String text = readAllFromFile(path);
            final Source source = new SourceFile(text);
            return parser.parse(source);
        } catch (IOException ex) {
            throw new ParserException("Failed to read file " + path, ex);
        }
    }

    /**
     * Parses the specified source code file and returns an AST for it.
     * The language determined by the file extension.
     *
     * @param path the path to the source code file.
     * @return the AST.
     * @throws ParserException if the parser failed to parse the specified file.
     */
    public Node parseFile(final String path) throws ParserException {
        final String lang = path.substring(path.lastIndexOf('.') + 1);
        return parseFile(lang, path);
    }

    /**
     * Parses the specified code snippet and returns an AST for it.
     *
     * @param lang the language of the snippet.
     * @param text the text of the snippet.
     * @return the AST.
     * @throws ParserException if the parser failed to parse the specified snippet.
     */
    public Node parseSnippet(final String lang, final String text) throws ParserException {
        final Parser parser = getParser(lang);
        final Source source = new SourceFile(text);
        return parser.parseSnippet(source);
    }

    /**
     * Registers a parser for the specified language.
     *
     * @param lang the language.
     * @param parser the parser.
     * @throws IllegalStateException if the parser is already registered.
     */
    public void registerParser(final String lang, final Parser parser) {
        final Parser prev =
                parsersByLang.put(lang.toLowerCase(Locale.ROOT), Objects.requireNonNull(parser));
        if (prev != null) {
            throw new IllegalStateException(
                    "A parser for language '" + lang + "' is already registered!"
                );
        }
    }

    /**
     * Checks whether the language is supported.
     *
     * @param lang the language.
     * @return {@code true} or {@code false}.
     */
    public boolean isLanguageSupported(final String lang) {
        return parsersByLang.containsKey(lang);
    }

    /**
     * Gets a parser for the specified language.
     *
     * @param lang the language identifier.
     * @return the parser for the specified language.
     * @throws ParserException if the parser is not found.
     */
    private Parser getParser(final String lang) throws ParserException {
        final Parser parser = parsersByLang.get(lang.toLowerCase(Locale.ROOT));
        if (parser == null) {
            throw new ParserException("Unsupported language: " + lang);
        }
        return parser;
    }

    /**
     * Reads all text from the specified file.
     *
     * @param path the file path.
     * @return the text read from the file.
     * @throws IOException if failed to read text from the file.
     */
    private static String readAllFromFile(final String path) throws IOException {
        final File file = new File(path);
        final byte[] data = Files.readAllBytes(file.toPath());
        return new String(data);
    }

}
