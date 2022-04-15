package dev.hawu.plugins.api.collections.ranges;

/**
 * A range of floating point values.
 *
 * @since 1.6
 */
public abstract class FloatingRange<T> implements Iterable<T> {

    /**
     * Returns the lower bound of this range.
     *
     * @return the lower bound
     * @since 1.6
     */
    public abstract T getLowerBound();

    /**
     * Returns the upper bound of this range.
     *
     * @return the upper bound
     * @since 1.6
     */
    public abstract T getUpperBound();

    /**
     * Returns the size of this range.
     *
     * @return the size
     * @since 1.6
     */
    public abstract long getSize();

    /**
     * Returns the average of the elements in this range.
     *
     * @return the average
     * @since 1.6
     */
    public abstract double getAverage();

}
