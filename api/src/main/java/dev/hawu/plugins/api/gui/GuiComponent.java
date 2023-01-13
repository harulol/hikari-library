package dev.hawu.plugins.api.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Represents a rendered component of the GUI.
 *
 * @param <S> the type of the state
 * @since 2.0
 */
public abstract class GuiComponent<S> {

    protected S state;
    private GuiModel model = null;
    private int index = -1;

    /**
     * Constructs a GuiComponent with props.
     *
     * @since 2.0
     */
    public GuiComponent() {
        this(null);
    }

    /**
     * Constructs a GuiComponent with states and props.
     *
     * @param initialState the initial state
     * @since 2.0
     */
    public GuiComponent(final @Nullable S initialState) {
        this.state = initialState;
    }

    /**
     * Retrieves the current state of the component.
     *
     * @return the state
     * @since 2.0
     */
    @Nullable
    public final S getState() {
        return this.state;
    }

    /**
     * Updates the current state of the component.
     *
     * @param state the state
     * @since 2.0
     */
    public final void setState(final @Nullable S state) {
        if(this.state == null && state == null) return;
        if(this.state != null && this.state.equals(state)) return;

        final S prevState = this.state;
        this.state = state;

        if(shouldComponentUpdate(state)) {
            this.forceUpdate();
            this.componentDidUpdate(prevState);
        }
    }

    /**
     * Updates the current state of the component
     * via an updater.
     *
     * @param updater the updater that takes in the current state, and returns the new state
     * @since 2.0
     */
    public final void setState(final @NotNull Function<S, S> updater) {
        this.setState(updater.apply(this.state));
    }

    /**
     * Retrieves the identifier of this component.
     *
     * @return the identifier
     * @since 2.0
     */
    @Nullable
    public final String getRef() {
        return null;
    }

    /**
     * This function is called once for every time the player
     * has clicked on this element, if mounted.
     *
     * @param event The event passed in.
     * @since 2.0
     */
    public void handleClick(final @NotNull InventoryClickEvent event) {
        event.setCancelled(true);
    }

    /**
     * Is called when the first mount happens, and the component has
     * finished mounting.
     *
     * @since 2.0
     */
    public void componentDidMount() {
        // Left blank intentionally.
    }

    /**
     * Performs a check whether the component should update using the next
     * state and props.
     * <p>
     * For most use cases, you'd want {@link GuiComponent#setState(Object)} to re-render everything.
     *
     * @param nextState the next state
     * @return whether it should update
     * @since 2.0
     */
    public boolean shouldComponentUpdate(final @Nullable S nextState) {
        return true;
    }

    /**
     * Is called after the component finishes updating and is placed
     * within the UI.
     *
     * @param prevState the state previously
     * @since 2.0
     */
    public void componentDidUpdate(final @Nullable S prevState) {
        // Intentionally left blank.
    }

    /**
     * Is called when the component is being unmounted.
     *
     * @since 2.0
     */
    public void componentWillUnmount() {
        // Intentionally left blank.
    }

    /**
     * Is called when the rendering of the component yielded
     * an error.
     *
     * @param throwable the throwable instance
     * @since 2.0
     */
    public void componentDidCatch(final @NotNull Throwable throwable) {
        throwable.printStackTrace();
    }

    /**
     * Forcefully updates the component.
     *
     * @since 2.0
     */
    public void forceUpdate() {
        if(this.model == null || this.index < 0) return;
        this.model.update(this.index);
    }

    /**
     * Tells this element to unmount
     * itself, if possible.
     *
     * @since 2.0
     */
    public final void unmount() {
        if(this.model != null) model.unmount(this.index);
    }

    /**
     * Tells this element to mount
     * another element in its place.
     * <p>
     * This element will be unmounted.
     *
     * @param another Another element.
     * @since 2.0
     */
    public final void swap(final @Nullable GuiComponent<?> another) {
        model.mount(index, another);
    }

    /**
     * Mounts the component on a specified model.
     *
     * @param model the model
     * @param index the index
     * @since 2.0
     */
    @Internal
    final void apply(final @NotNull GuiModel model, final int index) {
        this.model = model;
        this.index = index;
    }

    /**
     * Dismounts the component.
     *
     * @since 2.0
     */
    @Internal
    final void dismount() {
        this.model = null;
        this.index = -1;
    }

    /**
     * Retrieves the value to be rendered.
     * <p>
     * Can be null instead of AIR.
     *
     * @return the item
     * @since 2.0
     */
    @Nullable
    public abstract ItemStack render();

}
