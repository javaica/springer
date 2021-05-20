package com.github.javaica.springer.codegen;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.ui.PackageChooser;
import com.intellij.psi.PsiPackage;
import org.jetbrains.annotations.NotNull;

public class DummyCodegenAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PackageChooser chooser = new PackageChooserDialog("Test", e.getProject());
        chooser.show();
        PsiPackage psiPackage = chooser.getSelectedPackage();
        if (psiPackage == null)
            return;
        CodegenOptions options = createOptions(e, psiPackage);
        CommandProcessor.getInstance().executeCommand(
                options.getProject(),
                () -> DummyCodeGenerator.getInstance().generate(options),
                "Springer Codegen",
                null);
    }

    private CodegenOptions createOptions(AnActionEvent event, PsiPackage psiPackage) {
        return CodegenOptions.builder()
                .project(event.getProject())
                .psiPackage(psiPackage)
                .psiFile(event.getData(CommonDataKeys.PSI_FILE))
                .build();
    }
}
