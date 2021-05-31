package com.github.javaica.springer.ui;

import com.github.javaica.springer.model.MethodDialogOptions;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.function.Consumer;

public class MethodDialogWrapper extends DialogWrapper {

    private final Consumer<MethodDialogOptions> callback;
    private final Project project;
    private MethodDialogUI dialogUI;

    public MethodDialogWrapper(Project project, Consumer<MethodDialogOptions> callback) {
        super(project);
        setOKActionEnabled(true);
        setTitle("Select Desired Methods");
        setSize(200, 200);
        this.callback = callback;
        this.project = project;
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        dialogUI = new MethodDialogUI(project, callback);
        return dialogUI.getContentPane();
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
        dialogUI.onOK();
    }
}
