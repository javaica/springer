package com.github.javaica.springer.codegen;

import com.intellij.ide.util.PackageUtil;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;

import java.util.Arrays;
import java.util.Optional;

public class PsiUtilService {
    public Optional<PsiDirectory> getDirectoryOfPackage(Module module, String packageName) {
        return Optional.ofNullable(PackageUtil.findOrCreateDirectoryForPackage(
                module,
                packageName,
                null,
                false)
        );
    }

    public Optional<PsiClass> getEntityClass(PsiFile entityClass) {
        PsiJavaFile psiJavaFile = (PsiJavaFile) entityClass;
        return Arrays.stream(psiJavaFile.getClasses())
                .filter(psiClass -> psiClass.hasAnnotation("javax.persistence.Entity"))
                .findAny();
    }

    public static PsiUtilService getInstance() {
        return Optional.ofNullable(ServiceManager.getService(PsiUtilService.class))
                .orElseThrow();
    }
}
