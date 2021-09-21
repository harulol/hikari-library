package dev.hawu.plugins.api;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.Nullable;

/**
 * A generic container that holds 2 immutable objects
 * with 2 different types.
 * Usually used similarly to {@link java.util.Map.Entry}.
 *
 * @param <A> The type of the first value.
 * @param <B> The type of the second value.
 * @since 1.0
 */
public final class Pair<A, B> {

    private final @Nullable A first;
    private final @Nullable B second;

    /**
     * Constructs a 2-tuple with first and second values.
     *
     * @param first  The first value of the tuple.
     * @param second The second value of the tuple.
     * @since 1.0
     */
    public Pair(@Nullable final A first, @Nullable final B second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Retrieves the first value of the tuple.
     *
     * @return The first or the left of the pair.
     * @since 1.0
     */
    @Nullable
    public A getFirst() {
        return first;
    }

    /**
     * Retrieves the second value of the tuple.
     *
     * @return The second or the right of the pair.
     * @since 1.0
     */
    @Nullable
    public B getSecond() {
        return second;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return new EqualsBuilder().append(first, pair.first).append(second, pair.second).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(first).append(second).toHashCode();
    }

    @Override
    public String toString() {
        return String.format("Pair{first=%s,second=%s}", first, second);
    }

}
