package com.github.javaica.springer;

import com.github.javaica.springer.codegen.SpringerCodeGenerator;
import com.github.javaica.springer.model.CodegenDialogOptions;
import com.github.javaica.springer.model.CodegenElement;
import com.github.javaica.springer.model.CodegenElementType;
import com.github.javaica.springer.model.CodegenOptions;
import com.github.javaica.springer.ui.GeneratorDialogUI;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.CommandProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public class SpringerCodegenAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        GeneratorDialogUI dialog = new GeneratorDialogUI(dialogCallback(e));
        dialog.pack();
        dialog.setVisible(true);
    }

    private Consumer<CodegenDialogOptions> dialogCallback(AnActionEvent event) {
        return options -> CommandProcessor.getInstance().executeCommand(
                event.getProject(),
                () -> generate(event, options),
                "Springer Codegen",
                null);
    }

    private void generate(AnActionEvent event, CodegenDialogOptions dialogOptions) {
        CodegenOptions options = createOptions(event, dialogOptions);
        SpringerCodeGenerator.getInstance().generate(options);
    }

    private CodegenOptions createOptions(AnActionEvent event, CodegenDialogOptions dialogOptions) {
        Objects.requireNonNull(event.getProject(), "Cannot resolve project where classes should be generated");
        CodegenOptions.CodegenOptionsBuilder builder = CodegenOptions.builder()
                .project(event.getProject())
                .originalEntity(event.getData(CommonDataKeys.PSI_FILE));
        builder = addElementIfRequired(builder, CodegenElementType.MODEL, dialogOptions.getModelPackage(), dialogOptions.isGenerateModel());
        builder = addElementIfRequired(builder, CodegenElementType.REPOSITORY, dialogOptions.getRepositoryPackage(), dialogOptions.isGenerateRepository());
        builder = addElementIfRequired(builder, CodegenElementType.SERVICE, dialogOptions.getServicePackage(), dialogOptions.isGenerateService());
        builder = addElementIfRequired(builder, CodegenElementType.CONTROLLER, dialogOptions.getControllerPackage(), dialogOptions.isGenerateController());
        return builder.build();
    }

    CodegenOptions.CodegenOptionsBuilder addElementIfRequired(
            CodegenOptions.CodegenOptionsBuilder builder,
            CodegenElementType elementType,
            String packageName,
            boolean shouldGenerateElement) {
        if (shouldGenerateElement) {
            CodegenElement model = new CodegenElement(packageName, elementType);
            return builder.element(model);
        } else {
            return builder;
        }
    }
}
