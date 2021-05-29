package com.github.javaica.springer.codegen;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MethodGenerator {

    PsiElementFactory psiElementFactory;

    public void generateMethods(PsiClass psiClass) {
        List<PsiField> psiFields = Arrays.stream(psiClass.getFields())
                .filter(psiField ->
                        !psiField.hasAnnotation("javax.persistence.Id"))
                .collect(Collectors.toList());

        psiElementFactory = JavaPsiFacade.getInstance(psiClass.getProject())
                .getElementFactory();

        WriteCommandAction.runWriteCommandAction(psiClass.getProject(), () ->
                psiFields
                .forEach(psiField ->
                        psiClass.add(generateGettersForEachField(psiField))
                ));
    }


    public PsiMethod generateGettersForEachField(PsiField psiField) {

        return psiElementFactory.createMethodFromText(String.format(
                "public %s get%s() { " +
                        "return %s; }", psiField.getType().getPresentableText(),
                psiField.getName().substring(0, 1).toUpperCase() + psiField.getName().substring(1),
                psiField.getName()),
                psiField.getContext());

    }
}
