package com.github.javaica.springer.codegen;

import com.github.javaica.springer.model.Component;
import com.github.javaica.springer.model.ComponentConfig;
import com.github.javaica.springer.model.ComponentOptions;
import com.intellij.ide.util.PackageUtil;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

import java.util.Arrays;
import java.util.Optional;

public class ComponentGenerator {

    public void generate(ComponentOptions options) {
        Optional<PsiClass> entityClassOptional = getEntityClass(options.getOriginalEntity());
        if (entityClassOptional.isEmpty()) {
            Messages.showErrorDialog("The current file is not Entity", "Error");
            return;
        }
        options.getComponents().stream()
                .map(element -> tryCreateElementOptions(entityClassOptional.get(), options, element))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(this::generate);
    }

    private Optional<Component> tryCreateElementOptions(PsiClass original, ComponentOptions options, ComponentConfig element) {
        Module module = ModuleUtil.findModuleForFile(original.getContainingFile());
        if (module == null) {
            Messages.showErrorDialog("Cannot find module where class should be generated", "Module Not Found");
            return Optional.empty();
        }

        Optional<PsiDirectory> directory = getDirectoryOfPackage(module, element.getPsiPackage());
        if (directory.isEmpty()) {
            Messages.showErrorDialog("Cannot find directory where class should be placed", "Directory Not Found");
            return Optional.empty();
        }

        return Optional.of(
                Component.builder()
                        .project(options.getProject())
                        .location(directory.get())
                        .original(original)
                        .elementType(element.getType())
                        .name(element.getType().createClassName(original))
                        .build());
    }

    private void generate(Component options) {
        if (shouldExitWhenFileExists(options.getOriginal(), options))
            return;
        Optional.ofNullable(options.getLocation().findFile(options.getName() + ".java"))
                .ifPresent(PsiFile::delete);

        Optional<String> primaryKeyType = getIdField(options.getOriginal())
                .map(field -> field.getType().getCanonicalText());
        if (primaryKeyType.isEmpty()) {
            Messages.showErrorDialog("The entity does not have a field with @id", "Id Field not Found");
            return;
        }

        PsiClass generatedClass = options.getElementType()
                .generateClass(options.getOriginal(), options.getLocation(), primaryKeyType.get());

        generateMethodsForSpecClass(options, generatedClass);

        WriteCommandAction.runWriteCommandAction(options.getProject(),
                () -> shortenClassReferences(options.getProject(), generatedClass.getContainingFile()));
    }

    private void generateMethodsForSpecClass(Component options, PsiClass psiClass) {
        // TODO: 5/25/2021 fields adding should be optional and prohibited for repo (interface)
        WriteCommandAction.runWriteCommandAction(options.getProject(), () ->
                Arrays.stream(options
                        .getOriginal()
                        .getFields())
                        .forEach(psiClass::add));

        MethodGenerator methodGenerator = new MethodGenerator();

        methodGenerator.generateMethods(psiClass);
    }

    private Optional<PsiDirectory> getDirectoryOfPackage(Module module, String packageName) {
         return Optional.ofNullable(PackageUtil.findOrCreateDirectoryForPackage(
                module,
                packageName,
                null,
                false)
         );
    }

    private Optional<PsiClass> getEntityClass(PsiFile entityClass) {
        PsiJavaFile psiJavaFile = (PsiJavaFile) entityClass;
        return Arrays.stream(psiJavaFile.getClasses())
                .filter(psiClass -> psiClass.hasAnnotation("javax.persistence.Entity"))
                .findAny();
    }

    private Optional<PsiField> getIdField(PsiClass entityClass) {
        return Arrays.stream(entityClass.getFields())
                .filter(field -> field.hasAnnotation("javax.persistence.Id"))
                .findAny();
    }

    private boolean shouldExitWhenFileExists(PsiClass entityClass, Component options) {
        PsiDirectory directory = options.getOriginal().getContainingFile().getContainingDirectory();
        String className = options.getElementType().createClassName(entityClass);
        if (directory.findFile(className + ".java") == null)
            return false;

        int answer = Messages.showOkCancelDialog("File already exists. Do you want to override?",
                "File Already Exist", "Override", "Cancel", null);
        return answer == Messages.OK;
    }

    private void shortenClassReferences(Project project, PsiFile file) {
        if(!(file instanceof PsiJavaFile))
            return;
        final PsiJavaFile javaFile = (PsiJavaFile) file;
        JavaCodeStyleManager.getInstance(project).shortenClassReferences(javaFile);
    }

    public static ComponentGenerator getInstance() {
        return Optional.ofNullable(ServiceManager.getService(ComponentGenerator.class))
                .orElseThrow();
    }
}
