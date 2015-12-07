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
        outputStreamLogListener.setLevel(LogListener.Level.DEBUG);
        logger.addListener(outputStreamLogListener);
        logger.debug(successMsg);
    }

    @Test
    public void testRemoveListener() throws Exception {
        OutputStreamLogListener outputStreamLogListener = new OutputStreamLogListener(System.out);
        outputStreamLogListener.setLevel(LogListener.Level.DEBUG);
        logger.addListener(outputStreamLogListener);
        logger.debug(successMsg);
        logger.removeListener(outputStreamLogListener);
        logger.debug(failMsg);
    }

    @Test
    public void testDebug() throws Exception {
        File file = new File("./test");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamLogListener outputStreamLogListener = new OutputStreamLogListener(fileOutputStream, LogListener.Level.DEBUG);
        logger.addListener(outputStreamLogListener);
        logger.debug(successMsg);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String output = bufferedReader.readLine();
        Assert.assertEquals(output, "DEBUG: " + successMsg);
        if(!file.delete()) {
            throw new Exception();
        }
    }

    @Test
    public void testInfo() throws Exception {
        File file = new File("./test");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamLogListener outputStreamLogListener = new OutputStreamLogListener(fileOutputStream, LogListener.Level.DEBUG);
        logger.addListener(outputStreamLogListener);
        logger.info(successMsg);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String output = bufferedReader.readLine();
        Assert.assertEquals(output, "INFO: " + successMsg);
        if(!file.delete()) {
            throw new Exception();
        }
    }

    @Test
    public void testWarn() throws Exception {
        File file = new File("./test");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamLogListener outputStreamLogListener = new OutputStreamLogListener(fileOutputStream, LogListener.Level.DEBUG);
        logger.addListener(outputStreamLogListener);
        logger.warn(successMsg);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String output = bufferedReader.readLine();
        Assert.assertEquals(output, "WARNING: " + successMsg);
        if(!file.delete()) {
            throw new Exception();
        }
    }

    @Test
    public void testError() throws Exception {
        File file = new File("./test");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamLogListener outputStreamLogListener = new OutputStreamLogListener(fileOutputStream, LogListener.Level.DEBUG);
        logger.addListener(outputStreamLogListener);
        logger.error(successMsg);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String output = bufferedReader.readLine();
        Assert.assertEquals(output, "ERROR: " + successMsg);
        if(!file.delete()) {
            throw new Exception();
        }
    }

    @Test
    public void testFatal() throws Exception {
        File file = new File("./test");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamLogListener outputStreamLogListener = new OutputStreamLogListener(fileOutputStream, LogListener.Level.DEBUG);
        logger.addListener(outputStreamLogListener);
        logger.fatal(successMsg);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String output = bufferedReader.readLine();
        Assert.assertEquals(output, "FATAL: " + successMsg);
        if(!file.delete()) {
            throw new Exception();
        }
    }
}
