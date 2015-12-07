package org.zeropage.log;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamLogListener implements LogListener {
    private OutputStream outputStream;
    private Level level;

    public OutputStreamLogListener(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.level = Level.INFO;
    }

    public OutputStreamLogListener(OutputStream outputStream, Level level) {
        this.outputStream = outputStream;
        this.level = level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public void debug(String message) {
        if (Level.DEBUG.getValue() <= level.getValue()) {
            print("DEBUG: " + message);
        }

    }

    @Override
    public void info(String message) {
        if (Level.INFO.getValue() <= level.getValue()) {
            print("INFO: " + message);
        }
    }

    @Override
    public void warn(String message) {
        if (Level.WARN.getValue() <= level.getValue()) {
            print("WARNING: " + message);
        }
    }

    @Override
    public void error(String message) {
        if (Level.ERROR.getValue() <= level.getValue()) {
            print("ERROR: " + message);
        }
    }

    @Override
    public void fatal(String message) {
        if (Level.FATAL.getValue() <= level.getValue()) {
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
