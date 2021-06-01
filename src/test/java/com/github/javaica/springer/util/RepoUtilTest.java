package com.github.javaica.springer.util;

import com.github.javaica.springer.codegen.util.RepoUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RepoUtilTest {

    @Test
    public void post() {
        MockedStatic<JavaPsiFacade> facadeStaticMock = Mockito.mockStatic(JavaPsiFacade.class);
        PsiElementFactory factory = mock(PsiElementFactory.class);
        when(factory.createMethodFromText(any(), any())).thenReturn(mock(PsiMethod.class));
        JavaPsiFacade facadeMock = mock(JavaPsiFacade.class);
        when(facadeMock.getElementFactory()).thenReturn(factory);
        facadeStaticMock.when(() -> JavaPsiFacade.getInstance(any())).thenReturn(facadeMock);
        RepoUtil util = new RepoUtil(mock(Project.class));

        assertTrue(util.post(mock(PsiClass.class)).isEmpty());

        facadeStaticMock.close();
    }

    @Test
    public void put() {
        MockedStatic<JavaPsiFacade> facadeStaticMock = Mockito.mockStatic(JavaPsiFacade.class);
        PsiElementFactory factory = mock(PsiElementFactory.class);
        when(factory.createMethodFromText(any(), any())).thenReturn(mock(PsiMethod.class));
        JavaPsiFacade facadeMock = mock(JavaPsiFacade.class);
        when(facadeMock.getElementFactory()).thenReturn(factory);
        facadeStaticMock.when(() -> JavaPsiFacade.getInstance(any())).thenReturn(facadeMock);
        RepoUtil util = new RepoUtil(mock(Project.class));

        assertTrue(util.put(mock(PsiClass.class)).isEmpty());

        facadeStaticMock.close();
    }

    @Test
    public void delete() {
        MockedStatic<JavaPsiFacade> facadeStaticMock = Mockito.mockStatic(JavaPsiFacade.class);
        PsiElementFactory factory = mock(PsiElementFactory.class);
        when(factory.createMethodFromText(any(), any())).thenReturn(mock(PsiMethod.class));
        JavaPsiFacade facadeMock = mock(JavaPsiFacade.class);
        when(facadeMock.getElementFactory()).thenReturn(factory);
        facadeStaticMock.when(() -> JavaPsiFacade.getInstance(any())).thenReturn(facadeMock);
        RepoUtil util = new RepoUtil(mock(Project.class));

        assertTrue(util.delete(mock(PsiClass.class)).isEmpty());

        facadeStaticMock.close();
    }
}
