package com.github.javaica.springer.codegen;

import com.github.javaica.springer.model.MethodOptions;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiUtil;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MethodGenerator {

    private final Project project;
    private MethodGenUtil methodGenUtil;
    private AnnotationUtil stuffGenerator;
    String CONSTRUCTOR_TEMPLATE = "%s(%s){%s}";
    String CONSTRUCTOR_ARGUMENT_TEMPLATE = "%s %s";
    String CONSTRUCTOR_ASSIGNMENT_TEMPLATE = "this.%s = %s;";


    public void generateMethods(MethodOptions options) {
        stuffGenerator = new AnnotationUtil(project);
        methodGenUtil = new MethodGenUtil(project);

        generateModelMethods(options);
        generateRepositoryMethods(options);
        generateServiceMethods(options);
        generateControllerMethods(options);
    }

    public void generateModelMethods(MethodOptions options) {
        WriteCommandAction.runWriteCommandAction(project, () ->
                Arrays.stream(options.getEntity().getFields())
                        .forEach(options.getModel()::add)
        );
    }

    public void generateRepositoryMethods(MethodOptions options) {
        if (options.getDialogOptions().isGet())
            WriteCommandAction.runWriteCommandAction(project, (Computable<PsiElement>) () ->
                    options.getRepository().add(methodGenUtil.repoGetByField(Arrays.stream(options.getEntity().getFields())
                    .filter(el -> el.hasAnnotation("javax.persistence.Id"))
                    .findAny()
                    .orElseThrow(), options.getEntity())));

    }

    public void generateServiceMethods(MethodOptions options) {
        List<PsiMember> implementMembers = new ArrayList<>();

        PsiField repositoryField = getElementFactory().createFieldFromText(String.format(
                "private final %s %s;", options.getRepository().getName(), "repository"),
                options.getService().getContext());

        PsiMethod constructor = extractConstructorForClass(options.getService(),
                Collections.singletonList(repositoryField),
                Collections.singletonList(repositoryField));

        PsiUtil.setModifierProperty(constructor, PsiModifier.PUBLIC, true);
        implementMembers.add(constructor);
        implementMembers.add(repositoryField);

        if (options.getDialogOptions().isGet())
            implementMembers.add(methodGenUtil.serviceGetByField(Arrays.stream(options.getEntity().getFields())
                    .filter(el -> el.hasAnnotation("javax.persistence.Id"))
                    .findAny()
                    .orElseThrow(), options.getEntity()));

        if (options.getDialogOptions().isPost())
            implementMembers.add(methodGenUtil.servicePost(options.getEntity()));

        if (options.getDialogOptions().isPut())
            implementMembers.add(methodGenUtil.servicePut(options.getEntity()));

        if (options.getDialogOptions().isDelete())
            implementMembers.add(methodGenUtil.serviceDelete(options.getEntity()));

        implementMembers
                .forEach(method ->
                        WriteCommandAction.runWriteCommandAction(project,
                                (Computable<PsiElement>) () -> options.getService().add(method)));

        JavaCodeStyleManager.getInstance(project).shortenClassReferences(options.getService());
    }

    public void generateControllerMethods(MethodOptions options) {
        List<PsiMember> implementMethods = new ArrayList<>();

        if (options.getDialogOptions().isGet())
            implementMethods.add(methodGenUtil.controllerGet(Arrays.stream(options.getEntity().getFields())
                    .filter(el -> el.hasAnnotation("javax.persistence.Id"))
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

    private PsiMethod extractConstructorForClass(PsiClass psiClass,
                                                 List<PsiField> ctrArgs,
                                                 List<PsiField> ctrArgsToAssign) {

        String constructorArgsPart = ctrArgs.stream()
                .map(psiField -> String.format(CONSTRUCTOR_ARGUMENT_TEMPLATE, psiField.getType().getCanonicalText(), psiField.getNameIdentifier().getText()))
                .collect(Collectors.joining(", "));

        String constructorArgsAssignPart = ctrArgsToAssign.stream()
                .map(psiField -> String.format(CONSTRUCTOR_ASSIGNMENT_TEMPLATE, psiField.getNameIdentifier().getText(), psiField.getNameIdentifier().getText()))
                .collect(Collectors.joining(""));

        String constructor = String.format(CONSTRUCTOR_TEMPLATE, psiClass.getName(), constructorArgsPart, constructorArgsAssignPart);
        return JavaPsiFacade.getElementFactory(psiClass.getProject()).createMethodFromText(constructor, psiClass);
    }

    private PsiElementFactory getElementFactory() {
        return JavaPsiFacade.getInstance(this.project).getElementFactory();
    }

    public static MethodGenerator getInstance(Project project) {
        return Optional.ofNullable(ServiceManager.getService(project, MethodGenerator.class))
                .orElseThrow();
    }
}
