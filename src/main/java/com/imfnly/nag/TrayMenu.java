package com.imfnly.nag;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;

public class TrayMenu {

    private PopupMenu popup;

    public TrayMenu(boolean addToSystemTray) {

        popup = new PopupMenu();
        Main.TRAY_ICON.setPopupMenu(popup);
        if (addToSystemTray) {
            addToSystemTray();
        }
    }

    public TrayMenu() {
        this(true);
    }

    public void setClickAction(Runnable action) {
        Main.TRAY_ICON.addActionListener(event -> action.run());
    }

    public void addAction(String item, Runnable action) {
        MenuItem menuItem = new MenuItem(item);
        menuItem.addActionListener(event -> action.run());
        popup.add(menuItem);
    }

    public void addToSystemTray() {
        if (SystemTray.isSupported()) {
            try {
                SystemTray.getSystemTray().add(Main.TRAY_ICON);
            } catch (Exception e) {
                System.err.println("Can't add to tray");
            }
        } else {
            System.err.println("Tray unavailable");
        }
    }
}
