package log;

import log.LogListener;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamLogListener implements LogListener {
    private OutputStream outputStream;

    public OutputStreamLogListener(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void debug(String message) {
        print("DEBUG: " + message);
    }

    @Override
    public void info(String message) {
        print("INFO: " + message);
    }

    @Override
    public void warn(String message) {
        print("WARNING: " + message);
    }

    @Override
    public void error(String message) {
        print("ERROR: " + message);
    }

    @Override
    public void fatal(String message) {
        print("FATAL: " + message);
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
