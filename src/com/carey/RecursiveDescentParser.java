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
import static com.carey.Type.BUTTON;
import static com.carey.Type.COLON;
import static com.carey.Type.COMMA;
import static com.carey.Type.END;
import static com.carey.Type.ENDGROUP;
import static com.carey.Type.ENDOFINPUT;
import static com.carey.Type.ENDPANEL;
import static com.carey.Type.ENDWINDOW;
import static com.carey.Type.FLOW;
import static com.carey.Type.GRID;
import static com.carey.Type.GROUP;
import static com.carey.Type.LABEL;
import static com.carey.Type.LAYOUT;
import static com.carey.Type.LPAREN;
import static com.carey.Type.NUMBER;
import static com.carey.Type.PANEL;
import static com.carey.Type.PERIOD;
import static com.carey.Type.RADIO;
import static com.carey.Type.RPAREN;
import static com.carey.Type.SEMICOLON;
import static com.carey.Type.STRING;
import static com.carey.Type.TEXTFIELD;
import static com.carey.Type.WINDOW;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

/**
 * This class implements a Recursive Descent Parser. It parses the grammar as
 * specified in the Project 1 homework assignment. This grammar is intended to
 * represent a domain specific language designed to generate graphical user
 * interfaces.
 *
 * This code will act like an interpreter in that it will directly generate the
 * GUI using Java SWING API's instead of generating the Java Source Code to
 * produce the GUI.
 *
 * @author ReginaldCarey
 */
public class RecursiveDescentParser {

    private LexicalScanner lex;
    private Emitter emitter;

    /**
     * Parse a program from a Reader producing a GUI. This method will parse the
     * contents of a Reader and generate a GUI based on the content.
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
            gui();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    /**
     * Parse a program from a String producing a GUI. This method will parse the
     * contents of a String and generate a GUI based on the content.
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
     * Parse a program from a File producing a GUI. This method will parse the
     * contents of a File and generate a GUI based on the content.
     *
     * @param file A file containing the content to parse.
     * @throws IOException On problems consuming the file
     * @throws SyntaxException On any token parsing error
     * @throws ParseException On any structural parsing error
     */
    public void parse(File file) throws IOException, SyntaxException, ParseException {
        parse(new FileReader(file));
    }

    /**
     * Recursive Descent Parser for gui. Parses gui as per the grammar:
     *
     * gui ::= Window STRING '(' NUMBER ',' NUMBER ')' layout widgets End '.'
     *
     * @throws IOException On problems reading the reader
     * @throws SyntaxException On any token parsing error
     * @throws ParseException On any structural parsing error
     */
    private void gui() throws IOException, SyntaxException, ParseException {
        matchThenAdvance(BEGINOFINPUT);
        matchThenAdvance(WINDOW);
        String windowName = matchThenAdvance(STRING).getContent();
        matchThenAdvance(LPAREN);
        int width = Integer.parseInt(matchThenAdvance(NUMBER).getContent());
        matchThenAdvance(COMMA);
        int height = Integer.parseInt(matchThenAdvance(NUMBER).getContent());
        matchThenAdvance(RPAREN);
        emitter.emit(WINDOW, windowName, width, height);
        layout();
        widgets();
        matchThenAdvance(END);
        emitter.emit(ENDWINDOW);
        matchThenAdvance(PERIOD);
        match(ENDOFINPUT);
        emitter.emit(ENDOFINPUT);
    }

    /**
     * Recursive Descent Parser for layout. Parses layout as per the grammar:
     *
     * layout ::= Layout layout_type ':'
     *
     * @throws IOException On problems reading the reader
     * @throws SyntaxException On any token parsing error
     * @throws ParseException On any structural parsing error
     */
    private void layout() throws IOException, SyntaxException, ParseException {
        matchThenAdvance(LAYOUT);
        layout_type();
        matchThenAdvance(COLON);
        emitter.emit(LAYOUT);
    }

