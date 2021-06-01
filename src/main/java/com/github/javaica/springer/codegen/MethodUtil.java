package com.github.javaica.springer.codegen;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;

import java.util.Optional;

public interface MethodUtil {
    Optional<PsiMethod> get(PsiField psiField, PsiClass entity);
    Optional<PsiMethod> post(PsiClass entity);
    Optional<PsiMethod> put(PsiClass entity);
    Optional<PsiMethod> delete(PsiClass entity, PsiField field);
}
