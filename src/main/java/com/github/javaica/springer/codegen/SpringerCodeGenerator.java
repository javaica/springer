package com.github.javaica.springer.codegen;

import com.github.javaica.springer.model.CodegenElement;
import com.github.javaica.springer.model.CodegenOptions;
import com.intellij.ide.util.PackageUtil;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

import java.util.Objects;
import java.util.Optional;

public class SpringerCodeGenerator {

    public void generate(CodegenOptions options) {
        options.getElements().stream()
                .map(element -> createElementOptions(options, element))
                .forEach(this::generate);
    }

    private CodegenElementOptions createElementOptions(CodegenOptions options, CodegenElement element) {
        return new CodegenElementOptions(options.getProject(), element.getPsiPackage(), options.getOriginalEntity());
    }

    private void generate(CodegenElementOptions options) {
        Module module = ModuleUtil.findModuleForFile(options.getPsiFile());
        Objects.requireNonNull(module, "Cannot find module where class should be generated");

        PsiDirectory dir = PackageUtil.findOrCreateDirectoryForPackage(
                module,
                options.getPsiPackage().getQualifiedName(),
                null,
                false);
        Objects.requireNonNull(dir, "Cannot find directory where class should be placed");

        dir.add(createClassFile(options));
    }

    private PsiFile createClassFile(CodegenElementOptions options) {
        PsiFile file = PsiFileFactory.getInstance(options.getProject())
                .createFileFromText("Dummy.java", JavaLanguage.INSTANCE, generateCode(options));
        JavaCodeStyleManager.getInstance(options.getProject()).optimizeImports(file);
        return file;
    }

    private String generateCode(CodegenElementOptions options) {
        return "\n\npublic class Dummy {\n" +
                "\n" +
                "}";
    }

    public static SpringerCodeGenerator getInstance() {
        return Optional.ofNullable(ServiceManager.getService(SpringerCodeGenerator.class))
                .orElseThrow();
    }
}
