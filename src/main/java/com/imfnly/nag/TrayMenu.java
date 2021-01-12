package com.imfnly.nag;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

public class TrayMenu {

    private PopupMenu popup;
    private TrayIcon trayIcon;

    public TrayMenu(String imageFileName, boolean addToSystemTray) {

        popup = new PopupMenu();
        Image image = Toolkit.getDefaultToolkit().getImage(imageFileName);
        trayIcon = new TrayIcon(image, "Nag To Sleep", popup);
        if (addToSystemTray) {
            addToSystemTray();
        }
    }

    public TrayMenu(String imageFileName) {
        this(imageFileName, true);
    }

    public void setClickAction(Runnable action) {
        trayIcon.addActionListener(event -> action.run());
    }

    public void addAction(String item, Runnable action) {
        MenuItem menuItem = new MenuItem(item);
        menuItem.addActionListener(event -> action.run());
        popup.add(menuItem);
    }

    public void addToSystemTray() {
        if (SystemTray.isSupported()) {
            try {
                SystemTray.getSystemTray().add(trayIcon);
            } catch (Exception e) {
                System.err.println("Can't add to tray");
            }
        } else {
            System.err.println("Tray unavailable");
        }
    }
}
