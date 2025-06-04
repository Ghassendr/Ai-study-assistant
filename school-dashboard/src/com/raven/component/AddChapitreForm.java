package com.raven.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Boîte de dialogue pour saisir le nom d'un nouveau chapitre.
 */
public class AddChapitreForm extends JDialog {
    private JTextField txtName;
    private JButton btnOk;
    private JButton btnCancel;
    private String newChapitreName;

    public AddChapitreForm(Frame owner, String matiereName) {
        super(owner, "Ajouter un chapitre à " + matiereName, true);
        initComponents();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lbl = new JLabel("Nom du chapitre :");
        txtName = new JTextField(20);

        JPanel inputPane = new JPanel(new BorderLayout(5, 5));
        inputPane.add(lbl, BorderLayout.WEST);
        inputPane.add(txtName, BorderLayout.CENTER);

        panel.add(inputPane, BorderLayout.CENTER);

        btnOk = new JButton("OK");
        btnCancel = new JButton("Annuler");

        JPanel btnPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPane.add(btnCancel);
        btnPane.add(btnOk);

        panel.add(btnPane, BorderLayout.SOUTH);

        setContentPane(panel);

        // Listeners
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newChapitreName = txtName.getText().trim();
                if (newChapitreName.isEmpty()) {
                    JOptionPane.showMessageDialog(AddChapitreForm.this,
                            "Le nom du chapitre ne peut pas être vide.",
                            "Attention",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                dispose();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newChapitreName = null;
                dispose();
            }
        });
    }

    /**
     * @return le nom saisi ou null si l'utilisateur a annulé
     */
    public String getNewChapitreName() {
        return newChapitreName;
    }
}
