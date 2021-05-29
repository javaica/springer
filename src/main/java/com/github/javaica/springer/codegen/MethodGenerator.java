package com.github.javaica.springer.codegen;

import com.github.javaica.springer.model.MethodOptions;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class MethodGenerator {

    private final Project project;

    public void generateMethods(MethodOptions options) {
        generateModelMethods(options);
        generateRepositoryMethods(options);
        generateServiceMethods(options);
        generateControllerMethods(options);
    }

    public void generateModelMethods(MethodOptions options) {
        WriteCommandAction.runWriteCommandAction(project, () ->
                options.getFields()
                        .forEach(options.getModel()::add)
        );
    }

    public void generateRepositoryMethods(MethodOptions options) {

    }

    public void generateServiceMethods(MethodOptions options) {

    }

    public void generateControllerMethods(MethodOptions options) {

    }

    public static MethodGenerator getInstance(Project project) {
        return Optional.ofNullable(ServiceManager.getService(project, MethodGenerator.class))
                .orElseThrow();
    }
}
