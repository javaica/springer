package com.github.javaica.springer.codegen;

import com.github.javaica.springer.codegen.util.ControllerUtil;
import com.github.javaica.springer.codegen.util.RepoUtil;
import com.github.javaica.springer.codegen.util.ServiceUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;

public class MethodGenUtil {

    private final RepoUtil repoUtil;
    private final ServiceUtil serviceUtil;
    private final ControllerUtil controllerUtil;

    public MethodGenUtil(Project project) {
        repoUtil = new RepoUtil(project);
        serviceUtil = new ServiceUtil(project);
        controllerUtil = new ControllerUtil(project);
    }

    public PsiMethod repoGetByField(PsiField psiField, PsiClass entity) {
        return repoUtil.get(psiField, entity).orElseThrow();
    }

    public PsiMethod serviceGetByField(PsiField psiField, PsiClass entity) {
        return serviceUtil.get(psiField, entity).orElseThrow();
    }

    public PsiMethod servicePost(PsiClass entity) {
        return serviceUtil.post(entity).orElseThrow();
    }

    public PsiMethod servicePut(PsiClass entity) {
        return serviceUtil.put(entity).orElseThrow();
    }

    public PsiMethod serviceDelete(PsiField psiField, PsiClass entity) {
        return serviceUtil.delete(entity, psiField).orElseThrow();
    }

    public PsiMethod controllerGet(PsiField psiField, PsiClass entity) {
        return controllerUtil.get(psiField, entity).orElseThrow();
    }

    public PsiMethod controllerPost(PsiClass entity) {
        return controllerUtil.post(entity).orElseThrow();
    }

    public PsiMethod controllerPut(PsiClass entity) {
        return controllerUtil.put(entity).orElseThrow();
    }

    public PsiMethod controllerDelete(PsiField psiField, PsiClass entity) {
        return controllerUtil.delete(entity, psiField).orElseThrow();
    }
}
