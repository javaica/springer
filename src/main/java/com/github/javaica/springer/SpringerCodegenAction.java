package com.github.javaica.springer;

import com.github.javaica.springer.codegen.SpringerCodeGenerator;
import com.github.javaica.springer.model.CodegenDialogOptions;
import com.github.javaica.springer.model.CodegenElement;
import com.github.javaica.springer.model.CodegenElementType;
import com.github.javaica.springer.model.CodegenOptions;
import com.github.javaica.springer.ui.GeneratorDialogUI;
import com.github.javaica.springer.util.PackageResolver;
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
        PackageResolver resolver = new PackageResolver(event.getProject());
        CodegenOptions.CodegenOptionsBuilder builder = CodegenOptions.builder()
                .project(event.getProject())
                .originalEntity(event.getData(CommonDataKeys.PSI_FILE));
        builder = addModelOptionsIfRequired(resolver, builder, dialogOptions);
        builder = addRepositoryOptionsIfRequired(resolver, builder, dialogOptions);
        builder = addServiceOptionsIfRequired(resolver, builder, dialogOptions);
        builder = addControllerOptionsIfRequired(resolver, builder, dialogOptions);
        return builder.build();
    }

    CodegenOptions.CodegenOptionsBuilder addModelOptionsIfRequired(
            PackageResolver resolver,
            CodegenOptions.CodegenOptionsBuilder builder,
            CodegenDialogOptions dialogOptions) {
        if (dialogOptions.isGenerateModel()) {
            CodegenElement model = new CodegenElement(
                    resolver.getPackage(dialogOptions.getModelPackage()),
                    CodegenElementType.MODEL);
            return builder.element(model);
        } else {
            return builder;
        }
    }

    CodegenOptions.CodegenOptionsBuilder addRepositoryOptionsIfRequired(
            PackageResolver resolver,
            CodegenOptions.CodegenOptionsBuilder builder,
            CodegenDialogOptions dialogOptions) {
        if (dialogOptions.isGenerateRepository()) {
            CodegenElement repository = new CodegenElement(
                    resolver.getPackage(dialogOptions.getRepositoryPackage()),
                    CodegenElementType.REPOSITORY);
            return builder.element(repository);
        } else {
            return builder;
        }
    }

    CodegenOptions.CodegenOptionsBuilder addServiceOptionsIfRequired(
            PackageResolver resolver,
            CodegenOptions.CodegenOptionsBuilder builder,
            CodegenDialogOptions dialogOptions) {
        if (dialogOptions.isGenerateService()) {
            CodegenElement service = new CodegenElement(
                    resolver.getPackage(dialogOptions.getServicePackage()),
                    CodegenElementType.SERVICE);
            return builder.element(service);
        } else {
            return builder;
        }
    }

    CodegenOptions.CodegenOptionsBuilder addControllerOptionsIfRequired(
            PackageResolver resolver,
            CodegenOptions.CodegenOptionsBuilder builder,
            CodegenDialogOptions dialogOptions) {
        if (dialogOptions.isGenerateController()) {
            CodegenElement controller = new CodegenElement(
                    resolver.getPackage(dialogOptions.getControllerPackage()),
                    CodegenElementType.CONTROLLER);
            return builder.element(controller);
        } else {
            return builder;
        }
    }
}
