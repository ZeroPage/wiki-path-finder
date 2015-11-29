import log.Logger;
import log.OutputStreamLogListener;
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
        logger.addListener(new OutputStreamLogListener(System.out));
        logger.error("Test error message");
    }

    // TODO: write down more test code
}
