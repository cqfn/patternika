package org.cqfn.patternika.lang.java.parser.javaparser;

import org.cqfn.patternika.parser.ParserException;
import org.cqfn.patternika.source.Source;
import org.cqfn.patternika.source.SourceFile;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link JavaParserException} class.
 */
public class JavaParserExceptionTest {
    /**
     * Tests that JavaParser generates an exception with a valid message
     * when provided with an incomplete code of a compilation unit.
     */
    @Test
    public void testParseCompilationUnitExceptionMessage() {
        final String expectedMessage = "JavaParser failed to parse the document.";
        final String text =
                "int x = 1;\n"
              + "int y = 2;\n"
              + "int z = x + y;";
        final Source source = new SourceFile(text);
        final ParserJava parserJava = new ParserJava((a, b) -> null);
        try {
            parserJava.parse(source);
        } catch (final ParserException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
            Assert.assertTrue(e.toString().startsWith(expectedMessage));
        }
    }

    /**
     * Tests that JavaParser generates an exception with a valid message
     * when it fails to parse the provided snippet.
     */
    @Test
    public void testParseSnippetExceptionMessage()  {
        final String expectedMessage = "JavaParser failed to parse the snippet.";
        final String text =
                "int a = 10;\n"
              + "int b = 5;\n"
              + "int c = a + b;";
        final Source source = new SourceFile(text);
        final ParserJava parserJava = new ParserJava((a, b) -> null);
        try {
            parserJava.parseSnippet(source);
        } catch (final ParserException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
            Assert.assertTrue(e.toString().startsWith(expectedMessage));
        }
    }

}
