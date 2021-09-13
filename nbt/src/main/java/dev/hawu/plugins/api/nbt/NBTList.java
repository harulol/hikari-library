package dev.hawu.plugins.api.nbt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Represents a generic tag list that holds multiple
 * tags in insertion order.
 *
 * @since 1.0
 */
public final class NBTList extends NBTType implements List<NBTType> {

    private final List<@NotNull NBTType> list;

    /**
     * Constructs the tag with an empty list of tags.
     *
     * @since 1.0
     */
    public NBTList() {
        this.list = new ArrayList<>();
    }

    /**
     * Constructs the tag to hold the provided value
     * as its underlying list.
     *
     * @param list The list to pass in.
     * @since 1.0
     */
    public NBTList(@NotNull final List<@NotNull NBTType> list) {
        this.list = list;
    }

    @Override
    @NotNull
    public String toString() {
        return list.toString();
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public boolean equals(@Nullable final Object other) {
        if(!(other instanceof NBTList)) return false;
        return ((NBTList) other).list.equals(list);
    }

    @Override
    @NotNull
    public NBTType clone() {
        return new NBTList(new ArrayList<>(list));
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(@Nullable final Object o) {
        return list.contains(o);
    }

    @NotNull
    @Override
    public Iterator<@NotNull NBTType> iterator() {
        return list.iterator();
    }

    @Override
    @NotNull
    public Object @NotNull [] toArray() {
        return list.toArray();
    }

    @NotNull
    @Override
    public <T> T @NotNull [] toArray(@NotNull final T @NotNull [] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(@NotNull final NBTType nbtType) {
        return list.add(nbtType);
    }

    /**
     * Appends a named binary tag of type {@link Byte}
     * containing the value provided to the list.
     *
     * @param b The byte to be contained in the tag.
     * @return Always {@code true}.
     * @since 1.0
     */
    public boolean addByte(final byte b) {
        return list.add(new NBTByte(b));
    }

    /**
     * Appends a named binary tag of type {@link Short}
     * containing the value provided to the list.
     *
     * @param s The short to be contained in the tag.
     * @return Always {@code true}.
     * @since 1.0
     */
    public boolean addShort(final short s) {
        return list.add(new NBTShort(s));
    }

    /**
     * Appends a named binary tag of type {@link Integer}
     * containing the value provided to the list.
     *
     * @param i The int to be contained in the tag.
     * @return Always {@code true}.
     * @since 1.0
     */
    public boolean addInt(final int i) {
        return list.add(new NBTInt(i));
    }

    /**
     * Appends a named binary tag of type {@link Long}
     * containing the value provided to the list.
     *
     * @param l The long to be contained in the tag.
     * @return Always {@code true}.
     * @since 1.0
     */
    public boolean addLong(final long l) {
        return list.add(new NBTLong(l));
    }

    /**
     * Appends a named binary tag of type {@link Float}
     * containing the value provided to the list.
     *
     * @param f The float to be contained in the tag.
     * @return Always {@code true}.
     * @since 1.0
     */
    public boolean addFloat(final float f) {
        return list.add(new NBTFloat(f));
    }

    /**
     * Appends a named binary tag of type {@link Double}
     * containing the value provided to the list.
     *
     * @param d The double to be contained in the tag.
     * @return Always {@code true}.
     * @since 1.0
     */
    public boolean addDouble(final double d) {
        return list.add(new NBTDouble(d));
    }

    /**
     * Appends a named binary tag of type {@link String}
     * containing the value provided to the list.
     *
     * @param s The string to be contained in the tag.
     * @return Always {@code true}.
     * @since 1.0
     */
    public boolean addString(@NotNull final String s) {
        return list.add(new NBTString(s));
    }

    /**
     * Appends a named binary tag that holds the value of
     * an array of 8-bit integers.
     *
     * @param array The array to append as a tag.
     * @return Always {@code true}.
     * @since 1.0
     */
    public boolean addByteArray(final byte[] array) {
        return list.add(new NBTByteArray(array));
    }

    /**
     * Appends a named binary tag that holds the value of
     * an array of 32-bit integers.
     *
     * @param array The array to append as a tag.
     * @return Always {@code true}.
     * @since 1.0
     */
    public boolean addIntArray(final int[] array) {
        return list.add(new NBTIntArray(array));
    }

    /**
     * Appends a named binary tag that holds the value of
     * an array of 64-bit integers.
     *
     * @param array The array to append as a tag.
     * @return Always {@code true}.
     * @since 1.0
     */
    public boolean addLongArray(final long[] array) {
        return list.add(new NBTLongArray(array));
    }

    /**
     * Appends a named binary tag that holds the value of
     * a list of tags.
     *
     * @param list The list of tags as the value of the new tag.
     * @return Always {@code true}.
     * @since 1.0
     */
    public boolean addList(@NotNull final List<@NotNull NBTType> list) {
        return list.add(new NBTList(list));
    }

    /**
     * Appends a named binary tag that holds the value of
     * a map of {@link String} and tags.
     *
     * @param map The map of tags as the value of the new tag.
     * @return Always {@code true}.
     * @since 1.0
     */
    public boolean addCompound(@NotNull final Map<@NotNull String, @NotNull NBTType> map) {
        return list.add(new NBTCompound(map));
    }

    @Override
    public boolean remove(@Nullable final Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull final Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull final Collection<? extends NBTType> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(final int index, @NotNull final Collection<? extends NBTType> c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NotNull final Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull final Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    @NotNull
    public NBTType get(final int index) {
        return list.get(index);
    }

    @Override
    @NotNull
    public NBTType set(final int index, @NotNull final NBTType element) {
        return list.set(index, element);
    }

    @Override
    public void add(final int index, @NotNull final NBTType element) {
        list.add(index, element);
    }

    @Override
    @NotNull
    public NBTType remove(final int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(@Nullable final Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(@Nullable final Object o) {
        return list.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<@NotNull NBTType> listIterator() {
        return list.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<@NotNull NBTType> listIterator(final int index) {
        return list.listIterator(index);
    }

    @NotNull
    @Override
    public List<@NotNull NBTType> subList(final int fromIndex, final int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

}
