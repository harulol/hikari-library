package dev.hawu.plugins.api.collections.tuples;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an immutable tuple with 2 values.
 *
 * @param <A> The type of the first value.
 * @param <B> The type of the second value.
 * @since 1.0
 */
public class Pair<A, B> {

    private final A first;
    private final B second;

    /**
     * Constructs a pair with the provided values.
     *
     * @param first  The first value.
     * @param second The second value.
     * @since 1.0
     */
    public Pair(@Nullable final A first, @Nullable final B second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Retrieves the first value of the tuple.
     *
     * @return The first value of the tuple, nullable.
     * @since 1.0
     */
    @Nullable
    public final A getFirst() {
        return this.first;
    }

    /**
     * Retrieves the second value of the tuple.
     *
     * @return The second value of the tuple, nullable.
     * @since 1.0
     */
    @Nullable
    public final B getSecond() {
        return this.second;
    }

    /**
     * Retrieves the first component for the tuple.
     * This function only delegates the invocation back to
     * {@link Pair#getFirst()}.
     *
     * @return The first component of the tuple.
     * @since 1.0
     */
    @Nullable
    public final A component1() {
        return getFirst();
    }

    /**
     * Retrieves the second component for the tuple.
     * This function only delegates the invocation back to
     * {@link Pair#getSecond()}.
     *
     * @return The second component of the tuple.
     * @since 1.0
     */
    @Nullable
    public final B component2() {
        return getSecond();
    }

    @Override
    public String toString() {
        return String.format("Pair{first=%s,second=%s}",
            getFirst() == null ? "null" : getFirst().toString(), getSecond() == null ? "null" : getSecond().toString());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(first).append(second).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) obj;
        return new EqualsBuilder().append(first, pair.first).append(second, pair.second).isEquals();
    }

}
