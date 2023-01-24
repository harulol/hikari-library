package dev.hawu.plugins.api.gui;

import dev.hawu.plugins.api.Strings;
import dev.hawu.plugins.api.collections.Property;
import dev.hawu.plugins.api.gui.transitions.Transition;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Represents an inventory model
 * for interactive UIs.
 *
 * @since 2.0
 */
public final class GuiModel implements InventoryHolder {

    private final Inventory inventory;
    private final GuiComponent<?>[] components;
    private final Map<String, GuiComponent<?>> refs = new HashMap<>();

    private Consumer<InventoryClickEvent> outsideConsumer = event -> event.setCancelled(true);
    private Consumer<InventoryDragEvent> dragEventConsumer = event -> event.setCancelled(true);
    private Runnable onCloseHook;
    private boolean cooldown = true;
    private boolean unmountOnClose = true;

    /**
     * Creates an inventory with the given size
     *
     * @param size  the size
     * @param title the title colorized
     * @since 2.0
     */
    public GuiModel(final int size, final @NotNull String title) {
        this.inventory = Bukkit.createInventory(this, size, Strings.color(title));
        this.components = new GuiComponent[size];
    }

    /**
     * Creates an inventory with the given type
     *
     * @param type  the type
     * @param title the title colorized
     * @since 2.0
     */
    public GuiModel(final @NotNull InventoryType type, final @NotNull String title) {
        this.inventory = Bukkit.createInventory(this, type, Strings.color(title));
        this.components = new GuiComponent[inventory.getSize()];
    }

    /**
     * Retrieves the component mounted at the specified index.
     *
     * @param index the index
     * @param <T>   the type of the GuiComponent
     * @return the component
     * @since 2.0
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> GuiComponent<T> get(final int index) {
        if(index < 0 || index >= inventory.getSize())
            throw new IndexOutOfBoundsException(String.format("Index %d is out of bounds for inventory of size %d", index, inventory.getSize()));

        return (GuiComponent<T>) components[index];
    }

    /**
     * Retrieves the component mounted with the specified ref.
     *
     * @param ref the ref
     * @param <T> the type of the GuiComponent
     * @return the component
     * @since 2.0
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> GuiComponent<T> get(final @Nullable String ref) {
        return (GuiComponent<T>) this.refs.get(ref);
    }

    /**
     * Configures whether this inventory should automatically unmount
     * everything as the inventory closes.
     *
     * @param v Whether to unmount
     * @since 1.6
     */
    public void setUnmountOnClose(final boolean v) {
        this.unmountOnClose = v;
    }

    /**
     * Checks if the model is currently affected
     * by the cooldown checks in inventory click events.
     *
     * @return Whether it is affected.
     * @since 1.2
     */
    public boolean hasCooldown() {
        return cooldown;
    }

    /**
     * Specifies whether clicks on this model should be put
     * on cooldowns, in case of bugs while users spamming clicks.
     *
     * @since 1.2
     */
    public void setCooldown(final boolean cooldown) {
        this.cooldown = cooldown;
    }

    /**
     * Configures what to run when this inventory model is closed
     * by the viewers.
     *
     * @param runnable The function to run.
     * @since 1.2
     */
    public void onClose(final @NotNull Runnable runnable) {
        this.onCloseHook = runnable;
    }

    /**
     * Configures the consumer to run when this inventory model is invoked
     * in an InventoryDragEvent.
     *
     * @param handler The handler to run.
     * @since 1.2
     */
    public void onDrag(final @NotNull Consumer<InventoryDragEvent> handler) {
        this.dragEventConsumer = handler;
    }

    /**
     * Configures the consumer to be invoked when the viewer
     * clicks outside this inventory.
     *
     * @param handler The consumer to invoke.
     * @since 1.2
     */
    public void onOutsideClick(final @NotNull Consumer<InventoryClickEvent> handler) {
        this.outsideConsumer = handler;
    }

