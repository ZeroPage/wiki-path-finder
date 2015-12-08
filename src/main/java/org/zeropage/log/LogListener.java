package org.zeropage.log;


/**
 * A class can be listener for Logger when implements LogListener.
 */
public interface LogListener {
    /**
     * Identifies logging level.
     */
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

    /**
     * Logs a message with the debug level.
     *
     * @param message an listener to be added
     */
    void debug(String message);

    /**
     * Logs a message with the info level.
     *
     * @param message an listener to be added
     */
    void info(String message);

    /**
     * Logs a message with the warn level.
     *
     * @param message an listener to be added
     */
    void warn(String message);

    /**
     * Logs a message with the error level.
     *
     * @param message an listener to be added
     */
    void error(String message);

    /**
     * Logs a message with the fatal level.
     *
     * @param message an listener to be added
     */
    void fatal(String message);
}
