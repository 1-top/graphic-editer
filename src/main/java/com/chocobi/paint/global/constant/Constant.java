package com.chocobi.paint.global.constant;

import dev.akkinoc.util.YamlResourceBundle;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.comparator.ExtensionFileComparator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.ResourceBundle;

public class Constant {
    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(
            "lang/message",
            YamlResourceBundle.Control.INSTANCE
    );

    public static final String SAVE_FILE_EXTENSION = "sh";

    public static final JFileChooser FILE_CHOOSER = new JFileChooser(new File(System.getProperty("user.home")));

    public static final JFileChooser IMAGE_FILE_CHOOSER = new JFileChooser(new File(System.getProperty("user.home")));

    static {
        FILE_CHOOSER.setFileFilter(new FileNameExtensionFilter("Shapes", SAVE_FILE_EXTENSION));
        FILE_CHOOSER.setAcceptAllFileFilterUsed(false);

        IMAGE_FILE_CHOOSER.setFileFilter(new FileNameExtensionFilter("images", "jpg", "jpeg", "png"));
        IMAGE_FILE_CHOOSER.setAcceptAllFileFilterUsed(false);
    }

    @Getter
    @RequiredArgsConstructor
    public enum MessageKey {
        ERROR("dialog.error"),
        INFORMATION("dialog.information"),
        FILE("menu.file.file"),
        EDIT("menu.edit.edit"),
        SAVE_YES("dialog.save.yes"),
        SAVE_NO("dialog.save.no"),
        SHAPE_ATTRIBUTE_FILL("shape.attribute.fill"),
        SHAPE_ATTRIBUTE_LINE("shape.attribute.line");

        private final String key;
    }
}
