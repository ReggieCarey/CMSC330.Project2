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
import static com.carey.Type.BEGINOFLINE;
import static com.carey.Type.COLON;
import static com.carey.Type.COMMA;
import static com.carey.Type.ENDOFINPUT;
import static com.carey.Type.ENDOFLINE;
import static com.carey.Type.EQUALS;
import static com.carey.Type.LITERAL;
import static com.carey.Type.LPAREN;
import static com.carey.Type.NOT;
import static com.carey.Type.OP;
import static com.carey.Type.QUESTION;
import static com.carey.Type.RPAREN;
import static com.carey.Type.SEMICOLON;
import static com.carey.Type.VARIABLE;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

/**
 * This class implements a Recursive Descent Parser. It parses the grammar as
 * specified in the Project 2 homework assignment. This grammar is intended to
 * represent a domain specific language designed to provide an expression
 * interpreter.
 *
 * This code will act like an interpreter in that it will evaluate the
 * expressions.
 *
 * The grammar for the language that this interpreter accepts is defined by the following grammar:
 *
 * <pre>{@code
 * <program> → <lines>
 * <lines> → <line> | <line> <lines>
 * <line> → <exp> , <assigns> ;
 * <exp> → ( <operand> <op> <operand> ) |
 *         ( <operand> : <operand> ? <operand> ) |
 *         ( <operand> ! )
 * <operand> → <literal> | <variable> | <exp>
 * <assigns> → <assigns> , <assign> | <assign>
 * <assign> → <variable> = <literal>
 * }</pre>
 *
 * The regular expressions defining the three tokens are the following:
 *
 * <pre>{@code
 * <op>         '+' | '-' | '*' | '/' | '>' | '<' | '=' | '&' | '|'
 * <variable>	[a-zA-Z][a-zA-Z0-9]*
 * <literal>	[0-9]+
 * }</pre>
 *
 * @author ReginaldCarey
 */
public class RecursiveDescentParser {

    private LexicalScanner lex;
    private Emitter emitter;

