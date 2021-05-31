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
import com.intellij.psi.util.PsiUtil;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.javaica.springer.codegen.util.ImportClassConstants.DELETE_MAPPING_PATH;
import static com.github.javaica.springer.codegen.util.ImportClassConstants.GET_MAPPING_PATH;
import static com.github.javaica.springer.codegen.util.ImportClassConstants.POST_MAPPING_PATH;
import static com.github.javaica.springer.codegen.util.ImportClassConstants.PUT_MAPPING_PATH;
import static com.github.javaica.springer.codegen.util.ImportClassConstants.REQUEST_MAPPING;
import static com.github.javaica.springer.codegen.util.ImportClassConstants.REQUEST_MAPPING_PATH;

@RequiredArgsConstructor
public class MethodGenerator {

    private final Project project;
    private MethodGenUtil methodGenUtil;
    String CONSTRUCTOR_TEMPLATE = "%s(%s){%s}";
    String CONSTRUCTOR_ARGUMENT_TEMPLATE = "%s %s";
    String CONSTRUCTOR_ASSIGNMENT_TEMPLATE = "this.%s = %s;";


    public void generateMethods(MethodOptions options) {
        methodGenUtil = new MethodGenUtil(project);

        generateModelMethods(options);
        //generateRepositoryMethods(options);
        generateServiceMethods(options);
        generateControllerMethods(options);
    }

    private void generateModelMethods(MethodOptions options) {
        WriteCommandAction.runWriteCommandAction(project, () ->
                Arrays.stream(options.getEntity().getFields())
                        .forEach(options.getModel()::add)
        );

        Arrays.stream(options
                .getModel()
                .getFields())
                .forEach(field ->
                        getAnnotationUtil().removeAnnotations(options.getModel(), field));
    }

    private void generateRepositoryMethods(MethodOptions options) {
        if (options.getDialogOptions().isGet())
            WriteCommandAction.runWriteCommandAction(project, (Computable<PsiElement>) () ->
                    options.getRepository().add(methodGenUtil.repoGetByField(Arrays.stream(options.getEntity().getFields())
                    .filter(el -> el.hasAnnotation("javax.persistence.Id"))
                    .findAny()
                    .orElseThrow(), options.getEntity())));
    }

    private void generateServiceMethods(MethodOptions options) {
        List<PsiMember> implementMembers = new ArrayList<>();

        PsiField repositoryField = getElementFactory().createFieldFromText(String.format(
                "private final %s %s;", options.getRepository().getName(), "repository"),
                options.getService().getContext());

        PsiMethod constructor = extractConstructorForClass(options.getService(),
                Collections.singletonList(repositoryField),
                Collections.singletonList(repositoryField))
                .orElseThrow();

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
    }

    private void generateControllerMethods(MethodOptions options) {
        List<PsiMember> implementMembers = new ArrayList<>();

        String qualifiedServiceName = String.format("%sService",
                Objects.requireNonNull(options.getEntity().getName()).toLowerCase());

        PsiField serviceField = getElementFactory().createFieldFromText(String.format(
                "private final %s %s;",
                options.getService().getName(),
                qualifiedServiceName),
                options.getService().getContext());

        PsiMethod constructor = extractConstructorForClass(options.getService(),
                Collections.singletonList(serviceField),
                Collections.singletonList(serviceField))
                .orElseThrow();

        PsiUtil.setModifierProperty(constructor, PsiModifier.PUBLIC, true);
        implementMembers.add(constructor);
        implementMembers.add(serviceField);

        if (options.getDialogOptions().isGet()) {
            implementMembers.add(methodGenUtil.controllerGet(Arrays.stream(options.getEntity().getFields())
                    .filter(el -> el.hasAnnotation("javax.persistence.Id"))
                    .findAny()
                    .orElseThrow(), options.getEntity()));

            getAnnotationUtil().addImportStatement(options.getController(), GET_MAPPING_PATH);
        }

        if (options.getDialogOptions().isPost()) {
            implementMembers.add(methodGenUtil.controllerPost(options.getEntity()));

            getAnnotationUtil().addImportStatement(options.getController(), POST_MAPPING_PATH);
        }

        if (options.getDialogOptions().isPut()) {
            implementMembers.add(methodGenUtil.controllerPut(options.getEntity()));

            getAnnotationUtil().addImportStatement(options.getController(), PUT_MAPPING_PATH);
        }

        if (options.getDialogOptions().isDelete()) {
            implementMembers.add(methodGenUtil.controllerDelete(options.getEntity()));

            getAnnotationUtil().addImportStatement(options.getController(), DELETE_MAPPING_PATH);
        }

        implementMembers
                .forEach(method ->
                        WriteCommandAction.runWriteCommandAction(project,
                                (Computable<PsiElement>) () ->
                                        options.getController().add(method)));

        String requestMappingAsString = String.format("%s(\"/%s\")", REQUEST_MAPPING,
                Objects.requireNonNull(options.getEntity().getName()).toLowerCase());

        getAnnotationUtil().addQualifiedAnnotationName(requestMappingAsString, options.getController());
        getAnnotationUtil().addImportStatement(options.getController(), REQUEST_MAPPING_PATH);
    }

    private Optional<PsiMethod> extractConstructorForClass(PsiClass psiClass,
                                                 List<PsiField> ctrArgs,
                                                 List<PsiField> ctrArgsToAssign) {

        String constructorArgsPart = ctrArgs.stream()
                .map(psiField -> String.format(CONSTRUCTOR_ARGUMENT_TEMPLATE, psiField.getType().getCanonicalText(), psiField.getNameIdentifier().getText()))
                .collect(Collectors.joining(", "));

        String constructorArgsAssignPart = ctrArgsToAssign.stream()
                .map(psiField -> String.format(CONSTRUCTOR_ASSIGNMENT_TEMPLATE, psiField.getNameIdentifier().getText(), psiField.getNameIdentifier().getText()))
                .collect(Collectors.joining(""));

        String constructor = String.format(CONSTRUCTOR_TEMPLATE, psiClass.getName(), constructorArgsPart, constructorArgsAssignPart);

        return Optional.of(
                JavaPsiFacade.getElementFactory(psiClass.getProject()).createMethodFromText(constructor, psiClass));
    }

    private PsiElementFactory getElementFactory() {
        return JavaPsiFacade.getInstance(project).getElementFactory();
    }

    private AnnotationUtil getAnnotationUtil() {
        return AnnotationUtil.getInstance(project);
    }

    public static MethodGenerator getInstance(Project project) {
        return Optional.ofNullable(ServiceManager.getService(project, MethodGenerator.class))
                .orElseThrow();
    }
}
