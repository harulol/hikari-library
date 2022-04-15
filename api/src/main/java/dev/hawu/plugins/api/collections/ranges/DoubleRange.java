package dev.hawu.plugins.api.collections.ranges;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * An iterable of doubles over a numeric range.
 *
 * @since 1.6
 */
public final class DoubleRange extends FloatingRange<Double> {

    private final double min;
    private final double max;
    private final double step;

    /**
     * Creates a new range with the given lower and upper bounds.
     *
     * @param min  the lower bound
     * @param max  the upper bound
     * @param step the step
     * @since 1.6
     */
    public DoubleRange(final double min, final double max, final double step) {
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
        this.step = step;

        if(step <= 0) {
            throw new IllegalArgumentException("Step must be greater than 0");
        }
    }

    @Override
    public Double getLowerBound() {
        return min;
    }

    @Override
    public Double getUpperBound() {
        return max;
    }

    @Override
    public long getSize() {
        return (long) Math.floor((max - min) / step) + 1;
    }

    @Override
    public double getAverage() {
        return max * 0.5 + min * 0.5;
    }

    @NotNull
    @Override
    public Iterator<Double> iterator() {
        final List<Double> doubles = new ArrayList<>();

        for(double i = min; i < max; i += step) {
            doubles.add(i);
        }
        doubles.add(max);

        return doubles.iterator();
    }

    @Override
    public void forEach(final Consumer<? super Double> action) {
        super.forEach(action);
    }

    @Override
    public Spliterator<Double> spliterator() {
        return super.spliterator();
    }

}
