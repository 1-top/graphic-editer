package com.chocobi.paint.menu;

import com.chocobi.paint.panel.DrawingDesktopPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static com.chocobi.paint.global.constant.Constant.RESOURCE_BUNDLE;

public class MenuConstant {
    @Getter
    @RequiredArgsConstructor
    public enum File {
        NEW(
                RESOURCE_BUNDLE.getString("menu.file.new"),
                KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()),
                e -> DrawingDesktopPane.getInstance().newDrawingPanel()
        ),
        OPEN(
                RESOURCE_BUNDLE.getString("menu.file.open"),
                KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()),
                e -> DrawingDesktopPane.getInstance().open()
        ),
        CLOSE(
                RESOURCE_BUNDLE.getString("menu.file.close"),
                KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()),
                e -> DrawingDesktopPane.getInstance().clickCloseButton()
        ),
        SAVE(
                RESOURCE_BUNDLE.getString("menu.file.save"),
                KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()),
                e -> DrawingDesktopPane.getInstance().save()
        ),
        SAVE_AS(
                RESOURCE_BUNDLE.getString("menu.file.save_as"),
                KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | Event.SHIFT_MASK),
                e -> DrawingDesktopPane.getInstance().saveAs()
        ),
        PRINT(
                RESOURCE_BUNDLE.getString("menu.file.print"),
                KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()),
                e -> DrawingDesktopPane.getInstance().print()
        ),
        QUIT(
                RESOURCE_BUNDLE.getString("menu.file.quit"),
                KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()),
                e -> DrawingDesktopPane.getInstance().quit()
        );

        private final String title;
        private final KeyStroke keyStroke;
        private final ActionListener actionListener;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Edit {
        SELECT_ALL(
                RESOURCE_BUNDLE.getString("menu.edit.select_all"),
                KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()),
                e -> DrawingDesktopPane.getInstance().getCurrentInternalFrame().getDrawingPanel().selectAllShapes()
        ),
        DESELECT_ALL(
                RESOURCE_BUNDLE.getString("menu.edit.deselect_all"),
                KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | Event.SHIFT_MASK),
                e -> DrawingDesktopPane.getInstance().getCurrentInternalFrame().getDrawingPanel().deselectAllShapes()),
        BRING_TO_FRONT(
                RESOURCE_BUNDLE.getString("menu.edit.bring_to_front"),
                KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()),
                e -> DrawingDesktopPane.getInstance().getCurrentInternalFrame().getDrawingPanel().bringToFront()
        ),
        BRING_TO_FORWARD(
                RESOURCE_BUNDLE.getString("menu.edit.bring_to_forward"),
                KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | Event.SHIFT_MASK),
                e -> DrawingDesktopPane.getInstance().getCurrentInternalFrame().getDrawingPanel().bringToForwardShape()
        ),
        SEND_TO_BACK(
                RESOURCE_BUNDLE.getString("menu.edit.send_to_back"),
                KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()),
                e -> DrawingDesktopPane.getInstance().getCurrentInternalFrame().getDrawingPanel().sendToBack()
        ),
        SEND_TO_BACKWARD(
                RESOURCE_BUNDLE.getString("menu.edit.send_to_backward"),
                KeyStroke.getKeyStroke(KeyEvent.VK_B, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | Event.SHIFT_MASK),
                e -> DrawingDesktopPane.getInstance().getCurrentInternalFrame().getDrawingPanel().sendToBackwardShape()
        ),
        UNDO(
                RESOURCE_BUNDLE.getString("menu.edit.undo"),
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()),
                e -> DrawingDesktopPane.getInstance().getCurrentInternalFrame().getDrawingPanel().getShapeMemory().undo()
        ),
        //        REDO(RESOURCE_BUNDLE.getString("menu.edit.redo"), e -> DrawingDesktopPane.getInstance().getCurrentInternalFrame().getDrawingPanel().getShapeMemory().),
        CUT(
                RESOURCE_BUNDLE.getString("menu.edit.cut"),
                KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()),
                e -> DrawingDesktopPane.getInstance().getCurrentInternalFrame().getDrawingPanel().getShapeMemory().cut()
        ),
        COPY(
                RESOURCE_BUNDLE.getString("menu.edit.copy"),
                KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()),
                e -> DrawingDesktopPane.getInstance().getCurrentInternalFrame().getDrawingPanel().getShapeMemory().copy()
        ),
        PASTE(
                RESOURCE_BUNDLE.getString("menu.edit.paste"),
                KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()),
                e -> DrawingDesktopPane.getInstance().getCurrentInternalFrame().getDrawingPanel().getShapeMemory().paste()
        );
//        GROUP(RESOURCE_BUNDLE.getString("menu.edit.group"), e -> {}),
//        UN_GROUP(RESOURCE_BUNDLE.getString("menu.edit.un_group"), e -> {});

        private final String title;
        private final KeyStroke keyStroke;
        private final ActionListener actionListener;
    }
}
