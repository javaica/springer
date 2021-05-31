package com.github.javaica.springer.model;

import com.intellij.psi.PsiClass;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MethodOptions {
    PsiClass entity;
    PsiClass model;
    PsiClass repository;
    PsiClass service;
    PsiClass controller;
    MethodDialogOptions dialogOptions;
}
