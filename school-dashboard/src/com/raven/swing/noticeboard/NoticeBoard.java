package com.raven.swing.noticeboard;

import com.raven.model.ModelNoticeBoard;
import com.raven.swing.scrollbar.ScrollBarCustom;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

public class NoticeBoard extends javax.swing.JPanel {

    private JTextArea txtNote;
    private JButton btnAddNote;
    private JPanel inputPanel; // Added inputPanel as a member variable

    public NoticeBoard() {
        initComponents();
        setBackground(Color.WHITE);
        jScrollPane1.setVerticalScrollBar(new ScrollBarCustom());
        panel.setLayout(new MigLayout("nogrid, fillx"));

        // Initialize inputPanel here
        inputPanel = new JPanel(new BorderLayout());
        txtNote = new JTextArea(3, 20);
        txtNote.setLineWrap(true);
        txtNote.setWrapStyleWord(true);
        txtNote.setBorder(BorderFactory.createTitledBorder("Write a note"));

        btnAddNote = new JButton("Add Note");
        btnAddNote.addActionListener(e -> addNoteAction());

        JScrollPane noteScrollPane = new JScrollPane(txtNote);

        inputPanel.add(noteScrollPane, BorderLayout.CENTER);
        inputPanel.add(btnAddNote, BorderLayout.SOUTH);

        // Add inputPanel to the main panel at the BOTTOM
        setLayout(new BorderLayout());
        add(jScrollPane1, BorderLayout.CENTER); // Keep the scroll pane in the center
        add(inputPanel, BorderLayout.SOUTH);   // Add the input panel to the bottom
    }

    public void addNoticeBoard(ModelNoticeBoard data) {
        JLabel title = new JLabel(data.getTitle());
        title.setFont(new Font("sansserif", Font.BOLD, 12));
        title.setForeground(data.getTitleColor());
        panel.add(title);

        JLabel time = new JLabel(data.getTime());
        time.setForeground(new Color(180, 180, 180));
        panel.add(time, "gap 10, wrap");

        JTextPane txt = new JTextPane();
        txt.setBackground(new Color(0, 0, 0, 0));
        txt.setForeground(new Color(120, 120, 120));
        txt.setSelectionColor(new Color(150, 150, 150));
        txt.setBorder(null);
        txt.setOpaque(false);
        txt.setEditable(false);
        txt.setText(data.getDescription());
        panel.add(txt, "w 100::90%, wrap");
        panel.revalidate();
        panel.repaint();
    }

    public void addDate(String date) {
        JLabel lbDate = new JLabel(date);
        lbDate.setBorder(new EmptyBorder(5, 5, 5, 5));
        lbDate.setFont(new Font("sansserif", Font.BOLD, 13));
        lbDate.setForeground(new Color(80, 80, 80));
        panel.add(lbDate, "wrap");
    }

    public void scrollToTop() {
        SwingUtilities.invokeLater(() -> jScrollPane1.getVerticalScrollBar().setValue(0));
    }

    private void addNoteAction() {
        String note = txtNote.getText().trim();
        if (!note.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String today = LocalDateTime.now().format(formatter);

            ModelNoticeBoard model = new ModelNoticeBoard(
                    "Note ",      // Title
                    today,        // Full Date + Time
                    note,         // Description
                    Color.BLUE    // Title color
            );
            addNoticeBoard(model);
            txtNote.setText("");

            panel.revalidate();
            panel.repaint();
            scrollToTop();
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        panel = new javax.swing.JPanel();

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
                panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 329, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
                panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 351, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(panel);

        // The initialization of txtNote, btnAddNote, noteScrollPane, and inputPanel
        // is now done in the constructor.
    }

    // Variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panel;
}