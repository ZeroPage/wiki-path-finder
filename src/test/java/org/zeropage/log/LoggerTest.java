package org.zeropage.log;

import org.zeropage.log.LogListener;
import org.zeropage.log.Logger;
import org.zeropage.log.OutputStreamLogListener;
import org.junit.Before;
import org.junit.Test;

public class LoggerTest {
    Logger logger;

    @Before
    public void setUp() {
        logger = Logger.getInstance();
    }

    @Test
    public void testPrintLogger() throws Exception {
        OutputStreamLogListener outputStreamLogListener = new OutputStreamLogListener(System.out);
        outputStreamLogListener.setLevel(LogListener.Level.DEBUG);
        logger.addListener(outputStreamLogListener);

        logger.error("Test error message");
    }

    // TODO: write down more test code
}
