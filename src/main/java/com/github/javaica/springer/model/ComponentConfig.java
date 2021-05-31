package com.github.javaica.springer.model;

import com.intellij.psi.PsiPackage;
import lombok.Value;

@Value
public class ComponentConfig {
    String psiPackage;
    ComponentType type;
}
