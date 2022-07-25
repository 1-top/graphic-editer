package com.chocobi.paint.frame;


import com.chocobi.paint.menu.MenuBar;
import com.chocobi.paint.panel.DrawingDesktopPane;
import com.chocobi.paint.toolbar.ShapeAttributeToolBar;
import org.apache.commons.lang3.SystemUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    private static final int width = 1000;
    private static final int height = 700;

    public MainFrame() {
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.addWindowListener(new WindowHandler());

        super.setSize(width, height);
        super.setLayout(new BorderLayout());
        this.setApplicationIcon();

        super.add(ShapeAttributeToolBar.getInstance(), BorderLayout.NORTH);
        super.add(DrawingDesktopPane.getInstance().getToolBar(), BorderLayout.WEST);
        super.add(DrawingDesktopPane.getInstance(), BorderLayout.CENTER);

        super.setJMenuBar(new MenuBar());
    }

    private void setApplicationIcon() {
        Image image = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/icon.png")).getImage();
        if (SystemUtils.IS_OS_MAC) Taskbar.getTaskbar().setIconImage(image);
        else super.setIconImage(image);
    }

    private static class WindowHandler extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            DrawingDesktopPane.getInstance().quit();
        }
    }
}
