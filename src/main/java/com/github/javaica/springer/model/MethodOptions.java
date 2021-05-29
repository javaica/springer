package com.github.javaica.springer.model;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MethodOptions {
    PsiClass entity;
    List<PsiField> fields;
    PsiClass model;
    PsiClass repository;
    PsiClass service;
    PsiClass controller;
    MethodDialogOptions dialogOptions;
}
