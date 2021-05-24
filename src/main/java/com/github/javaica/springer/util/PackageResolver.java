package com.github.javaica.springer.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiPackage;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class PackageResolver {

    private final Project project;

    public PsiPackage getPackage(String name) {
        return Optional.ofNullable(JavaPsiFacade.getInstance(project).findPackage(name))
                .orElseThrow();
    }
}
