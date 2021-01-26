package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.CommentsCollection;
import org.cqfn.patternika.parser.ParserException;
import org.cqfn.patternika.source.Source;
import org.cqfn.patternika.source.SourceFile;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * Tests for the {@link ParserJava} class.
 * <p>
 * It tests the parser logic without the logic of AST adaptor.
 */
public class ParserJavaTest {
    /**
     * Text of a full compilation unit.
     */
    private static final String COMPILATION_UNIT =
          "package org.cqfn.patternika.util;\n"
        + "\n"
        + "/**\n"
        + " * Simple pair in case someone needs to use (key, val) objects without creating a map.\n"
        + " *\n"
        + " * @param <K> key type.\n"
        + " * @param <V> value type.\n"
        + " *\n"
        + " * @since 2019/11/21\n"
        + " */\n"
        + "public final class Pair<K, V> {\n"
        + "    /** The key. */\n"
        + "    private final K key;\n"
        + "    /** The value. */\n"
        + "    private final V val;\n"
        + "\n"
        + "    /**\n"
        + "     * Constructor.\n"
        + "     *\n"
        + "     * @param key the key.\n"
        + "     * @param val the value.\n"
        + "     */\n"
        + "    public Pair(final K key, final V val) {\n"
        + "        this.key = key;\n"
        + "        this.val = val;\n"
        + "    }\n"
        + "\n"
        + "    public K getKey() {\n"
        + "        return key;\n"
        + "    }\n"
        + "\n"
        + "    public V getVal() {\n"
        + "        return val;\n"
        + "    }\n"
        + "\n"
        + "}\n";

    /**
     * Text of a full class.
     */
    private static final String CLASS =
          "public final class Pair<K, V> {\n"
        + "    private final K key;\n"
        + "    private final V val;\n"
        + "\n"
        + "    public Pair(final K key, final V val) {\n"
        + "        this.key = key;\n"
        + "        this.val = val;\n"
        + "    }\n"
        + "\n"
        + "    public K getKey() {\n"
        + "        return key;\n"
        + "    }\n"
        + "\n"
        + "    public V getVal() {\n"
        + "        return val;\n"
        + "    }\n"
        + "\n"
        + "}\n";

    /**
     * Text of a method.
     */
    private static final String METHOD =
          "public int add(int a, int b) {\n"
        + "    this.x = a;\n"
        + "    return a + b;\n"
        + "}";

    /**
     * Text of a code block.
     */
    private static final String BLOCK =
          "{\n"
        + "    int x = 0;\n"
        + "    int y = a;\n"
        + "    while (x < y) {\n"
        + "        this.m = x;\n"
        + "        this.z = process(x, y);\n"
        + "        x++;\n"
        + "    }\n"
        + "}";

    /**
     * Mock object for an AST adapter. Checks that JavaParser has produced an AST.
     */
    private static final Adapter ADAPTER_MOCK = (source, root) -> {
        Assert.assertEquals(Node.Parsedness.PARSED, root.getParsed());
        return null;
    };

    /**
     * Tests that JavaParser successfully parses a full compilation unit.
     *
     * @throws ParserException if the parser fails.
     */
    @Test
    public void testParseCompilationUnit() throws ParserException {
        final Source source = new SourceFile(COMPILATION_UNIT);
        final ParserJava parserJava = new ParserJava(ADAPTER_MOCK);
        Assert.assertNull(parserJava.parse(source));
    }

    /**
     * Tests that JavaParser generates the {@link JavaParserException}
     * exception when provided with an incomplete code of a compilation unit.
     *
     * @throws ParserException if the parser fails.
     */
    @Test(expected = JavaParserException.class)
    public void testParseCompilationUnitException() throws ParserException {
        final String text =
             "int x = 1;\n"
            + "int y = 2;\n"
            + "int z = x + y;";
        final Source source = new SourceFile(text);
        final ParserJava parserJava = new ParserJava(ADAPTER_MOCK);
        Assert.assertNull(parserJava.parse(source));
    }

    /**
     * Tests that JavaParser successfully parses a class.
     *
     * @throws ParserException if the parser fails.
     */
    @Test
    public void testParseClass() throws ParserException {
        final Source source = new SourceFile(CLASS);
        final ParserJava parserJava = new ParserJava(ADAPTER_MOCK);
        Assert.assertNull(parserJava.parse(source));
    }

    /**
     * Tests that JavaParser successfully parses a method.
     *
     * @throws ParserException if the parser fails.
     */
    @Test
    public void testParseMethod() throws ParserException {
        final Source source = new SourceFile(METHOD);
        final ParserJava parserJava = new ParserJava(ADAPTER_MOCK);
        Assert.assertNull(parserJava.parseSnippet(source));
    }

    /**
     * Tests that JavaParser successfully parses a code block.
     *
     * @throws ParserException if the parser fails.
     */
    @Test
    public void testParseBlock() throws ParserException {
        final Source source = new SourceFile(BLOCK);
        final ParserJava parserJava = new ParserJava(ADAPTER_MOCK);
        Assert.assertNull(parserJava.parseSnippet(source));
    }

    /**
     * Tests that JavaParser successfully parses a single statement.
     *
     * @throws ParserException if the parser fails.
     */
    @Test
    public void testParseStatement() throws ParserException {
        final String text = "int a = x + test(y);";
        final Source source = new SourceFile(text);
        final ParserJava parserJava = new ParserJava(ADAPTER_MOCK);
        Assert.assertNull(parserJava.parseSnippet(source));
    }

    /**
     * Tests that JavaParser successfully parses a expression statement.
     *
     * @throws ParserException if the parser fails.
     */
    @Test
    public void testParseExpression() throws ParserException {
        final String text = "a / x + b * 2";
        final Source source = new SourceFile(text);
        final ParserJava parserJava = new ParserJava(ADAPTER_MOCK);
        Assert.assertNull(parserJava.parseSnippet(source));
    }

    /**
     * Tests that JavaParser generates the {@link JavaParserException}
     * exception when it fails to parse the provided snippet.
     *
     * @throws ParserException if the parser fails.
     */
    @Test(expected = JavaParserException.class)
    public void testParseSnippetException() throws ParserException {
        final String text =
              "int a = 10;\n"
            + "int b = 5;\n"
            + "int c = a + b;";
        final Source source = new SourceFile(text);
        final ParserJava parserJava = new ParserJava(ADAPTER_MOCK);
        Assert.assertNull(parserJava.parseSnippet(source));
    }

    /**
     * Test that the adapt methods throws an exception if provided a parser result with AST .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdaptException() {
        final Source source = new SourceFile("int x = 10;");
        final ParserJava parserJava = new ParserJava(ADAPTER_MOCK);
        parserJava.adapt(
                source,
                new ParseResult<>(null, Collections.emptyList(), new CommentsCollection())
            );
    }

}
