package com.raven.swing.table;

import com.raven.model.ModelDocument;

public class ModelAction {

    private ModelDocument document;
    private EventAction event;

    public ModelAction() {
    }

    public ModelAction(ModelDocument document, EventAction event) {
        this.document = document;
        this.event = event;
    }

    public ModelDocument getDocument() {
        return document;
    }

    public void setDocument(ModelDocument document) {
        this.document = document;
    }

    public EventAction getEvent() {
        return event;
    }

    public void setEvent(EventAction event) {
        this.event = event;
    }
}
