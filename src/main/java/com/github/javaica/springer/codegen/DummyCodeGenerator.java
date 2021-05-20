package com.github.javaica.springer.codegen;

import com.intellij.ide.util.PackageUtil;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

import java.util.Objects;
import java.util.Optional;

public class DummyCodeGenerator {

    public void generate(CodegenOptions options) {
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

    private PsiFile createClassFile(CodegenOptions options) {
        PsiFile file = PsiFileFactory.getInstance(options.getProject())
                .createFileFromText("Dummy.java", JavaLanguage.INSTANCE, generateCode(options));
        JavaCodeStyleManager.getInstance(options.getProject()).optimizeImports(file);
        return file;
    }

    private String generateCode(CodegenOptions options) {
        return "\n\npublic class Dummy {\n" +
                "\n" +
                "}";
    }

    public static DummyCodeGenerator getInstance() {
        return Optional.ofNullable(ServiceManager.getService(DummyCodeGenerator.class))
                .orElseThrow();
    }
}
