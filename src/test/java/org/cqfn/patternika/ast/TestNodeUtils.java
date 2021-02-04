package org.cqfn.patternika.ast;

import org.cqfn.patternika.ast.iterator.Children;

/**
 * Utility methods for AST nodes to be used in tests.
 *
 * @since 2021/02/04
 */
public final class TestNodeUtils {
    private TestNodeUtils() { }

    /**
     * Prints an AST to the console.
     *
     * @param root the root of the tree.
     * @param level current level of the tree (offset from left).
     */
    @SuppressWarnings("PMD")
    public static void dumpTree(final Node root, final int level) {
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
