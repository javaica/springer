package com.github.javaica.springer.codegen;

import com.github.javaica.springer.model.MethodOptions;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MethodGenerator {

    private final Project project;
    private MethodGenUtil methodGenUtil;

    public void generateMethods(MethodOptions options) {
        methodGenUtil = new MethodGenUtil(project);
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
        if (options.getDialogOptions().isGet())
            WriteCommandAction.runWriteCommandAction(project, (Computable<PsiElement>) () ->
                    options.getRepository().add(methodGenUtil.repoGetByField(options.getFields()
                    .stream()
                    .filter(el -> el.hasAnnotation("javax.persistence.id"))
                    .findAny()
                    .orElseThrow(), options.getEntity())));

    }

    public void generateServiceMethods(MethodOptions options) {
        List<PsiMethod> implementMethods = new ArrayList<>();

        if (options.getDialogOptions().isGet())
            implementMethods.add(methodGenUtil.serviceGetByField(options.getFields()
                    .stream()
                    .filter(el -> el.hasAnnotation("javax.persistence.id"))
                    .findAny()
                    .orElseThrow(), options.getEntity()));

        if (options.getDialogOptions().isPost())
            implementMethods.add(methodGenUtil.servicePost(options.getEntity()));

        if (options.getDialogOptions().isPut())
            implementMethods.add(methodGenUtil.servicePut(options.getEntity()));

        if (options.getDialogOptions().isDelete())
            implementMethods.add(methodGenUtil.serviceDelete(options.getEntity()));

        implementMethods
                .forEach(method ->
                        WriteCommandAction.runWriteCommandAction(project,
                                (Computable<PsiElement>) () ->
                                        options.getService().add(method)));
    }

    public void generateControllerMethods(MethodOptions options) {
        List<PsiMethod> implementMethods = new ArrayList<>();

        if (options.getDialogOptions().isGet())
            implementMethods.add(methodGenUtil.controllerGet(options.getFields()
                    .stream()
                    .filter(el -> el.hasAnnotation("javax.persistence.id"))
                    .findAny()
                    .orElseThrow(), options.getEntity()));

        if (options.getDialogOptions().isPost())
            implementMethods.add(methodGenUtil.controllerPost(options.getEntity()));

        if (options.getDialogOptions().isPut())
            implementMethods.add(methodGenUtil.controllerPut(options.getEntity()));

        if (options.getDialogOptions().isDelete())
            implementMethods.add(methodGenUtil.controllerDelete(options.getEntity()));

        implementMethods
                .forEach(method ->
                        WriteCommandAction.runWriteCommandAction(project,
                                (Computable<PsiElement>) () ->
                                        options.getController().add(method)));
    }

    public static MethodGenerator getInstance(Project project) {
        return Optional.ofNullable(ServiceManager.getService(project, MethodGenerator.class))
                .orElseThrow();
    }
}
