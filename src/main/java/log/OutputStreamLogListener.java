package log;

import log.LogListener;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamLogListener implements LogListener {
    OutputStream outputStream;

    public OutputStreamLogListener(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void debug(String message) {
        message = "DEBUG: " + message;
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void info(String message) {
        message = "INFO: " + message;
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void warn(String message) {
        message = "WARNING: " + message;
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void error(String message) {
        message = "ERROR: " + message;
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fatal(String message) {
        message = "FATAL: " + message;
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
