package com.work.extractor.gui;

import com.work.extractor.dto.ArchiveInfo;
import com.work.extractor.service.ExtractorService;
import com.work.extractor.service.ReportService;
import com.work.extractor.util.IconUtil;
import com.work.extractor.util.MessageUtil;
import com.work.extractor.util.StringUtil;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author ajuar
 */
public class ExtractorFrame extends JFrame {

    private final ExtractorTableModel model = new ExtractorTableModel();
    private final JLabel status = new JLabel("Listo");
    private File inputFile;
    private String password;
    private transient ExtractorService currentFile;
    private transient ArchiveInfo info;
    private JButton extractBtn;
    private JButton commentBtn;
    private JButton reportBtn;
    
    public ExtractorFrame() {
        super("Extractor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setIconImage(IconUtil.loadIcon("/icons/main.png", 36, 36).getImage());

        JTable table = new JTable();
        table.setModel(model);
        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createToolBar(), BorderLayout.NORTH);
        add(status, BorderLayout.SOUTH);
    }
    
    private JToolBar createToolBar() {
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        
        extractBtn = makeButton("Extraer en", 'E', e -> extractSelected());
        commentBtn = makeButton("Ver comentario", 'C', e -> showComment());
        reportBtn = makeButton("Generar informe", 'I', e -> generateReport());
        JButton openBtn = makeButton("Abrir", 'O', e -> open());
        JButton aboutBtn = makeButton("Acerca de", 0, e -> about());
        openBtn.setIcon(IconUtil.loadIcon("/icons/open.png", 32, 32));
        extractBtn.setIcon(IconUtil.loadIcon("/icons/extract.png", 32, 32));
        reportBtn.setIcon(IconUtil.loadIcon("/icons/report.png", 32, 32));
        aboutBtn.setIcon(IconUtil.loadIcon("/icons/about.png", 32, 32));
        commentBtn.setIcon(IconUtil.loadIcon("/icons/comment.png", 32, 32));
        bar.add(openBtn);
        bar.add(extractBtn);
        bar.add(commentBtn);
        bar.add(reportBtn);
        bar.addSeparator();
        bar.add(aboutBtn);
        
        Stream.of(extractBtn, commentBtn, reportBtn).forEach(b -> b.setEnabled(false));
        return bar;
    }
    
    private void extractSelected() {
        if (currentFile == null || info == null) {
            return;
        }
        
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        try {
            ProgressDialog progressBar = new ProgressDialog(this, "Progreso");
            currentFile.extractArchive(inputFile, fc.getSelectedFile(), password, progressBar);
        } catch (IOException ex) {
            MessageUtil.showError("Error al extraer archivo: " + info.getFileName(), ex);
        }
    }
    
    private JButton makeButton(String text, int mnemonic, java.awt.event.ActionListener al) {
        JButton b = new JButton(text);
        if (mnemonic != 0) {
            b.setMnemonic(mnemonic);
        }
        
        b.addActionListener(al);
        return b;
    }
    
    private void open() {
        
        try {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("Archivos (ZIP, RAR, 7Z, TAR)", "zip", "rar", "7z", "tar", "gz", "bz2"));
            if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            inputFile = fc.getSelectedFile();
            currentFile = new ExtractorService();
            password = null;
            if (currentFile.requiresPassword(inputFile)) {
                PasswordDialog dialog = new PasswordDialog(this, "Password requerido");
                dialog.setVisible(true);
                password = dialog.getPassword();
            }
            info = currentFile.readEntries(inputFile, password);
            model.setEntries(info.getEntries());
            setTitle("Extractor - " + info.getFileName());
            status.setText(" " + info.getPath());
            Stream.of(extractBtn, commentBtn, reportBtn).forEach(b -> b.setEnabled(true));
        } catch (Exception ex) {
            MessageUtil.showError("No se pudo abrir el archivo", ex);
        }
    }
    
    private void generateReport() {
        if (currentFile == null || info == null) {
            return;
        }
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("informe_" + StringUtil.getName(info.getFileName()) + ".txt"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        ReportService reportService = new ReportService();
        reportService.generateReport(fc.getSelectedFile(), model);
        
    }
    
    private void showComment() {
        if (currentFile == null || info == null) {
            return;
        }
        String comment = info.getComment();
        CommentDialog commentDialog = new CommentDialog(this, info.getFileName(), comment);
        commentDialog.setVisible(true);
    }
    
    private void about() {
        AboutDialog aboutDialog = new AboutDialog(this);
        aboutDialog.setVisible(true);
    }
    
}