    /**
     * Parse a program from a Reader producing a evaluation result. This method
     * will parse the contents of a Reader and generate an expression evaluation
     * result.
     *
     * @param stream A basic reader containing the content to parse.
     * @throws IOException On problems consuming the reader
     * @throws SyntaxException On any token parsing error
     * @throws ParseException On any structural parsing error
     */
    public void parse(Reader stream) throws IOException, SyntaxException, ParseException {
        try {
            lex = new LexicalScanner(stream);
            emitter = new Emitter();
            program();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    /**
     * Parse a program from a String producing a evaluation result. This method
     * will parse the contents of a Reader and generate an expression evaluation
     * result.
     *
     * @param content A string containing the content to parse.
     * @throws IOException On problems consuming the string
     * @throws SyntaxException On any token parsing error
     * @throws ParseException On any structural parsing error
     */
    public void parse(String content) throws IOException, SyntaxException, ParseException {
        parse(new StringReader(content));
    }

    /**
     * Parse a program from a File producing a evaluation result. This method
     * will parse the contents of a Reader and generate an expression evaluation
     * result.
     *
     * @param file A file containing the content to parse.
     * @throws IOException On problems consuming the file
     * @throws SyntaxException On any token parsing error
     * @throws ParseException On any structural parsing error
     */
    public void parse(File file) throws IOException, SyntaxException, ParseException {
        parse(new FileReader(file));
    }

    private void program() throws IOException, SyntaxException, ParseException {
        emitter.emit(matchThenAdvance(BEGINOFINPUT).getType());
        lines();
        emitter.emit(matchThenAdvance(ENDOFINPUT).getType());
    }

    private void lines() throws IOException, SyntaxException, ParseException {
        line();
        try {
            lines();
        } catch (ParseException ex) {
        }
    }

    private void line() throws IOException, SyntaxException, ParseException {
        emitter.emit(BEGINOFLINE);
        exp();
        matchThenAdvance(COMMA);
        assigns();
        matchThenAdvance(SEMICOLON);
        emitter.emit(ENDOFLINE);
    }

    private void exp() throws IOException, SyntaxException, ParseException {
        matchThenAdvance(LPAREN);
        operand();
        Token operator = matchThenAdvance(OP, COLON, NOT, EQUALS);
        if (COLON.equals(operator.getType())) {
            operand();
            matchThenAdvance(QUESTION);
            operand();
        } else if (OP.equals(operator.getType()) || EQUALS.equals(operator.getType())) {
            operand();
        }
        matchThenAdvance(RPAREN);
        emitter.emit(OP, operator.getContent());
    }

    private void operand() throws IOException, SyntaxException, ParseException {
        try {
            Token token = matchThenAdvance(VARIABLE, LITERAL);
            emitter.emit(token.getType(), token.getContent());
        } catch (ParseException ex) {
            exp();
        }
    }

    private void assigns() throws IOException, SyntaxException, ParseException {
        assign();
        try {
            matchThenAdvance(COMMA);
            assigns();
        } catch (ParseException ex) {
        }
    }

    private void assign() throws IOException, SyntaxException, ParseException {
        Token variable = matchThenAdvance(VARIABLE);
        Token equals = matchThenAdvance(EQUALS);
        Token literal = matchThenAdvance(LITERAL);
        emitter.emit(VARIABLE, variable.getContent());
        emitter.emit(LITERAL, literal.getContent());
        emitter.emit(EQUALS, equals.getContent());
    }

    /**
     * ***********************************************************************
     */
    /* Helper methods for the recursive descent methods above                 */
    /**
     * ***********************************************************************
     */
    /**
     * Match the expected type(s). This method will match the token type to the
     * current token. A null <code>types</code> always matches.
     *
     * @param types The array of token types to match
     * @return The current token at the front of the stream
     * @throws ParseException If the token at the front of the stream does not
     * match
     */
    private Token match(Type... types) throws ParseException {
        if (doesMatch(types)) {
            return lex.getCurrentToken();
        }
        throw new ParseException(String.format("Unexpected content [%s] - expecting %s at [%d:%d]\n",
                lex.getCurrentToken().getContent(),
                Arrays.toString(types),
                lex.getCurrentToken().getLineNumber(),
                lex.getCurrentToken().getOffset()));
    }

    /**
     * Match the expected type(s) and consume the next token. This method will
     * match the token type to the current token and then consume the next token
     * from the stream. A null <code>types</code> always matches.
     *
     * @param types The array of token types to match
     * @return The matched token at the front of the stream
     * @throws ParseException If the token at the front of the stream does not
     * match
     * @throws IOException If there are problems consuming the stream
     * @throws SyntaxException If the front of the stream does not correspond to
     * a token
     */
    private Token matchThenAdvance(Type... types) throws ParseException, IOException, SyntaxException {
        Token prevToken = (types.length == 0) ? lex.getCurrentToken() : match(types);
        lex.advance();
        return prevToken;
    }

    /**
     * Match any of the types. This method will match the token types to the
     * current token. A null <code>types</code> always matches.
     *
     * @param types An array of token types to match
     * @return true if the current token is in the types set, false otherwise
     */
    private boolean doesMatch(Type... types) {
        return (types.length == 0) ? true : Arrays.asList(types).contains(lex.getCurrentToken().getType());
    }

    /**
     * Match the expected type(s) and consume the next token. This method will
     * match the token type(s) to the current token and then consume the next
     * token from the stream if there was a match. A null <code>types</code>
     * always matches.
     *
     * @param types The array of token types to match
     * @return The currentToken if matched, null otherwise.
     * @throws ParseException If the token at the front of the stream does not
     * match
     * @throws IOException If there are problems consuming the stream
     * @throws SyntaxException If the front of the stream does not correspond to
     * a token
     */
    private Token doesMatchThenAdvance(Type... types) throws ParseException, IOException, SyntaxException {
        return doesMatch(types) ? matchThenAdvance() : null;
    }

}
