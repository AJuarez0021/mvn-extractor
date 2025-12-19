package com.work.extractor.dto;

import java.util.List;

public class ArchiveInfo {
    
    private List<ArchiveEntry> entries;
    private String fileName;
    private String path;
    private String comment;
    

    public ArchiveInfo() {

    }

    public ArchiveInfo(List<ArchiveEntry> entries, String fileName, String path, String comment) {
        this.entries = entries;
        this.fileName = fileName;
        this.path = path;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

   
    public List<ArchiveEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<ArchiveEntry> entries) {
        this.entries = entries;
    }
}
