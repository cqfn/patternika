package org.cqfn.patternika.ast.mapper;

import org.cqfn.patternika.ast.NodeExt;

/**
 * Mapper for Node objects that builds connections based on Greed logic O(N^2).
 *
 * @since 2019/10/31
c*/
public class GreedMapper extends AbstractMapper {

    /**
     * Constructor.
     *
     * @param root1 first node tree to be mapped.
     * @param root2 second node tree to be mapped.
     */
    public GreedMapper(final NodeExt root1, final NodeExt root2) {
        super(root1, root2);
    }

    /**
     * Builds mappings between the two node trees.
     *
     * @param root1 root of the first node tree.
     * @param root2 root of the second node tree.
     */
    @Override
    protected void buildMapping(final NodeExt root1, final NodeExt root2) {
        // Simple greed logic has already been implemented in AbstractMapper
        // so that here we just have to do nothing to save some memory and time.
    }

}
