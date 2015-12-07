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

/**
 * SyntaxException is used to identify lexical parsing problems. When ever there
 * is a problem identifying a token, this exception is raised.
 *
 * @author ReginaldCarey
 */
public class SyntaxException extends Exception {

    public SyntaxException(String message) {
        super(message);
    }

}
