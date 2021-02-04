package org.cqfn.patternika.lang.java.parser.javaparser;

import org.cqfn.patternika.ast.DeepMatches;
import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.ast.TestNode;
import org.cqfn.patternika.parser.ParserException;
import org.cqfn.patternika.source.Source;
import org.cqfn.patternika.source.SourceFile;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Tests for the {@link JavaNode} class.
 *
 * @since 2021/02/01
 */
public class JavaNodeTest {

    /**
     * Tests parsing a code snippet of an entire compilation unit.
     *
     * @throws ParserException if JavaParser fails.
     */
    @Test
    public void testCompilationUnit() throws ParserException {
        final String text =
              "package org.cqfn.patternika;\n"
            + "public class TestClass {\n"
            + "    private final int a;\n"
            + "    private final int b;\n"
            + "    public TestClass(int a, int b) {\n"
            + "        this.a = a;\n"
            + "        this.b = b;\n"
            + "    }\n"
            + "}\n";
        final Source source = new SourceFile(text);
        final ParserJava parser = new ParserJava(new JavaParserAdapter());
        final Node root = parser.parseSnippet(source);
        Assert.assertNotNull(root);
        Assert.assertTrue(root.isChildCountLimitless());
        Assert.assertFalse(root.isChildOrderStrict());
        Assert.assertTrue(root.matches(root));
        Assert.assertFalse(root.matches(null));
        Assert.assertTrue(root.matches(newTestNode("CompilationUnit", null)));
        Assert.assertFalse(root.matches(newTestNode("SimpleName", null)));
        Assert.assertFalse(root.matches(newTestNode("CompilationUnit", "invalid")));
        final Node expectedTree =
            newTestNode("CompilationUnit", null,
                newTestNode("PackageDeclaration", null,
                    newTestNode("Name", "patternika",
                        newTestNode("Name", "cqfn",
                            newTestNode("Name", "org")
                        )
                    )
                ),
                newTestNode("ClassOrInterfaceDeclaration", null,
                    newTestNode("Modifier", "public"),
                    newTestNode("SimpleName", "TestClass"),
                    newTestNode("FieldDeclaration", null,
                        newTestNode("Modifier", "private"),
                            newTestNode("Modifier", "final"),
                            newTestNode("VariableDeclarator", null,
                                newTestNode("PrimitiveType", "int"),
                                newTestNode("SimpleName", "a")
                            )
                    ),
                    newTestNode("FieldDeclaration", null,
                        newTestNode("Modifier", "private"),
                        newTestNode("Modifier", "final"),
                        newTestNode("VariableDeclarator", null,
                            newTestNode("PrimitiveType", "int"),
                            newTestNode("SimpleName", "b")
                        )
                    ),
                    newTestNode("ConstructorDeclaration", null,
                        newTestNode("Modifier", "public"),
                        newTestNode("SimpleName", "TestClass"),
                        newTestNode("Parameter", null,
                            newTestNode("PrimitiveType", "int"),
                            newTestNode("SimpleName", "a")
                        ),
                        newTestNode("Parameter", null,
                            newTestNode("PrimitiveType", "int"),
                            newTestNode("SimpleName", "b")
                        ),
                        newTestNode("BlockStmt", null,
                            newTestNode("ExpressionStmt", null,
                                newTestNode("AssignExpr", null,
                                    newTestNode("FieldAccessExpr", null,
                                        newTestNode("ThisExpr", null),
                                        newTestNode("SimpleName", "a")
                                    ),
                                    newTestNode("NameExpr", null,
                                        newTestNode("SimpleName", "a")
                                    )
                                )
                            ),
                            newTestNode("ExpressionStmt", null,
                                newTestNode("AssignExpr", null,
                                    newTestNode("FieldAccessExpr", null,
                                        newTestNode("ThisExpr", null),
                                        newTestNode("SimpleName", "b")
                                    ),
                                    newTestNode("NameExpr", null,
                                        newTestNode("SimpleName", "b")
                                    )
                                )
                            )
                        )
                    )
                )
            );
        Assert.assertTrue(new DeepMatches().test(expectedTree, root));
    }

    /**
     * Tests parsing a code snippet of a method.
     *
     * @throws ParserException if JavaParser fails.
     */
    @Test
    public void testMethod() throws ParserException {
        final String text =
                "int sub(int a, int b) {\n"
              +  "    if (a > b) {\n"
              +  "        return a - b;\n"
              +  "    } else {\n"
              +  "        return b - a;\n"
              +  "    }\n"
              +  "}";
        final Source source = new SourceFile(text);
        final ParserJava parser = new ParserJava(new JavaParserAdapter());
        final Node root = parser.parseSnippet(source);
        Assert.assertNotNull(root);
        Assert.assertFalse(root.isChildCountLimitless());
        Assert.assertTrue(root.isChildOrderStrict());
        Assert.assertTrue(root.matches(root));
        Assert.assertFalse(root.matches(null));
        Assert.assertTrue(root.matches(newTestNode("MethodDeclaration", null)));
        Assert.assertFalse(root.matches(newTestNode("SimpleName", null)));
        Assert.assertFalse(root.matches(newTestNode("MethodDeclaration", "invalid")));
        final Node expectedTree =
            newTestNode("MethodDeclaration", null,
                newTestNode("SimpleName", "sub"),
                newTestNode("Parameter", null,
                    newTestNode("PrimitiveType", "int"),
                    newTestNode("SimpleName", "a")
                ),
                newTestNode("Parameter", null,
                    newTestNode("PrimitiveType", "int"),
                    newTestNode("SimpleName", "b")
                ),
                newTestNode("PrimitiveType", "int"),
                newTestNode("BlockStmt", null,
                    newTestNode("IfStmt", null,
                        newTestNode("BinaryExpr", ">",
                            newTestNode("NameExpr", null,
                                newTestNode("SimpleName", "a")
                            ),
                            newTestNode("NameExpr", null,
                                newTestNode("SimpleName", "b")
                            )
                        ),
                        newTestNode("BlockStmt", null,
                            newTestNode("ReturnStmt", null,
                                newTestNode("BinaryExpr", "-",
                                    newTestNode("NameExpr", null,
                                        newTestNode("SimpleName", "a")
                                    ),
                                    newTestNode("NameExpr", null,
                                        newTestNode("SimpleName", "b")
                                    )
                                )
                            )
                        ),
                        newTestNode("BlockStmt", null,
                            newTestNode("ReturnStmt", null,
                                newTestNode("BinaryExpr", "-",
                                    newTestNode("NameExpr", null,
                                        newTestNode("SimpleName", "b")
                                    ),
                                    newTestNode("NameExpr", null,
                                        newTestNode("SimpleName", "a")
                                    )
                                )
                            )
                        )
                    )
                )
            );
        Assert.assertTrue(new DeepMatches().test(expectedTree, root));
    }

    /**
     * Creates a test node for testing the {@link Node#matches} method.
     *
     * @param type the node type.
     * @param data the node data.
     * @param children the children of the node.
     * @return a new test node.
     */
    private Node newTestNode(final String type, final String data, final Node... children) {
        return new TestNode(null, type, data, Arrays.asList(children));
    }
}
