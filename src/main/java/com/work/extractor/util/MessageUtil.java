package com.work.extractor.util;

import javax.swing.JOptionPane;

/**
 *
 * @author ajuar
 */
public final class MessageUtil {

    private MessageUtil() {

    }

    public static void showError(String msg, String title) {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showError(String msg, String title, Exception ex) {
        JOptionPane.showMessageDialog(null, msg + "\n" + ex.getMessage(),
                title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showError(String msg, Exception ex) {
        showError(msg, "Error", ex);
    }

    public static void showInfo(String msg, String title) {
        JOptionPane.showMessageDialog(null, msg, title,
                JOptionPane.INFORMATION_MESSAGE);
    }
}
