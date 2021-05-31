package com.github.javaica.springer.codegen;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Optional;

// TODO: 5/29/2021 rename
@RequiredArgsConstructor
public class AnnotationUtil {

    private final Project project;

    public void addAutowired(PsiMember psiMember, PsiClass psiClass) {
        addQualifiedAnnotationName("org.springframework.beans.factory.annotation.Autowired", psiMember);
    }

    public void addOverride(PsiMethod resourceFieldGetter) {
        addQualifiedAnnotationName("Override", resourceFieldGetter);
    }

    public void addQualifiedAnnotationName(String qualifiedAnnotationName, PsiMember psiMember) {
        WriteCommandAction.runWriteCommandAction(psiMember.getProject(), (Computable<PsiAnnotation>) () ->
            Objects.requireNonNull(psiMember.getModifierList()).addAnnotation(qualifiedAnnotationName));
    }

    public static AnnotationUtil getInstance(Project project) {
        return Optional.ofNullable(ServiceManager.getService(project, AnnotationUtil.class))
                .orElseThrow();
    }
}
