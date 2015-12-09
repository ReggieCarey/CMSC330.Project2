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
        program = "(x + (y * 3)), x = 2, y = 6;";
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
            System.out.println("Output should have been 'Value = 20'");
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
            System.out.println("Output should have been 'Value = 20'");
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
            System.out.println("Output should have been 'Value = 20'");
        } catch (IOException | SyntaxException | ParseException ex) {
            fail("Unexpected exception : " + ex);
        }
    }

}
