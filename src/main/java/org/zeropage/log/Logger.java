package org.zeropage.log;

import java.util.ArrayList;

/**
 * A Logger object can log messages with specific level.
 */
public class Logger {
    private volatile static Logger instance;
    private ArrayList<LogListener> listeners;
    private Logger() {
        listeners = new ArrayList<>();
    }

    /**
     * Returns object of logger.
     *
     * @return object of logger
     */
    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    /**
     * Adds listener to the set of listener.
     *
     * @param listener an listener to be added
     */
    public void addListener(LogListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener to be added is null");
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Removes listener from the set of listener.
     *
     * @param listener an listener to be removed
     */
    public void removeListener(LogListener listener) {
        listeners.remove(listener);
    }

    /**
     * Logs a message with the debug level.
     *
     * @param message a string to be logged
     */
    public void debug(String message) {
        for (LogListener listener : listeners) {
            listener.debug(message);
        }

    }

    /**
     * Logs a message with the info level.
     *
     * @param message a string to be logged
     */
    public void info(String message) {
        for (LogListener listener : listeners) {
            listener.info(message);
        }
    }

    /**
     * Logs a message with the warn level.
     *
     * @param message a string to be logged
     */
    public void warn(String message) {
        for (LogListener listener : listeners) {
            listener.warn(message);
        }
    }

    /**
     * Logs a message with the error level.
     *
     * @param message a string to be logged
     */
    public void error(String message) {
        for (LogListener listener : listeners) {
            listener.error(message);
        }
    }

    /**
     * Logs a message with the fatal level.
     *
     * @param message a string to be logged
     */
    public void fatal(String message) {
        for (LogListener listener : listeners) {
            listener.fatal(message);
        }
    }
}
