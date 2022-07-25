package com.chocobi.paint.frame;

import com.chocobi.paint.global.constant.Constant;
import com.chocobi.paint.infra.file.FileService;
import com.chocobi.paint.infra.file.LocalFileServiceImpl;
import com.chocobi.paint.panel.DrawingPanel;
import com.chocobi.paint.shape.CustomShape;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.event.InternalFrameListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.util.List;
import java.util.Objects;

@Getter
public class DrawingInternalFrame extends JInternalFrame {
    public static final String DEFAULT_TITLE = "undefined";

    private File file;
    private final FileService<List<CustomShape>> fileService;
    private final DrawingPanel drawingPanel = new DrawingPanel();

    public DrawingInternalFrame(InternalFrameListener internalFrameListener) {
        super(DEFAULT_TITLE, true, true, true, true);
        super.setFrameIcon(null);
        super.addInternalFrameListener(internalFrameListener);
        super.add(this.drawingPanel);
        super.setBounds(10, 10, 500, 500);
        super.setVisible(true);
        this.fileService = new LocalFileServiceImpl<>(this.drawingPanel);
    }

    public void setTitleByRemoveFileExtension(File file) {
        super.setTitle(FilenameUtils.removeExtension(file.getName()));
    }

    public void save() {
        if (Objects.isNull(this.file)) {
            this.saveAs();
            return;
        }
        this.setTitleByRemoveFileExtension(this.fileService.save(this.file));
        this.drawingPanel.setUpdated(false);
    }

    public void saveAs() {
        Constant.FILE_CHOOSER.showSaveDialog(this);
        File selectedFile = Constant.FILE_CHOOSER.getSelectedFile();
        if (Objects.isNull(selectedFile)) return;
        this.file = this.fileService.save(new File(String.format("%s.%s", selectedFile, Constant.SAVE_FILE_EXTENSION)));
        this.setTitleByRemoveFileExtension(this.file);
        this.drawingPanel.setUpdated(false);
    }

    public void load(File file) {
        this.file = file;
        this.drawingPanel.loadShapes(this.fileService.open(this.file));
        this.setTitleByRemoveFileExtension(this.file);
        super.setVisible(true);
    }

    public void print() {
        try {
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            printerJob.setPrintable(this.drawingPanel);
            if (printerJob.printDialog()) {
                printerJob.print();
            }
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    public boolean isUpdated() {
        return this.drawingPanel.isUpdated();
    }
}
