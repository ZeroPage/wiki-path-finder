package org.zeropage.log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class LoggerTest {
    final String successMsg = "success";
    final String failMsg = "fail";
    Logger logger;

    @Before
    public void setUp() throws Exception {
        logger = Logger.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        Assert.assertNotNull(logger);
    }

    @Test
    public void testAddListener() throws Exception {
        OutputStreamLogListener outputStreamLogListener = new OutputStreamLogListener(System.out);
        logger.addListener(outputStreamLogListener);
        logger.info(successMsg);
    }

    @Test
    public void testRemoveListener() throws Exception {
        OutputStreamLogListener outputStreamLogListener = new OutputStreamLogListener(System.out);
        logger.addListener(outputStreamLogListener);
        logger.info(successMsg);
        logger.removeListener(outputStreamLogListener);
        logger.info(failMsg);
    }

    @Test
    public void testDebug() throws Exception {
        String prefix = "DEBUG: ";
        File file = new File("./test");

        addTestLogListner(file);
        logger.debug(successMsg);
        Assert.assertEquals(getLogContents(file), prefix + successMsg);
    }

    @Test
    public void testInfo() throws Exception {
        String prefix = "INFO: ";
        File file = new File("./test");

        addTestLogListner(file);
        logger.info(successMsg);
        Assert.assertEquals(getLogContents(file), prefix + successMsg);
    }

    @Test
    public void testWarn() throws Exception {
        String prefix = "WARNING: ";
        File file = new File("./test");

        addTestLogListner(file);
        logger.warn(successMsg);
        Assert.assertEquals(getLogContents(file), prefix + successMsg);
    }

    @Test
    public void testError() throws Exception {
        String prefix = "ERROR: ";
        File file = new File("./test");

        addTestLogListner(file);
        logger.error(successMsg);
        Assert.assertEquals(getLogContents(file), prefix + successMsg);
    }

    @Test
    public void testFatal() throws Exception {
        String prefix = "FATAL: ";
        File file = new File("./test");

        addTestLogListner(file);
        logger.fatal(successMsg);
        Assert.assertEquals(getLogContents(file), prefix + successMsg);
    }

    private void addTestLogListner(File file) throws FileNotFoundException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamLogListener outputStreamLogListener = new OutputStreamLogListener(fileOutputStream, LogListener.Level.DEBUG);
        logger.addListener(outputStreamLogListener);
    }

    private String getLogContents(File file) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String output = bufferedReader.readLine();
        if(!file.delete()) {
            throw new Exception();
        }
        return output;
    }

}
