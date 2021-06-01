package com.github.javaica.springer.ui;

import com.github.javaica.springer.model.ComponentDialogOptions;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

public class ComponentDialogUI extends JDialog {

    private final Project project;
    private final Consumer<ComponentDialogOptions> callback;

    private JPanel contentPane;
    private JCheckBox modelCheckBox;
    private JTextField textField1;
    private JCheckBox repositoryCheckBox;
    private JTextField textField2;
    private JCheckBox serviceCheckBox;
    private JTextField textField3;
    private JCheckBox controllerCheckBox;
    private JTextField textField4;
    private JCheckBox generateMethodsCheckBox;

    private JButton selectModelPackageButton;
    private JButton selectRepositoryPackageButton;
    private JButton selectServicePackageButton;
    private JButton selectControllerPackageButton;
    private PackageChooserDialog modelPackageDialog;
    private PackageChooserDialog repositoryPackageDialog;
    private PackageChooserDialog servicePackageDialog;
    private PackageChooserDialog controllerPackageDialog;

    public ComponentDialogUI(Project project, Consumer<ComponentDialogOptions> callback) {
        this.callback = callback;
        this.project = project;
        setContentPane(contentPane);
        setModal(true);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        modelCheckBox.addActionListener(e -> validateCheckboxes());
        repositoryCheckBox.addActionListener(e -> validateCheckboxes());
        serviceCheckBox.addActionListener(e -> validateCheckboxes());
        controllerCheckBox.addActionListener(e -> validateCheckboxes());

        selectModelPackageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSelectModelPackage();
            }
        });
        selectRepositoryPackageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSelectRepositoryPackage();
            }
        });
        selectServicePackageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSelectServicePackage();
            }
        });
        selectControllerPackageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSelectControllerPackage();
            }
        });
    }

    public void onOK() {
        ComponentDialogOptions options = ComponentDialogOptions.builder()
                .generateModel(modelCheckBox.isSelected())
                .generateRepository(repositoryCheckBox.isSelected())
                .generateService(serviceCheckBox.isSelected())
                .generateController(controllerCheckBox.isSelected())
                .modelPackage(modelCheckBox.isSelected() ? textField1.getText() : null)
                .repositoryPackage(repositoryCheckBox.isSelected() ? textField2.getText() : null)
                .servicePackage(serviceCheckBox.isSelected() ? textField3.getText() : null)
                .controllerPackage(controllerCheckBox.isSelected() ? textField4.getText() : null)
                .generateMethods(generateMethodsCheckBox.isSelected())
                .build();
        dispose();
        callback.accept(options);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void validateCheckboxes() {
        boolean isActive = modelCheckBox.isSelected() &&
                repositoryCheckBox.isSelected() &&
                serviceCheckBox.isSelected() &&
                controllerCheckBox.isSelected();
        generateMethodsCheckBox.setEnabled(isActive);
        if (!isActive)
            generateMethodsCheckBox.setSelected(false);
    }


    public void onSelectModelPackage() {
        modelPackageDialog = new PackageChooserDialog("Select Package for Model", project);
        modelPackageDialog.pack();
        modelPackageDialog.show();
        textField1.setText(modelPackageDialog.getSelectedPackage().getQualifiedName());
    }
    public void onSelectRepositoryPackage() {
        repositoryPackageDialog = new PackageChooserDialog("Select Package for Repository", project);
        repositoryPackageDialog.pack();
        repositoryPackageDialog.show();
        textField2.setText(repositoryPackageDialog.getSelectedPackage().getQualifiedName());
    }
    public void onSelectServicePackage() {
        servicePackageDialog = new PackageChooserDialog("Select Package for Service", project);
        servicePackageDialog.pack();
        servicePackageDialog.show();
        textField3.setText(servicePackageDialog.getSelectedPackage().getQualifiedName());
    }
    public void onSelectControllerPackage() {
        controllerPackageDialog = new PackageChooserDialog("Select Package for Controller", project);
        controllerPackageDialog.pack();
        controllerPackageDialog.show();
        textField4.setText(controllerPackageDialog.getSelectedPackage().getQualifiedName());
    }


    @Override
    public JPanel getContentPane() {
        return contentPane;
    }
}
