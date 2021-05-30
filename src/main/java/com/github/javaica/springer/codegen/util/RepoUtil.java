package com.github.javaica.springer.codegen.util;

import com.github.javaica.springer.codegen.MethodUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;

public class RepoUtil implements MethodUtil {

    private Project project;
    private final PsiElementFactory psiElementFactory;

    public RepoUtil(Project project) {
        this.project = project;
        psiElementFactory = JavaPsiFacade.getInstance(project).getElementFactory();
    }

    @Override
    public PsiMethod get(PsiField psiField, PsiClass entity) {
        return psiElementFactory.createMethodFromText(String.format(
                "%s getBy%s()", entity.getName(),
                psiField.getName().substring(0, 1).toUpperCase() + psiField.getName().substring(1)),
                psiField.getContext());
    }

    @Override
    public PsiMethod post(PsiClass entity) {
        return null;
    }

    @Override
    public PsiMethod put(PsiClass entity) {
        return null;
    }

    @Override
    public PsiMethod delete(PsiClass entity) {
        return null;
    }
}
