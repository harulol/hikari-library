package dev.hawu.plugins.api.gui.templates;

import dev.hawu.plugins.api.Tasks;
import dev.hawu.plugins.api.gui.GuiComponent;
import dev.hawu.plugins.api.items.ItemStackBuilder;
import dev.hawu.plugins.api.misc.PluginAdapter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Represents a GUI component that might take some time
 * to load, whose function is deferred.
 * <p>
 * This will render a "loading" state if the function is still running,
 * or "failed" state if the invocation had an error.
 *
 * @since 2.0
 */
public final class SuspenseComponent extends GuiComponent<ItemStack> {

    private final GuiComponent<?> loading;
    private final GuiComponent<?> failed;
    private final Supplier<ItemStack> supplier;

    /**
     * Creates a suspense component with the custom loading and failed states.
     *
     * @param loading  the component to show while loading
     * @param failed   the component to show if failed
     * @param supplier the supplier to invoke
     * @since 2.0
     */
    public SuspenseComponent(final @NotNull GuiComponent<?> loading, final @NotNull GuiComponent<?> failed,
                             final @NotNull Supplier<ItemStack> supplier) {
        this.loading = loading;
        this.failed = failed;
        this.supplier = supplier;
    }

    /**
     * Creates a suspense component with the default failed state.
     *
     * @param loading  the loading state
     * @param supplier the supplier to invoke
     * @since 2.0
     */
    public SuspenseComponent(final @NotNull GuiComponent<?> loading, final @NotNull Supplier<ItemStack> supplier) {
        this(
            loading,
            new StaticComponent(ItemStackBuilder.of(Material.BEDROCK)
                .name("&c&lFailed to load")
                .lore("&7An error has occurred while", "&7loading this item.")
                .build()),
            supplier
        );
    }

    /**
     * Creates a suspense component with the default loading and failed states.
     *
     * @param supplier the supplier to invoke
     * @since 2.0
     */
    public SuspenseComponent(final @NotNull Supplier<ItemStack> supplier) {
        this(
            new StaticComponent(ItemStackBuilder.of(Material.BEDROCK).name("&e&lLoading...").build()),
            supplier
        );
    }

    @Override
    public void componentDidMount() {
        this.swap(this.loading);
        Tasks.scheduleAsync(PluginAdapter.getPlugin(), runnable -> {
            try {
                this.setState(supplier.get());
                this.loading.swap(this);
            } catch(final Exception exception) {
                exception.printStackTrace();
                this.loading.swap(this.failed);
            }
        });
    }

    @Override
    public @Nullable ItemStack render() {
        return this.state;
    }

}
