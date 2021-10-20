package dev.hawu.plugins.api.inventories.style;

import dev.hawu.plugins.api.inventories.item.ItemStackBuilder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Represents a style that has its backing item immutable,
 * but not frozen. Functions and transformers can be used to
 * apply changes to the item itself, but the field never mutates.
 *
 * @since 2.0
 */
public final class StaticStyle implements Style {

    private final ItemStack item;

    /**
     * Constructs an unchanging style with the initial item stack.
     *
     * @param init The item to init with.
     * @since 2.0
     */
    public StaticStyle(@NotNull final ItemStack init) {
        item = init;
    }

    /**
     * Applies the provided transformer to the backing stack.
     *
     * @param consumer The function to apply.
     * @since 2.0
     */
    public void transform(@NotNull final Consumer<ItemStack> consumer) {
        consumer.accept(item);
    }

    /**
     * Attempts to modify the item stack by using a builder.
     *
     * @return A new instance of {@link ItemStackBuilder}.
     * @since 2.0
     */
    @NotNull
    public ItemStackBuilder withItemBuilder() {
        return new ItemStackBuilder(item);
    }

    @Override
    public @NotNull ItemStack getDisplay() {
        return item;
    }

}
