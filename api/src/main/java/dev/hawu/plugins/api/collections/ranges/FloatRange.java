package dev.hawu.plugins.api.collections.ranges;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * An iterable of floats over a numeric range.
 *
 * @since 1.6
 */
public final class FloatRange extends FloatingRange<Float> {

    private final float min;
    private final float max;
    private final float step;

    /**
     * Creates a new range with the given lower and upper bounds.
     *
     * @param min  the lower bound
     * @param max  the upper bound
     * @param step the step
     * @since 1.6
     */
    public FloatRange(final float min, final float max, final float step) {
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
        this.step = step;

        if(step <= 0) {
            throw new IllegalArgumentException("Step must be greater than 0");
        }
    }

    @Override
    public Float getLowerBound() {
        return min;
    }

    @Override
    public Float getUpperBound() {
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
    public Iterator<Float> iterator() {
        final List<Float> floats = new ArrayList<>();

        for(float i = min; i < max; i += step) {
            floats.add(i);
        }
        floats.add(max);

        return floats.iterator();
    }

    @Override
    public void forEach(final Consumer<? super Float> action) {
        super.forEach(action);
    }

    @Override
    public Spliterator<Float> spliterator() {
        return super.spliterator();
    }

}
