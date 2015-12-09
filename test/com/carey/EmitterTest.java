/*
 * *****************************************************************************
 * NAME: Reginald B Carey
 * EMPLID: 0316442
 * PROJECT: An Expression Interpreter - Project 2
 * COURSE: CMSC 330 - 7980
 * SECTION: 2158
 * SEMESTER: FALL 2015
 * *****************************************************************************
 */
package com.carey;

import static com.carey.Type.ENDOFLINE;
import static com.carey.Type.LITERAL;
import static com.carey.Type.OP;
import java.util.Collection;
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

        instance.emit(LITERAL, "10");

        Object expected = 1;
        Object actual = parsingStack.size();
        assertEquals("The stack is missing an object", expected, actual);

        instance.emit(LITERAL, "20");
        expected = 2;
        actual = parsingStack.size();
        assertEquals("The stack is missing an object", expected, actual);

        instance.emit(OP, "+");
        expected = 3;
        actual = parsingStack.size();
        assertEquals("The stack is missing an object", expected, actual);

        instance.emit(ENDOFLINE);
        expected = 0;
        actual = parsingStack.size();
        assertEquals("The stack should be empty", expected, actual);
    }

}
