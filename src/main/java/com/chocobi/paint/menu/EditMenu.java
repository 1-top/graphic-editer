package com.chocobi.paint.menu;

import javax.swing.*;

public class EditMenu extends JMenu {
    public EditMenu(String title) {
        super(title);
        for (MenuConstant.Edit value : MenuConstant.Edit.values()) {
            JMenuItem jMenuItem = new JMenuItem(value.getTitle());
            jMenuItem.addActionListener(value.getActionListener());
            jMenuItem.setAccelerator(value.getKeyStroke());
            super.add(jMenuItem);
        }
    }
}
