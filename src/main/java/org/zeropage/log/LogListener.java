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
        private int priority;

        /**
         * Constructs a new level with its priority.
         */

        Level(int priority) {
            this.priority = priority;
        }

        /**
         * Returns its priority. Lower priority is more important.
         *
         * @return priority priority of Level.
         */
        public int getPriority() {
            return priority;
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
