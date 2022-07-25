package com.chocobi.paint.panel;

import com.chocobi.paint.frame.DrawingInternalFrame;
import com.chocobi.paint.global.constant.Constant;
import com.chocobi.paint.infra.file.AlreadyOpenedFileException;
import com.chocobi.paint.infra.file.InvalidFileException;
import com.chocobi.paint.toolbar.ToolBar;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.*;
import java.io.File;

import static com.chocobi.paint.global.constant.Constant.RESOURCE_BUNDLE;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DrawingDesktopPane extends JDesktopPane {
    private static final DrawingDesktopPane INSTANCE = new DrawingDesktopPane();

    private final InternalFrameListener internalFrameListener = new DrawingInternalFrameHandler();
    private DrawingInternalFrame currentInternalFrame;

    private final ToolBar toolBar = new ToolBar(JToolBar.VERTICAL);

    public DrawingInternalFrame newDrawingPanel() {
        this.currentInternalFrame = new DrawingInternalFrame(this.internalFrameListener);
        super.add(this.currentInternalFrame);
        this.currentInternalFrameMoveToFrontAndSetActivated();
        return this.currentInternalFrame;
    }

    private void currentInternalFrameMoveToFrontAndSetActivated() {
        this.currentInternalFrame.moveToFront();
        this.currentInternalFrame.setVisible(false);
        this.currentInternalFrame.setVisible(true);
    }

    @SneakyThrows
    public void open() {
        DrawingInternalFrame drawingInternalFrame = null;
        try {
            Constant.FILE_CHOOSER.showOpenDialog(this);
            File file = requireNonNull(Constant.FILE_CHOOSER.getSelectedFile());
            this.validateAlreadyOpenedFile(file);
            drawingInternalFrame = this.newDrawingPanel();
            drawingInternalFrame.setVisible(false);
            drawingInternalFrame.load(file);
        } catch (InvalidFileException e) {
            drawingInternalFrame.setClosed(true);
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    RESOURCE_BUNDLE.getString(Constant.MessageKey.ERROR.getKey()),
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (NullPointerException | AlreadyOpenedFileException ignored) {
        }
    }

    private void validateAlreadyOpenedFile(File file) {
        for (Component component : super.getComponents()) {
            DrawingInternalFrame drawingInternalFrame = (DrawingInternalFrame) component;
            File drawingFile = drawingInternalFrame.getFile();
            if (drawingFile == null) continue;
            if (drawingFile.equals(file)) {
                this.currentInternalFrame = drawingInternalFrame;
                this.currentInternalFrameMoveToFrontAndSetActivated();
                throw new AlreadyOpenedFileException();
            }
        }
    }

    @SneakyThrows
    public void clickCloseButton() {
        if (isNull(this.currentInternalFrame)) return;
        this.currentInternalFrame.setClosed(true);
    }

    private void close() {
        if (isNull(this.currentInternalFrame)) return;
        if (this.currentInternalFrame.isUpdated() && this.showUpdateSaveDialog() == JOptionPane.YES_OPTION) this.save();
    }

    private int showUpdateSaveDialog() {
        Object[] options = {
                RESOURCE_BUNDLE.getString(Constant.MessageKey.SAVE_YES.getKey()),
                RESOURCE_BUNDLE.getString(Constant.MessageKey.SAVE_NO.getKey())
        };
        String message = String.format(
                RESOURCE_BUNDLE.getString("dialog.save.message"),
                isNull(this.currentInternalFrame.getFile()) ?
                        DrawingInternalFrame.DEFAULT_TITLE :
                        this.currentInternalFrame.getFile().getName()
        );
        return JOptionPane.showOptionDialog(
                this,
                message,
                RESOURCE_BUNDLE.getString(Constant.MessageKey.INFORMATION.getKey()),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );
    }

    public void save() {
        if (isNull(this.currentInternalFrame)) return;
        this.currentInternalFrame.save();
    }

    public void saveAs() {
        if (isNull(this.currentInternalFrame)) return;
        this.currentInternalFrame.saveAs();
    }

    @SneakyThrows
    public void quit() {
        for (Component component : super.getComponents()) {
            if (component instanceof JInternalFrame.JDesktopIcon)
                ((JInternalFrame.JDesktopIcon) component).getInternalFrame().setClosed(true);
            else ((JInternalFrame) component).setClosed(true);
        }
        System.exit(0);
    }

    public void print() {
        if (isNull(this.currentInternalFrame)) return;
        this.currentInternalFrame.print();
    }

    public static DrawingDesktopPane getInstance() {
        return INSTANCE;
    }

    private class DrawingInternalFrameHandler extends InternalFrameAdapter {
        @Override
        public void internalFrameClosing(InternalFrameEvent e) {
            close();
        }

        @Override
        public void internalFrameActivated(InternalFrameEvent e) {
            currentInternalFrame = (DrawingInternalFrame) e.getInternalFrame();
        }
    }
}
