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

import static com.carey.Type.BEGINOFINPUT;
import static com.carey.Type.LAYOUT;
import static com.carey.Type.STRING;
import static com.carey.Type.WINDOW;
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
        String testProgram = "             \n"
                + "Window \"Calculator\" (300, 300) Layout Flow:\n"
                + "  Textfield 20;\n"
                + "  Panel Layout Grid(2,1):\n"
                + "    Panel Layout Flow: "
                + "      Group\n"
                + "        Radio \"Basic\";\n"
                + "        Radio \"Scientific\";\n"
                + "        Radio Window \"Programmer\";\n"
                + "      End;\n"
                + "    End;\n"
                + "    Label \"CMSC 330 - Reggie Carey\";\n"
                + "  End;\n"
                + "  Panel Layout Grid(5, 4, 5, 5):\n"
                + "    Button \"AC\";\n"
                + "    Button \"+/-\";\n"
                + "    Button \"%\";\n"
                + "    Button \"/\";\n"
                + "    \n"
                + "    Button \"7\";\n"
                + "    Button \"8\";\n"
                + "    Button \"9\";\n"
                + "    Button \"*\";\n"
                + "    \n"
                + "    Button \"4\";\n"
                + "    Button \"5\";\n"
                + "    Button \"6\";\n"
                + "    Button \"-\";\n"
                + "    \n"
                + "    Button \"1\";\n"
                + "    Button \"2\";\n"
                + "    Button \"3\";\n"
                + "    Button \"+\";\n"
                + "    \n"
                + "    Button \"0\";\n"
                + "    Label \"\";\n"
                + "    Button \".\";\n"
                + "    Button \"=\";\n"
                + "  End;\n"
                + "End.";
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

            instance.advance(); // Window
            Token expResult = new Token(WINDOW, "Window", 0, 0);
            Token result = instance.getCurrentToken();
            assertEquals("Should have had a WINDOW after 1 advance", expResult, result);

            instance.advance(); // "Calculator"
            expResult = new Token(STRING, "Calculator", 0, 0);
            result = instance.getCurrentToken();
            assertEquals("Should have had a CALCULATOR after 2nd advance", expResult, result);

            instance.advance(); // (
            instance.advance(); // 300
            instance.advance(); // ,
            instance.advance(); // 300
            instance.advance(); // )
            instance.advance(); // Layout
            expResult = new Token(LAYOUT, "Layout", 0, 0);
            result = instance.getCurrentToken();
            assertEquals("Should have had a LAYOUT after 5 additional advances", expResult, result);
        } catch (IOException | SyntaxException ex) {
            fail("Unexpected exception : " + ex);
        }
    }

}
