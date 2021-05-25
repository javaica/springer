package com.github.javaica.springer.codegen;

import com.github.javaica.springer.model.CodegenElement;
import com.github.javaica.springer.model.CodegenOptions;
import com.intellij.ide.util.PackageUtil;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiType;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class SpringerCodeGenerator {

    public void generate(CodegenOptions options) {
        options.getElements().stream()
                .map(element -> createElementOptions(options, element))
                .forEach(this::generate);
    }

    private CodegenElementOptions createElementOptions(CodegenOptions options, CodegenElement element) {
        return new CodegenElementOptions(options.getProject(), element.getPsiPackage(), options.getOriginalEntity(), element.getType());
    }

    private void generate(CodegenElementOptions options) {
        Module module = ModuleUtil.findModuleForFile(options.getPsiFile());
        Objects.requireNonNull(module, "Cannot find module where class should be generated");

        PsiDirectory dir = PackageUtil.findOrCreateDirectoryForPackage(
                module,
                options.getPsiPackage().getQualifiedName(),
                null,
                false);
        Objects.requireNonNull(dir, "Cannot find directory where class should be placed");

        dir.add(Objects.requireNonNull(createClassFile(options)));
    }

    //generate class file for each element
    private PsiFile createClassFile(CodegenElementOptions options) {

        PsiClass entityClass = getEntityClass(options.getPsiFile());
        if (entityClass == null) {
            Messages.showErrorDialog("The current file is not Entity", "Error");
            return null;
        }

        String primaryKeyType = "Long";
        PsiField idField = getIdField(entityClass);

        if (idField == null) {
            Messages.showErrorDialog("The entity does not have a field with @id", "Id Field not Found");
        } else {
            PsiType type = idField.getType();

            primaryKeyType = type.getCanonicalText();
        }

        JavaDirectoryService directoryService = JavaDirectoryService.getInstance();
        PsiDirectory directory = entityClass.getContainingFile().getContainingDirectory();
        String className = entityClass.getName() + options.getElementType().toString();
        String codeTemplate = generateCode(options);

        Map<String, String> props = new HashMap<>();

        props.put("Entity", entityClass.getName());
        props.put("PrimaryKeyType", primaryKeyType);
        PsiClass generatedClass = null;

        if (directory.findFile(className + ".java") != null) {
            int ans = Messages.showOkCancelDialog("File already exists. Do you want to override?",
                    "File Already Exist", "Override", "Cancel", null);

            if (ans == Messages.YES) {
                Objects.requireNonNull(directory.findFile(className + ".java")).delete();
                generatedClass = directoryService.createClass(directory, className, codeTemplate, true, props);
            }

        } else {
            generatedClass = directoryService.createClass(directory, className, codeTemplate, true, props);
        }

        Project project = options.getProject();
        final PsiClass finalGeneratedClass = generatedClass;

        WriteCommandAction.runWriteCommandAction(project, () -> {
            shortenClassReferences(project, finalGeneratedClass.getContainingFile());
        });

        return finalGeneratedClass.getContainingFile();
    }


    private PsiClass getEntityClass(PsiFile entityClass) {
        PsiJavaFile psiJavaFile = (PsiJavaFile) entityClass;
        final PsiClass[] psiClasses = psiJavaFile.getClasses();
        for (PsiClass psiClass : psiClasses) {
            if (psiClass.hasAnnotation("javax.persistence.Entity")) {
                return psiClass;
            }
        }
        return null;
    }

    private PsiField getIdField(PsiClass entityClass) {
        for (PsiField psiField : entityClass.getFields()) {
            if (psiField.hasAnnotation("javax.persistence.Id")) {
                return psiField;
            }
        }
        return null;
    }

    private void shortenClassReferences(Project project, PsiFile file) {
        if(!(file instanceof PsiJavaFile)) {
            return;
        }
        final PsiJavaFile javaFile = (PsiJavaFile) file;
        JavaCodeStyleManager.getInstance(project).shortenClassReferences(javaFile);
    }

    private String generateCode(CodegenElementOptions options) {
        String codeTemplate;
        switch (options.getElementType()) {
            case MODEL:
                codeTemplate = "SpringModel.java";
                break;
            case REPOSITORY:
                codeTemplate = "SpringDataRepo.java";
                break;
            case SERVICE:
                codeTemplate = "SpringService.java";
                break;
            case CONTROLLER:
                codeTemplate = "SpringController.java";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + options.getElementType());
        }
        return codeTemplate;
    }

    public static SpringerCodeGenerator getInstance() {
        return Optional.ofNullable(ServiceManager.getService(SpringerCodeGenerator.class))
                .orElseThrow();
    }
}
