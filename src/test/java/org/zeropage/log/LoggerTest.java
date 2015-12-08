package org.zeropage.log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class LoggerTest {
    private final String successMsg = "success";
    private final String failMsg = "fail";
    private Logger logger;
    private OutputStreamLogListener outputStreamLogListener;
    private FileOutputStream fileOutputStream;
    private File file;

    @Before
    public void setUp() throws Exception {
        logger = Logger.getInstance();
        file = new File("./test");
        file.deleteOnExit();
    }

    @Test
    public void testGetInstance() throws Exception {
        Assert.assertNotNull(logger);
    }

    @Test
    public void testAddAndRemoveListener() throws Exception {
        outputStreamLogListener = new OutputStreamLogListener(System.out);
        logger.addListener(outputStreamLogListener);
        logger.info(successMsg);
        logger.removeListener(outputStreamLogListener);
        logger.info(failMsg);
    }

    @Test
    public void testDebug() throws Exception {
        String prefix = "DEBUG: ";

        addTestLogListener();
        logger.debug(successMsg);
        Assert.assertEquals(getLogContents(), prefix + successMsg);
        removeTestLogListener();
    }

    @Test
    public void testInfo() throws Exception {
        String prefix = "INFO: ";

        addTestLogListener();
        logger.info(successMsg);
        Assert.assertEquals(getLogContents(), prefix + successMsg);
        removeTestLogListener();
    }

    @Test
    public void testWarn() throws Exception {
        String prefix = "WARNING: ";

        addTestLogListener();
        logger.warn(successMsg);
        Assert.assertEquals(getLogContents(), prefix + successMsg);
        removeTestLogListener();
    }

    @Test
    public void testError() throws Exception {
        String prefix = "ERROR: ";

        addTestLogListener();
        logger.error(successMsg);
        Assert.assertEquals(getLogContents(), prefix + successMsg);
        removeTestLogListener();
    }

    @Test
    public void testFatal() throws Exception {
        String prefix = "FATAL: ";

        addTestLogListener();
        logger.fatal(successMsg);
        Assert.assertEquals(getLogContents(), prefix + successMsg);
        removeTestLogListener();
    }

    private void addTestLogListener() throws FileNotFoundException {
        fileOutputStream = new FileOutputStream(file);
        outputStreamLogListener = new OutputStreamLogListener(fileOutputStream, LogListener.Level.DEBUG);
        logger.addListener(outputStreamLogListener);
    }

    private String getLogContents() throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String contents = bufferedReader.readLine();
        bufferedReader.close();

        return contents;
    }

    private void removeTestLogListener() throws IOException {
        logger.removeListener(outputStreamLogListener);
        fileOutputStream.close();
    }
}
