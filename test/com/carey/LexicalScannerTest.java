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

import static com.carey.Type.BEGINOFINPUT;
import static com.carey.Type.LPAREN;
import static com.carey.Type.RPAREN;
import static com.carey.Type.VARIABLE;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ReginaldCarey
 */
public class LexicalScannerTest {

    private Reader reader;

    public LexicalScannerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        String testProgram = "(x + (y * 3)), x = 2, y = 6;";
        reader = new StringReader(testProgram);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getCurrentToken method, of class LexicalScanner.
     */
    @Test
    public void testGetCurrentToken() {
        try {
            System.out.println("getCurrentToken");
            LexicalScanner instance = new LexicalScanner(reader);
            Token expResult = new Token(BEGINOFINPUT, null, 0, 0);
            Token result = instance.getCurrentToken();
            assertEquals("Should have had a BEGINOFINPUT as first token", expResult, result);
        } catch (Exception ex) {
            fail("Unexpected exception : " + ex);
        }
    }

    /**
     * Test of advance method, of class LexicalScanner.
     */
    @Test
    public void testAdvance() {
        try {
            System.out.println("advance");
            LexicalScanner instance = new LexicalScanner(reader);

            instance.advance(); // (
            Token expResult = new Token(LPAREN, "(", 0, 0);
            Token result = instance.getCurrentToken();
            assertEquals("Should have had a LPAREN after 1 advance", expResult, result);

            instance.advance(); // "x"
            expResult = new Token(VARIABLE, "x", 0, 0);
            result = instance.getCurrentToken();
            assertEquals("Should have had a VARIABLE after 2nd advance", expResult, result);

            instance.advance(); // +
            instance.advance(); // (
            instance.advance(); // y
            instance.advance(); // *
            instance.advance(); // 3
            instance.advance(); // )
            expResult = new Token(RPAREN, ")", 0, 0);
            result = instance.getCurrentToken();
            assertEquals("Should have had a RPAREN after 5 additional advances", expResult, result);
        } catch (IOException | SyntaxException ex) {
            fail("Unexpected exception : " + ex);
        }
    }

}
