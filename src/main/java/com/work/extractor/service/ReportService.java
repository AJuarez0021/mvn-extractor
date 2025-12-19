package com.work.extractor.service;

import com.work.extractor.dto.ArchiveEntry;
import com.work.extractor.gui.ExtractorTableModel;
import com.work.extractor.util.MessageUtil;
import com.work.extractor.util.StringUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The Class ReportService.
 *
 * @author ajuar
 */
public class ReportService {

    /**
     * Generate report.
     *
     * @param outputFile the output file
     * @param model the model
     */
    public void generateReport(File outputFile, ExtractorTableModel model) {
        Path out = outputFile.toPath();
        try (BufferedWriter w = Files.newBufferedWriter(out)) {
            w.write(String.format("Informe de %s (%d entradas)%n%n", outputFile.getName(), model.getRowCount()));
            for (int i = 0; i < model.getRowCount(); i++) {
                ArchiveEntry e = model.getEntry(i);
                w.write(String.format("%-60s %10s %10s %s%n",
                        e.getName(),
                        StringUtil.toFormat(e.getSize()),
                        StringUtil.toFormat(e.getCompressedSize()),
                        e.isDirectory() ? "DIR" : ""));
            }

            MessageUtil.showInfo("Informe generado: " + out, "Informe");
        } catch (IOException ex) {
            MessageUtil.showError("No se pudo generar informe", ex);
        }
    }
}
