package com.github.javaica.springer.codegen;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;

public interface MethodUtil {
    PsiMethod get(PsiField psiField, PsiClass entity);
    PsiMethod post(PsiClass entity);
    PsiMethod put(PsiClass entity);
    PsiMethod delete(PsiClass entity);
}
