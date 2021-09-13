package dev.hawu.plugins.api;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * A small utility class to access off hand items and main hand
 * items from legacy APIs.
 *
 * @since 1.1
 */
public final class Players {

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    private static final MethodHandle offHandGetter;
    private static final MethodHandle offHandSetter;

    private static final MethodHandle mainHandGetter;
    private static final MethodHandle mainHandSetter;

    static {
        offHandGetter = getHandle(PlayerInventory.class, "getItemInOffHand", MethodType.methodType(ItemStack.class));
        offHandSetter = getHandle(PlayerInventory.class, "setItemInOffHand", MethodType.methodType(Void.TYPE, ItemStack.class));
        mainHandGetter = getHandle(PlayerInventory.class, "getItemInMainHand", MethodType.methodType(ItemStack.class));
        mainHandSetter = getHandle(PlayerInventory.class, "setItemInMainHand", MethodType.methodType(Void.TYPE, ItemStack.class));
    }

    private Players() {}

    @Nullable
    private static MethodHandle getHandle(@NotNull final Class<?> cls, @NotNull final String name, @NotNull final MethodType type) {
        try {
            return lookup.findVirtual(cls, name, type);
        } catch(final NoSuchMethodException | IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Retrieves the item in the player's inventory's offhand slot.
     *
     * @param inventory The inventory whose offhand slot to retrieve.
     * @return The item there, if possible, or {@code null} if there's nothing in the slot or an error happened.
     * @since 1.1
     */
    @Nullable
    public static ItemStack getItemInOffHand(@NotNull final PlayerInventory inventory) {
        try {
            return offHandGetter == null ? null : (ItemStack) offHandGetter.invokeExact(inventory);
        } catch(final Throwable throwable) {
            return null;
        }
    }

    /**
     * Attempts to put the provided item stack in the player inventory's
     * offhand slot. Fails silently if any errors happen.
     *
     * @param inventory The inventory whose offhand slot to put.
     * @param item      The item to put.
     * @since 1.1
     */
    public static void setItemInOffHand(@NotNull final PlayerInventory inventory, @Nullable final ItemStack item) {
        try {
            offHandSetter.invokeExact(inventory, item);
        } catch(final Throwable ignored) {}
    }

    /**
     * Attempts to retrieve the item stack in the player's main hand slot, or
     * just returns the {@link PlayerInventory#getItemInHand()}'s result if
     * the former was not possible.
     *
     * @param inventory The inventory whose slot to retrieve.
     * @return The {@link ItemStack} at the main hand slot.
     * @since 1.1
     */
    @Nullable
    public static ItemStack getItemInMainHand(@NotNull final PlayerInventory inventory) {
        try {
            return mainHandGetter == null ? inventory.getItemInHand() : (ItemStack) mainHandGetter.invokeExact(inventory);
        } catch(final Throwable throwable) {
            return null;
        }
    }

    /**
     * Attempts to put the item stack in the player's main hand slot or
     * invokes the method {@link PlayerInventory#setItemInHand(ItemStack)} if the former
     * was not possible.
     *
     * @param inventory The inventory whose main hand slot to put.
     * @param item      The item to put.
     * @since 1.1
     */
    public static void setItemInMainHand(@NotNull final PlayerInventory inventory, @Nullable final ItemStack item) {
        try {
            if(mainHandSetter != null) mainHandSetter.invokeExact(inventory, item);
            else inventory.setItemInHand(item);
        } catch(final Throwable ignored) {}
    }

}
