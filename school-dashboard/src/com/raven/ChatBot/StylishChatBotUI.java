package com.raven.ChatBot;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StylishChatBotUI extends JFrame {

    private final JPanel chatPanel;
    private final JTextField inputField;
    private final JButton sendButton;
    private final JScrollPane scrollPane;


    private final ImageIcon userIcon = new ImageIcon(getClass().getResource("user.png"));
    private final ImageIcon botIcon  = new ImageIcon(getClass().getResource("bot.jpg"));

    public StylishChatBotUI() {
        setTitle("AI Chat Assistant 🤖");
        setSize(1370, 790);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.decode("#F1F2F6"));


        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Horizontal gradient from blue (33, 105, 249) to purple (93, 58, 196)
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(33, 105, 249),
                        getWidth(), 0, new Color(93, 58, 196)
                );

                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(getWidth(), 60));

        // Back Button
        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(33, 105, 249)); // Match button to gradient color
        backButton.setFocusPainted(false);
        backButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Title Label
        JLabel title = new JLabel(" Chat with AI");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        // Add the Back Button and Title to the header
        header.add(backButton);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // --- Chat Area ---
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Color.WHITE);
        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // --- Input Area ---
        JPanel inputBar = new JPanel(new BorderLayout(10, 0));
        inputBar.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputBar.setBackground(Color.decode("#F1F2F6"));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        inputField.setBorder(new CompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(10, 14, 10, 14)
        ));

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(Color.decode("#2F80ED"));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        sendButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        inputBar.add(inputField, BorderLayout.CENTER);
        inputBar.add(sendButton, BorderLayout.EAST);
        add(inputBar, BorderLayout.SOUTH);

        // --- Events ---
        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());

        // --- Back Button Action ---
        backButton.addActionListener(e -> {
            setVisible(false);  // Hide this window
            new com.raven.Frame.Accueil().setVisible(true);  // Show Accueil window again
        });

        setVisible(true);
    }


    private void sendMessage() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) return;

        addChatBubble(text, true);
        inputField.setText("");

        SwingUtilities.invokeLater(() -> {
            String response = getAIResponse(text);
            addChatBubble(response, false);
        });
    }

    private void addChatBubble(String message, boolean isUser) {
        // 1) Message pane with HTML wrap + max width
        JTextPane pane = new JTextPane();
        pane.setContentType("text/html");
        pane.setText("<html><body style='font-family:Segoe UI; font-size:14px;'>" + message + "</body></html>");
        pane.setEditable(false);
        pane.setOpaque(true);
        pane.setBackground(isUser ? new Color(0xE3F2FD) : new Color(0xF0F0F0));
        pane.setBorder(new EmptyBorder(10, 12, 10, 12));
        pane.setMaximumSize(new Dimension(280, Integer.MAX_VALUE));


        JLabel time = new JLabel(getCurrentTime());
        time.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        time.setForeground(Color.GRAY);

        // 3) Stack message + time
        Box box = Box.createVerticalBox();
        box.add(pane);
        box.add(Box.createRigidArea(new Dimension(0, 5)));
        box.add(time);

        // 4) Bubble container (with icon)
        JPanel bubble = new JPanel(new BorderLayout());
        bubble.setBackground(Color.WHITE);
        bubble.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel icon = new JLabel(isUser ? userIcon : botIcon);
        icon.setBorder(new EmptyBorder(0, 5, 0, 5));

        if (isUser) {
            bubble.add(icon, BorderLayout.WEST);
            bubble.add(box, BorderLayout.CENTER);
        } else {
            bubble.add(box, BorderLayout.CENTER);
            bubble.add(icon, BorderLayout.EAST);
        }

        // 5) Alignment wrapper
        FlowLayout layout = new FlowLayout(isUser ? FlowLayout.LEFT : FlowLayout.RIGHT, 10, 5);
        JPanel align = new JPanel(layout);
        align.setBackground(Color.WHITE);
        align.add(bubble);

        // 6) Add to chat
        chatPanel.add(align);
        chatPanel.revalidate();
        chatPanel.repaint();

        // 7) Auto-scroll on EDT
        SwingUtilities.invokeLater(() -> {
            JScrollBar vsb = scrollPane.getVerticalScrollBar();
            vsb.setValue(vsb.getMaximum());
        });
    }

    private String getAIResponse(String msg) {
        try {
            return HuggingFaceChat.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private String getCurrentTime() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm, MMM dd");
        return LocalDateTime.now().format(fmt);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StylishChatBotUI::new);
    }
}
