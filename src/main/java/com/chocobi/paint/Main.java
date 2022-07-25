package com.chocobi.paint;

import com.chocobi.paint.frame.MainFrame;
import com.chocobi.paint.global.swing.util.SwingUtil;
import com.formdev.flatlaf.FlatLightLaf;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.SystemUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.desktop.QuitStrategy;

public class Main {
    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatLightLaf());
        isMacOsSetProperty();
        MainFrame mainFrame = new MainFrame();
        SwingUtil.setFrameLocationCenter(mainFrame);
        mainFrame.setVisible(true);
    }

    private static void isMacOsSetProperty() {
        if (SystemUtils.IS_OS_MAC) {
            System.setProperty("apple.laf.useScreenMenuBar", BooleanUtils.TRUE);
            Desktop.getDesktop().setQuitStrategy(QuitStrategy.CLOSE_ALL_WINDOWS);
            Desktop.getDesktop().disableSuddenTermination();
        }
    }
}
