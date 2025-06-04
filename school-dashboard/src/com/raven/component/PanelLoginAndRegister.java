package com.raven.component;

import com.raven.Service.UserService;
import com.raven.swing.Button;
import com.raven.swingLogin.MyPasswordField;
import com.raven.swingLogin.MyTextField;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PanelLoginAndRegister extends javax.swing.JLayeredPane {

    private MyTextField txtLoginEmail;
    private MyPasswordField txtLoginPass;
    private MyTextField txtRegName;
    private MyTextField txtRegEmail;
    private MyPasswordField txtRegPass;

    public PanelLoginAndRegister() {
        initComponents();
        initRegister();
        initLogin();
        login.setVisible(false);
        register.setVisible(true);
    }

    private void initRegister() {
        register.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("Create Account");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(new Color(7, 164, 121));
        register.add(label);

        txtRegName = new MyTextField();
        txtRegName.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/user.png")));
        txtRegName.setHint("Name");
        register.add(txtRegName, "w 60%");

        txtRegEmail = new MyTextField();
        txtRegEmail.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/mail.png")));
        txtRegEmail.setHint("Email");
        register.add(txtRegEmail, "w 60%");

        txtRegPass = new MyPasswordField();
        txtRegPass.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/pass.png")));
        txtRegPass.setHint("Password");
        register.add(txtRegPass, "w 60%");

        Button cmd = new Button();
        cmd.setBackground(new Color(7, 164, 121));
        cmd.setForeground(new Color(250, 250, 250));
        cmd.setText("SIGN UP");

        cmd.addActionListener((ActionEvent e) -> {
            String name = txtRegName.getText();
            String email = txtRegEmail.getText();
            String pass = new String(txtRegPass.getPassword());

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!");
                return;
            }

            if (UserService.registerUser(name, email, pass)) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                showRegister(false);
            } else {
                JOptionPane.showMessageDialog(this, "Email already exists!");
            }
        });

        register.add(cmd, "w 40%, h 40");
    }

    private void initLogin() {
        login.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("Sign In");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(new Color(7, 164, 121));
        login.add(label);

        txtLoginEmail = new MyTextField();
        txtLoginEmail.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/mail.png")));
        txtLoginEmail.setHint("Email");
        login.add(txtLoginEmail, "w 60%");

        txtLoginPass = new MyPasswordField();
        txtLoginPass.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/pass.png")));
        txtLoginPass.setHint("Password");
        login.add(txtLoginPass, "w 60%");

        JButton cmdForget = new JButton("Forgot your password ?");
        cmdForget.setForeground(new Color(100, 100, 100));
        cmdForget.setFont(new Font("sansserif", 1, 12));
        cmdForget.setContentAreaFilled(false);
        cmdForget.setCursor(new Cursor(Cursor.HAND_CURSOR));
        login.add(cmdForget);

        Button cmd = new Button();
        cmd.setBackground(new Color(7, 164, 121));
        cmd.setForeground(new Color(250, 250, 250));
        cmd.setText("SIGN IN");

        cmd.addActionListener((ActionEvent e) -> {
            String email = txtLoginEmail.getText();
            String pass = new String(txtLoginPass.getPassword());

            if (email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Email and password are required!");
                return;
            }

            if (UserService.loginUser(email, pass)) {
                JOptionPane.showMessageDialog(this, "Login successful!");

                // Show Accueil and close login frame
                new com.raven.Frame.Accueil().setVisible(true);
                SwingUtilities.getWindowAncestor(this).dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        });

        login.add(cmd, "w 40%, h 40");
    }

    public void showRegister(boolean show) {
        register.setVisible(show);
        login.setVisible(!show);
    }

    private void initComponents() {
        login = new javax.swing.JPanel();
        register = new javax.swing.JPanel();

        setLayout(new java.awt.CardLayout());

        login.setBackground(new Color(255, 255, 255));
        javax.swing.GroupLayout loginLayout = new javax.swing.GroupLayout(login);
        login.setLayout(loginLayout);
        loginLayout.setHorizontalGroup(
                loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 327, Short.MAX_VALUE)
        );
        loginLayout.setVerticalGroup(
                loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );
        add(login, "card3");

        register.setBackground(new Color(255, 255, 255));
        javax.swing.GroupLayout registerLayout = new javax.swing.GroupLayout(register);
        register.setLayout(registerLayout);
        registerLayout.setHorizontalGroup(
                registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 327, Short.MAX_VALUE)
        );
        registerLayout.setVerticalGroup(
                registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );
        add(register, "card2");
    }

    private javax.swing.JPanel login;
    private javax.swing.JPanel register;
}
