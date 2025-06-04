package com.raven.component;

import com.raven.event.EventMenu;
import com.raven.event.EventMenuSelected;
import com.raven.event.EventShowPopupMenu;
import com.raven.model.ModelMenu;
import com.raven.swing.MenuAnimation;
import com.raven.swing.MenuItem;
import com.raven.swing.scrollbar.ScrollBarCustom;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;

public class Menu extends javax.swing.JPanel {

    private javax.swing.JButton addMatiereButton;
    private javax.swing.JButton addChapitreButton;
    private ModelMenu selectedMatiere;

    public boolean isShowMenu() {
        return showMenu;
    }

    public void addEvent(EventMenuSelected event) {
        this.event = event;
    }

    public void setEnableMenu(boolean enableMenu) {
        this.enableMenu = enableMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    public void addEventShowPopup(EventShowPopupMenu eventShowPopup) {
        this.eventShowPopup = eventShowPopup;
    }

    private final MigLayout layout;
    private EventMenuSelected event;
    private EventShowPopupMenu eventShowPopup;
    private boolean enableMenu = true;
    private boolean showMenu = true;

    public Menu() {





        initComponents();
        setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setVerticalScrollBar(new ScrollBarCustom());
        layout = new MigLayout("wrap, fillx, insets 0", "[fill]", "[]0[]");
        panel.setLayout(layout);

        addMatiereButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                addMatiereButtonActionPerformed(evt);
            }
        });

        addChapitreButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                addChapitreButtonActionPerformed(evt);
            }
        });
        addChapitreButton.setEnabled(false);
    }

    public void addChapitreButtonActionPerformed(ActionEvent evt) {
        if (selectedMatiere != null) {
            JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            AddChapitreForm form = new AddChapitreForm(mainFrame, selectedMatiere.getMenuName());
            form.setVisible(true);
            String newChapitre = form.getNewChapitreName();
            if (newChapitre != null && !newChapitre.trim().isEmpty()) { // Vérification: non null et non vide
                System.out.println("Ajouter le chapitre '" + newChapitre + "' à la matière '" + selectedMatiere.getMenuName() + "'");
                addChapitreToMatiere(selectedMatiere, newChapitre);
            } else {
                System.out.println("Nom du chapitre invalide.");
                // Potentiellement afficher un message à l'utilisateur
            }
        } else {
            System.out.println("Veuillez sélectionner une matière avant d'ajouter un chapitre.");
            // Potentiellement afficher un message à l'utilisateur
        }
    }

    public void initMenuItem() {
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/com/raven/icon/1.png")), "Math", "Statestique", "matrice", "graph"));

        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/com/raven/icon/2.png")), "info ", "AVR", "ABR"));
    }

    private void addMenu(ModelMenu menu) {
        if (menu != null && menu.getMenuName() != null) { // Vérification: menu et nom non nuls
            MenuItem item = new MenuItem(menu, getEventMenu(), new EventMenuSelected() {
                @Override
                public void menuSelected(int menuIndex, int subMenuIndex) {
                    if (menu.getType() == ModelMenu.MenuType.SUBMENU || menu.getType() == ModelMenu.MenuType.TITLE) {
                        selectedMatiere = menu;
                        addChapitreButton.setEnabled(true);
                    } else {
                        selectedMatiere = null;
                        addChapitreButton.setEnabled(false);
                    }
                    if (event != null) {
                        event.menuSelected(menuIndex, subMenuIndex);
                    }
                }
            }, panel.getComponentCount());
            panel.add(item, "h 40!");
        } else {
            System.out.println("Tentative d'ajouter un menu invalide (null).");
        }
    }

    private EventMenu getEventMenu() {
        return new EventMenu() {
            @Override
            public boolean menuPressed(Component com, boolean open) {
                if (enableMenu) {
                    if (isShowMenu()) {
                        MenuItem item = (MenuItem) com;
                        if (open) {
                            new MenuAnimation(layout, com).openMenu();
                            // item.hideSubMenu(); // Supprimez cette ligne
                        } else {
                            new MenuAnimation(layout, com).closeMenu();
                        }
                        return true;
                    } else {
                        eventShowPopup.showPopup(com);
                    }
                }
                return false;
            }
        };
    }

    public void hideallMenu() {
        for (Component com : panel.getComponents()) {
            MenuItem item = (MenuItem) com;
            if (item.isOpen()) {
                new MenuAnimation(layout, com, 500).closeMenu();
                item.setOpen(false);
            }
        }
    }

    private void addMatiereButtonActionPerformed(ActionEvent evt) {
        JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        AddMatiereForm form = new AddMatiereForm(mainFrame);
        form.setVisible(true);
        String newMatiere = form.getNewMatiereName();
        if (newMatiere != null && !newMatiere.trim().isEmpty()) { // Vérification: non null et non vide
            System.out.println("Nouvelle matière à ajouter : " + newMatiere);
            addNewMatiereToMenu(newMatiere);
        } else {
            System.out.println("Nom de la matière invalide.");
            // Potentiellement afficher un message à l'utilisateur
        }
    }

    public void addNewMatiereToMenu(String matiereName) {
        if (matiereName != null && !matiereName.trim().isEmpty()) { // Vérification: non null et non vide
            ModelMenu newMenu = new ModelMenu(new ImageIcon(getClass().getResource("/com/raven/icon/2.png")), matiereName);

            addMenu(newMenu);
            panel.revalidate();
            panel.repaint();
        } else {
            System.out.println("Nom de la matière à ajouter invalide.");
        }
    }

    public void addChapitreToMatiere(ModelMenu matiere, String chapitreName) {
        for (Component com : panel.getComponents()) {
            if (com instanceof MenuItem) {
                MenuItem menuItem = (MenuItem) com;
                if (menuItem.getMenu().equals(matiere)) {
                    // Ajouter le nouveau chapitre au ModelMenu
                    String[] existingSubMenu = matiere.getSubMenu();
                    String[] newSubMenu = new String[existingSubMenu.length + 1];
                    System.arraycopy(existingSubMenu, 0, newSubMenu, 0, existingSubMenu.length);
                    newSubMenu[newSubMenu.length - 1] = chapitreName;
                    matiere.setSubMenu(newSubMenu);

                    // Forcer la réouverture du sous-menu pour afficher le nouveau chapitre
                    if (menuItem.isOpen()) {
                        menuItem.setOpen(false);
                        menuItem.setOpen(true);
                    } else {
                        menuItem.revalidate();
                        menuItem.repaint();
                    }

                    return;
                }
            }
        }
        System.out.println("Matière non trouvée : " + matiere.getMenuName());
    }

    public void removeMatiere(MenuItem itemToRemove) {
        if (itemToRemove != null && itemToRemove.getMenu() != null) { // Vérifications
            ModelMenu matiereToRemove = itemToRemove.getMenu();
            panel.remove(itemToRemove);
            panel.revalidate();
            panel.repaint();
            System.out.println("Supprimer la matière : " + matiereToRemove.getMenuName());
            // Ici, vous devrez ajouter la logique pour supprimer la matière de votre modèle de données
        } else {
            System.out.println("Tentative de supprimer un MenuItem invalide.");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sp = new javax.swing.JScrollPane();
        panel = new javax.swing.JPanel();
        profile1 = new Profile();
        addMatiereButton = new javax.swing.JButton();
        addChapitreButton = new javax.swing.JButton();

        sp.setBorder(null);
        sp.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setViewportBorder(null);

        panel.setOpaque(false);

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
                panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(addMatiereButton, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                        .addComponent(addChapitreButton, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                        .addGap(0, 312, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
                panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(addMatiereButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addChapitreButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGap(0, 490, Short.MAX_VALUE))
        );

        addMatiereButton.setText("+ Ajouter une matière");
        addMatiereButton.setFocusPainted(false);
        addMatiereButton.setContentAreaFilled(false);
        addMatiereButton.setBorderPainted(false);
        addMatiereButton.setForeground(Color.WHITE);

        addChapitreButton.setText("+ Ajouter un chapitre");
        addChapitreButton.setFocusPainted(false);
        addChapitreButton.setContentAreaFilled(false);
        addChapitreButton.setBorderPainted(false);
        addChapitreButton.setForeground(Color.WHITE);
        addChapitreButton.setEnabled(false);

        sp.setViewportView(panel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                        .addComponent(profile1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(profile1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gra = new GradientPaint(0, 0, new Color(33, 105, 249), getWidth(), 0, new Color(93, 58, 196));
        g2.setPaint(gra);
        g2.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(grphcs);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panel;
    private Profile profile1;
    private javax.swing.JScrollPane sp;
    // End of variables declaration//GEN-END:variables
}
