package org.cqfn.patternika.ast;

/**
 * This interface is to be supported by all holes (wildcard nodes).
 *
 * @since 2021/02/10
 */
public interface Hole {
    /**
     * Gets the number of the hole that uniquely identifies it.
     *
     * @return the number of the hole.
     */
    int getNumber();
}
