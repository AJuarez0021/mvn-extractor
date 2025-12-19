package com.work.extractor.gui;

import com.work.extractor.dto.ArchiveEntry;
import com.work.extractor.util.DateUtil;
import com.work.extractor.util.StringUtil;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author ajuar
 */
public class ExtractorTableModel extends AbstractTableModel {

    private final String[] cols = {"Nombre", "Tamaño", "Comprimido", "Tipo", "Modificado", "CRC"};
    private transient List<ArchiveEntry> entries = List.of();

    public void setEntries(List<ArchiveEntry> list) {
        this.entries = list.stream()
                .sorted(Comparator.comparing(ArchiveEntry::getName))
                .toList();
        fireTableDataChanged();
    }

    public ArchiveEntry getEntry(int row) {
        return entries.get(row);
    }

    @Override
    public int getRowCount() {
        return entries.size();
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }

    @Override
    public String getColumnName(int col) {
        return cols[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        ArchiveEntry e = getEntry(row);
        return switch (col) {
            case 0 ->
                e.getName();
            case 1 ->
                StringUtil.toFormat(e.getSize());
            case 2 ->
                StringUtil.toFormat(e.getCompressedSize());
            case 3 ->
                e.isDirectory() ? "Directorio" : "Archivo";
            case 4 ->
                DateUtil.format(e.getTime());
            case 5 ->
                String.format("%08X", e.getCrc());
            default ->
                "";
        };
    }
}
