package dev.hawu.plugins.api.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Represents the base element of the GUI.
 *
 * @param <T> The type of the state.
 * @since 1.2
 */
public abstract class GuiElement<T> {

    private Map<String, Object> prevProps = new HashMap<>();
    private T prevState;

    private Map<String, Object> props;
    private T state;

    private boolean firstMount = true;
    private GuiModel model;
    private int slot = -1;

    /**
     * Constructs a quick element using an empty map
     * for props.
     *
     * @since 1.2
     */
    public GuiElement() {
        this(new HashMap<>());
    }

    /**
     * Constructs a gui element with an initial state.
     *
     * @param initial The initial state.
     * @since 1.2
     */
    public GuiElement(final @Nullable T initial) {
        this(new HashMap<>());
        this.state = initial;
    }

    /**
     * Constructs a quick element using the given props.
     *
     * @param props The props to use.
     * @since 1.2
     */
    public GuiElement(@NotNull Map<@NotNull String, @Nullable Object> props) {
        this.props = props;
    }

    /**
     * Creates a new gui element with a static item stack.
     *
     * @param itemStack The item stack.
     * @param <T>       The type of the gui element.
     * @return The gui element.
     * @since 1.2
     */
    @NotNull
    public static <T> GuiElement<T> createStaticElement(final @Nullable ItemStack itemStack) {
        return new GuiElement<T>() {
            @Override
            public @Nullable ItemStack render() {
                return itemStack;
            }
        };
    }

    /**
     * This function is called whenever the props
     * or the state of this element mutate.
     * <p>
     * If the function returns false, the element will
     * not re-render. But the state and props will still change.
     *
     * @param nextProps The new props.
     * @param nextState The new state.
     * @return True if the element should update, false otherwise.
     * @since 1.2
     */
    public boolean shouldElementUpdate(final @NotNull Map<@NotNull String, @Nullable Object> nextProps, final @Nullable T nextState) {
        return true;
    }

    /**
     * This function is called once the element is mounted
     * the first time.
     *
     * @since 1.2
     */
    public void elementDidMount() {
    }

    /**
     * This function is called once the element has updated, after
     * the element has re-rendered.
     *
     * @param prevProps The previous props.
     * @param prevState The previous state.
     * @since 1.2
     */
    public void elementDidUpdate(final @NotNull Map<@NotNull String, @Nullable Object> prevProps, final @Nullable T prevState) {
    }

    /**
     * This function is called once the element's re-rendering
     * has thrown something to handle the error.
     *
     * @param exception The throwable caught.
     * @since 1.2
     */
    public void elementDidCatch(final @NotNull Exception exception) {
        exception.printStackTrace();
    }

    /**
     * This function is called once the component is unmounted,
     * and the element is no longer in the GUI.
     *
     * @since 1.2
     */
    public void elementWillUnmount() {
    }

    /**
     * This function is called once for every time the player
     * has clicked on this element, if mounted.
     *
     * @param event The event passed in.
     * @since 1.2
     */
    public void handleClick(final @NotNull InventoryClickEvent event) {
        event.setCancelled(true);
    }

    /**
     * Retrieves the state of this element.
     *
     * @return The current state.
     * @since 1.2
     */
    @Nullable
    public final T getState() {
        return this.state;
    }

    /**
     * Sets the state of the element.
     *
     * @param updater The function to generate the state.
     * @since 1.2
     */
    public final void setState(final @NotNull BiFunction<@Nullable T, @NotNull Map<@NotNull String, @Nullable Object>, @Nullable T> updater) {
        final T newState = updater.apply(this.state, this.props);
        this.prevState = this.state;
        this.state = updater.apply(this.state, this.props);
        if(shouldElementUpdate(props, newState)) {
            forceUpdate();
        }
    }

    /**
     * Sets the state of the element.
     *
     * @param newState The new state to set to.
     * @since 1.2
     */
    public final void setState(final @Nullable T newState) {
        this.prevState = this.state;
        this.state = newState;
        if(shouldElementUpdate(props, newState)) {
            forceUpdate();
        }
    }

    /**
     * Retrieves the map of this element.
     *
     * @return The current props.
     * @since 1.2
     */
    @NotNull
    @UnmodifiableView
    public final Map<@NotNull String, @Nullable Object> getProps() {
        return Collections.unmodifiableMap(this.props);
    }

    /**
     * Sets the props of this element.
     *
     * @param props The props to set to.
     * @since 1.2
     */
    public final void setProps(final @NotNull Map<@NotNull String, @Nullable Object> props) {
        this.prevProps = this.props;
        this.props = props;
        if(shouldElementUpdate(props, this.state)) {
            forceUpdate();
        }
    }

    /**
     * Force updates and re-renders this element,
     * whether state and props changed or not.
     *
     * @since 1.2
     */
    public final void forceUpdate() {
        if(this.model != null) {
            model.update(slot);
        }
        elementDidUpdate(prevProps, prevState);
    }

    void mount(final @NotNull GuiModel model, final int slot) {
        this.model = model;
        this.slot = slot;

        if(firstMount) {
            elementDidMount();
        }

        firstMount = false;
    }

    /**
     * Generates an {@link ItemStack} to put on the
     * inventory model.
     *
     * @return The generated item.
     * @since 1.2
     */
    @Nullable
    public abstract ItemStack render();

}
