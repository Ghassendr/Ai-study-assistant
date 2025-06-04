package com.raven.swing.table;

import com.raven.model.ModelDocument;


public interface EventAction {

    void delete(ModelDocument document);
    void update(ModelDocument document);
    void view(ModelDocument document);
    void addPdf(ModelDocument document); // Add this method
    void viewPdf(ModelDocument document); // Add this method


}