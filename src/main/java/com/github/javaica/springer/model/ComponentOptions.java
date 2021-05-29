package com.github.javaica.springer.model;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ComponentOptions {
    Project project;
    PsiClass entity;
    @Singular
    List<ComponentConfig> components;
}
