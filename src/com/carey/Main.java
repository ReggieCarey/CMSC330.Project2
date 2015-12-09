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

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The Main class provides a command line launch of the parser. This class
 * consists of the public static void main entry point.
 *
 * @author ReginaldCarey
 */
public class Main {

    /**
     * @param args the command line arguments. No arguments means to parse
     * standard input. --demo or -D means to generate a demonstration parse of a
     * program. --program or -P to output the code that drives the demo.
     * Otherwise it's a filename of a file to be parsed.
     */
    public static void main(String[] args) {

        String demoProgram
                = "(x + (y * 3)), x = 2, y = 6;"
                + "(x:y?(z|(q!))), z=1,x=10,y=20,q=1;\n"
                + "(x:y?(z|(q!))), z=1,x=10,y=20,q=0;\n"
                + "(x:y?(z|(q!))), z=0,x=10,y=20,q=1;\n"
                + "(x:y?(z|(q!))), z=0,x=10,y=20,q=0\n;";
        RecursiveDescentParser parser = new RecursiveDescentParser();
        Integer a = 7;
        Integer b = 3;
        try {
            if (args.length == 0) {
                parser.parse(new InputStreamReader(System.in));
            } else if ("--program".equals(args[0]) || "-P".equals(args[0])) {
                System.out.println(demoProgram);
            } else if ("--demo".equals(args[0]) || "-D".equals(args[0])) {
                parser.parse(demoProgram);
            } else {
                parser.parse(new File(args[0]));
            }
        } catch (IOException ex) {
            System.err.println("I/O Exception consuming the input program");
            if (ex.getMessage() != null) {
                System.err.println("WITH MESSAGE: " + ex.getMessage());
            }
        } catch (SyntaxException ex) {
            System.err.println("SYNTAX ERROR: " + ex.getMessage());
            if (ex.getCause() != null) {
                System.err.println("CAUSED BY: " + ex.getCause().getClass().getName());
                System.err.println("WITH MESSAGE: " + ex.getCause().getMessage());
            }
        } catch (ParseException ex) {
            System.err.println("PARSING ERROR: " + ex.getMessage());
            if (ex.getCause() != null) {
                System.err.println("CAUSED BY: " + ex.getCause().getClass().getName());
                System.err.println("WITH MESSAGE: " + ex.getCause().getMessage());
            }
        }
    }

}
