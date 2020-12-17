package org.cqfn.patternika.ast.mapper;

import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.ast.NodeExt;
import org.cqfn.patternika.ast.NodeMatcher;
import org.cqfn.patternika.ast.TestNode;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Tests for the {@link AbstractMapper} class.
 *
 * @since 2020/12/17
 */
public class AbstractMapperTest {

    /**
     * Test for basic functionality of {@link GreedMapper}.
     * Checks that recursively matching subtrees of the two trees are connected.
     */
    @Test
    public void testMatchingGreedMapper() {
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
            Assert.assertTrue("No connection for " + source, isConnected);
        }
    }

}
