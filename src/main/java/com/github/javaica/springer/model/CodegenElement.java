package com.github.javaica.springer.model;

import lombok.Value;

@Value
public class CodegenElement {
    String psiPackage;
    CodegenElementType type;
}
