/**
 * This package contains logic that traverses the AST and applies visitors
 * to its nodes. This can be used to collect additional information on entities
 * represented by AST nodes.
 *
 * <p>This logic needs to be separated from AST nodes, for the following reasons:
 * 1. Smaller nodes classes: less methods, no intermediate analysis results in nodes.
 * 2. Better code reuse: same traversal infrastructure in many analyzers for different languages.
 * 3. Less dependencies: changes in analysis logic stay local and do not affect the AST.
 *
 * @since 2020/5/8
 */
package org.cqfn.patternika.ast.visitor;
