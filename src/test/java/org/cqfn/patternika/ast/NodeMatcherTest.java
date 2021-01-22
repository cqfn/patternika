package org.cqfn.patternika.ast;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        final Map<Node, List<Node>> matches = new NodeMatcher(firstNode, secondNode).findAll();
        final int numberOfMatches = 4;
        assertEquals(numberOfMatches, matches.size());

        final Node key1 = new TestNode("red", 0);
        final Node key2 = new TestNode("purple", 2);
        final Node key3 = new TestNode(
            "green", 1,
            new TestNode("purple", 2)
        );
        final Node key4 = new TestNode(
            "green", 1,
            new TestNode("red", 0),
            new TestNode("red", 0)
        );
        assertTrue(matches.containsKey(key1));
        assertTrue(matches.containsKey(key2));
        assertTrue(matches.containsKey(key3));
        assertTrue(matches.containsKey(key4));

        final int sizeOfMatch1 = 6;
        final int sizeOfMatch2 = 3;
        final int sizeOfMatch3 = 1;
        final int sizeOfMatch4 = 1;
        assertEquals(sizeOfMatch1, matches.get(key1).size());
        assertEquals(sizeOfMatch2, matches.get(key2).size());
        assertEquals(sizeOfMatch3, matches.get(key3).size());
        assertEquals(sizeOfMatch4, matches.get(key4).size());
    }
}
