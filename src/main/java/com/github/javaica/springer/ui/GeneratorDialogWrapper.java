package com.github.javaica.springer.ui;

import com.github.javaica.springer.model.ComponentDialogOptions;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.function.Consumer;

public class GeneratorDialogWrapper extends DialogWrapper {

    private final Consumer<ComponentDialogOptions> callback;
    private GeneratorDialogUI dialogUI;

    public GeneratorDialogWrapper(Project project, Consumer<ComponentDialogOptions> callback) {
        super(project);
        setOKActionEnabled(true);
        setTitle("Select Desired Options");
        this.callback = callback;
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        dialogUI = new GeneratorDialogUI(callback);
        return dialogUI.getContentPane();
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
        dialogUI.onOK();
    }
}
