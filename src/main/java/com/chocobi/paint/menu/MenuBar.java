package com.chocobi.paint.menu;

import com.chocobi.paint.global.constant.Constant;
import com.chocobi.paint.global.swing.util.SwingUtil;

import javax.swing.*;

import static com.chocobi.paint.global.constant.Constant.RESOURCE_BUNDLE;

public class MenuBar extends JMenuBar {
    private FileMenu fileMenu;
    private EditMenu editMenu;

    public MenuBar() {
        this.fileMenu = new FileMenu(RESOURCE_BUNDLE.getString(Constant.MessageKey.FILE.getKey()));
        this.editMenu = new EditMenu(RESOURCE_BUNDLE.getString(Constant.MessageKey.EDIT.getKey()));
        SwingUtil.addAll(this, this.fileMenu, this.editMenu);
    }
}
