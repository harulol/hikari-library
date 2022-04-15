package dev.hawu.plugins.api.collections;

import dev.hawu.plugins.api.TimeConversions;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A delegated map that works with cooldowns.
 * This works the same way as <code>Map[K, Long]</code>.
 *
 * @param <K> the type of the key
 * @since 1.6
 */
public final class CooldownMap<K> {

    private final long cooldown;
    private final Map<K, Long> map = new HashMap<>();

    /**
     * Creates the cooldown map with a 1s cooldown.
     *
     * @since 1.6
     */
    public CooldownMap() {
        this.cooldown = 1000;
    }

    /**
     * Creates the cooldown map with the given cooldown
     * in milliseconds.
     *
     * @param cooldown the cooldown in milliseconds
     * @since 1.6
     */
    public CooldownMap(final @NotNull Number cooldown) {
        this.cooldown = cooldown.longValue();
    }

    /**
     * Puts the key on cooldown for whatever the default cooldown is.
     *
     * @param key the key to put on cooldown
     */
    public void putOnCooldown(final @NotNull K key) {
        map.put(key, System.currentTimeMillis() + cooldown);
    }

    /**
     * Puts the key on cooldown for the given cooldown.
     *
     * @param key      the key to put on cooldown
     * @param cooldown the cooldown in milliseconds
     * @since 1.6
     */
    public void putOnCooldown(final @NotNull K key, final long cooldown) {
        map.put(key, System.currentTimeMillis() + cooldown);
    }

    /**
     * Checks if the key is on cooldown.
     *
     * @param key the key to check
     * @return true if the key is on cooldown.
     * @since 1.6
     */
    public boolean isOnCooldown(final @NotNull K key) {
        if(!map.containsKey(key)) return false;
        return map.get(key) > System.currentTimeMillis();
    }

    /**
     * Removes the key from the cooldown map.
     *
     * @param key the key to remove
     * @since 1.6
     */
    public void remove(final @NotNull K key) {
        map.remove(key);
    }

    /**
     * Retrieves the formatted cooldown for the key.
     *
     * @param key the key to retrieve the cooldown for
     * @return the formatted cooldown
     * @since 1.6
     */
    @NotNull
    public String getCooldown(final @NotNull K key) {
        if(!map.containsKey(key)) return "0s";
        return TimeConversions.buildTimestamp(map.get(key)).withSpaces().build();
    }

    /**
     * Retrieves the cooldown for the key in milliseconds.
     *
     * @param key the key to retrieve the cooldown for
     * @return the cooldown in milliseconds
     * @since 1.6
     */
    public long getCooldownValue(final @NotNull K key) {
        return Math.max(0, System.currentTimeMillis() - map.get(key));
    }

}
