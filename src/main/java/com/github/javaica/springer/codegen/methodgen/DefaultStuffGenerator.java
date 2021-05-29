package com.github.javaica.springer.codegen.methodgen;

import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;

import java.util.Objects;

// TODO: 5/29/2021 rename
public class DefaultStuffGenerator {


    public void addAutowiredTo(PsiMember psiMember) {
        addQualifiedAnnotationNameTo("org.springframework.beans.factory.annotation.Autowired", psiMember);
    }

    public void addOverrideTo(PsiMethod resourceFieldGetter) {
        addQualifiedAnnotationNameTo("java.lang.Override", resourceFieldGetter);
    }

    private void addQualifiedAnnotationNameTo(String qualifiedAnnotationName, PsiMember psiMember) {
        Objects.requireNonNull(psiMember.getModifierList()).addAnnotation(qualifiedAnnotationName);
    }
}
