package com.github.javaica.springer.codegen.util;

import com.github.javaica.springer.codegen.MethodUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;

import java.util.Objects;
import java.util.Optional;

public class ServiceUtil implements MethodUtil {

    private final Project project;
    private final PsiElementFactory psiElementFactory;

    public ServiceUtil(Project project) {
        this.project = project;
        psiElementFactory = JavaPsiFacade.getInstance(project).getElementFactory();
    }

    @Override
    public Optional<PsiMethod> get(PsiField psiField, PsiClass entity) {
        return Optional.of(psiElementFactory.createMethodFromText(String.format(
                "public %s getBy%s(%s %s) {" +
                        "return repository.findById(%s).orElseThrow(); }",
                entity.getName(),
                psiField.getName().substring(0, 1).toUpperCase() + psiField.getName().substring(1),
                psiField.getType().getPresentableText(),
                psiField.getName(),
                psiField.getName()), psiField.getContext()));
    }

    @Override
    public Optional<PsiMethod> post(PsiClass entity) {
        return Optional.of(psiElementFactory.createMethodFromText(String.format(
                "public %s post%s(%s %s) {" +
                        "return repository.save(%s); }",
                entity.getName(),
                Objects.requireNonNull(entity.getName()).substring(0, 1).toUpperCase() + entity.getName().substring(1),
                entity.getName(),
                entity.getName().toLowerCase(),
                entity.getName().toLowerCase()
        ), entity.getContext()));
    }

    @Override
    public Optional<PsiMethod> put(PsiClass entity) {
        return Optional.of(psiElementFactory.createMethodFromText(String.format(
                "public %s put%s(%s %s) {" +
                        "return repository.save(%s); }",
                entity.getName(),
                Objects.requireNonNull(entity.getName()).substring(0, 1).toUpperCase() + entity.getName().substring(1),
                entity.getName(),
                entity.getName().toLowerCase(),
                entity.getName().toLowerCase()
        ), entity.getContext()));
    }

    @Override
    public Optional<PsiMethod> delete(PsiClass entity, PsiField psiField) {
        return Optional.of(psiElementFactory.createMethodFromText(String.format(
                "public void delete%s(%s %s) {" +
                        "repository.deleteById(%s); }",
                Objects.requireNonNull(entity.getName()).substring(0, 1).toUpperCase() + entity.getName().substring(1),
                psiField.getType().getPresentableText(),
                psiField.getName(),
                psiField.getName()
        ), entity.getContext()));
    }
}
