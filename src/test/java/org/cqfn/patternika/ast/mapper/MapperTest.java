package org.cqfn.patternika.ast.mapper;

import org.cqfn.patternika.ast.NodeExt;
import org.cqfn.patternika.ast.NodeMatcher;
import org.cqfn.patternika.ast.TestNode;
import org.cqfn.patternika.ast.iterator.Bfs;

import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link AbstractMapper} class.
 *
 * @since 2020/12/17
 */
public class MapperTest {

    /**
     * Test for functionality of {@link GreedMapper}.
     * <p>
     * Tests that two node trees are correctly mapped.
     * Currently, fails.
     */
    @Test
    public void testMapper() {
        final NodeExt root1 = new NodeExt(
            new TestNode("a", 0,
                new TestNode("g", 2),
                new TestNode("b", 1,
                    new TestNode("c", 2,
                        new TestNode("d", 3,
                            new TestNode("e", 4),
                            new TestNode("f", 5)
                        )
                    )
                )
            )
        );
        final NodeExt root2 = new NodeExt(
            new TestNode("a", 0,
                new TestNode("b", 1,
                    new TestNode("g", 2,
                        new TestNode("d", 3,
                            new TestNode("f", 5),
                            new TestNode("e", 4)
                        )
                    )
                )
            )
        );
        final Mapping<NodeExt> mapping = new GreedMapper(root1, root2).buildMapping();
        // Recursively matching subtrees of the two trees must be connected.
        // assertMatchesConnected(mapping, root1, root2);
        // a(1) <-> a(2)
        assertConnected(mapping, root1, root2);
        // g(1) not mapped
        assertNull(mapping.get(root1.getChild(0)));
        // b(1) <-> b(2)
        assertConnected(mapping, root1.getChild(1), root2.getChild(0));
        // c(1) not mapped
        assertNull(mapping.get(root1.getChild(1).getChild(0)));
        // g(2) not mapped
        assertNull(mapping.get(root2.getChild(0).getChild(0)));
    }

    /**
     * BFS test 1 for functionality of {@link GreedMapper}.
     * <p>
     * Checks that two trees, nodes of which match in the BFS order,
     * are correctly connected in the mapping.
     */
    @Test
    public void testBfs1() {
        final NodeExt root1 = new NodeExt(
            new TestNode("a", 0,
                new TestNode("a", 1,
                    new TestNode("b", 3)
                ),
                new TestNode("b", 2,
                    new TestNode("c", 4)
                )
            )
        );
        final NodeExt root2 = new NodeExt(
            new TestNode("a", 0,
                new TestNode("a", 1,
                    new TestNode("b", 3)
                ),
                new TestNode("b", 2)
            )
        );
        final Mapping<NodeExt> mapping = new GreedMapper(root1, root2).buildMapping();
        // Recursively matching subtrees of the two trees must be connected.
        assertMatchesConnected(mapping, root1, root2);
        // Nodes of the two trees traversed in the BFS order must be connected.
        assertBfsConnected(mapping, root1, root2);
    }

    /**
     * BFS test 2 for functionality of {@link GreedMapper}.
     * <p>
     * Checks that two trees, nodes of which match in the BFS order,
     * are correctly connected in the mapping.
     */
    @Test
    public void testBfs2() {
        final NodeExt root1 = new NodeExt(
            new TestNode("a", 0,
                new TestNode("a", 1,
                    new TestNode("b", 3,
                        new TestNode("c", 4)
                    )
                ),
                new TestNode("b", 2)
            )
        );
        final NodeExt root2 = new NodeExt(
            new TestNode("a", 0,
                new TestNode("a", 1,
                    new TestNode("b", 3)
                ),
                new TestNode("b", 2)
            )
        );
        final Mapping<NodeExt> mapping = new GreedMapper(root1, root2).buildMapping();
        // Recursively matching subtrees of the two trees must be connected.
        assertMatchesConnected(mapping, root1, root2);
        // Nodes of the two trees traversed in the BFS order must be connected.
        assertBfsConnected(mapping, root1, root2);
    }

    /**
     * Checks that recursively matching subtrees of the two trees
     * are connected in the mapping.
     *
     * @param mapping mapping to be checked.
     * @param root1 first tree root.
     * @param root2 second tree root.
     * @throws AssertionError if at least one pair of matching subtrees is not connected.
     */
    private static void assertMatchesConnected(
            final Mapping<NodeExt> mapping,
            final NodeExt root1,
            final NodeExt root2) {
        final Map<NodeExt, List<NodeExt>> matches = new NodeMatcher<>(root1, root2).findAll();
        for (final Map.Entry<NodeExt, List<NodeExt>> entry : matches.entrySet()) {
            final NodeExt source = entry.getKey();
            final List<NodeExt> targets = entry.getValue();
            // The source must be connected to at least one of the targets.
            boolean isConnected = false;
            for (final NodeExt target : targets) {
                if (mapping.connected(source, target)) {
                    isConnected = true;
                    break;
                }
            }
            assertTrue("No connection for " + source, isConnected);
        }
    }

    /**
     * Checks that two trees, nodes of which match in the BFS order,
     * are correctly connected in the mapping.
     *
     * @param mapping mapping to be checked.
     * @param root1 first tree root.
     * @param root2 second tree root.
     * @throws AssertionError if at least one pair of nodes traversed in BFS order is not connected.
     */
    private static void assertBfsConnected(
            final Mapping<NodeExt> mapping,
            final NodeExt root1,
            final NodeExt root2) {
        final Iterator<NodeExt> it1 = new Bfs<>(root1).iterator();
        final Iterator<NodeExt> it2 = new Bfs<>(root2).iterator();
        while (it1.hasNext() && it2.hasNext()) {
            assertConnected(mapping, it1.next(), it2.next());
        }
    }

    /**
     * Checks that two nodes are connected to each other.
     *
     * @param mapping mapping to be checked.
     * @param node1 first node.
     * @param node2 second node.
     * @throws AssertionError if at least one pair of nodes traversed in BFS order is not connected.
     */
    private static void assertConnected(
            final Mapping<NodeExt> mapping,
            final NodeExt node1,
            final NodeExt node2) {
        assertSame(mapping.get(node1), node2);
        assertSame(mapping.get(node2), node1);
    }

}
