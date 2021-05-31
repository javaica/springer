package com.github.javaica.springer.codegen;

import com.github.javaica.springer.codegen.util.ImportClassConstants;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiImportList;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class AnnotationUtil {

    private final Project project;

    public void addAutowired(PsiMember psiMember) {
        addQualifiedAnnotationName(ImportClassConstants.AUTOWIRED, psiMember);
    }

    public void addOverride(PsiMethod resourceFieldGetter) {
        addQualifiedAnnotationName("Override", resourceFieldGetter);
    }

    public void addQualifiedAnnotationName(String qualifiedAnnotationName, PsiMember psiMember) {
        WriteCommandAction.runWriteCommandAction(psiMember.getProject(), (Computable<PsiAnnotation>) () ->
                Objects.requireNonNull(psiMember.getModifierList()).addAnnotation(qualifiedAnnotationName));
    }

    public void removeAnnotations(PsiField psiField) {
        WriteCommandAction.runWriteCommandAction(psiField.getProject(), () ->
                Arrays.fill(psiField.getAnnotations(), null));
    }

    public void addAnnotationToParameter(String qualifiedAnnotationName, PsiParameter parameter) {
        WriteCommandAction.runWriteCommandAction(parameter.getProject(), (Computable<PsiAnnotation>) () ->
                Objects.requireNonNull(parameter.getModifierList()).addAnnotation(qualifiedAnnotationName));
    }

    public void addImportStatement(PsiClass psiClass, String fullQualifiedName) {
        if (!(psiClass.getContainingFile() instanceof PsiJavaFile))
            return;

        final PsiJavaFile javaFile = (PsiJavaFile) psiClass.getContainingFile();

        PsiImportList importList = javaFile.getImportList();

        if (importList == null)
            return;

        // check if it's already imported
        if (Arrays.stream(importList
                .getAllImportStatements())
                .map(is -> Objects.requireNonNull(is.getImportReference()).getQualifiedName().equals(fullQualifiedName))
                .findAny()
                .orElseThrow())
            return;

        Optional<PsiClass> importClass = Optional.ofNullable(
                JavaPsiFacade.getInstance(project).findClass(fullQualifiedName, GlobalSearchScope.allScope(project)));
        WriteCommandAction.runWriteCommandAction(project, () ->
                importClass
                        .ifPresent(val -> JavaCodeStyleManager.getInstance(project).addImport(javaFile, val)));
    }

    public static AnnotationUtil getInstance(Project project) {
        return Optional.ofNullable(ServiceManager.getService(project, AnnotationUtil.class))
                .orElseThrow();
    }
}
