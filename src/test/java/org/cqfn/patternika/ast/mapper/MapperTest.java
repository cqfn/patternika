package org.cqfn.patternika.ast.mapper;

import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.ast.NodeExt;
import org.cqfn.patternika.ast.NodeMatcher;
import org.cqfn.patternika.ast.TestNode;
import org.cqfn.patternika.ast.iterator.Bfs;

import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link AbstractMapper} class.
 *
 * @since 2020/12/17
 */
public class MapperTest {

    /**
     * Tests functionality of {@link GreedMapper}.
     * <p>
     * Checks that two trees, nodes of which match in the BFS order,
     * are correctly connected in the mapping.
     */
    @Test
    public void testBfs() {
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
        final Iterator<NodeExt> it1 = new Bfs<>(root1).iterator();
        final Iterator<NodeExt> it2 = new Bfs<>(root2).iterator();
        while (it1.hasNext() && it2.hasNext()) {
            final NodeExt node1 = it1.next();
            final NodeExt node2 = it2.next();
            assertSame(mapping.get(node1), node2);
            assertSame(mapping.get(node2), node1);
        }
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
    private void assertMatchesConnected(
            final Mapping<NodeExt> mapping,
            final NodeExt root1,
            final NodeExt root2) {
        final Map<Node, List<Node>> matches = new NodeMatcher(root1, root2).findAll();
        for (final Map.Entry<Node, List<Node>> entry : matches.entrySet()) {
            final Node source = entry.getKey();
            final List<Node> targets = entry.getValue();
            // The source must be connected to at least one of the targets.
            boolean isConnected = false;
            for (final Node target : targets) {
                if (mapping.connected((NodeExt) source, (NodeExt) target)) {
                    isConnected = true;
                    break;
                }
            }
            assertTrue("No connection for " + source, isConnected);
        }
    }

}
