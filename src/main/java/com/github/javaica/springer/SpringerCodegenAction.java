package com.github.javaica.springer;

import com.github.javaica.springer.codegen.ComponentGenerator;
import com.github.javaica.springer.codegen.MethodGenerator;
import com.github.javaica.springer.codegen.PsiUtilService;
import com.github.javaica.springer.model.ComponentConfig;
import com.github.javaica.springer.model.ComponentDialogOptions;
import com.github.javaica.springer.model.ComponentOptions;
import com.github.javaica.springer.model.ComponentType;
import com.github.javaica.springer.model.GeneratedComponent;
import com.github.javaica.springer.model.MethodDialogOptions;
import com.github.javaica.springer.model.MethodOptions;
import com.github.javaica.springer.ui.ComponentDialogWrapper;
import com.github.javaica.springer.ui.MethodDialogWrapper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiPackage;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class SpringerCodegenAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ComponentDialogWrapper dialog = new ComponentDialogWrapper(e.getProject(), dialogCallback(e));
        dialog.pack();
        dialog.show();
    }

    private Consumer<ComponentDialogOptions> dialogCallback(AnActionEvent event) {
        return options -> {
            if (!options.isGenerateMethods()) {
                generateComponents(event, options);
                return;
            }
            MethodDialogWrapper dialog = new MethodDialogWrapper(event.getProject(), methodDialogCallback(event, options));
            dialog.pack();
            dialog.show();
        };
    }

    private Consumer<MethodDialogOptions> methodDialogCallback(AnActionEvent event, ComponentDialogOptions componentOptions) {
        return options -> {
                    List<GeneratedComponent> components = generateComponents(event, componentOptions);
                    generateMethods(event, options, components);
                };
    }

    private List<GeneratedComponent> generateComponents(AnActionEvent event, ComponentDialogOptions dialogOptions) {
        Optional<ComponentOptions> componentOptions = createComponentOptions(event, dialogOptions);
        return componentOptions
                .map(ComponentGenerator.getInstance(event.getProject())::generate)
                .orElse(Collections.emptyList());
    }

    private void generateMethods(AnActionEvent event,
                                 MethodDialogOptions dialogOptions,
                                 List<GeneratedComponent> components) {
        Optional<PsiClass> model = components.stream()
                .filter(component -> component.getType() == ComponentType.MODEL)
                .map(GeneratedComponent::getComponent)
                .findAny();
        Optional<PsiClass> repo = components.stream()
                .filter(component -> component.getType() == ComponentType.REPOSITORY)
                .map(GeneratedComponent::getComponent)
                .findAny();
        Optional<PsiClass> service = components.stream()
                .filter(component -> component.getType() == ComponentType.SERVICE)
                .map(GeneratedComponent::getComponent)
                .findAny();
        Optional<PsiClass> controller = components.stream()
                .filter(component -> component.getType() == ComponentType.CONTROLLER)
                .map(GeneratedComponent::getComponent)
                .findAny();
        if (model.isEmpty() || repo.isEmpty() || service.isEmpty() || controller.isEmpty())
            return;

        Optional<MethodOptions> methodOptions = createMethodOptions(event,
                model.get(), repo.get(), service.get(), controller.get(),
                dialogOptions);

        methodOptions.ifPresent(options -> MethodGenerator.getInstance(event.getProject())
                .generateMethods(options));
    }

    private Optional<ComponentOptions> createComponentOptions(AnActionEvent event, ComponentDialogOptions dialogOptions) {
        Objects.requireNonNull(event.getProject(), "Cannot resolve project where classes should be generated");

        Optional<PsiClass> entity = PsiUtilService.getInstance().getEntityClass(event.getData(CommonDataKeys.PSI_FILE));
        if (entity.isEmpty()) {
            Messages.showErrorDialog("The current file is not Entity", "Error");
            return Optional.empty();
        }

        ComponentOptions.ComponentOptionsBuilder builder = ComponentOptions.builder()
                .entity(entity.get());

        builder = addElementIfRequired(builder, ComponentType.MODEL, dialogOptions.getModelPackage(), dialogOptions.isGenerateModel());
        builder = addElementIfRequired(builder, ComponentType.REPOSITORY, dialogOptions.getRepositoryPackage(), dialogOptions.isGenerateRepository());
        builder = addElementIfRequired(builder, ComponentType.SERVICE, dialogOptions.getServicePackage(), dialogOptions.isGenerateService());
        builder = addElementIfRequired(builder, ComponentType.CONTROLLER, dialogOptions.getControllerPackage(), dialogOptions.isGenerateController());

        return Optional.of(builder.build());
    }

    private Optional<MethodOptions> createMethodOptions(AnActionEvent event,
                                                        PsiClass model,
                                                        PsiClass repository,
                                                        PsiClass service,
                                                        PsiClass controller,
                                                        MethodDialogOptions dialogOptions) {
        Objects.requireNonNull(event.getProject(), "Cannot resolve project where classes should be generated");
        Optional<PsiClass> entity = PsiUtilService.getInstance().getEntityClass(event.getData(CommonDataKeys.PSI_FILE));
        if (entity.isEmpty())
            return Optional.empty();
        MethodOptions result = MethodOptions.builder()
                .entity(entity.get())
                .model(model)
                .repository(repository)
                .service(service)
                .controller(controller)
                .dialogOptions(dialogOptions)
                .build();
        return Optional.of(result);
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
