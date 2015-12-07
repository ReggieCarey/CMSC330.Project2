/*
 * *****************************************************************************
 * NAME: Reginald B Carey
 * EMPLID: 0316442
 * PROJECT: Recursive Descent Parser - Project 1
 * COURSE: CMSC 330 - 7980
 * SECTION: 2158
 * SEMESTER: FALL 2015
 * *****************************************************************************
 */
package com.carey;

import static com.carey.Type.ENDWINDOW;
import static com.carey.Type.WINDOW;
import java.util.Collection;
import javax.swing.JFrame;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ReginaldCarey
 */
public class EmitterTest {

    public EmitterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of emit method, of class Emitter.
     * @throws java.lang.Exception
     */
    @Test
    public void testEmit() throws Exception {
        System.out.println("emit");

        Emitter instance = new Emitter();
        Collection parsingStack = instance.getParsingStack();

        Type cmd = WINDOW;
        Object[] args = {"Goober", 42, 16};
        instance.emit(cmd, args);

        Object expected = 1;
        Object actual = parsingStack.size();
        assertEquals("The stack is missing an object", expected, actual);

        expected = JFrame.class;
        actual = parsingStack.toArray()[0].getClass();
        assertEquals("The stack should show a jFrame", expected, actual);

        cmd = ENDWINDOW;
        instance.emit(cmd);

        expected = 0;
        actual = parsingStack.size();
        assertEquals("The stack should be empty", expected, actual);
    }

}
