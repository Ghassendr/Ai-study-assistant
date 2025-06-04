package com.raven.main;

import com.raven.Frame.Accueil;

public class Main {

    public static void main(String[] args) {
        // Set the look and feel
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


        javax.swing.SwingUtilities.invokeLater(() -> {
           new  Accueil().setVisible(true);
        });
    }
}
