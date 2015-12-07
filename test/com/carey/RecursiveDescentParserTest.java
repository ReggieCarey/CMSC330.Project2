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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ReginaldCarey
 */
public class RecursiveDescentParserTest {

    private String program;

    public RecursiveDescentParserTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        program = "             \n"
                + "Window \"Calculator\" (300, 300) Layout Flow:\n"
                + "  Textfield 20;\n"
                + "  Panel Layout Grid(2,1):\n"
                + "    Panel Layout Flow: "
                + "      Group\n"
                + "        Radio \"Basic\";\n"
                + "        Radio \"Scientific\";\n"
                + "        Radio \"Programmer\";\n"
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
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of parse method, of class RecursiveDescentParser.
     */
    @Test
    public void testParse_Reader() {
        System.out.println("parse");
        Reader stream = new StringReader(program);
        RecursiveDescentParser instance = new RecursiveDescentParser();
        try {
            instance.parse(stream);
            System.out.println("A GUI WAS GENERATED");
        } catch (IOException | SyntaxException | ParseException ex) {
            fail("Unexpected exception : " + ex);
        }
    }

    /**
     * Test of parse method, of class RecursiveDescentParser.
     */
    @Test
    public void testParse_String() {
        System.out.println("parse");
        String content = program;
        RecursiveDescentParser instance = new RecursiveDescentParser();
        try {
            instance.parse(content);
            System.out.println("A GUI WAS GENERATED");
        } catch (IOException | SyntaxException | ParseException ex) {
            fail("Unexpected exception : " + ex);
        }
    }

    /**
     * Test of parse method, of class RecursiveDescentParser.
     * @throws java.io.IOException
     */
    @Test
    public void testParse_File() throws IOException {
        System.out.println("parse");

        File file = File.createTempFile("unitTest", "unitTest");
        try (FileWriter fw = new FileWriter(file)) {
            fw.append(program);
        }

        RecursiveDescentParser instance = new RecursiveDescentParser();
        try {
            instance.parse(file);
            System.out.println("A GUI WAS GENERATED");
        } catch (IOException | SyntaxException | ParseException ex) {
            fail("Unexpected exception : " + ex);
        }
    }

}
