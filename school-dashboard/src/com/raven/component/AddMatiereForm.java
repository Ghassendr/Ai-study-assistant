package com.raven.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class AddMatiereForm extends JDialog {

    private JTextField matiereNameField;
    private JButton addButton;
    private JButton cancelButton;
    private String newMatiereName;

    public AddMatiereForm(JFrame parent) {
        super(parent, "Ajouter une matière", true);
        setUndecorated(true); // Remove default window decorations
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setSize(400, 300);
        setLocationRelativeTo(parent);

        // Make the window rounded
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));

        // Create a main panel with futuristic styling
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Futuristic gradient background
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(20, 30, 48),
                        getWidth(), getHeight(), new Color(36, 59, 85));
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // Glowing border
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(new Color(0, 168, 255, 150));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 25, 25);
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Create a futuristic title
        JLabel titleLabel = new JLabel("AJOUTER MATIÈRE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 230, 255));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Custom text field
        matiereNameField = new JTextField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2.setColor(new Color(30, 40, 60));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // Border
                g2.setColor(new Color(0, 168, 255));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);

                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // Do nothing to remove default border
            }
        };
        matiereNameField.setForeground(Color.WHITE);
        matiereNameField.setCaretColor(new Color(0, 230, 255));
        matiereNameField.setOpaque(false);
        matiereNameField.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        // Create futuristic buttons
        addButton = createFuturisticButton("AJOUTER", new Color(0, 200, 150));
        cancelButton = createFuturisticButton("ANNULER", new Color(200, 50, 50));

        // Layout components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1;

        mainPanel.add(titleLabel, gbc);

        JLabel nameLabel = new JLabel("NOM DE LA MATIÈRE :");
        nameLabel.setForeground(new Color(180, 230, 255));
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mainPanel.add(nameLabel, gbc);

        gbc.ipady = 10;
        mainPanel.add(matiereNameField, gbc);
        gbc.ipady = 0;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, gbc);

        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newMatiereName = matiereNameField.getText().trim();
                if (!newMatiereName.isEmpty()) {
                    dispose();
                } else {
                    JOptionPane optionPane = new JOptionPane("Le nom de la matière ne peut pas être vide.",
                            JOptionPane.ERROR_MESSAGE);
                    JDialog dialog = optionPane.createDialog("Erreur");
                    dialog.setLocationRelativeTo(AddMatiereForm.this);
                    dialog.setVisible(true);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newMatiereName = null;
                dispose();
            }
        });

        add(mainPanel);
    }

    private JButton createFuturisticButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(baseColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(baseColor.brighter());
                } else {
                    g2.setColor(baseColor);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Glow effect
                if (getModel().isRollover()) {
                    g2.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 100));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                }

                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 80));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
        };

        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(120, 35));

        return button;
    }

    public String getNewMatiereName() {
        return newMatiereName;
    }
}