    /**
     * Mounts a component on a specified index.
     *
     * @param index     the index
     * @param component the component
     * @since 2.0
     */
    public void mount(final int index, final @Nullable GuiComponent<?> component) {
        this.unmount(index);
        if(component == null) return;

        this.components[index] = component;
        component.apply(this, index);
        try {
            inventory.setItem(index, component.render());
            component.componentDidMount();
            this.refs.put(component.getRef(), component);
        } catch(final Throwable throwable) {
            throwable.printStackTrace();
            component.componentDidCatch(throwable);
        }
    }

    /**
     * Unmounts the component currently mounted at the specified index
     *
     * @param index the index
     * @since 2.0
     */
    public void unmount(final int index) {
        final GuiComponent<?> component = get(index);
        if(component != null) {
            component.dismount();
            component.componentWillUnmount();
            this.refs.remove(standardize(component.getRef()));
        }
        components[index] = null;
    }

    /**
     * Forcefully updates the component at the index.
     * <p>
     * Using this function to invoke an update will not
     * fire event listeners like {@link GuiComponent#componentDidUpdate(Object)}.
     * <p>
     * Only {@link GuiComponent#componentDidCatch(Throwable)} will be invoked.
     *
     * @param index the index
     * @since 2.0
     */
    @SuppressWarnings("deprecation")
    public void update(final int index) {
        final GuiComponent<?> component = get(index);
        try {
            inventory.setItem(index, Property.of(component).map(GuiComponent::render).orNull());
        } catch(final Throwable throwable) {
            Property.of(component).ifPresent(comp -> comp.componentDidCatch(throwable));
        }

        inventory.getViewers().stream()
            .filter(player -> player instanceof Player)
            .map(entity -> (Player) entity)
            .forEach(Player::updateInventory);
    }

    /**
     * Updates at all indicies.
     *
     * @since 2.0
     */
    public void updateAll() {
        IntStream.range(0, inventory.getSize()).forEach(this::update);
    }

    /**
     * Updates all and renders the inventory to a human entity.
     *
     * @param entity the entity
     * @since 2.0
     */
    public void open(final @NotNull HumanEntity entity) {
        updateAll();
        entity.openInventory(inventory);
    }

    /**
     * Applies a transition to a new inventory.
     *
     * @param entity     the entity
     * @param transition the transition
     * @param target     the target inventory
     * @since 2.0
     */
    public void useTransition(final @NotNull HumanEntity entity, final @NotNull Transition transition, final @NotNull GuiModel target) {
        transition.startTransition(entity, this, target);
    }

    @Nullable
    private String standardize(final @Nullable String ref) {
        if(ref == null) return null;
        return ref.trim().toLowerCase().replaceAll("\\s+", "_").replaceAll("[^a-zA-Z1-9_$]", "");
    }

    /**
     * Handles the close event.
     *
     * @since 2.0
     */
    @Internal
    public void handleClose() {
        if(this.onCloseHook != null) this.onCloseHook.run();
        if(unmountOnClose) IntStream.range(0, components.length).forEach(this::unmount);
    }

    /**
     * Handles the click event.
     *
     * @param event the event
     * @since 2.0
     */
    @Internal
    public void handleClick(final @NotNull InventoryClickEvent event) {
        if(event.getSlotType() == SlotType.OUTSIDE || event.getRawSlot() >= inventory.getSize()) {
            if(outsideConsumer != null) outsideConsumer.accept(event);
            return;
        }

        final GuiComponent<?> component = get(event.getRawSlot());
        if(component != null) component.handleClick(event);
        else event.setCancelled(true);
    }

    /**
     * Handles the drag event.
     * <p>
     * Would not recommend messing with this event since it introduces
     * a ton of loopholes for users to exploit the GUI.
     *
     * @param event the event
     * @since 2.0
     */
    @Internal
    public void handleDrag(final @NotNull InventoryDragEvent event) {
        if(dragEventConsumer != null) dragEventConsumer.accept(event);
    }

    /**
     * Retrieves the object's inventory.
     *
     * @return the inventory
     * @since 2.0
     */
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

}
