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

import static com.carey.Type.STRING;
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
public class TokenTest {

    public TokenTest() {
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
     * Test of getContent method, of class Token.
     */
    @Test
    public void testGetContent() {
        System.out.println("getContent");
        Token instance = new Token(STRING, "The world at your fingers", 10, 20);
        String expResult = "The world at your fingers";
        String result = instance.getContent();
        assertEquals(expResult, result);
    }

    /**
     * Test of getType method, of class Token.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        Token instance = new Token(STRING, "The world at your fingers", 10, 20);
        Type expResult = STRING;
        Type result = instance.getType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLineNumber method, of class Token.
     */
    @Test
    public void testGetLineNumber() {
        System.out.println("getLineNumber");
        Token instance = new Token(STRING, "The world at your fingers", 10, 20);
        int expResult = 10;
        int result = instance.getLineNumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of getOffset method, of class Token.
     */
    @Test
    public void testGetOffset() {
        System.out.println("getOffset");
        Token instance = new Token(STRING, "The world at your fingers", 10, 20);
        int expResult = 20;
        int result = instance.getOffset();
        assertEquals(expResult, result);
    }

}
