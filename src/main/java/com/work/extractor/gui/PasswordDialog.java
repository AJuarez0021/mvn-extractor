package com.work.extractor.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.Serial;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/**
 * The Class PasswordDialog.
 *
 * @author ajuar
 */
public class PasswordDialog extends JDialog {

   
	/** The Constant serialVersionUID. */
	@Serial
    private static final long serialVersionUID = 1L;
	
	/** The password field. */
	private JPasswordField passwordField;
    
    /** The aceptado. */
    private boolean accepted = false;

    /**
     * Instantiates a new password dialog.
     *
     * @param parent the parent
     * @param titulo the titulo
     */
    public PasswordDialog(JFrame parent, String titulo) {
        super(parent, titulo, true);
        initComponents();
    }

    /**
     * Inits the components.
     */
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
            accepted = true;
            dispose();
        });

        btnCancelar.addActionListener(e -> {
            accepted = false;
            dispose();
        });

        passwordField.addActionListener(e -> {
            accepted = true;
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

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        if (accepted) {
            return new String(passwordField.getPassword());
        }
        return null;
    }

    /**
     * Checks if is aceptado.
     *
     * @return true, if is aceptado
     */
    public boolean isAccepted() {
        return accepted;
    }
}
