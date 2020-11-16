package org.cqfn.patternika.ast;

import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests NodeMatcher class.
 *
 * @since 2020/11/11
 */
public class NodeMatcherTest {

    /**
     * Tests {@link NodeMatcher#findAll()}.
     */
    @Test
    public void findAllTest() {
        final Node firstNode = new TestNode(
            "red", 0,
            new TestNode(
                "red", 0,
                new TestNode(
                    "green", 1,
                    new TestNode("purple", 2),
                    new TestNode("purple", 2)
                )
            ),
            new TestNode(
                "green", 1,
                new TestNode(
                    "green", 1,
                    new TestNode("red", 0),
                    new TestNode("red", 0)
                )
            ),
            new TestNode(
                "green", 1,
                new TestNode(
                    "purple", 2,
                    new TestNode("red", 0)
                )
            ),
            new TestNode(
                "green", 1,
                new TestNode("purple", 2)
            )
        );
        final Node secondNode = new TestNode(
            "red", 0,
            new TestNode(
                "red", 0,
                new TestNode(
                    "green", 1,
                    new TestNode("purple", 2)
                ),
                new TestNode("green", 1)
            ),
            new TestNode("green", 1,
                new TestNode("red", 0),
                new TestNode("red", 0)
            ),
            new TestNode("green", 1)
        );
        Map<Node, List<Node>> matches = new NodeMatcher(firstNode, secondNode).findAll();
        final int numberOfMatches = 4;
        assertEquals(matches.size(), numberOfMatches);

        Node key1 = new TestNode("red", 0);
        Node key2 = new TestNode("purple", 2);
        Node key3 = new TestNode(
            "green", 1,
            new TestNode("purple", 2)
        );
        Node key4 = new TestNode(
            "green", 1,
            new TestNode("red", 0),
            new TestNode("red", 0)
        );
        assertTrue(matches.containsKey(key1));
        assertTrue(matches.containsKey(key2));
        assertTrue(matches.containsKey(key3));
        assertTrue(matches.containsKey(key4));

        final int numberOfNodesInMatch1 = 6;
        final int numberOfNodesInMatch2 = 3;
        final int numberOfNodesInMatch3 = 1;
        final int numberOfNodesInMatch4 = 1;
        assertEquals(numberOfNodesInMatch1, matches.get(key1).size());
        assertEquals(numberOfNodesInMatch2, matches.get(key2).size());
        assertEquals(numberOfNodesInMatch3, matches.get(key3).size());
        assertEquals(numberOfNodesInMatch4, matches.get(key4).size());
    }
}
