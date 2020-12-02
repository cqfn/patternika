package org.cqfn.patternika.ast;

import org.cqfn.patternika.source.Fragment;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tests for the {@link NodeFactory} class.
 * Also, this test demonstrates how to use the factory.
 *
 * @since 2020/11/30
 */
public class NodeFactoryTest {

    /**
     * Creates a new test factory and register creator for specific node types.
     * <p>
     * Demonstrates ways to register creators for specific node types.
     *
     * @return new test factory.
     */
    private NodeFactory createFactory() {
        return new NodeFactory()
                // Use case 0: the object is constructed using the constructor of its class.
                .register("TestNode", (f, d, c) -> new TestNode(f, "TestNode", d, c))
                // Use case 1: the object is created using a factory method provided by its class.
                .register("TestNode1", TestNode1::create)
                // Use case 2: the object is create using an external factory method.
                .register("TestNode2", this::createTestNode2);
    }

    /**
     * A test node with a factory method.
     * The factory method can do a complex processing of its parameters.
     */
    private static class TestNode1 extends TestNode {
        TestNode1(final Fragment fragment, final String data, final List<Node> children) {
            super(fragment, "TestNode1", data, children);
        }

        TestNode1(final String data, final List<Node> children) {
            this(null, data, children);
        }

        private static Node create(
                final Fragment fragment,
                final String data,
                final List<Node> children) {
            // This implementation is trivial, but it can be complex.
            // E.g. when it is needed to check the order of children
            // and put them into a specific fields of the node class.
            return new TestNode1(fragment, data, children);
        }
    }

    /**
     * A factory method that creates a test node.
     *
     * @param fragment fragment associated with the node.
     * @param data node data.
     * @param children list of node children.
     * @return new node.
     */
    private Node createTestNode2(
            final Fragment fragment,
            final String data,
            final List<Node> children) {
        // This implementation is trivial, but it can be complex.
        // E.g. when it is needed to check the order of children
        // and put them into a specific fields of the node class.
        return new TestNode(fragment, "TestNode2", data, children);
    }

    /**
     * Test for the {@link NodeFactory#createNode(String, Fragment, String, List)} method.
     * <p>
     * Creates single nodes and checks that they are created correctly.
     */
    @Test
    public void createNodeSingleTest() {
        final NodeFactory factory = createFactory();
        final Node node0 = factory.createNode("TestNode", null, "0");
        Assert.assertEquals("TestNode", node0.getType());
        Assert.assertEquals("0", node0.getData());
        Assert.assertEquals(0, node0.getChildCount());
        final Node node1 = factory.createNode("TestNode1", null, "1");
        Assert.assertEquals("TestNode1", node1.getType());
        Assert.assertEquals("1", node1.getData());
        Assert.assertEquals(0, node1.getChildCount());
        final Node node2 = factory.createNode("TestNode2", null, "2");
        Assert.assertEquals("TestNode2", node2.getType());
        Assert.assertEquals("2", node2.getData());
        Assert.assertEquals(0, node2.getChildCount());
    }

    /**
     * Test for the {@link NodeFactory#createNode(String, Fragment, String, List)} method.
     * <p>
     * Creates node trees and checks that they are created correctly.
     */
    @Test
    public void createNodeTreeTest() {
        final NodeFactory factory = createFactory();
        final Node root =
                factory.createNode(
                        "TestNode",
                        null,
                        "0",
                        factory.createNode(
                                "TestNode1",
                                null,
                                "1",
                                factory.createNode("TestNode", null, "11"),
                                factory.createNode("TestNode", null, "12")
                            ),
                        factory.createNode(
                                "TestNode2",
                                null,
                                "2",
                                factory.createNode("TestNode", null, "20"),
                                factory.createNode("TestNode1", null, "21"),
                                factory.createNode("TestNode2", null, "22")
                            )
                );
        final Node expectedRoot =
                new TestNode(
                        0,
                        new TestNode1(
                                "1",
                                Arrays.asList(
                                    new TestNode(11),
                                    new TestNode(12)
                                )
                            ),
                        new TestNode(
                                "TestNode2",
                                2,
                                new TestNode(20),
                                new TestNode1("21", Collections.emptyList()),
                                new TestNode("TestNode2", 22)
                            )
                );
        Assert.assertTrue(new DeepMatches().test(expectedRoot, root));
    }

    /**
     * Test for the {@link NodeFactory#createNode(String, Fragment, String, List)} method.
     * <p>
     * Checks that an exception is generated on an attempt to create a node of an unknown type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void createNodeWithExceptionTest() {
        final NodeFactory factory = createFactory();
        factory.createNode("UnknownType", null, "data");
    }

    /**
     * Test for the {@link NodeFactory#register(String, NodeCreator)} method.
     * <p>
     * Checks that an exception is generated on an attempt to register a creator for node,
     * which has already been registered.
     */
    @Test(expected = IllegalArgumentException.class)
    public void registerWithExceptionTest() {
        new NodeFactory()
                .register("TestNode1", TestNode1::create)
                .register("TestNode1", this::createTestNode2);
    }

}
