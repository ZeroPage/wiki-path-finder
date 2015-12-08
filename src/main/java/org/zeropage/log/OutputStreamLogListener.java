package org.zeropage.log;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamLogListener implements LogListener {
    private OutputStream outputStream;
    @NotNull
    private Level level;

    /**
     * Constructs a logger with its <code>OutputStream</code>.
     *
     * @param outputStream outputStream to log
     */
    public OutputStreamLogListener(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.level = Level.INFO;
    }

    /**
     * Constructs a logger with its <code>OutputStream</code> and level.
     *
     * @param outputStream outputStream to log
     * @param level value for logging level
     */

    public OutputStreamLogListener(OutputStream outputStream, Level level) {
        this.outputStream = outputStream;
        this.level = level;
    }

    /**
     * Sets logging level.
     *
     * @param level new value for logging level
     */
    public void setLevel(Level level) {
        this.level = level;
    }

    /**
     * Logs a message with the debug level.
     *
     * @param message an listener to be added
     */
    @Override
    public void debug(String message) {
        if (Level.DEBUG.getPriority() <= level.getPriority()) {
            print("DEBUG: " + message);
        }

    }

    /**
     * Logs a message with the debug level.
     *
     * @param message an listener to be added
     */

    @Override
    public void info(String message) {
        if (Level.INFO.getPriority() <= level.getPriority()) {
            print("INFO: " + message);
        }
    }

    /**
     * Logs a message with the debug level.
     *
     * @param message an listener to be added
     */
    @Override
    public void warn(String message) {
        if (Level.WARN.getPriority() <= level.getPriority()) {
            print("WARNING: " + message);
        }
    }

    /**
     * Logs a message with the debug level.
     *
     * @param message an listener to be added
     */
    @Override
    public void error(String message) {
        if (Level.ERROR.getPriority() <= level.getPriority()) {
            print("ERROR: " + message);
        }
    }

    /**
     * Logs a message with the debug level.
     *
     * @param message an listener to be added
     */
    @Override
    public void fatal(String message) {
        if (Level.FATAL.getPriority() <= level.getPriority()) {
            print("FATAL: " + message);
        }
    }

    private void print(String message) {
        message = message + System.lineSeparator();
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
