package com.github.javaica.springer.codegen;

import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;

import java.util.Objects;

// TODO: 5/29/2021 rename
public class DefaultStuffGenerator {


    public void addAutowired(PsiMember psiMember) {
        addQualifiedAnnotationName("org.springframework.beans.factory.annotation.Autowired", psiMember);
    }

    public void addOverride(PsiMethod resourceFieldGetter) {
        addQualifiedAnnotationName("java.lang.Override", resourceFieldGetter);
    }

    private void addQualifiedAnnotationName(String qualifiedAnnotationName, PsiMember psiMember) {
        Objects.requireNonNull(psiMember.getModifierList()).addAnnotation(qualifiedAnnotationName);
    }
}
