package com.chocobi.paint.infra.file;


import com.chocobi.paint.panel.DrawingPanel;
import lombok.RequiredArgsConstructor;

import java.io.*;

@RequiredArgsConstructor
public class LocalFileServiceImpl<T> implements FileService<T> {
    private final DrawingPanel drawingPanel;

    @Override
    public File save(File file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            oos.writeObject(this.drawingPanel.getShapes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @Override
    public T open(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            return (T) ois.readObject();
        } catch (StreamCorruptedException e) {
            throw new InvalidFileException();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
