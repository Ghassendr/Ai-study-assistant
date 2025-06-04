
package com.raven.swing;

import com.raven.component.AddChapitreForm;
import com.raven.component.Menu;
import com.raven.event.EventMenu;
import com.raven.event.EventMenuSelected;
import com.raven.form.Form_Home;
import com.raven.model.ModelMenu;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

public class MenuItem extends javax.swing.JPanel {

    private float alpha;
    private ModelMenu menu;
    private boolean open;
    private EventMenuSelected eventSelected;
    private int index;
    private JPopupMenu popupMenu;

    public ModelMenu getMenu() {
        return menu;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public EventMenuSelected getEventSelected() {
        return eventSelected;
    }

    public void setEventSelected(EventMenuSelected eventSelected) {
        this.eventSelected = eventSelected;
    }

    public int getIndex() {
        return index;
    }

    public MenuItem(ModelMenu menu, EventMenu event, EventMenuSelected eventSelected, int index) {
        initComponents();
        this.menu = menu;
        this.eventSelected = eventSelected;
        this.index = index;
        setOpaque(false);
        setLayout(new MigLayout("wrap, fillx, insets 0", "[fill]", "[fill, 40!]0[fill, 35!]"));

        // Create main menu button
        MenuButton firstItem = new MenuButton(menu.getIcon(), "      " + menu.getMenuName());

        // Handle main menu click
        firstItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (menu.getSubMenu().length > 0) {
                    if (event.menuPressed(MenuItem.this, !open)) {
                        open = !open;
                        refreshMenuItems(firstItem);
                    }
                }
                eventSelected.menuSelected(index, -1);
            }
        });

        // Handle right-click for context menu
        firstItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me)) {
                    popupMenu.show(firstItem, me.getX(), me.getY());
                }
            }
        });

        add(firstItem);
        initPopup();
        createSubMenuItems();
    }

    private void refreshMenuItems(MenuButton firstItem) {
        removeAll();
        setLayout(new MigLayout("wrap, fillx, insets 0", "[fill]", "[fill, 40!]0[fill, 35!]"));
        add(firstItem);
        createSubMenuItems();
        revalidate();
        repaint();
    }

    private void createSubMenuItems() {
        int subMenuIndex = -1;
        for (String st : menu.getSubMenu()) {
            MenuButton item = new MenuButton(st);
            item.setIndex(++subMenuIndex);
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    eventSelected.menuSelected(index, item.getIndex());
                    // Update the home form when a chapter is selected
                    updateHomeFormChapterSelection(st);
                }
            });
            add(item);
        }
    }

    private void updateHomeFormChapterSelection(String chapterName) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            Form_Home homeForm = findHomeForm(frame.getContentPane());
            if (homeForm != null) {
                homeForm.updateSelectedChapter(menu.getMenuName(), chapterName);
            }
        }
    }

    private Form_Home findHomeForm(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof Form_Home) {
                return (Form_Home) comp;
            } else if (comp instanceof Container) {
                Form_Home found = findHomeForm((Container) comp);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private void initPopup() {
        popupMenu = new JPopupMenu();
        popupMenu.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Add Chapter menu item
        JMenuItem addItem = new JMenuItem("Add Chapter");
        addItem.addActionListener(e -> {
            if (menu != null && (menu.getType() == ModelMenu.MenuType.SUBMENU || menu.getType() == ModelMenu.MenuType.TITLE)) {
                // Instead of writing the AddChapterForm logic here, call your existing method
                ((Menu) eventSelected).addChapitreButtonActionPerformed(null);
            }
        });
        popupMenu.add(addItem);

        // Delete Subject menu item
        JMenuItem deleteItem = new JMenuItem("Delete Subject");
        deleteItem.setForeground(Color.RED);
        deleteItem.addActionListener(e -> {
            if (menu != null && (menu.getType() == ModelMenu.MenuType.SUBMENU || menu.getType() == ModelMenu.MenuType.TITLE)) {
                if (eventSelected instanceof com.raven.component.Menu) {
                    ((com.raven.component.Menu) eventSelected).removeMatiere(MenuItem.this);
                    SwingUtilities.invokeLater(() -> {
                        com.raven.component.Menu parentMenu = (com.raven.component.Menu) SwingUtilities.getAncestorOfClass(com.raven.component.Menu.class, MenuItem.this);
                        if (parentMenu != null) {
                            parentMenu.initMenuItem();  // Refresh the menu
                        }
                    });
                }
            }
        });
        popupMenu.add(deleteItem);

        setComponentPopupMenu(popupMenu);
    }


    @Override
    protected void paintComponent(Graphics grphcs) {
        int width = getWidth();
        int height = getPreferredSize().height;
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(50, 50, 50));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.fillRect(0, 2, width, 38);
        g2.setComposite(AlphaComposite.SrcOver);

        if (open && menu.getSubMenu().length > 0) {
            g2.fillRect(0, 40, width, height - 40);
            g2.setColor(new Color(100, 100, 100));
            g2.drawLine(30, 40, 30, height - 17);
            for (int i = 0; i < menu.getSubMenu().length; i++) {
                int y = ((i + 1) * 35 + 40) - 17;
                g2.drawLine(30, y, 38, y);
            }
        }

        if (menu.getSubMenu().length > 0) {
            createArrowButton(g2);
        }
        super.paintComponent(grphcs);
    }

    private void createArrowButton(Graphics2D g2) {
        int size = 4;
        int y = 19;
        int x = 205;
        g2.setColor(new Color(230, 230, 230));
        float ay = alpha * size;
        float ay1 = (1f - alpha) * size;
        g2.drawLine(x, (int) (y + ay), x + 4, (int) (y + ay1));
        g2.drawLine(x + 4, (int) (y + ay1), x + 8, (int) (y + ay));
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );
    }
}