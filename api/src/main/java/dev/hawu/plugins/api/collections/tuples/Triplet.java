package dev.hawu.plugins.api.collections.tuples;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an immutable tuple with 3 values.
 *
 * @param <A> The type of the first value.
 * @param <B> The type of the second value.
 * @param <C> The type of the third value.
 * @since 1.0
 */
public class Triplet<A, B, C> extends Pair<A, B> {

    private final @Nullable C third;

    /**
     * Constructs a triplet with the provided values.
     *
     * @param first  The first value.
     * @param second The second value.
     * @param third  The third value.
     * @since 1.0
     */
    public Triplet(@Nullable final A first, @Nullable final B second, @Nullable final C third) {
        super(first, second);
        this.third = third;
    }

    /**
     * Retrieves the third value of the tuple.
     *
     * @return The third value of the tuple, nullable.
     * @since 1.0
     */
    @Nullable
    public final C getThird() {
        return third;
    }

    /**
     * Retrieves the third component for the tuple.
     * This function only delegates the invocation back to
     * {@link Triplet#getThird()}.
     *
     * @return The third component of the tuple.
     * @since 1.1
     */
    @Nullable
    public final C component3() {
        return getThird();
    }

    @Override
    public String toString() {
        return String.format("Triplet{first=%s,second=%s,third=%s}",
            getFirst() == null ? "null" : getFirst().toString(), getSecond() == null ? "null" : getSecond().toString(),
            getThird() == null ? "null" : getThird().toString());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getFirst()).append(getSecond()).append(third).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) obj;
        return new EqualsBuilder().append(getFirst(), triplet.getFirst()).append(getSecond(), triplet.getSecond()).append(third, triplet.third).isEquals();
    }

}
