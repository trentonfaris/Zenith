package com.trentonfaris.zenith.input;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.input.KeyButtonInput.KeyButtonType;
import com.trentonfaris.zenith.input.MouseMovementAxis.MouseAxis;
import org.apache.logging.log4j.Level;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Input {
    /**
     * The {@link KeyboardCallback} used by this {@link Input}.
     */
    private final KeyboardCallback keyboardCallback = new KeyboardCallback();

    /**
     * The {@link MouseButtonCallback} used by this {@link Input}.
     */
    private final MouseButtonCallback mouseButtonCallback = new MouseButtonCallback();

    /**
     * The {@link ScrollCallback} used by this {@link Input}.
     */
    private final ScrollCallback scrollCallback = new ScrollCallback();

    /**
     * The {@link CursorPosCallback} used by this {@link Input}.
     */
    private final CursorPosCallback cursorPosCallback = new CursorPosCallback();

    /**
     * A map of axes.
     */
    private final Map<String, Axis> axes = new HashMap<>();

    /**
     * A list of flags indicating which keyboard keys are pressed.
     */
    private boolean[] keys;

    /**
     * A list of flags indicating which keyboard keys are pressed for the first time
     * this frame.
     */
    private boolean[] keysDown;

    /**
     * A list of flags indicating which mouse buttons are pressed.
     */
    private boolean[] buttons;

    /**
     * A list of flags indicating which mouse buttons are pressed for the first time
     * this frame.
     */
    private boolean[] buttonsDown;

    /**
     * The position of the mouse in the previous frame.
     */
    private Vector2d prevMousePosition = new Vector2d();

    /**
     * The position of the mouse in the current frame.
     */
    private Vector2d mousePosition = new Vector2d();

    public void init() {
        Zenith.getLogger().log(Level.INFO, "Input initializing...");

        // TODO : This is problematic. It assumes Window has been initialized with no checks to verify.
        long handle = Zenith.getEngine().getWindow().getHandle();

        GLFW.glfwSetKeyCallback(handle, keyboardCallback);
        GLFW.glfwSetMouseButtonCallback(handle, mouseButtonCallback);
        GLFW.glfwSetScrollCallback(handle, scrollCallback);
        GLFW.glfwSetCursorPosCallback(handle, cursorPosCallback);

        this.keys = keyboardCallback.getKeys();
        this.keysDown = Arrays.copyOf(keys, keys.length);

        this.buttons = mouseButtonCallback.getButtons();
        this.buttonsDown = Arrays.copyOf(buttons, buttons.length);
    }

    public void update() {
        GLFW.glfwPollEvents();

        updateKeys();
        updateButtons();

        prevMousePosition.set(mousePosition);
        mousePosition.set(cursorPosCallback.getPosition());
    }

    /**
     * Updates the {@link #keys} and {@link #keysDown} arrays from the
     * {@link KeyboardCallback}.
     */
    private void updateKeys() {
        boolean[] prevKeys = keys;
        this.keys = keyboardCallback.getKeys();

        Arrays.fill(keysDown, false);
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] != prevKeys[i] && keys[i]) {
                keysDown[i] = true;
            }
        }
    }

    /**
     * Updates the {@link #buttons} and {@link #buttonsDown} arrays from the
     * {@link MouseButtonCallback}.
     */
    private void updateButtons() {
        boolean[] prevButtons = buttons;
        this.buttons = mouseButtonCallback.getButtons();

        Arrays.fill(buttonsDown, false);
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] != prevButtons[i] && buttons[i]) {
                buttonsDown[i] = true;
            }
        }
    }

    /**
     * Registers an {@link Axis} with this {@link Input}. If an association for the
     * {@link Axis} name already exists, it will be overridden.
     *
     * @param axis
     */
    public void registerAxis(Axis axis) {
        if (axis == null) {
            Zenith.getLogger().warn("Cannot register a null Axis.");
            return;
        }

        Zenith.getLogger().log(Level.INFO, "Registering input axis: " + axis.getName());

        axes.put(axis.getName(), axis);
    }

    /**
     * Removes an {@link Axis} from this {@link Input}, if it has been registered.
     *
     * @param name
     */
    public void removeAxis(String name) {
        if (name == null) {
            Zenith.getLogger().warn("Cannot remove a null Axis.");
            return;
        }

        axes.remove(name);
    }

    /**
     * Gets the value of the specified {@link Axis}. Unregistered axes return a
     * value of 0.
     *
     * @param name
     * @return The value of the specified {@link Axis}.
     */
    public float getAxis(String name) {
        if (name == null) {
            Zenith.getLogger().warn("Cannot get the value of a null Axis.");
            return 0;
        }

        float result = 0;

        Axis axis = axes.get(name);
        if (axis instanceof KeyButtonAxis keyButtonAxis) {

            KeyButtonInput positive = keyButtonAxis.getPositive();
            int positiveValue = positive.getValue();
            if ((positive.getKeyButtonType() == KeyButtonType.KEY && isKey(positiveValue))
                    || (positive.getKeyButtonType() == KeyButtonType.BUTTON && isButton(positiveValue))) {
                result++;
            }

            KeyButtonInput negative = keyButtonAxis.getNegative();
            int negativeValue = negative.getValue();
            if ((negative.getKeyButtonType() == KeyButtonType.KEY && isKey(negativeValue))
                    || (negative.getKeyButtonType() == KeyButtonType.BUTTON && isButton(negativeValue))) {
                result--;
            }
        } else if (axis instanceof MouseMovementAxis mouseMovementAxis) {

            if (mouseMovementAxis.getMouseAxis() == MouseAxis.X_AXIS) {
                result += (mousePosition.x - prevMousePosition.x) * mouseMovementAxis.getSensitivity();
            } else if (mouseMovementAxis.getMouseAxis() == MouseAxis.Y_AXIS) {
                result += (mousePosition.y - prevMousePosition.y) * mouseMovementAxis.getSensitivity();
            }
        }

        return result;
    }

    /**
     * Gets the flag indicating if the specified keyboard key is pressed.
     *
     * @return A flag indicating if the specified keyboard key is pressed.
     */
    public boolean isKey(int key) {
        if (key < 0 || key >= keys.length) {
            return false;
        }

        return keys[key];
    }

    /**
     * Gets the flag indicating if the specified keyboard key is pressed for the
     * first time this frame.
     *
     * @return A flag indicating if the specified keyboard key is pressed for the
     * first time this frame.
     */
    public boolean isKeyDown(int key) {
        if (key < 0 || key >= keysDown.length) {
            return false;
        }

        return keysDown[key];
    }

    /**
     * Gets the flag indicating if the specified mouse button is pressed.
     *
     * @return A flag indicating if the specified mouse button is pressed.
     */
    public boolean isButton(int button) {
        if (button < 0 || button > GLFW.GLFW_MOUSE_BUTTON_LAST) {
            return false;
        }

        return buttons[button];
    }

    /**
     * Gets the flag indicating if the specified mouse button is pressed for the
     * first time this frame.
     *
     * @return A flag indicating if the specified mouse button is pressed for the
     * first time this frame.
     */
    public boolean isButtonDown(int button) {
        if (button < 0 || button > GLFW.GLFW_MOUSE_BUTTON_LAST) {
            return false;
        }

        return buttonsDown[button];
    }
}
