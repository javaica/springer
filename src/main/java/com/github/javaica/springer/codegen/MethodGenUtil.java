package com.github.javaica.springer.codegen;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MethodGenUtil {

    private final PsiElementFactory psiElementFactory;
    private final DefaultStuffGenerator stuffGenerator;

    private final String GET_MAPPING_QN = "org.springframework.web.bind.annotation.GetMapping";
    private final String POST_MAPPING_QN = "org.springframework.web.bind.annotation.PostMapping";


    public PsiMethod repoGetByField(PsiField psiField, PsiClass entity) {
        return psiElementFactory.createMethodFromText(String.format(
                "%s getBy%s()", entity.getName(),
                psiField.getName().substring(0, 1).toUpperCase() + psiField.getName().substring(1)),
                psiField.getContext());
    }


    public PsiMethod serviceGetByField(PsiField psiField) {

        return psiElementFactory.createMethodFromText(String.format(
                "repository.getBy%s(%s)" +
                        ".orElseThrow(() -> new TaskNotFoundException(taskId))", psiField.getType().getPresentableText(),
                psiField.getName().substring(0, 1).toUpperCase() + psiField.getName().substring(1),
                psiField.getName()),
                psiField.getContext());
    }

}
