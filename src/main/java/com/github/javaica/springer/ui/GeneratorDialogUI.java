package com.github.javaica.springer.ui;

import com.github.javaica.springer.model.GeneratorElementOptions;
import com.github.javaica.springer.model.GeneratorParams;

import javax.swing.*;
import java.awt.event.*;

public class GeneratorDialogUI extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JCheckBox modelCheckBox;
    private JTextField textField1;
    private JCheckBox repositoryCheckBox;
    private JTextField textField2;
    private JCheckBox serviceCheckBox;
    private JTextField textField3;
    private JCheckBox controllerCheckBox;
    private JTextField textField4;

    public GeneratorDialogUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        GeneratorParams params = GeneratorParams.builder()
                .modelOptions(
                        modelCheckBox.isSelected() ? new GeneratorElementOptions(textField1.getText()) : null
                )
                .repositoryOptions(
                        repositoryCheckBox.isSelected() ? new GeneratorElementOptions(textField2.getText()) : null
                )
                .serviceOptions(
                        serviceCheckBox.isSelected() ? new GeneratorElementOptions(textField3.getText()) : null
                )
                .controllerOptions(
                        controllerCheckBox.isSelected() ? new GeneratorElementOptions(textField4.getText()) : null
                )
                .build();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        GeneratorDialogUI dialog = new GeneratorDialogUI();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
