package com.github.javaica.springer.codegen;

import com.github.javaica.springer.model.MethodOptions;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.JavaPsiFacade;

import java.util.Optional;

public class MethodGenerator {

    MethodGenUtil methodGenUtil;
    MethodOptions options;

    public MethodGenerator(MethodOptions options) {
        this.options = options;
        this.methodGenUtil = new MethodGenUtil(
                JavaPsiFacade.getInstance(options.getProject())
                .getElementFactory(), new DefaultStuffGenerator());
    }

    public void generateMethods(MethodOptions options) {
        generateModelMethods(options);
        generateRepositoryMethods(options);
        generateServiceMethods(options);
        generateControllerMethods(options);
    }

    public void generateModelMethods(MethodOptions options) {
        WriteCommandAction.runWriteCommandAction(options.getProject(), () ->
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

    public static MethodGenerator getInstance() {
        return Optional.ofNullable(ServiceManager.getService(MethodGenerator.class))
                .orElseThrow();
    }
}
