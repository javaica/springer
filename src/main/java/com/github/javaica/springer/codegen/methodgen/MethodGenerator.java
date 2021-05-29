package com.github.javaica.springer.codegen.methodgen;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.JavaPsiFacade;

public class MethodGenerator {

    MethodGenUtil methodGenUtil;
    MethodOptions options;

    public MethodGenerator(MethodOptions options) {
        this.options = options;
        this.methodGenUtil = new MethodGenUtil(
                JavaPsiFacade.getInstance(options.getProject())
                .getElementFactory(), new DefaultStuffGenerator());
    }

    public void generateMethods() {
        generateForModel();
        generateForRepository();
        generateForService();
        generateForController();
    }

    public void generateForModel() {
        WriteCommandAction.runWriteCommandAction(options.getProject(), () ->
                options.getFields()
                        .forEach(
                                options.getModel()::add)
        );
    }

    public void generateForRepository() {

    }

    public void generateForService() {

    }

    public void generateForController() {

    }
}
