package com.github.javaica.springer.codegen;

import com.github.javaica.springer.model.CodegenElementType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPackage;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CodegenElementOptions {
    Project project;
    PsiPackage psiPackage;
    PsiFile psiFile;
    CodegenElementType elementType;
}
