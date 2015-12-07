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

import java.util.Objects;

/**
 * Token is the representation of the basic lexeme of the language. Tokens
 * embody the type content and location in the source file where they were
 * found.
 *
 * @author ReginaldCarey
 */
class Token {

    private final Type type;
    private final String content;
    private final int lineNumber;
    private final int offset;

    /**
     * Tokens are immutable. Once constructed they cannot be changed.
     *
     * @param type The token type
     * @param content The extracted relevant content of the token
     * @param lineNumber The line number where the token was found
     * @param offset The offset into the line where the token was found
     */
    Token(Type type, String content, int lineNumber, int offset) {
        this.type = type;
        this.content = content;
        this.lineNumber = lineNumber;
        this.offset = offset;
    }

    /**
     * Generic getter for content.
     *
     * @return content as a String
     */
    String getContent() {
        return content;
    }

    /**
     * Generic getter for type.
     *
     * @return type as a Type
     */
    Type getType() {
        return type;
    }

    /**
     * Generic getter for lineNumber
     *
     * @return lineNumber as an Integer. First line is 1.
     */
    int getLineNumber() {
        return lineNumber;
    }

    /**
     * Generic getter for offset
     *
     * @return offset as an Integer. First character in a line is 1.
     */
    int getOffset() {
        return offset;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.type);
        hash = 53 * hash + Objects.hashCode(this.content);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Token other = (Token) obj;
        if (this.type != other.type) {
            return false;
        }
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s [%s]", type.name(), content);
    }

}
