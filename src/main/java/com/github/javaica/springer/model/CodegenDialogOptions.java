package com.github.javaica.springer.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CodegenDialogOptions {
    boolean generateModel;
    boolean generateRepository;
    boolean generateService;
    boolean generateController;

    String modelPackage;
    String repositoryPackage;
    String servicePackage;
    String controllerPackage;
}
