package org.cqfn.patternika.lang.java.parser.javaparser;

import org.cqfn.patternika.parser.ParserException;
import org.cqfn.patternika.source.Source;
import org.cqfn.patternika.source.SourceFile;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link JavaParserAdapter} class.
 */
public class JavaParserAdapterTest {
    /**
     * Test code to be parsed.
     */
    private static final String CODE =
          "class X {\n"
        + "    public enum E {\n"
        + "        ONE,\n"
        + "        TWO;\n"
        + "    }\n"
        + "\n"
        + "    private final int x;\n"
        + "\n"
        + "    public X(int x) {\n"
        + "        this.x = x;\n"
        + "    }\n"
        + "\n"
        + "    public int getX() { return x; }\n"
        + "    public int add(int y) { return this.x + y; }\n"
        + "\n"
        + "    @Override\n"
        + "    public String toString() { return \"X{x=\" + x + '}'; }\n"
        + "}";

    /**
     * Basic test to start.
     *
     * @throws ParserException if the parser fails.
     */
    @Test
    public void test() throws ParserException {
        final Source source = new SourceFile(CODE);
        final ParserJava parserJava = new ParserJava(new JavaParserAdapter());
        Assert.assertNull(parserJava.parse(source));
    }

}
