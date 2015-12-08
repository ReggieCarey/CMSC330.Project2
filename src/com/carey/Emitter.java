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

import static com.carey.Type.BEGINOFLINE;
import static com.carey.Type.ENDOFLINE;
import static com.carey.Type.EQUALS;
import static com.carey.Type.LITERAL;
import static com.carey.Type.OP;
import static com.carey.Type.VARIABLE;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Emitter has the job of implementing the output of the recursive descent
 * parser. In this implementation, Emitter will produce a Swing JFrame which is
 * the result of the parsing activity over a provided source document.
 *
 * @author ReginaldCarey
 */
class Emitter {

    private final LinkedList parsingStack;
    private final HashMap<String,Integer> variables;

    /**
     * Emitter constructor sets up the environment for the emitter and its
     * output.
     *
     * @throws ParseException
     */
    Emitter() throws ParseException {
        parsingStack = new LinkedList();
        variables = new HashMap();
    }

    /**
     * Emit stores and acts on the activity of the parser. This method will
     * accept a series of commands and their arguments and act to generate a
     * GUI. The emit method utilizes a context stack to keep track of the
     * context of GUI component placement and association.
     *
     * @param cmd The command from the parser.
     * @param args The list of arguments (possibly empty) for each command.
     * @throws ParseException
     */
    void emit(Type cmd, Object... args) throws ParseException {

        try {
            if (BEGINOFLINE.equals(cmd)) {
                parsingStack.clear();
                variables.clear();
            }
            if (VARIABLE.equals(cmd)) {
                parsingStack.push((String) args[0]);
            }
            if (LITERAL.equals(cmd)) {
                parsingStack.push(Integer.parseInt((String) args[0]));
            }
            if (OP.equals(cmd)) {
                parsingStack.push(new Token(cmd, (String) args[0], 0, 0));
            }
            if (EQUALS.equals(cmd)) {
                Object literal = parsingStack.pop();
                Object variable = parsingStack.pop();
                variables.put((String) variable, (Integer) literal);
            }
            if (ENDOFLINE.equals(cmd)) {
                evaluate();
            }

        } catch (ClassCastException ex) {
            throw new ParseException("Command arguments or elements on the parsing stack are of the wrong type", ex);
        }
    }

    /**
     * In support of unit testing. This method returns an unmodifiable version
     * of the internal parsing Stack.
     */
    Collection getParsingStack() {
        return Collections.unmodifiableList(parsingStack);
    }

    private void evaluate() throws ParseException {
        LinkedList<Integer> args = new LinkedList();
        while (!parsingStack.isEmpty()) {

            Object element = parsingStack.removeLast();

            if (element instanceof String) {
                Integer a = variables.get((String)element);
                if (a == null) throw new ParseException("Variable ["+element+"] Undefined");
                args.push(a);

            } else if (element instanceof Integer) {

                args.push(((Integer)element));

            } else if (element instanceof Token) {

                Token t = (Token)element;

                if ("+".equals(t.getContent())) {
                    Integer b = args.pop();
                    Integer a = args.pop();
                    args.push(a + b);
                }

                if ("-".equals(t.getContent())) {
                    Integer b = args.pop();
                    Integer a = args.pop();
                    args.push(a - b);
                }

                if ("*".equals(t.getContent())) {
                    Integer b = args.pop();
                    Integer a = args.pop();
                    args.push(a * b);
                }

                if ("/".equals(t.getContent())) {
                    Integer b = args.pop();
                    Integer a = args.pop();
                    args.push(a / b);
                }

                if (":".equals(t.getContent())) {
                    boolean c = args.pop() != 0;
                    Integer b = args.pop();
                    Integer a = args.pop();
                    args.push(c ? a : b);
                }

                if (">".equals(t.getContent())) {
                    Integer b = args.pop();
                    Integer a = args.pop();
                    args.push(a > b ? 1 : 0);
                }

                if ("=".equals(t.getContent())) {
                    Integer b = args.pop();
                    Integer a = args.pop();
                    args.push(a == b ? 1 : 0);
                }

                if ("<".equals(t.getContent())) {
                    Integer b = args.pop();
                    Integer a = args.pop();
                    args.push(a < b ? 1 : 0);
                }

                if ("&".equals(t.getContent())) {
                    boolean b = args.pop() != 0;
                    boolean a = args.pop() != 0;
                    args.push(a && b ? 1 : 0);
                }

                if ("|".equals(t.getContent())) {
                    boolean b = args.pop() != 0;
                    boolean a = args.pop() != 0;
                    args.push(a || b ? 1 : 0);
                }

                if ("!".equals(t.getContent())) {
                    boolean a = args.pop() != 0;
                    args.push(!a ? 1 : 0);
                }

            } else {

                System.out.println("UNK " + element.toString());
            }
        }
        System.out.println("Value = "+args.pop());
    }

}
