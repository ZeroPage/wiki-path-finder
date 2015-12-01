package log;

public interface LogListener {
    enum Level { FATAL, ERROR, WARN, INFO, DEBUG }

    void debug(String message);
    void info(String message);
    void warn(String message);
    void error(String message);
    void fatal(String message);
}
