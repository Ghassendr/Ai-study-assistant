package com.raven.quiz;

import com.formdev.flatlaf.FlatLightLaf;
import com.raven.Frame.Accueil;
import com.raven.main.Main;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizApp extends JFrame {

    /* ---------- theme ---------- */
    private static final Color BLUE = new Color(0xFFD2CFCA, true);
    private static final Color BLUE_DARK = new Color(0x004FA0);
    private static final Color RED_ALERT = new Color(0xCC0000);
    private static final Font FONT_Q = new Font("SansSerif", Font.BOLD, 18);
    private static final Font FONT_OPT = new Font("SansSerif", Font.PLAIN, 16);
    private static final Font FONT_BTN = new Font("SansSerif", Font.BOLD, 15);

    /* ---------- data ---------- */
    private List<Question> questions;
    private int idx = 0, score = 0;

    /* ---------- widgets ---------- */
    private final JLabel lblQuestion = new JLabel();
    private final JRadioButton[] radios = new JRadioButton[4];
    private final ButtonGroup group = new ButtonGroup();
    private final JLabel lblTimer = new JLabel();
    private final javax.swing.Timer timer;

    private int secondsLeft = 20;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuizApp::new);
    }

    public QuizApp() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ignore) {
        }
        UIManager.put("Button.arc", 20);
        UIManager.put("Component.focusWidth", 0);

        loadQuestions("java proj/school-dashboard/src/com/raven/quiz/quiz1.json");
        Collections.shuffle(questions);

        setUndecorated(true);
        setSize(1000, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(createIllustrationPanel(), BorderLayout.WEST);
        add(createContentPanel(), BorderLayout.CENTER);

        addMouseListener(new Drag());
        addMouseMotionListener(new Drag());

        displayQuestion();
        setVisible(true);

        /* ---- countdown ---- */
        timer = new javax.swing.Timer(1000, e -> tick());
        timer.start();
    }

    /* ---------- header bar ---------- */
    private JPanel buildHeader() {
        JPanel head = new JPanel(new BorderLayout());
        head.setBackground(BLUE);
        head.setPreferredSize(new Dimension(0, 38));

        JLabel btnHome = iconButton("back.png");
        JLabel btnExit = iconButton("closer.png");

        btnHome.setToolTipText("Back to Dashboard");
        btnExit.setToolTipText("Exit");

        // Action for the back icon
        btnHome.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose(); // Close the Quiz window

                // Find and show the hidden Accueil window
                for (Frame frame : Frame.getFrames()) {
                    if (frame.isDisplayable() && frame.getTitle().equals("Accueil")) {
                        frame.setVisible(true);
                        frame.toFront();
                        return;
                    }
                }
                try {
                    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            javax.swing.UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
                // Fallback: open a new one if needed
                new Accueil().setVisible(true);
            }
        });





        // Action for the exit icon
        btnExit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        head.add(btnHome, BorderLayout.WEST);
        head.add(btnExit, BorderLayout.EAST);
        return head;
    }

    /** loads an image, scales it to 24×24 px, returns as clickable label */
    private JLabel iconButton(String path) {
        ImageIcon raw = new ImageIcon(getClass().getResource(path));
        Image scaled = raw.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        JLabel l = new JLabel(new ImageIcon(scaled));
        l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return l;
    }

    /* ---------- left picture ---------- */
    private JPanel createIllustrationPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(320, 0));
        p.setBackground(Color.WHITE);

        ImageIcon raw = new ImageIcon(getClass().getResource("java.png"));
        int tgtW = 320, tgtH = raw.getIconHeight() * tgtW / raw.getIconWidth();
        ImageIcon scaled = new ImageIcon(
                raw.getImage().getScaledInstance(tgtW, tgtH, Image.SCALE_SMOOTH));
        p.add(new JLabel(scaled), BorderLayout.CENTER);
        return p;
    }

    /* ---------- right panel ---------- */
    private JPanel createContentPanel() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(Color.WHITE);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;

        /* question */
        lblQuestion.setFont(FONT_Q);
        gc.gridy = 0;
        root.add(lblQuestion, gc);

        /* options */
        JPanel opts = new JPanel(new GridLayout(4, 1, 4, 4));
        opts.setBackground(Color.WHITE);
        for (int i = 0; i < radios.length; i++) {
            radios[i] = new JRadioButton();
            radios[i].setBackground(Color.WHITE);
            radios[i].setFont(FONT_OPT);
            group.add(radios[i]);
            opts.add(radios[i]);
        }
        gc.gridy = 1;
        root.add(opts, gc);

        /* timer + next */
        JButton btnNext = new JButton("NEXT");
        style(btnNext);
        btnNext.addActionListener(e -> nextPressed());

        lblTimer.setFont(FONT_BTN);
        lblTimer.setForeground(BLUE_DARK);

        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(Color.WHITE);
        south.add(lblTimer, BorderLayout.WEST);
        south.add(btnNext, BorderLayout.EAST);

        gc.gridy = 2;
        gc.anchor = GridBagConstraints.CENTER;
        root.add(south, gc);
        return root;
    }

    private void style(AbstractButton b) {
        b.setFont(FONT_BTN);
        b.setForeground(Color.WHITE);
        b.setBackground(BLUE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(BLUE_DARK);
            }

            public void mouseExited(MouseEvent e) {
                b.setBackground(BLUE);
            }
        });
    }

    /* ---------- logic ---------- */
    private void displayQuestion() {
        secondsLeft = 20;
        updateTimerLabel();
        group.clearSelection();
        Question q = questions.get(idx);
        lblQuestion.setText("Q" + (idx + 1) + ". " + q.text);
        for (int i = 0; i < radios.length; i++) {
            if (i < q.answers.size()) {
                radios[i].setText(q.answers.get(i).text);
                radios[i].setVisible(true);
            } else
                radios[i].setVisible(false);
        }
    }

    private void nextPressed() {
        evaluate();
    }

    private void evaluate() {
        Question q = questions.get(idx);
        for (int i = 0; i < q.answers.size(); i++)
            if (radios[i].isSelected() && q.answers.get(i).isCorrect) {
                score++;
                break;
            }
        idx++;
        if (idx < questions.size()) {
            displayQuestion();
        } else {
            timer.stop();
            dispose();
            new ResultFrame(score, questions.size());
        }
    }

    private void tick() {
        secondsLeft--;
        updateTimerLabel();
        if (secondsLeft <= 0)
            evaluate();
    }

    private void updateTimerLabel() {
        lblTimer.setText("Time left : " + secondsLeft + "s");
        lblTimer.setForeground(secondsLeft <= 5 ? RED_ALERT : BLUE_DARK);
    }

    /* ---------- result ---------- */
    private class ResultFrame extends JFrame {
        ResultFrame(int correct, int total) {
            setUndecorated(true);
            setSize(450, 260);
            setLocationRelativeTo(null);
            setLayout(new GridBagLayout());
            getContentPane().setBackground(Color.WHITE);
            addMouseListener(new Drag());
            addMouseMotionListener(new Drag());

            GridBagConstraints g = new GridBagConstraints();
            g.insets = new Insets(6, 6, 6, 6);
            g.gridwidth = 2;

            JLabel title = lab("Your Performance Card", 20, true);
            title.setForeground(BLUE);
            add(title, g);
            g.gridwidth = 1;
            g.anchor = GridBagConstraints.WEST;

            g.gridy = 1;
            add(lab("Correct Answers :", 16, false), g);
            g.gridx = 1;
            g.anchor = GridBagConstraints.EAST;
            add(lab(String.valueOf(correct), 16, false), g);

            g.gridy++;
            g.gridx = 0;
            g.anchor = GridBagConstraints.WEST;
            add(lab("Incorrect Answers :", 16, false), g);
            g.gridx = 1;
            g.anchor = GridBagConstraints.EAST;
            add(lab(String.valueOf(total - correct), 16, false), g);

            g.gridy++;
            g.gridx = 0;
            g.anchor = GridBagConstraints.WEST;
            add(lab("Score :", 16, false), g);
            g.gridx = 1;
            g.anchor = GridBagConstraints.EAST;
            add(lab(correct * 4 + "/" + (total * 4), 16, false), g);

            g.gridy++;
            g.gridx = 0;
            g.gridwidth = 2;
            g.anchor = GridBagConstraints.CENTER;
            String remark = (correct >= total * 0.8) ? "Excellent!" : (correct >= total * 0.5) ? "Average" : "Needs practice";
            add(lab(remark, 16, false), g);

            JButton again = new JButton("PLAY AGAIN"), exit = new JButton("EXIT");
            style(again);
            style(exit);
            again.addActionListener(e -> {
                dispose();
                new QuizApp();
            });
            exit.addActionListener(e -> {
                dispose(); // Ferme la fenêtre actuelle
                new Accueil().setVisible(true); // Affiche la nouvelle fenêtre
            });

            JPanel bp = new JPanel(new GridLayout(1, 2, 20, 0));
            bp.setBackground(Color.WHITE);
            bp.add(again);
            bp.add(exit);

            g.gridy++;
            add(bp, g);
            setVisible(true);
        }

        private JLabel lab(String txt, int sz, boolean bold) {
            JLabel l = new JLabel(txt);
            l.setFont(new Font("SansSerif", bold ? Font.BOLD : Font.PLAIN, sz));
            return l;
        }
    }

    /* ---------- data ---------- */
    private void loadQuestions(String file) {
        try {
            String js = Files.readString(Paths.get(file));
            JSONArray arr = new JSONArray(js);
            questions = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                JSONArray a = o.getJSONArray("answers");
                List<Answer> list = new ArrayList<>();
                for (int j = 0; j < a.length(); j++) {
                    JSONObject aa = a.getJSONObject(j);
                    list.add(new Answer(aa.getString("text"), aa.getBoolean("isCorrect")));
                }
                questions.add(new Question(o.getString("question"), list));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Load error: " + ex.getMessage());
            System.exit(1);
        }
    }

    /* ---------- drag‑to‑move undecorated window ---------- */
    private class Drag extends MouseAdapter {
        private Point p;

        public void mousePressed(MouseEvent e) {
            p = e.getPoint();
        }

        public void mouseDragged(MouseEvent e) {
            Point loc = getLocation();
            setLocation(loc.x + e.getX() - p.x, loc.y + e.getY() - p.y);
        }
    }

    /* ---------- DTOs ---------- */
    private record Question(String text, List<Answer> answers) {}

    private record Answer(String text, boolean isCorrect) {}
}
