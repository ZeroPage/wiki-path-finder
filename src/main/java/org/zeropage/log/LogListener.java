package org.zeropage.log;

public interface LogListener {
    enum Level {
        FATAL(1), ERROR(2), WARN(3), INFO(4), DEBUG(5);
        private int value;

        Level(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    void debug(String message);

    void info(String message);

    void warn(String message);

    void error(String message);

    void fatal(String message);
}
