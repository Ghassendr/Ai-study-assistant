package com.raven.quiz;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SmartPDFUI extends JFrame {

    private JLabel pdfNameLabel;
    private JButton choosePDFButton, viewPDFButton, askAIButton;
    private File selectedPDF;

    public SmartPDFUI() {
        //icon
        ImageIcon icon = new ImageIcon("java proj/school-dashboard/src/com/raven/quiz/assetss/folder.png");
        Image scaled = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon icon2 = new ImageIcon("java proj/school-dashboard/src/com/raven/quiz/assetss/eye.png");
        Image scaled2 = icon2.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon icon3 = new ImageIcon("java proj/school-dashboard/src/com/raven/quiz/assetss/google-docs.png");
        Image scaled3 = icon3.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon icon4 = new ImageIcon("java proj/school-dashboard/src/com/raven/quiz/img.png");
        Image scaled4 = icon4.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);




        setTitle("Study Assistant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1370, 790);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Dégradé horizontal de bleu (33,105,249) à violet (93,58,196)
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(33, 105, 249),
                        getWidth(), 0, new Color(93, 58, 196)
                );

                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

// Conserver les autres configurations

        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        add(mainPanel);

        // Title
        JLabel title = new JLabel("Exam Maker", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(255, 255, 255));
        mainPanel.add(title, BorderLayout.NORTH);

        // Center content
        JPanel center = new JPanel();
        center.setBackground(Color.WHITE);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // PDF name label
        pdfNameLabel = new JLabel("No PDF selected");
        pdfNameLabel.setIcon(new ImageIcon(scaled));
        pdfNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        pdfNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Buttons
        choosePDFButton = styledButton(" Choose PDF File");
        choosePDFButton.setIcon(new ImageIcon(scaled3));
        viewPDFButton = styledButton("View PDF");
        viewPDFButton.setIcon(new ImageIcon(scaled2));
        viewPDFButton.setEnabled(false);
        askAIButton = styledButton("Quiz");
        askAIButton.setIcon(new ImageIcon(scaled4));

        // Add components
        center.add(pdfNameLabel);
        center.add(Box.createRigidArea(new Dimension(0, 20)));
        center.add(choosePDFButton);
        center.add(Box.createRigidArea(new Dimension(0, 10)));
        center.add(viewPDFButton);
        center.add(Box.createRigidArea(new Dimension(0, 10)));
        center.add(askAIButton);

        mainPanel.add(center, BorderLayout.CENTER);

        // Button logic
        choosePDFButton.addActionListener(e -> choosePDF());
        viewPDFButton.addActionListener(e -> viewPDF());
        askAIButton.addActionListener(e -> askAI());
    }

    private JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(33, 150, 243));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    private void choosePDF() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedPDF = chooser.getSelectedFile();
            pdfNameLabel.setText("Selected: " + selectedPDF.getName());
            viewPDFButton.setEnabled(true);
        }
    }

    private void viewPDF() {
        try {
            Desktop.getDesktop().open(selectedPDF);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot open PDF", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void askAI() {
        dispose();
        QuizApp quizApp=new QuizApp();
        quizApp.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf()); // Modern look
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        SwingUtilities.invokeLater(() -> {
            SmartPDFUI ui = new SmartPDFUI();
            ui.setVisible(true);
        });
    }
}
