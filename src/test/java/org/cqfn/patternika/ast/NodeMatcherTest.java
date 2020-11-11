package org.cqfn.patternika.ast;

import java.util.List;
import static org.junit.Assert.assertEquals;
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
        List<NodePair> matches = new NodeMatcher(firstNode, secondNode).findAll();
        final int numberOfMatches = 3;
        assertEquals(matches.size(), numberOfMatches);

        assertEquals(matches.get(0).left().getChildCount(), 0);
        assertEquals(matches.get(0).left().getType(), "purple");
        assertEquals(matches.get(0).right().getChildCount(), 0);
        assertEquals(matches.get(0).right().getType(), "purple");

        assertEquals(matches.get(1).left().getChildCount(), 2);
        assertEquals(matches.get(1).left().getType(), "green");
        assertEquals(matches.get(1).left().getChild(0).getType(), "red");
        assertEquals(matches.get(1).left().getChild(1).getType(), "red");
        assertEquals(matches.get(1).right().getChildCount(), 2);
        assertEquals(matches.get(1).right().getType(), "green");
        assertEquals(matches.get(1).right().getChild(0).getType(), "red");
        assertEquals(matches.get(1).right().getChild(1).getType(), "red");

        assertEquals(matches.get(2).left().getChildCount(), 0);
        assertEquals(matches.get(2).left().getType(), "red");
        assertEquals(matches.get(2).right().getChildCount(), 0);
        assertEquals(matches.get(2).right().getType(), "red");
    }
}
