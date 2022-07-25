package com.chocobi.paint.global.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

@Getter
@RequiredArgsConstructor
public enum CursorConstant {
    DEFAULT(new Cursor(Cursor.DEFAULT_CURSOR)),
    HAND(new Cursor(Cursor.HAND_CURSOR)),
    CROSS(new Cursor(Cursor.CROSSHAIR_CURSOR)),
    NORTH_WEST_RESIZE(new Cursor(Cursor.NW_RESIZE_CURSOR)),
    NORTH_RESIZE(new Cursor(Cursor.N_RESIZE_CURSOR)),
    NORTH_EAST_RESIZE(new Cursor(Cursor.NE_RESIZE_CURSOR)),
    EAST_RESIZE(new Cursor(Cursor.E_RESIZE_CURSOR)),
    SOUTH_EAST(new Cursor(Cursor.SE_RESIZE_CURSOR)),
    SOUTH(new Cursor(Cursor.S_RESIZE_CURSOR)),
    SOUTH_WEST(new Cursor(Cursor.SW_RESIZE_CURSOR)),
    WEST(new Cursor(Cursor.W_RESIZE_CURSOR)),
    ROTATE(createCursor("assets/icon/rotate_cursor.png","Rotate Cursor")),
    ERASER(createCursor("assets/icon/eraser.png","Eraser Cursor"));

    private final Cursor cursor;

    @SneakyThrows
    private static Cursor createCursor(String url, String name) {
        BufferedImage bufferedImage = ImageIO.read(ClassLoader.getSystemClassLoader().getResource(url));
        return Toolkit.getDefaultToolkit().createCustomCursor(
                bufferedImage,
                new Point(bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2),
                name
        );
    }
}
