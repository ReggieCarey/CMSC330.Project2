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
    private final HashMap<String, Integer> symbolTable;

    /**
     * Emitter constructor sets up the environment for the emitter and its
     * output.
     *
     * @throws ParseException
     */
    Emitter() throws ParseException {
        parsingStack = new LinkedList();
        symbolTable = new HashMap();
    }

    /**
     * Emit stores and acts on the activity of the parser. This method will
     * initialize and use two structures, a symbolTable and a parsingStack. The
     * parsingStack is loaded with postfix expressions. Effectively it contains
     * a linear representation of a parse tree. The symbolTable is loaded with
     * values only when variables are declared with values.
     *
     * @param cmd The command from the parser.
     * @param args The list of arguments (possibly empty) for each command.
     * @throws ParseException
     */
    void emit(Type cmd, Object... args) throws ParseException {

        try {
            // Each virtual line to be parsed starts with a cleaned symbolTable and parsingStack.
            if (BEGINOFLINE.equals(cmd)) {
                parsingStack.clear();
                symbolTable.clear();
            }

            // When we encounter a variable, add it's name to the stack. Strings on the stack are variable names
            if (VARIABLE.equals(cmd)) {
                parsingStack.push((String) args[0]);
            }

            // When we encounter a literal, we convert it to an integer and add it to the stack
            if (LITERAL.equals(cmd)) {
                parsingStack.push(Integer.parseInt((String) args[0]));
            }

            // When we encounter an operator we put a Token on the stack that represents the particular operator
            if (OP.equals(cmd)) {
                parsingStack.push(new Token(cmd, (String) args[0], 0, 0));
            }

            // When we encounter an equals, it's an assignment, so we pop the two values and update the symbol table
            if (EQUALS.equals(cmd)) {
                Object literal = parsingStack.pop();
                Object variable = parsingStack.pop();
                symbolTable.put((String) variable, (Integer) literal);
            }

            // When we read the endofline, we are ready to evaluate the parseStack using the symbolTable
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

    /**
     * Evaluate the contents of the parsingStack. This method will traverse the
     * parsing stack in reverse order and evaluate the contents. Whenever a
     * variable is encountered it is replaced with the value in the symbol
     * stack. We implement a stack machine for the evaluation of expressions. As
     * a result we create a stack called args. Non operators go on this stack.
     * Operators cause data to be removed from the stack to be replaced by
     * results of the operation.
     *
     * @throws ParseException is thrown when a variable not in the symbol table
     * is found
     */
    private void evaluate() throws ParseException {
        LinkedList<Integer> args = new LinkedList();
        while (!parsingStack.isEmpty()) {

            Object element = parsingStack.removeLast();

            if (element instanceof String) {
                Integer a = symbolTable.get((String) element);
                if (a == null) {
                    throw new ParseException("Variable [" + element + "] Undefined");
                }
                args.push(a);

            } else if (element instanceof Integer) {

                args.push(((Integer) element));

            } else if (element instanceof Token) {

                Token t = (Token) element;

                // add the two operands on the top of the stack.
                // push the results on the stack
                if ("+".equals(t.getContent())) {
                    Integer b = args.pop();
                    Integer a = args.pop();
                    args.push(a + b);
                }

                // subtract the two operands on the top of the stack.
                // push the results on the stack
                if ("-".equals(t.getContent())) {
                    Integer b = args.pop();
                    Integer a = args.pop();
                    args.push(a - b);
                }

                // multiply the two operands on the top of the stack.
                // push the results on the stack
                if ("*".equals(t.getContent())) {
                    Integer b = args.pop();
                    Integer a = args.pop();
                    args.push(a * b);
                }

                // divide the two operands on the top of the stack.
                // push the results on the stack
                if ("/".equals(t.getContent())) {
                    Integer b = args.pop();
                    Integer a = args.pop();
                    args.push(a / b);
                }

                // conditional operator. push one of three operands as the
                // result. One operand is treated as boolean
                if (":".equals(t.getContent())) {
                    boolean c = args.pop() != 0;
                    Integer b = args.pop();
                    Integer a = args.pop();
                    args.push(c ? a : b);
                }

                // push a numeric boolean for 'greater than' of two operands
                if (">".equals(t.getContent())) {
                    Integer b = args.pop();
                    Integer a = args.pop();
                    args.push(a > b ? 1 : 0);
                }

                // push a numeric boolean for 'equal' of two operands. operands
                // are treated as numbers.
                if ("=".equals(t.getContent())) {
                    Integer b = args.pop();
                    Integer a = args.pop();
                    args.push(a == b ? 1 : 0);
                }

                // push a numeric boolean for 'less than' of two operands
                if ("<".equals(t.getContent())) {
                    Integer b = args.pop();
                    Integer a = args.pop();
                    args.push(a < b ? 1 : 0);
                }

                // compute the logical and of two operands and push a numeric
                // boolean result. Treat operands as numeric booleans
                if ("&".equals(t.getContent())) {
                    boolean b = args.pop() != 0;
                    boolean a = args.pop() != 0;
                    args.push(a && b ? 1 : 0);
                }

                // compute the logical or of two operands and push a numeric
                // boolean result. Treat operands as numeric booleans
                if ("|".equals(t.getContent())) {
                    boolean b = args.pop() != 0;
                    boolean a = args.pop() != 0;
                    args.push(a || b ? 1 : 0);
                }

                // compute the logical not of the operand treating it as a
                // numeric boolean and push the result
                if ("!".equals(t.getContent())) {
                    boolean a = args.pop() != 0;
                    args.push(!a ? 1 : 0);
                }
            }
        }

        // The top of the arg stack now contains the computed result.
        System.out.println("Value = " + args.pop());
    }

}
