package com.raven.model;

import javax.swing.Icon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Modèle d'une matière, avec icône, nom et liste dynamique de chapitres.
 */
public class ModelMenu {

    private Icon icon;
    private String menuName;
    private List<String> menuItems;

    // (optionnel) Ajout d'un type si besoin
    public enum MenuType { ITEM, SUBMENU, TITLE }
    private MenuType type = MenuType.SUBMENU;

    /** Constructeur principal : icône, nom de la matière et chapitres initiaux. */
    public ModelMenu(Icon icon, String menuName, String... subMenu) {
        this.icon = icon;
        this.menuName = menuName;
        this.menuItems = new ArrayList<>();
        if (subMenu != null) {
            this.menuItems.addAll(Arrays.asList(subMenu));
        }
    }

    /** Constructeur vide */
    public ModelMenu() {
        this.menuItems = new ArrayList<>();
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public List<String> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<String> menuItems) {
        this.menuItems = menuItems;
    }

    public String[] getSubMenu() {
        return menuItems.toArray(new String[0]);
    }

    public void setSubMenu(String[] subMenu) {
        this.menuItems = new ArrayList<>(Arrays.asList(subMenu));
    }




    // Ajout des méthodes getType/setType si tu veux gérer différents types de menus
    public MenuType getType() {
        return type;
    }

    public void setType(MenuType type) {
        this.type = type;
    }




    // Dans votre classe com.raven.component.Menu (ou similaire)
    public void addChapitreToMatiere(ModelMenu menu, String chapitre) {
        if (menu != null) {
            menu.getMenuItems().add(chapitre);
            // Potentiellement notifier un changement si vous utilisez un modèle de données observable
        }
    }
}