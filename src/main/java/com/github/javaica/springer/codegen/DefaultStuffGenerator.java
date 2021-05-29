package com.github.javaica.springer.codegen;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Optional;

// TODO: 5/29/2021 rename
@RequiredArgsConstructor
public class DefaultStuffGenerator {

    private final Project project;


    public void addAutowired(PsiMember psiMember) {
        addQualifiedAnnotationName("org.springframework.beans.factory.annotation.Autowired", psiMember);
    }

    public void addOverride(PsiMethod resourceFieldGetter) {
        addQualifiedAnnotationName("java.lang.Override", resourceFieldGetter);
    }

    private void addQualifiedAnnotationName(String qualifiedAnnotationName, PsiMember psiMember) {
        Objects.requireNonNull(psiMember.getModifierList()).addAnnotation(qualifiedAnnotationName);
    }

    public static DefaultStuffGenerator getInstance(Project project) {
        return Optional.ofNullable(ServiceManager.getService(project, DefaultStuffGenerator.class))
                .orElseThrow();
    }
}
