package com.github.javaica.springer.ui;

import com.github.javaica.springer.model.CodegenDialogOptions;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

public class GeneratorDialogUI extends JDialog {

    private final Consumer<CodegenDialogOptions> callback;

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

    public GeneratorDialogUI(Consumer<CodegenDialogOptions> callback) {
        this.callback = callback;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        CodegenDialogOptions options = CodegenDialogOptions.builder()
                .generateModel(modelCheckBox.isSelected())
                .generateRepository(repositoryCheckBox.isSelected())
                .generateService(serviceCheckBox.isSelected())
                .generateController(controllerCheckBox.isSelected())
                .modelPackage(modelCheckBox.isSelected() ? textField1.getText() : null)
                .repositoryPackage(repositoryCheckBox.isSelected() ? textField2.getText() : null)
                .servicePackage(serviceCheckBox.isSelected() ? textField3.getText() : null)
                .controllerPackage(controllerCheckBox.isSelected() ? textField4.getText() : null)
                .build();
        dispose();
        callback.accept(options);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        GeneratorDialogUI dialog = new GeneratorDialogUI(any -> {});
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
