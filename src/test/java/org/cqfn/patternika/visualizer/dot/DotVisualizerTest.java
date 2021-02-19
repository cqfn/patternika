package org.cqfn.patternika.visualizer.dot;

import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.ast.TestNode;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * Tests for the {@link DotVisualizer} class.
 *
 * @since 2021/02/19
 */
public class DotVisualizerTest {
    /**
     * Basic test.
     */
    @Test
    public void test() {
        final Node root = new TestNode(0);
        final StringBuilder builder = new StringBuilder();
        final DotVisualizer visualizer = new DotVisualizer(builder, root, Collections.emptyMap());
        visualizer.visualize();
        final String expected =
                 "digraph AST {\n"
               + "  node [shape=box style=rounded];\n"
               + "  node_0 [label=<TestNode<br/><font color=\"blue\">0</font>>]; // NODE\n"
               + "}\n";
        Assert.assertEquals(expected, builder.toString());
    }
}
