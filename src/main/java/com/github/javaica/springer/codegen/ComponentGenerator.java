package com.github.javaica.springer.codegen;

import com.github.javaica.springer.model.Component;
import com.github.javaica.springer.model.ComponentConfig;
import com.github.javaica.springer.model.ComponentOptions;
import com.github.javaica.springer.model.GeneratedComponent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ComponentGenerator {

    private final Project project;

    public List<GeneratedComponent> generate(ComponentOptions options) {
        return options.getComponents()
                .stream()
                .map(element -> tryCreateElementOptions(options, element))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(component -> generate(component)
                        .map(generated -> new GeneratedComponent(generated, component.getType())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<Component> tryCreateElementOptions(ComponentOptions options, ComponentConfig element) {
        PsiClass original = options.getEntity();

        Module module = ModuleUtil.findModuleForFile(original.getContainingFile());
        if (module == null) {
            Messages.showErrorDialog("Cannot find module where class should be generated", "Module Not Found");
            return Optional.empty();
        }

        Optional<PsiDirectory> directory = PsiUtilService.getInstance()
                .getDirectoryOfPackage(module, element.getPsiPackage());
        if (directory.isEmpty()) {
            Messages.showErrorDialog("Cannot find directory where class should be placed", "Directory Not Found");
            return Optional.empty();
        }

        return Optional.of(
                Component.builder()
                        .location(directory.get())
                        .original(original)
                        .type(element.getType())
                        .name(element.getType().createClassName(original))
                        .build());
    }

    private Optional<PsiClass> generate(Component options) {
        if (shouldExitWhenFileExists(options.getOriginal(), options))
            return Optional.empty();
        Optional.ofNullable(options.getLocation().findFile(options.getName() + ".java"))
                .ifPresent(PsiFile::delete);

        Optional<String> primaryKeyType = getIdField(options.getOriginal())
                .map(field -> field.getType().getCanonicalText());
        if (primaryKeyType.isEmpty()) {
            Messages.showErrorDialog("The entity does not have a field with @id", "Id Field not Found");
            return Optional.empty();
        }

        PsiClass generatedClass = options.getType()
                .generateClass(options.getOriginal(), options.getLocation(), primaryKeyType.get());

        WriteCommandAction.runWriteCommandAction(project,
                () -> shortenClassReferences(generatedClass.getContainingFile()));

        return Optional.ofNullable(generatedClass);
    }

    private Optional<PsiField> getIdField(PsiClass entityClass) {
        return Arrays.stream(entityClass.getFields())
                .filter(field -> field.hasAnnotation("javax.persistence.Id"))
                .findAny();
    }

    private boolean shouldExitWhenFileExists(PsiClass entityClass, Component options) {
        PsiDirectory directory = options.getOriginal().getContainingFile().getContainingDirectory();
        String className = options.getType().createClassName(entityClass);
        if (directory.findFile(className + ".java") == null)
            return false;

        int answer = Messages.showOkCancelDialog("File already exists. Do you want to override?",
                "File Already Exist", "Override", "Cancel", null);
        return answer == Messages.OK;
    }

    private void shortenClassReferences(PsiFile file) {
        if(!(file instanceof PsiJavaFile))
            return;
        final PsiJavaFile javaFile = (PsiJavaFile) file;
        JavaCodeStyleManager.getInstance(project).shortenClassReferences(javaFile);
    }

    public static ComponentGenerator getInstance(Project project) {
        return Optional.ofNullable(ServiceManager.getService(project, ComponentGenerator.class))
                .orElseThrow();
    }
}
