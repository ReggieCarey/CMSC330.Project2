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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Type enumeration captures the definition of each token type in the
 * language. Each element of the enumeration has a compiled regular expression
 * (except synthetic types) used to parse the input readers. Each regular
 * expression consumes input. RE's that reduce to a* or a? are not permitted
 * based on the implementation of LexicalScanner not supporting multiple
 * concurrent matching tokens while scanning. White space is not considered as a
 * token type.
 *
 * @author ReginaldCarey
 */
enum Type {

    BEGINOFINPUT, BEGINOFLINE,
    ENDOFINPUT, ENDOFLINE,
    COMMA(",", Pattern.LITERAL),
    SEMICOLON(";", Pattern.LITERAL),
    COLON(":", Pattern.LITERAL),
    QUESTION("?", Pattern.LITERAL),
    NOT("!", Pattern.LITERAL),
    LPAREN("(", Pattern.LITERAL),
    RPAREN(")", Pattern.LITERAL),
    EQUALS("=", Pattern.LITERAL),
    OP("[\\+\\-\\*\\/\\>\\<\\&\\|]"),
    VARIABLE("[a-zA-Z][a-zA-Z0-9]*"),
    LITERAL("[0-9]+");

    private final Pattern pattern;

    private Type() {
        pattern = null;
    }

    private Type(String str) {
        pattern = Pattern.compile(str);
    }

    private Type(String str, int flags) {
        pattern = Pattern.compile(str, flags);
    }

    /**
     * A matcher for the given Type. Return a matcher that can identify the
     * compiled regular expression.
     *
     * @param charSequence The sequence of characters to match against
     * @return A matcher initialized with a char sequence and regular expression
     */
    Matcher getMatcher(CharSequence charSequence) {
        return pattern != null ? pattern.matcher(charSequence) : null;
    }

    @Override
    public String toString() {
        if (pattern == null) {
            return name() + " token";
        } else {
            return ((pattern.flags() & Pattern.LITERAL) == Pattern.LITERAL) ? pattern.toString() : name() + " token";
        }
    }

}
