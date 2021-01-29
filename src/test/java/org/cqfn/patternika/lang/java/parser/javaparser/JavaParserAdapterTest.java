package org.cqfn.patternika.lang.java.parser.javaparser;

import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.ast.iterator.Children;
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
          "@SuppressWarnings(\"PMD\")\n"
        + "public class X<T extends Object> extends MyClass<T, String> {\n"
        + "    /** JavaDoc comment. */\n"
        + "    static { int z[] = new int[] {1, 2, 3}; T t = null; }\n"
        + "    public enum E implements Iterator<E> {\n"
        + "        ONE,\n"
        + "        TWO;\n"
        + "    }\n"
        + "\n"
        + "    private final int x;// This is comment.\n"
        + "    private final Map<String, List<Integer>> y = new ArrayList<>();\n"
        + "\n"
        + "    public X(int x) {\n"
        + "        this.x = x;\n"
        + "    }\n"
        + "\n"
        + "    public int getX() { return x; }\n"
        + "    public <U extends Integer> int add(@NotNull U y) { return this.x + y + 10; }\n"
        + "\n"
        + "    @Override\n"
        + "    public String toString() { return \"X{x=\" + x + '}'; }\n"
        + "}";

    /**
     * Basic test to start.
     *
     * @throws ParserException if the parser fails.
     */
    @SuppressWarnings("PMD")
    @Test
    public void test() throws ParserException {
        final Source source = new SourceFile(CODE);
        final ParserJava parser = new ParserJava(new JavaParserAdapter());
        final Node root = parser.parse(source);
        Assert.assertNotNull(root);
        dumpTree(root, 0);
        System.out.println(root.getFragment());
    }

    @SuppressWarnings("PMD")
    private void dumpTree(final Node root, final int level) {
        for (int i = 0; i < level; ++i) {
            System.out.print("    ");
        }
        System.out.print(root.getType());
        final String data = root.getData();
        if (data != null) {
            System.out.print(" : " + root.getData());
        }
        System.out.println();
        for (final Node child : new Children<>(root)) {
            dumpTree(child, level + 1);
        }
    }

}
