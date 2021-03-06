package com.github.javaica.springer.ui;

import com.github.javaica.springer.model.MethodDialogOptions;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

public class MethodDialogUI extends JDialog {

    private final Project project;
    private final Consumer<MethodDialogOptions> callback;

    private JPanel contentPane;
    private JCheckBox getCheckBox;
    private JCheckBox postCheckBox;
    private JCheckBox putCheckBox;
    private JCheckBox deleteCheckBox;

    public MethodDialogUI(Project project, Consumer<MethodDialogOptions> callback) {
        this.callback = callback;
        this.project = project;
        setContentPane(contentPane);
        setModal(true);

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
    }

    public void onOK() {
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

    @Override
    public JPanel getContentPane() {
        return contentPane;
    }
}