    /**
     * Recursive Descent Parser for layout_type. Parses layout_type as per the
     * grammar:
     *
     * layout_type ::= Flow | Grid '(' NUMBER ',' NUMBER [',' NUMBER ',' NUMBER]
     * ')'
     *
     * @throws IOException On problems reading the reader
     * @throws SyntaxException On any token parsing error
     * @throws ParseException On any structural parsing error
     */
    private void layout_type() throws IOException, SyntaxException, ParseException {
        Token token = matchThenAdvance(FLOW, GRID);

        if (FLOW == token.getType()) {
            emitter.emit(FLOW);
        }

        if (GRID == token.getType()) {
            int rows, cols;
            matchThenAdvance(LPAREN);
            rows = Integer.parseInt(matchThenAdvance(NUMBER).getContent());
            matchThenAdvance(COMMA);
            cols = Integer.parseInt(matchThenAdvance(NUMBER).getContent());
            if (doesMatchThenAdvance(COMMA) != null) {
                int hgap, vgap;
                hgap = Integer.parseInt(matchThenAdvance(NUMBER).getContent());
                matchThenAdvance(COMMA);
                vgap = Integer.parseInt(matchThenAdvance(NUMBER).getContent());
                emitter.emit(GRID, rows, cols, hgap, vgap);
            } else {
                emitter.emit(GRID, rows, cols);
            }
            matchThenAdvance(RPAREN);
        }
    }

    /**
     * Recursive Descent Parser for widgets. Parses widgets as per the grammar:
     *
     * widgets ::= widget widgets | widget
     *
     * NOTE: We COULD take advantage of tail recursion optimization and convert
     * to a loop.
     *
     * @throws IOException On problems reading the reader
     * @throws SyntaxException On any token parsing error
     * @throws ParseException On any structural parsing error
     */
    private void widgets() throws IOException, SyntaxException, ParseException {
        widget();
        try {
            widgets();
        } catch (ParseException ex) {
        }
    }

    /**
     * Recursive Descent Parser for widget. Parses widget as per the grammar:
     *
     * widget ::= Button STRING ';' | Group radio_buttons End ';' | Label STRING
     * ';' | Panel layout widgets End ';' | Textfield NUMBER ';'
     *
     * @throws IOException On problems reading the reader
     * @throws SyntaxException On any token parsing error
     * @throws ParseException On any structural parsing error
     */
    private void widget() throws IOException, SyntaxException, ParseException {
        Token token = matchThenAdvance(BUTTON, GROUP, LABEL, PANEL, TEXTFIELD);

        if (BUTTON == token.getType()) {
            String buttonText = matchThenAdvance(STRING).getContent();
            matchThenAdvance(SEMICOLON);
            emitter.emit(BUTTON, buttonText);
        }

        if (GROUP == token.getType()) {
            emitter.emit(GROUP);
            radio_buttons();
            matchThenAdvance(END);
            matchThenAdvance(SEMICOLON);
            emitter.emit(ENDGROUP);
        }

        if (LABEL == token.getType()) {
            String labelText = matchThenAdvance(STRING).getContent();
            matchThenAdvance(SEMICOLON);
            emitter.emit(LABEL, labelText);
        }

        if (PANEL == token.getType()) {
            emitter.emit(PANEL);
            layout();
            widgets();
            matchThenAdvance(END);
            matchThenAdvance(SEMICOLON);
            emitter.emit(ENDPANEL);
        }

        if (TEXTFIELD == token.getType()) {
            int columns = Integer.parseInt(matchThenAdvance(NUMBER).getContent());
            matchThenAdvance(SEMICOLON);
            emitter.emit(TEXTFIELD, columns);
        }
    }

    /**
     * Recursive Descent Parser for radio_buttons. Parses radio_buttons as per
     * the grammar:
     *
     * radio_buttons ::= radio_button radio_buttons | radio_button
     *
     * NOTE: We COULD take advantage of tail recursion optimization and convert
     * to a loop.
     *
     * @throws IOException On problems reading the reader
     * @throws SyntaxException On any token parsing error
     * @throws ParseException On any structural parsing error
     */
    private void radio_buttons() throws IOException, SyntaxException, ParseException {
        radio_button();
        try {
            radio_buttons();
        } catch (ParseException ex) {
        }
    }

    /**
     * Recursive Descent Parser for radio_button. Parses radio_button as per the
     * grammar:
     *
     * radio_button ::= Radio STRING ';'
     *
     * @throws IOException On problems reading the reader
     * @throws SyntaxException On any token parsing error
     * @throws ParseException On any structural parsing error
     */
    private void radio_button() throws IOException, SyntaxException, ParseException {
        matchThenAdvance(RADIO);
        String name = matchThenAdvance(STRING).getContent();
        matchThenAdvance(SEMICOLON);
        emitter.emit(RADIO, name);
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
