package com.work.extractor.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * The Class ProgressDialog.
 *
 * @author ajuar
 */
public class ProgressDialog extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
	
	/** The progress bar. */
	private JProgressBar progressBar;
    
    /** The lbl mensaje. */
    private JLabel lblMensaje;

    /**
     * Instantiates a new progress dialog.
     *
     * @param parent the parent
     * @param title the title
     */
    public ProgressDialog(JFrame parent, String title) {
        super(parent, title, true);
        initComponents();
    }

    /**
     * Inits the components.
     */
    private void initComponents() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblMensaje = new JLabel("Procesando...");
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(350, 25));

        panelCentral.add(lblMensaje);
        panelCentral.add(Box.createVerticalStrut(15));
        panelCentral.add(progressBar);

        add(panelCentral, BorderLayout.CENTER);
    }

    /**
     * Sets the progress.
     *
     * @param valor the new progress
     */
    public void setProgress(int valor) {
        progressBar.setValue(valor);
    }

    /**
     * Sets the mensaje.
     *
     * @param mensaje the new mensaje
     */
    public void setMensaje(String mensaje) {
        lblMensaje.setText(mensaje);
    }

    /**
     * Sets the indeterminate.
     *
     * @param indeterminate the new indeterminate
     */
    public void setIndeterminate(boolean indeterminate) {
        progressBar.setIndeterminate(indeterminate);
    }
}
