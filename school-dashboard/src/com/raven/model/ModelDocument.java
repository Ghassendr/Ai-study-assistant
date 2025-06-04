package com.raven.model;

import com.raven.swing.table.EventAction;
import com.raven.swing.table.ModelAction;

import java.io.File;

public class ModelDocument {
    private String name;
    private File pdfFile;  // Store the PDF file object

    public ModelDocument(String name) {
        this.name = name;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setPdfFile(File pdfFile) {
        this.pdfFile = pdfFile;
    }

    public File getPdfFile() {
        return pdfFile;
    }

    public String getPdfFileName() {
        return pdfFile != null ? pdfFile.getName() : "No PDF";
    }

    public Object[] toRowTable(EventAction event) {
        return new Object[] {
                name,               // Chapter name
                getPdfFileName(),    // PDF filename
                new ModelAction(this, event)  // Action buttons
        };
    }
}