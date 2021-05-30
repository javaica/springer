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
        return repoUtil.get(psiField, entity);
    }

    public PsiMethod serviceGetByField(PsiField psiField, PsiClass entity) {
        return serviceUtil.get(psiField, entity);
    }

    public PsiMethod servicePost(PsiClass entity) {
        return serviceUtil.post(entity);
    }

    public PsiMethod servicePut(PsiClass entity) {
        return serviceUtil.put(entity);
    }

    public PsiMethod serviceDelete(PsiClass entity) {
        return serviceUtil.delete(entity);
    }

    public PsiMethod controllerGet(PsiField psiField, PsiClass entity) {
        return controllerUtil.get(psiField, entity);
    }

    public PsiMethod controllerPost(PsiClass entity) {
        return controllerUtil.post(entity);
    }

    public PsiMethod controllerPut(PsiClass entity) {
        return controllerUtil.put(entity);
    }

    public PsiMethod controllerDelete(PsiClass entity) {
        return controllerUtil.delete(entity);
    }
}
