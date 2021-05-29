package com.github.javaica.springer;

import com.github.javaica.springer.codegen.ComponentGenerator;
import com.github.javaica.springer.model.ComponentConfig;
import com.github.javaica.springer.model.ComponentDialogOptions;
import com.github.javaica.springer.model.ComponentOptions;
import com.github.javaica.springer.model.ComponentType;
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

    private Consumer<ComponentDialogOptions> dialogCallback(AnActionEvent event) {
        return options -> CommandProcessor.getInstance().executeCommand(
                event.getProject(),
                () -> generate(event, options),
                "Springer Codegen",
                null);
    }

    private void generate(AnActionEvent event, ComponentDialogOptions dialogOptions) {
        ComponentOptions options = createOptions(event, dialogOptions);
        ComponentGenerator.getInstance().generate(options);
    }

    private ComponentOptions createOptions(AnActionEvent event, ComponentDialogOptions dialogOptions) {
        Objects.requireNonNull(event.getProject(), "Cannot resolve project where classes should be generated");
        ComponentOptions.ComponentOptionsBuilder builder = ComponentOptions.builder()
                .project(event.getProject())
                .originalEntity(event.getData(CommonDataKeys.PSI_FILE));
        builder = addElementIfRequired(builder, ComponentType.MODEL, dialogOptions.getModelPackage(), dialogOptions.isGenerateModel());
        builder = addElementIfRequired(builder, ComponentType.REPOSITORY, dialogOptions.getRepositoryPackage(), dialogOptions.isGenerateRepository());
        builder = addElementIfRequired(builder, ComponentType.SERVICE, dialogOptions.getServicePackage(), dialogOptions.isGenerateService());
        builder = addElementIfRequired(builder, ComponentType.CONTROLLER, dialogOptions.getControllerPackage(), dialogOptions.isGenerateController());
        return builder.build();
    }

    ComponentOptions.ComponentOptionsBuilder addElementIfRequired(
            ComponentOptions.ComponentOptionsBuilder builder,
            ComponentType elementType,
            String packageName,
            boolean shouldGenerateElement) {
        if (shouldGenerateElement) {
            ComponentConfig model = new ComponentConfig(packageName, elementType);
            return builder.component(model);
        } else {
            return builder;
        }
    }
}
