package com.github.javaica.springer.codegen.methodgen;

import com.github.javaica.springer.model.MethodDialogOptions;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MethodOptions {
    List<PsiField> fields;
    PsiClass model;
    PsiClass repository;
    PsiClass service;
    PsiClass controller;
    Project project;
    MethodDialogOptions dialogOptions;
}
