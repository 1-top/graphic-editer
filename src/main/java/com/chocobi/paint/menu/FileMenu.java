package com.chocobi.paint.menu;

import javax.swing.*;

public class FileMenu extends JMenu {
    public FileMenu(String title) {
        super(title);
        for (MenuConstant.File value : MenuConstant.File.values()) {
            JMenuItem jMenuItem = new JMenuItem(value.getTitle());
            jMenuItem.addActionListener(value.getActionListener());
            jMenuItem.setAccelerator(value.getKeyStroke());
            super.add(jMenuItem);
        }
    }
}
