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

import static com.carey.Type.LITERAL;
import static com.carey.Type.VARIABLE;
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
public class TypeTest {

    public TypeTest() {
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
     * Test of getMatcher method, of class Type.
     */
    @Test
    public void testGetMatcher() {
        System.out.println("getMatcher");
        CharSequence charSequence = "Window to the stars";
        Type instance = VARIABLE;
        boolean expResult = true;
        boolean result = instance.getMatcher(charSequence).lookingAt();
        assertEquals(expResult, result);

        charSequence = "123 to the stars";
        instance = LITERAL;
        expResult = true;
        result = instance.getMatcher(charSequence).lookingAt();
        assertEquals(expResult, result);
    }

}
