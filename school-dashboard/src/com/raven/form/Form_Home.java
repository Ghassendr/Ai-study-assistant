package com.raven.form;

import com.raven.dialog.Message;
import com.raven.model.ModelDocument;
import com.raven.model.ModelNoticeBoard;
import com.raven.swing.table.EventAction;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class Form_Home extends javax.swing.JPanel {

    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton addpdf;
    private javax.swing.JScrollPane jScrollPane1;
    private com.raven.swing.noticeboard.NoticeBoard noticeBoard;
    private com.raven.swing.table.Table table1;
    private List<ModelDocument> pdfDocuments = new ArrayList<>();
    public Form_Home() {
        initComponents();
        table1.fixTable(jScrollPane1);
        setOpaque(false);
        initData();

        // Add action listener to the addpdf button
        // Configure table model
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"PDF Name", "PDF Path"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Only Actions column is editable
            }
        };
        table1.setModel(model);
        table1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table1.rowAtPoint(evt.getPoint());
                int col = table1.columnAtPoint(evt.getPoint());

                if (row >= 0 && col == 0) { // If clicked on PDF Name (column 0)
                    try {
                        String path = (String) table1.getModel().getValueAt(row, 1); // Get the PDF path from column 1
                        File pdfFile = new File(path);
                        if (pdfFile.exists()) {
                            Desktop.getDesktop().open(pdfFile); // Open it with default app
                        } else {
                            showMessage("File not found: " + path);
                        }
                    } catch (Exception ex) {
                        showMessage("Error opening file: " + ex.getMessage());
                    }
                }
            }
        });


        // Add action listener to the addpdf button
        addpdf.addActionListener(e -> addNewPdf());
    }

    private void initData() {
        initNoticeBoard();

    }

    private void initNoticeBoard() {
        noticeBoard.addDate("04/10/2021");
        noticeBoard.addNoticeBoard(new ModelNoticeBoard(
                "Hidemode",
                "Now",
                "Sets the hide mode for the component.",
                new Color(94, 49, 238)));
        noticeBoard.addDate("03/10/2021");
        noticeBoard.scrollToTop();
    }
    private void addNewPdf() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select PDF File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            ModelDocument doc = new ModelDocument(selectedFile.getName());
            doc.setPdfFile(selectedFile);
            pdfDocuments.add(doc);
            updateTable();
        }
    }
    private void updateTable() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0); // Clear existing rows

        for (ModelDocument doc : pdfDocuments) {
            model.addRow(new Object[]{
                    doc.getName(),
                    doc.getPdfFile().getAbsolutePath(),
                    createActionButtons(doc)
            });
        }
    }
    private javax.swing.JPanel createActionButtons(ModelDocument doc) {
        javax.swing.JPanel panel = new javax.swing.JPanel();

        // View button
        javax.swing.JButton viewBtn = new javax.swing.JButton("View");
        viewBtn.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(doc.getPdfFile());
            } catch (Exception ex) {
                showMessage("Error opening PDF: " + ex.getMessage());
            }
        });

        // Remove button
        javax.swing.JButton removeBtn = new javax.swing.JButton("Remove");
        removeBtn.addActionListener(e -> {
            pdfDocuments.remove(doc);
            updateTable();
        });

        panel.add(viewBtn);
        panel.add(removeBtn);
        return panel;
    }

    private boolean showMessage(String message) {
        int result = javax.swing.JOptionPane.showConfirmDialog(
                this,
                message,
                "Confirmation",
                javax.swing.JOptionPane.OK_CANCEL_OPTION
        );
        return result == javax.swing.JOptionPane.OK_OPTION;
    }


    private EventAction createEventAction() {
        return new EventAction() {
            @Override
            public void delete(ModelDocument document) {
                if (showMessage("Delete Chapter: " + document.getName())) {

                }
            }

            @Override
            public void update(ModelDocument document) {
                if (showMessage("Update Chapter: " + document.getName())) {
                    // Handle update action
                }
            }

            @Override
            public void view(ModelDocument document) {
                // Handle view action
            }

            @Override
            public void addPdf(ModelDocument document) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select PDF File");
                fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));

                if (fileChooser.showOpenDialog(Form_Home.this) == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    document.setPdfFile(selectedFile);
                    updateTableRow(document);
                }
            }

            @Override
            public void viewPdf(ModelDocument document) {
                try {
                    if (document.getPdfFile() != null && document.getPdfFile().exists()) {
                        Desktop.getDesktop().open(document.getPdfFile());
                    } else {
                        showMessage("No PDF file available for this chapter");
                    }
                } catch (Exception e) {
                    showMessage("Error opening PDF: " + e.getMessage());
                }
            }
        };
    }

    private void initTableData() {
        EventAction eventAction = createEventAction();
        clearTable();

        table1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table1.rowAtPoint(evt.getPoint());
                int col = table1.columnAtPoint(evt.getPoint());

                if (row >= 0 && col == 1) { // PDF File column
                    ModelDocument doc = getDocumentAtRow(row);
                    if (doc != null && doc.getPdfFile() != null) {
                        try {
                            Desktop.getDesktop().open(doc.getPdfFile());
                        } catch (Exception e) {
                            showMessage("Error opening PDF: " + e.getMessage());
                        }
                    }
                }
            }
        });

        // Add initial rows with empty PDF fields
        table1.addRow(new ModelDocument("Analiseur lexicale").toRowTable(eventAction));
        table1.addRow(new ModelDocument("Statistique").toRowTable(eventAction));
        table1.addRow(new ModelDocument("Matrice").toRowTable(eventAction));
        table1.addRow(new ModelDocument("Graph").toRowTable(eventAction));
    }

    private void clearTable() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);
    }

    private ModelDocument getDocumentAtRow(int row) {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        if (row >= 0 && row < model.getRowCount()) {
            ModelDocument doc = new ModelDocument((String) model.getValueAt(row, 0));
            // You might need to store the actual PDF file reference somewhere
            return doc;
        }
        return null;
    }



    public void updateSelectedChapter(String subjectName, String chapterName) {
        EventAction eventAction = createEventAction();
        table1.clearRows();
        table1.addRow(new ModelDocument(chapterName).toRowTable(eventAction));
    }

    private void updateTableRow(ModelDocument document) {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            // Update both the chapter name and PDF filename columns
            model.setValueAt(document.getName(), i, 0);
            model.setValueAt(document.getPdfFile() != null ? document.getPdfFile().getName() : "", i, 1);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        noticeBoard = new com.raven.swing.noticeboard.NoticeBoard();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        addpdf = new javax.swing.JButton("Add PDF");
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table1 = new com.raven.swing.table.Table();

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 12));
        jLabel1.setForeground(new java.awt.Color(4, 72, 210));
        jLabel1.setText("Dashboard / Home");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("sansserif", 1, 15));
        jLabel2.setForeground(new java.awt.Color(76, 76, 76));
        jLabel2.setText("Notes Board");
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        jLabel3.setFont(new java.awt.Font("sansserif", 1, 12));
        jLabel3.setForeground(new java.awt.Color(105, 105, 105));
        jLabel3.setText("note of: " + LocalDate.now());
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        jLabel4.setOpaque(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(noticeBoard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel2)
                                                        .addComponent(jLabel3))
                                                .addGap(0, 257, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2)
                                .addGap(15, 15, 15)
                                .addComponent(jLabel3)
                                .addGap(9, 9, 9)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(noticeBoard, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("sansserif", 1, 15));
        jLabel5.setForeground(new java.awt.Color(76, 76, 76));
        jLabel5.setText("Chapters List");
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        table1.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] {
                        "Pdf Name", "PDF Path", "Actions"
                }
        ) {
            boolean[] canEdit = new boolean [] {
                    false, false, true
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        jScrollPane1.setViewportView(table1);
        if (table1.getColumnModel().getColumnCount() > 0) {
            table1.getColumnModel().getColumn(0).setPreferredWidth(150);
            table1.getColumnModel().getColumn(1).setPreferredWidth(200);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(addpdf)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(jScrollPane1))
                                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(addpdf))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1)
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
    }
}