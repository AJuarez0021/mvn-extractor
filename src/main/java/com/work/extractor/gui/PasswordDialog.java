package com.work.extractor.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/**
 *
 * @author ajuar
 */
public class PasswordDialog extends JDialog {

    private JPasswordField passwordField;
    private boolean aceptado = false;

    public PasswordDialog(JFrame parent, String titulo) {
        super(parent, titulo, true);
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(350, 150);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        JPanel panelCentral = new JPanel(new GridLayout(2, 1, 5, 5));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lblMensaje = new JLabel("Ingrese la contraseña:");
        passwordField = new JPasswordField(20);

        panelCentral.add(lblMensaje);
        panelCentral.add(passwordField);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(e -> {
            aceptado = true;
            dispose();
        });

        btnCancelar.addActionListener(e -> {
            aceptado = false;
            dispose();
        });

        passwordField.addActionListener(e -> {
            aceptado = true;
            dispose();
        });

        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);

        add(panelCentral, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                passwordField.requestFocusInWindow();
            }
        });
    }

    public String getPassword() {
        if (aceptado) {
            return new String(passwordField.getPassword());
        }
        return null;
    }

    public boolean isAceptado() {
        return aceptado;
    }
}
