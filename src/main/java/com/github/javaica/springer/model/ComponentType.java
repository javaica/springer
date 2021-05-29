package com.github.javaica.springer.model;

import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;

import java.util.Map;
import java.util.Objects;

public enum ComponentType {
    MODEL("SpringModel.java"),
    REPOSITORY("SpringRepository.java"),
    SERVICE("SpringService.java"),
    CONTROLLER("SpringController.java");

    private final String templateName;

    ComponentType(String templateName) {
        this.templateName = templateName;
    }

    public String createClassName(PsiClass original) {
        return original.getName() + this;
    }

    public PsiClass generateClass(PsiClass original, PsiDirectory directory, String primaryKeyType) {
        String name = createClassName(original);
        Map<String, String> props = Map.of(
                "Entity", Objects.requireNonNull(original.getName()),
                "PrimaryKeyType", primaryKeyType);
        return JavaDirectoryService.getInstance()
                .createClass(directory, name, this.templateName, true, props);
    }

    @Override
    public String toString() {
        return this.name().charAt(0) + this.name().toLowerCase().substring(1);
    }
}
