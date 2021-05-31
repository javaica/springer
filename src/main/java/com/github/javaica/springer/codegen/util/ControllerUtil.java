package com.github.javaica.springer.codegen.util;

import com.github.javaica.springer.codegen.AnnotationUtil;
import com.github.javaica.springer.codegen.MethodUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;

import java.util.Objects;
import java.util.Optional;

public class ControllerUtil implements MethodUtil {

    private final Project project;
    private final PsiElementFactory psiElementFactory;
    private final AnnotationUtil annotationUtil;

    private final String GET_MAPPING = "org.springframework.web.bind.annotation.GetMapping";
    private final String POST_MAPPING = "org.springframework.web.bind.annotation.PostMapping";
    private final String PUT_MAPPING = "org.springframework.web.bind.annotation.PutMapping";
    private final String DELETE_MAPPING = "org.springframework.web.bind.annotation.DeleteMapping";
    private final String PATH_VARIABLE = "org.springframework.web.bind.annotation.PathVariable";

    public ControllerUtil(Project project) {
        this.project = project;
        this.annotationUtil = new AnnotationUtil(project);
        psiElementFactory = JavaPsiFacade.getInstance(project).getElementFactory();
    }

    @Override
    public Optional<PsiMethod> get(PsiField psiField, PsiClass entity) {

        Optional<PsiMethod> psiMethod = Optional.of(psiElementFactory.createMethodFromText(String.format(
                "public %s getBy%s(%s %s %s) {" +
                        "return %sService.getBy%s(%s); }",
                entity.getName(),
                psiField.getName().substring(0, 1).toUpperCase() + psiField.getName().substring(1),
                // TODO: 5/31/2021 wtf rewrite
                "@" + PATH_VARIABLE,
                psiField.getType().getPresentableText(),
                psiField.getName(),
                Objects.requireNonNull(entity.getName()).toLowerCase(),
                psiField.getName().substring(0, 1).toUpperCase() + psiField.getName().substring(1),
                psiField.getName()
        ), entity.getContext()));

        String annotationAsString = String.format("%s({\"/%s\"})", GET_MAPPING, psiField.getName());

        psiMethod
                .ifPresent(method -> annotationUtil.addQualifiedAnnotationName(annotationAsString, method));

        return psiMethod;
    }

    @Override
    public Optional<PsiMethod> post(PsiClass entity) {

        Optional<PsiMethod> psiMethod = Optional.of(psiElementFactory.createMethodFromText(String.format(
                "public %s post%s(%s %s) {" +
                        "return %sService.post%s(%s); }",
                entity.getName(),
                Objects.requireNonNull(entity.getName()).substring(0, 1).toUpperCase() + entity.getName().substring(1),
                entity.getName(),
                entity.getName().toLowerCase(),
                entity.getName().toLowerCase(),
                Objects.requireNonNull(entity.getName()).substring(0, 1).toUpperCase() + entity.getName().substring(1),
                entity.getName().toLowerCase()
        ), entity.getContext()));

        String annotationAsString = String.format("%s", POST_MAPPING);

        psiMethod
                .ifPresent(method -> annotationUtil.addQualifiedAnnotationName(annotationAsString, method));

        return psiMethod;
    }

    @Override
    public Optional<PsiMethod> put(PsiClass entity) {

        Optional<PsiMethod> psiMethod = Optional.of(psiElementFactory.createMethodFromText(String.format(
                "public %s put%s(%s %s) {" +
                        "return %sService.put%s(%s); }",
                entity.getName(),
                Objects.requireNonNull(entity.getName()).substring(0, 1).toUpperCase() + entity.getName().substring(1),
                entity.getName(),
                entity.getName().toLowerCase(),
                entity.getName().toLowerCase(),
                Objects.requireNonNull(entity.getName()).substring(0, 1).toUpperCase() + entity.getName().substring(1),
                entity.getName().toLowerCase()
        ), entity.getContext()));

        String annotationAsString = String.format("%s", PUT_MAPPING);

        psiMethod
                .ifPresent(method -> annotationUtil.addQualifiedAnnotationName(annotationAsString, method));

        return psiMethod;
    }

    @Override
    public Optional<PsiMethod> delete(PsiClass entity) {

        Optional<PsiMethod> psiMethod = Optional.of(psiElementFactory.createMethodFromText(String.format(
                "public void delete%s(%s %s) {" +
                        "%sService.delete%s(%s); }",
                Objects.requireNonNull(entity.getName()).substring(0, 1).toUpperCase() + entity.getName().substring(1),
                entity.getName(),
                entity.getName().toLowerCase(),
                entity.getName().toLowerCase(),
                Objects.requireNonNull(entity.getName()).substring(0, 1).toUpperCase() + entity.getName().substring(1),
                entity.getName().toLowerCase()
        ), entity.getContext()));

        String annotationAsString = String.format("%s", DELETE_MAPPING);

        psiMethod
                .ifPresent(method -> annotationUtil.addQualifiedAnnotationName(annotationAsString, method));

        return psiMethod;
    }
}
