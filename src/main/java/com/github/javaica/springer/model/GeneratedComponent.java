package com.github.javaica.springer.model;

import com.intellij.psi.PsiClass;
import lombok.Value;

@Value
public class GeneratedComponent {
    PsiClass component;
    ComponentType type;
}
