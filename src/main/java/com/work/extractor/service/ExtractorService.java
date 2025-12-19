package com.work.extractor.service;

import com.work.extractor.dto.ArchiveEntry;
import com.work.extractor.dto.ArchiveInfo;
import com.work.extractor.exception.ExtractorException;
import com.work.extractor.gui.ProgressDialog;
import com.work.extractor.util.MessageUtil;
import com.work.extractor.util.StringUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.PropID;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;

/**
 *
 * @author ajuar
 */
public class ExtractorService {

    private static final Logger log
            = Logger.getLogger(ExtractorService.class.getName());

    public ArchiveInfo readEntries(File inputFile) throws IOException {
        return readEntries(inputFile, null);
    }

    public ArchiveInfo readEntries(File inputFile, String password) throws IOException {

        ArchiveInfo entry = new ArchiveInfo();
        List<ArchiveEntry> result = new ArrayList<>();
        if (!inputFile.exists()) {
            throw new FileNotFoundException("File not found: " + inputFile.getName());
        }

        entry.setFileName(inputFile.getName());
        entry.setPath(inputFile.getAbsolutePath());

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(inputFile, "r"); IInArchive archive = StringUtil.hasText(password)
                ? SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile), password)
                : SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile))) {

            int items = archive.getNumberOfItems();

            Object comment = archive.getArchiveProperty(PropID.COMMENT);
            entry.setComment(comment != null ? comment.toString() : "");
            
            for (int i = 0; i < items; i++) {

                ArchiveEntry info = new ArchiveEntry();

                info.setName((String) archive.getProperty(i, PropID.PATH));

                Long size = (Long) archive.getProperty(i, PropID.SIZE);
                info.setSize(size != null ? size : 0L);

                Long packed = (Long) archive.getProperty(i, PropID.PACKED_SIZE);
                info.setCompressedSize(packed != null ? packed : 0L);

                Boolean folder = (Boolean) archive.getProperty(i, PropID.IS_FOLDER);
                info.setDirectory(Boolean.TRUE.equals(folder));

                Integer crc = (Integer) archive.getProperty(i, PropID.CRC);
                info.setCrc(crc != null ? crc : 0);

                Date date = (Date) archive.getProperty(i, PropID.LAST_MODIFICATION_TIME);
                long time = date != null ? date.getTime() : 0L;
                info.setTime(time);

                result.add(info);
            }
        }
        entry.setEntries(result);
        return entry;
    }

    public boolean requiresPassword(File inputFile) {

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(inputFile, "r"); IInArchive inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile))) {

            if (inArchive.getNumberOfItems() > 0) {
                Boolean isEncrypted = (Boolean) inArchive.getProperty(0, PropID.ENCRYPTED);
                return isEncrypted != null && isEncrypted;
            }

            return false;

        } catch (Exception e) {
            return e.getMessage() != null
                    && (e.getMessage().contains("password")
                    || e.getMessage().contains("encrypted"));
        }
    }

    public void extractArchive(File inputFile, File outputFile, ProgressDialog progressBar)
            throws IOException {
        extractArchive(inputFile, outputFile, null, progressBar);
    }

    public void extractArchive(File inputFile, File outputFile, String password, ProgressDialog progressBar)
            throws IOException {

        if (!inputFile.exists()) {
            throw new FileNotFoundException("File not found: " + inputFile.getName());
        }

        var worker = new SwingWorker<Void, Object[]>() {
            boolean success = false;

            @Override
            protected Void doInBackground() {
                
                try (RandomAccessFile randomAccessFile = new RandomAccessFile(inputFile, "r"); IInArchive inArchive = StringUtil.hasText(password)
                        ? SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile), password)
                        : SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile))) {

                    Path pathDestino = outputFile.toPath();
                    Files.createDirectories(pathDestino);

                    log.log(Level.INFO, "Format: {0}", inArchive.getArchiveFormat());
                    log.log(Level.INFO, "Items: {0}", inArchive.getNumberOfItems());

                    ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();

                    int total = inArchive.getNumberOfItems();
                    int contador = 0;

                    for (ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
                        contador++;
                        int progress = (contador * 100 / total);
                        publish(new Object[]{progress, item.getPath()});
                        if (!item.isFolder()) {
                            extractItem(item, pathDestino, password);
                        }
                    }
                    success = true;
                } catch (Exception ex) {
                    MessageUtil.showError("Error al descomprimir", ex);
                    success = false;
                }
                return null;
            }

            @Override
            protected void process(List<Object[]> chunks) {
                Object[] data = chunks.getLast();
                progressBar.setProgress((int) data[0]);
                progressBar.setMensaje("Procesando: " + data[1] + "%");
            }

            @Override
            protected void done() {
                progressBar.dispose();
                if (success) {
                    log.log(Level.INFO, "Extraction completed: {0}", inputFile.getName());
                    MessageUtil.showInfo("Extracción finalizada.", "Success");
                }

            }
        };
        worker.execute();
        progressBar.setVisible(true);
    }

    /**
     * Extrae un elemento individual del archivo
     *
     * @param item The item
     * @param baseDirectory The directory
     * @param password The password
     */
    private void extractItem(ISimpleInArchiveItem item, Path baseDirectory, String password)
            throws IOException {

        String relativePath = item.getPath();
        Path archivoDestino = baseDirectory.resolve(relativePath);

        Files.createDirectories(archivoDestino.getParent());

        ExtractOperationResult result;
        if (StringUtil.hasText(password)) {
            result = item.extractSlow(data -> {
                try (FileOutputStream fos = new FileOutputStream(archivoDestino.toFile())) {
                    fos.write(data);
                } catch (IOException e) {
                    throw new ExtractorException("Error writing file: " + archivoDestino, e);
                }
                return data.length;
            }, password);
        } else {
            result = item.extractSlow(data -> {
                try (FileOutputStream fos = new FileOutputStream(archivoDestino.toFile())) {
                    fos.write(data);
                } catch (IOException e) {
                    throw new ExtractorException("Error writing file: " + archivoDestino, e);
                }
                return data.length;
            });
        }

        if (result != ExtractOperationResult.OK) {
            throw new ExtractorException("Error extracting: " + relativePath + " - " + result);
        }
    }

}
