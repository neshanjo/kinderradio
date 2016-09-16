/* 
 * Copyright (C) 2016 Johannes C. Schneider
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.neshanjo.kinderradio.gpio;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
import static de.neshanjo.kinderradio.config.Configuration.CONFIG;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 *
 * @author Johannes C. Schneider
 */
public final class GpioFacade {

    private static final Logger LOG = Logger.getLogger(GpioFacade.class.getName());
    private static GpioFacade INSTANCE;
    
    public static synchronized GpioFacade getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GpioFacade();
        }
        return INSTANCE;
    }

    private final GpioController gpio;
    private final GpioPinDigitalOutput ledGreen, ledYellow, ledRed;
    private final GpioPinDigitalInput myButton[];
    private Callable<Void>[] pressAgainCallback;
    private volatile long lastCallTime = System.currentTimeMillis() - CONFIG.getPlayerConfig().getBusyTimeInMs();
    private volatile int lastButtonNr = -1;

    private GpioFacade() {
        LOG.fine("Init GPIO");
        gpio = GpioFactory.getInstance();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.fine("Shutdown GPIO");
            gpio.shutdown();
        }));

        ledGreen = gpio.provisionDigitalOutputPin(Wiring.LED_GREEN, "LED GREEN", PinState.LOW);
        ledGreen.setState(PinState.HIGH);
        ledYellow = gpio.provisionDigitalOutputPin(Wiring.LED_YELLOW, "LED YELLOW", PinState.LOW);
        ledRed = gpio.provisionDigitalOutputPin(Wiring.LED_RED, "LED RED", PinState.LOW);

        ledGreen.setShutdownOptions(true, PinState.LOW);
        ledYellow.setShutdownOptions(true, PinState.LOW);
        ledRed.setShutdownOptions(true, PinState.LOW);

        myButton = new GpioPinDigitalInput[]{
            gpio.provisionDigitalInputPin(Wiring.BUTTON_ONE, "BUTTON ONE", PinPullResistance.PULL_DOWN),
            gpio.provisionDigitalInputPin(Wiring.BUTTON_TWO, "BUTTON TWO", PinPullResistance.PULL_DOWN),
            gpio.provisionDigitalInputPin(Wiring.BUTTON_THREE, "BUTTON THREE", PinPullResistance.PULL_DOWN),
            gpio.provisionDigitalInputPin(Wiring.BUTTON_FOUR, "BUTTON FOUR", PinPullResistance.PULL_DOWN),
            gpio.provisionDigitalInputPin(Wiring.BUTTON_FIVE, "BUTTON FIVE", PinPullResistance.PULL_DOWN)
        };
        
        initPressAgainCallbacks();

        for (GpioPinDigitalInput myButton1 : myButton) {
            myButton1.setShutdownOptions(true);
        }
    }

    @SuppressWarnings("unchecked")
    private void initPressAgainCallbacks() {
        pressAgainCallback = new Callable[myButton.length];
    }

    public void addButtonCallback(int buttonNr, Callable<Void> callback) {
        Callable<Void> wrappingCallback = () -> {
            if (System.currentTimeMillis() - lastCallTime < CONFIG.getPlayerConfig().getBusyTimeInMs()) {
                LOG.finer("Ignored a button press within busy time.");
                return null;
            } else {
                synchronized (this) {
                    if (System.currentTimeMillis() - lastCallTime > CONFIG.getPlayerConfig().getBusyTimeInMs()) {
                        lastCallTime = System.currentTimeMillis();
                        ledGreen.pulse(CONFIG.getPlayerConfig().getBusyTimeInMs(), PinState.LOW);
                        GpioPinDigitalOutput led = (buttonNr == 4) ? ledRed : ledYellow;
                        led.pulse(CONFIG.getPlayerConfig().getBusyTimeInMs(), PinState.HIGH);
                        if (pressAgainCallback[buttonNr] != null && lastButtonNr == buttonNr) {
                            pressAgainCallback[buttonNr].call();
                        } else {
                            callback.call();
                            lastButtonNr = buttonNr;
                        }
                    } else {
                        LOG.finer("Ignored a button press within busy time.");
                    }
                }
            }
            return null;
        };
        myButton[buttonNr].addTrigger(new GpioCallbackTrigger(PinState.HIGH, wrappingCallback));
    }
    
    public void setButtonPressAgainCallback(int buttonNr, Callable<Void> callback) {
        pressAgainCallback[buttonNr] = callback;
    }
    
    public void addReleaseAfterLongPressButtonCallback(int buttonNr, Callable<Void> callback) {
        myButton[buttonNr].addTrigger(new GpioCallbackTrigger(PinState.LOW, () -> {
            if (buttonNr == lastButtonNr && System.currentTimeMillis() - lastCallTime >= CONFIG.getPlayerConfig().getLongPressTimeInMs()) {
                ledRed.blink(250, 1400);
                callback.call();
            }
            return null;
        }));
    }
}
