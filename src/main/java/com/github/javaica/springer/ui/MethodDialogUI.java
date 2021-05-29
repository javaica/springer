package com.github.javaica.springer.ui;

import com.github.javaica.springer.model.MethodDialogOptions;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

public class MethodDialogUI extends JDialog {

    private final Consumer<MethodDialogOptions> callback;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JCheckBox getCheckBox;
    private JCheckBox postCheckBox;
    private JCheckBox putCheckBox;
    private JCheckBox deleteCheckBox;

    public MethodDialogUI(Consumer<MethodDialogOptions> callback) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
                e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );

        this.callback = callback;
    }

    private void onOK() {
        MethodDialogOptions options = MethodDialogOptions.builder()
                .get(getCheckBox.isSelected())
                .post(postCheckBox.isSelected())
                .put(putCheckBox.isSelected())
                .delete(deleteCheckBox.isSelected())
                .build();
        dispose();
        callback.accept(options);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
