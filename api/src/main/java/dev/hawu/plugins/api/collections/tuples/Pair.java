package dev.hawu.plugins.api.collections.tuples;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents an immutable tuple with 2 values.
 *
 * @param <A> The type of the first value.
 * @param <B> The type of the second value.
 * @since 1.0
 */
public class Pair<A, B> {

    protected final A first;
    protected final B second;

    /**
     * Quickly construct a pair using a call that is generally
     * shorter and cleaner than using the normal constructor.
     *
     * @param first  The first value of the pair.
     * @param second The second value of the pair.
     * @param <A>    The type for the first value.
     * @param <B>    The type for the second value.
     * @return The created pair.
     * @since 1.2
     */
    @NotNull
    public static <A, B> Pair<A, B> of(final @Nullable A first, final @Nullable B second) {
        return new Pair<>(first, second);
    }

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
     * @return The first value of the tuple.
     * @since 1.0
     */
    @NotNull
    public final A getFirst() {
        return Objects.requireNonNull(this.first, "First value of pair required to be non-null is null.");
    }

    /**
     * Retrieves the second value of the tuple.
     *
     * @return The second value of the tuple.
     * @since 1.0
     */
    @NotNull
    public final B getSecond() {
        return Objects.requireNonNull(this.second, "Second value of pair required to be non-null is null.");
    }

    /**
     * Retrieves the first value of the tuple and does
     * not throw an error if it is null.
     *
     * @return The first value or null.
     * @since 1.2
     * @deprecated Use {@link Pair#getFirstOption()} for nullability and {@link Pair#getFirst()} for non-null instead.
     */
    @Nullable
    @Deprecated
    public final A getFirstOrNull() {
        return first;
    }

    /**
     * Retrieves the first value encapsulated in an {@link Optional}.
     *
     * @return The first value in option.
     * @since 1.4
     */
    @NotNull
    public final Optional<A> getFirstOption() {
        return Optional.ofNullable(first);
    }

    /**
     * Retrieves the second value of the tuple and does
     * not throw an error if it is null.
     *
     * @return The second value or null.
     * @since 1.2
     * @deprecated Use {@link Pair#getSecondOption()} for nullability and {@link Pair#getSecond()} for non-null instead.
     */
    @Deprecated
    @Nullable
    public final B getSecondOrNull() {
        return second;
    }

    /**
     * Retrieves the second value encapsulated in an {@link Optional}.
     *
     * @return The second value in option.
     * @since 1.4
     */
    @NotNull
    public final Optional<B> getSecondOption() {
        return Optional.ofNullable(second);
    }

    /**
     * Retrieves the first component for the tuple.
     * This function only delegates the invocation back to
     * {@link Pair#getFirst()}.
     *
     * @return The first component of the tuple.
     * @since 1.0
     */
    @NotNull
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
    @NotNull
    public final B component2() {
        return getSecond();
    }

    @Override
    public String toString() {
        return String.format("Pair{first=%s,second=%s}", first, second);
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
