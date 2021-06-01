package com.github.javaica.springer.util;

import com.github.javaica.springer.codegen.AnnotationUtil;
import com.github.javaica.springer.codegen.util.ControllerUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ControllerUtilTest {

    @Test
    public void post() {
        PsiElementFactory factory = mock(PsiElementFactory.class);
        when(factory.createMethodFromText(any(), any())).thenReturn(mock(PsiMethod.class));
        MockedStatic<JavaPsiFacade> facadeStaticMock = Mockito.mockStatic(JavaPsiFacade.class);
        MockedStatic<AnnotationUtil> annotationStaticMock = Mockito.mockStatic(AnnotationUtil.class);
        AnnotationUtil annotationUtilMock = mock(AnnotationUtil.class);
        annotationStaticMock.when(() -> AnnotationUtil.getInstance(any())).thenReturn(annotationUtilMock);
        JavaPsiFacade facadeMock = mock(JavaPsiFacade.class);
        when(facadeMock.getElementFactory()).thenReturn(factory);
        facadeStaticMock.when(() -> JavaPsiFacade.getInstance(any())).thenReturn(facadeMock);
        ControllerUtil util = new ControllerUtil(mock(Project.class));
        PsiClass psiClass = mock(PsiClass.class);
        when(psiClass.getName()).thenReturn("Example");
        util.post(psiClass);

        facadeStaticMock.close();
        annotationStaticMock.close();

        verify(factory, times(1)).createMethodFromText(any(), any());
        verify(annotationUtilMock, times(1)).addQualifiedAnnotationName(any(), any());
    }

    @Test
    public void put() {
        PsiElementFactory factory = mock(PsiElementFactory.class);
        when(factory.createMethodFromText(any(), any())).thenReturn(mock(PsiMethod.class));
        MockedStatic<JavaPsiFacade> facadeStaticMock = Mockito.mockStatic(JavaPsiFacade.class);
        MockedStatic<AnnotationUtil> annotationStaticMock = Mockito.mockStatic(AnnotationUtil.class);
        AnnotationUtil annotationUtilMock = mock(AnnotationUtil.class);
        annotationStaticMock.when(() -> AnnotationUtil.getInstance(any())).thenReturn(annotationUtilMock);
        JavaPsiFacade facadeMock = mock(JavaPsiFacade.class);
        when(facadeMock.getElementFactory()).thenReturn(factory);
        facadeStaticMock.when(() -> JavaPsiFacade.getInstance(any())).thenReturn(facadeMock);
        ControllerUtil util = new ControllerUtil(mock(Project.class));
        PsiClass psiClass = mock(PsiClass.class);
        when(psiClass.getName()).thenReturn("Example");
        util.put(psiClass);

        facadeStaticMock.close();
        annotationStaticMock.close();

        verify(factory, times(1)).createMethodFromText(any(), any());
        verify(annotationUtilMock, times(1)).addQualifiedAnnotationName(any(), any());
    }

    @Test
    public void delete() {
        PsiElementFactory factory = mock(PsiElementFactory.class);
        when(factory.createMethodFromText(any(), any())).thenReturn(mock(PsiMethod.class));
        MockedStatic<JavaPsiFacade> facadeStaticMock = Mockito.mockStatic(JavaPsiFacade.class);
        MockedStatic<AnnotationUtil> annotationStaticMock = Mockito.mockStatic(AnnotationUtil.class);
        AnnotationUtil annotationUtilMock = mock(AnnotationUtil.class);
        annotationStaticMock.when(() -> AnnotationUtil.getInstance(any())).thenReturn(annotationUtilMock);
        JavaPsiFacade facadeMock = mock(JavaPsiFacade.class);
        when(facadeMock.getElementFactory()).thenReturn(factory);
        facadeStaticMock.when(() -> JavaPsiFacade.getInstance(any())).thenReturn(facadeMock);
        ControllerUtil util = new ControllerUtil(mock(Project.class));
        PsiClass psiClass = mock(PsiClass.class);
        PsiField psiField = mock(PsiField.class);
        when(psiClass.getName()).thenReturn("Example");
        util.delete(psiClass, psiField);

        facadeStaticMock.close();
        annotationStaticMock.close();

        verify(factory, times(1)).createMethodFromText(any(), any());
        verify(annotationUtilMock, times(1)).addQualifiedAnnotationName(any(), any());
    }
}
