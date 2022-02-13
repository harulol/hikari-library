package dev.hawu.plugins.api.collections.progressions;

/**
 * Represents a progression of integers.
 *
 * @since 1.0
 */
public abstract class Progression {

    /**
     * The starting number of this progression.
     *
     * @since 1.0
     */
    protected final double from;

    /**
     * The common difference between each term
     * of the progression.
     *
     * @since 1.0
     */
    protected final double step;

    /**
     * Empty constructor to construct a generic progression.
     *
     * @param from The minimum value of this progression.
     * @param step The common difference.
     * @since 1.0
     */
    Progression(final double from, final double step) {
        this.from = from;
        this.step = step;
    }

    /**
     * Gets the term at the index n (1-indexed).
     * <p>
     * The minimum value of this progression is considered
     * the first term ({@code n = 1}).
     *
     * @param n The index to retrieve.
     * @return The term at the provided index.
     * @since 1.0
     */
    public abstract double getTerm(final int n);

    /**
     * Gets the minimum term index that has the value
     * the closest to the provided value, but does not
     * go over it.
     *
     * @param v The value to compute with.
     * @return The index of the term.
     * @since 1.0
     */
    public abstract int getNthTerm(final double v);

}
