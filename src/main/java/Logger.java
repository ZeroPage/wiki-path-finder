import java.util.ArrayList;

public class Logger {
    private volatile static Logger instance;
    private ArrayList<LogListener> listeners;

    private Logger() {
        listeners = new ArrayList<>();
    }

    public static Logger getInstance() {
        if(instance == null) {
            synchronized (Logger.class) {
                if(instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    public void removeListener(LogListener listener) {
        listeners.remove(listener);
    }

    public void addListener(LogListener listener) {
        listeners.add(listener);
    }

    public void debug(String message) {
        for (LogListener listener : listeners) {
            listener.debug(message);
        }

    }

    public void info(String message) {
        for (LogListener listener : listeners) {
            listener.info(message);
        }
    }

    public void warn(String message) {
        for (LogListener listener : listeners) {
            listener.warn(message);
        }
    }

    public void error(String message) {
        for (LogListener listener : listeners) {
            listener.error(message);
        }
    }

    public void fatal(String message) {
        for (LogListener listener : listeners) {
            listener.fatal(message);
        }
    }
}
