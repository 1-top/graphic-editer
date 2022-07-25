package com.chocobi.paint.global.swing;

import javax.swing.*;

public class CustomButtonGroup extends ButtonGroup {
    public void addAll(AbstractButton... buttons) {
        for (AbstractButton button : buttons) super.add(button);
    }

    public AbstractButton getSelectedButton() {
        return super.buttons.stream().filter(AbstractButton::isSelected).findFirst().orElse(null);
    }
}
