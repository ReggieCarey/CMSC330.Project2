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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The LexicalScanner generates a stream of tokens. Based on the specified
 * grammar and defined lexemes, the lexical scanner generates a stream of
 * tokens.
 *
 * @author ReginaldCarey
 */
class LexicalScanner {

    private final BufferedReader reader;
    private int lineNumber = 0;
    private String current = null;
    private int offset = 0;
    private final Pattern whiteSpace = Pattern.compile("[ \t]*");
    private Token currentToken;

    /**
     * Construct a lexical scanner to scan the contents of a reader stream. The
     * stream is assumed to be unbuffered and should consist of the the program
     * to be parsed. This class does not close the stream.
     *
     * @param stream The Reader containing the program to parse.
     * @throws IOException on any problems reading from the stream.
     */
    LexicalScanner(Reader stream) throws IOException {
        reader = new BufferedReader(stream);
        currentToken = new Token(Type.BEGINOFINPUT, null, 0, 0);
    }

    /**
     * The current token.
     *
     * @return the current token.
     */
    Token getCurrentToken() {
        return currentToken;
    }

    /**
     * Advance to the next token. This method is the heavy lifter of the lexical
     * scanner. It keeps track of line number and character offsets of tokens
     * and consumes the reader. It will either return a token or throw a syntax
     * error or I/O exception if a fault is detected. It will NOT close the
     * reader at end of file detection. Instead, it will continuously return an
     * ENDOFINPUT token.
     */
    void advance() throws IOException, SyntaxException {

        // In order to consume white space and blank lines, we must loop until
        // a terminating condition occurs - which may mean returning an end of
        // input token or determining that there is non white space content
        // at the front of the current string.
        while (true) {
            if (current == null || current.isEmpty()) {
                current = reader.readLine();
                if (current == null) {
                    currentToken = new Token(Type.ENDOFINPUT, null, lineNumber, offset);
                    return;
                }
                lineNumber++;
                offset = 1;
            }

            // Look for white space and consume it
            Matcher m = whiteSpace.matcher(current);
            if (m.lookingAt()) {
                offset += m.group().length();
                current = current.substring(m.group().length());
            }

            // If there is content remaining, break and look for a token
            if (!current.isEmpty()) {
                break;
            }
        }

        // Find the next legal token.  This implementation is horrendously
        // inefficient.  The proper approach would be to combine the regular
        // expressions into one but that would require a custom FSA.  We loop
        // through each of the token types, getting their matchers to analyze
        // the current string.  We return the first matched token, or throw a
        // syntax error.
        for (Type tokenType : Type.values()) {

            Matcher tokenMatcher = tokenType.getMatcher(current);
            // Some token types do not have matchers.
            if (tokenMatcher == null) {
                continue;
            }

            if (tokenMatcher.lookingAt()) {
                String foundStr = tokenMatcher.group(tokenMatcher.groupCount());
                currentToken = new Token(tokenType, foundStr, lineNumber, offset);
                offset += tokenMatcher.group().length();
                current = current.substring(tokenMatcher.group().length());
                if (current.length() == 0) {
                    current = null;
                }
                return;
            }
        }

        // We did not find a token. Throw a syntax error.
        throw new SyntaxException(String.format("Unexpected content [%s] after [%s] at [%d:%d]\n", current, currentToken.getType(), lineNumber, offset));
    }

}
