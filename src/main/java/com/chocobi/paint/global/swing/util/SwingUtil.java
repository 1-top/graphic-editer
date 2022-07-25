package com.chocobi.paint.global.swing.util;

import javax.swing.*;
import java.awt.*;

public class SwingUtil {
    public static void setFrameLocationCenter(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((screenSize.getWidth() - window.getWidth()) / 2);
        int y = (int) ((screenSize.getHeight() - window.getHeight()) / 2);
        window.setLocation(x, y);
    }

    @SafeVarargs
    public static <T extends JComponent> void addAll(JComponent container, T... components) {
        for (T component : components) container.add(component);
    }
}
