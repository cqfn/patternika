package org.cqfn.patternika;

import org.cqfn.patternika.lang.java.parser.javaparser.JavaParserAdapter;
import org.cqfn.patternika.lang.java.parser.javaparser.ParserJava;
import org.cqfn.patternika.parser.Parsers;

/**
 * Parsers support in Patternika.
 *
 * @since 2021/03/09
 */
public class PatternikaParsers {
    /** Parsers for Patternika. */
    private final Parsers parsers = new Parsers();

    /**
     * Constructor.
     * <p>
     * Configures supported parsers.
     */
    public PatternikaParsers() {
        parsers.registerParser("java", new ParserJava(new JavaParserAdapter()));
    }

    /**
     * Returns parsers configured for Patternika.
     *
     * @return Patternika parsers.
     */
    public Parsers getParsers() {
        return parsers;
    }

}
