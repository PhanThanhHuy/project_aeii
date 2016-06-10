package com.toyknight.aeii.screen.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.toyknight.aeii.screen.StageScreen;

/**
 * @author toyknight 6/10/2016.
 */
public class MessageDialog extends BasicDialog {

    private Label label_message;

    public MessageDialog(StageScreen owner) {
        super(owner);
        label_message = new Label("", getContext().getSkin());
        add(label_message).pad(ts / 2);
    }

    public void setMessage(String message) {
        label_message.setText(message);
        layout();
        float width = label_message.getPrefWidth() + ts;
        float height = label_message.getPrefHeight() + ts;
        setBounds((Gdx.graphics.getWidth() - width) / 2, (Gdx.graphics.getHeight() - height) / 2, width, height);
    }

}
