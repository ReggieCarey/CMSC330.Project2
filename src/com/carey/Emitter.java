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

import static com.carey.Type.BUTTON;
import static com.carey.Type.ENDGROUP;
import static com.carey.Type.ENDOFINPUT;
import static com.carey.Type.ENDPANEL;
import static com.carey.Type.ENDWINDOW;
import static com.carey.Type.FLOW;
import static com.carey.Type.GRID;
import static com.carey.Type.GROUP;
import static com.carey.Type.LABEL;
import static com.carey.Type.LAYOUT;
import static com.carey.Type.PANEL;
import static com.carey.Type.RADIO;
import static com.carey.Type.TEXTFIELD;
import static com.carey.Type.WINDOW;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Emitter has the job of implementing the output of the recursive descent
 * parser. In this implementation, Emitter will produce a Swing JFrame which is
 * the result of the parsing activity over a provided source document.
 *
 * @author ReginaldCarey
 */
class Emitter {

    private final LinkedList parsingStack;
    private JFrame gui;

    /**
     * Emitter constructor sets up the environment for the emitter and its
     * output.
     *
     * @throws ParseException
     */
    Emitter() throws ParseException {
        try {
            // Set the Nimbus look and feel if available
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            throw new ParseException("Unable to render the GUI", ex);
        }
        parsingStack = new LinkedList();
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
            if (cmd == WINDOW) {
                String title = (String) args[0];
                Integer width = (Integer) args[1];
                Integer height = (Integer) args[2];
                parsingStack.push(generateWindow(title, width, height));
            }

            if (cmd == ENDWINDOW) {
                gui = (JFrame) parsingStack.pop();
            }

            if (cmd == PANEL) {
                parsingStack.push(generatePanel());
            }

            if (cmd == ENDPANEL) {
                JPanel panel = (JPanel) parsingStack.pop();
                Container container = (Container) parsingStack.peek();
                container.add(panel);
            }

            if (cmd == GROUP) {
                parsingStack.push(generateButtonGroup());
            }

            if (cmd == ENDGROUP) {
                ButtonGroup buttonGroup = (ButtonGroup) parsingStack.pop();

                // Turn an enumeration into a list.
                Enumeration<AbstractButton> enumeration = buttonGroup.getElements();
                List<AbstractButton> radioButtonList = Collections.list(enumeration);

                Container container = (Container) parsingStack.peek();
                for (AbstractButton button : radioButtonList) {
                    container.add(button);
                }
            }

            if (cmd == TEXTFIELD) {
                Integer columns = (Integer) args[0];
                Container container = (Container) parsingStack.peek();
                container.add(generateTextfield(columns));
            }

            if (cmd == BUTTON) {
                String buttonText = (String) args[0];
                Container container = (Container) parsingStack.peek();
                container.add(generateButton(buttonText));
            }

            if (cmd == RADIO) {
                String radioText = (String) args[0];
                ButtonGroup buttonGroup = (ButtonGroup) parsingStack.peek();
                buttonGroup.add(generateRadioButton(radioText));
            }

            if (cmd == LABEL) {
                String labelText = (String) args[0];
                Container container = (Container) parsingStack.peek();
                container.add(generateLabel(labelText));
            }

            if (cmd == LAYOUT) {
                LayoutManager layoutManager = (LayoutManager) parsingStack.pop();
                Container container = (Container) parsingStack.peek();
                container.setLayout(layoutManager);
            }

            if (cmd == FLOW) {
                parsingStack.push(generateFlowLayout());
            }

            if (cmd == GRID) {
                Integer rows = (Integer) args[0];
                Integer cols = (Integer) args[1];
                if (args.length == 4) {
                    Integer hgap = (Integer) args[2];
                    Integer vgap = (Integer) args[3];
                    parsingStack.push(generateGridLayout(rows, cols, hgap, vgap));
                } else {
                    parsingStack.push(generateGridLayout(rows, cols));
                }
            }

            if (cmd == ENDOFINPUT) {
                gui.setVisible(true);
            }
        } catch (ClassCastException ex) {
            throw new ParseException("Command arguments or elements on the parsing stack are of the wrong type", ex);
        }
    }

    /*
     * Generate JFrame. The jFrame is embued with title width and height.
     */
    private JFrame generateWindow(String title, int width, int height) {
        JFrame jFrame = new JFrame();
        jFrame.setTitle(title);
        jFrame.setSize(new Dimension(width, height));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        return jFrame;
    }

    /*
     * Genearte FlowLayout. The flowLayout is not configured
     */
    private FlowLayout generateFlowLayout() {
        FlowLayout flowLayout = new FlowLayout();
        return flowLayout;
    }

    /*
     * Generate GridLayout. The gridLayout is embued with rows and columns.
     */
    private GridLayout generateGridLayout(int rows, int cols) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.setRows(rows);
        gridLayout.setColumns(cols);
        return gridLayout;
    }

    /*
     * Generate GridLayout. The gridLayout is embued with rows, columns,
     * horizontal gap and vertical gap.
     */
    private GridLayout generateGridLayout(int rows, int cols, int hgap, int vgap) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.setRows(rows);
        gridLayout.setColumns(cols);
        gridLayout.setHgap(hgap);
        gridLayout.setVgap(vgap);
        return gridLayout;
    }

    /*
     * Genrate JTextField. The jTextField is embuded with the number of columns.
     */
    private JTextField generateTextfield(int columns) {
        JTextField jTextField = new JTextField();
        jTextField.setFont(new java.awt.Font("Courier", 0, 14));
        jTextField.setColumns(columns);
        return jTextField;
    }

    /*
     * Generate JLabel. The jLabel is embued with label text.
     */
    private JLabel generateLabel(String labelText) {
        JLabel jLabel = new JLabel();
        jLabel.setText(labelText);
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        return jLabel;
    }

    /*
     * Generate JButton. The jButton is embued with button text.
     */
    private JButton generateButton(String buttonText) {
        JButton jButton = new JButton();
        jButton.setText(buttonText);
        return jButton;
    }

    /*
     * Generate JRadioButton. The jRadioButton is embued with radio button text.
     */
    private JRadioButton generateRadioButton(String radioText) {
        JRadioButton jRadioButton = new JRadioButton();
        jRadioButton.setText(radioText);
        return jRadioButton;
    }

    /*
     * Generate JPanel. The jPanel is not configured.
     */
    private JPanel generatePanel() {
        JPanel jPanel = new JPanel();
        return jPanel;
    }

    /*
     * Generate ButtonGroup. The buttonGroup is not configured.
     */
    private ButtonGroup generateButtonGroup() {
        ButtonGroup buttonGroup = new ButtonGroup();
        return buttonGroup;
    }

    /**
     * In support of unit testing. This method returns an unmodifiable version
     * of the internal parsing Stack.
     */
    Collection getParsingStack() {
        return Collections.unmodifiableList(parsingStack);
    }

}
