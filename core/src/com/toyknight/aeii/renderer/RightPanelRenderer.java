package com.toyknight.aeii.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.toyknight.aeii.entity.Unit;
import com.toyknight.aeii.manager.GameManager;
import com.toyknight.aeii.ResourceManager;
import com.toyknight.aeii.screen.GameScreen;

/**
 * Created by toyknight on 4/26/2015.
 */
public class RightPanelRenderer {

    private final String STR_HP = "HP ";
    private final String STR_EXP = "XP ";

    private final int ts;
    private final int pad;
    private final int fsize;
    private final GameScreen screen;

    private Unit target_unit;

    public RightPanelRenderer(final GameScreen screen, int ts) {
        this.ts = ts;
        this.pad = ts / 2;
        this.fsize = screen.getRightPanelWidth() - pad * 2; //unit frame size;
        this.screen = screen;
        Button btn_focus = new Button(screen.getContext().getSkin());
        btn_focus.setBounds(screen.getViewportWidth() + pad, screen.getViewportHeight() - pad - fsize, fsize, fsize);
        btn_focus.setStyle(new Button.ButtonStyle());
        btn_focus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (target_unit != null) {
                    RightPanelRenderer.this.screen.locateViewport(target_unit.getX(), target_unit.getY());
                }
            }
        });
        screen.addActor(btn_focus);
    }

    public void drawStatusBar(Batch batch) {
        batch.draw(ResourceManager.getPanelBackground(),
                Gdx.graphics.getWidth() - screen.getRightPanelWidth(), 0,
                screen.getRightPanelWidth(), Gdx.graphics.getHeight());
        BorderRenderer.drawBorder(batch,
                Gdx.graphics.getWidth() - screen.getRightPanelWidth(), ts,
                screen.getRightPanelWidth(), Gdx.graphics.getHeight() - ts * 2);
        drawInformation(batch);
        batch.flush();
    }

    private UnitRenderer getUnitRenderer() {
        return screen.getUnitRenderer();
    }

    private GameManager getManager() {
        return screen.getGameManager();
    }

    private void drawInformation(Batch batch) {
        int hw = ts / 24 * 13;
        int hh = ts / 24 * 16;
        float lbh = FontRenderer.getTextFont().getCapHeight();
        batch.draw(ResourceManager.getBorderLightColor(),
                screen.getViewportWidth() + pad, screen.getViewportHeight() - pad - fsize, fsize, fsize);
        batch.draw(ResourceManager.getListBackground(),
                screen.getViewportWidth() + pad + 2, screen.getViewportHeight() - pad - fsize + 2, fsize - 4, fsize - 4);
        Unit unit = getManager().getGame().getMap().getUnit(screen.getCursorMapX(), screen.getCursorMapY());
        if (unit != null) {
            target_unit = unit;
        }
        if (target_unit != null) {
            getUnitRenderer().drawUnit(batch, target_unit,
                    screen.getViewportWidth() + ts, screen.getViewportHeight() - ts * 2, true);
            //draw level
            String level_str = Integer.toString(target_unit.getLevel());
            FontRenderer.drawText(batch, level_str,
                    screen.getViewportWidth() + pad + hw + (fsize - hw - FontRenderer.getTextFont().getBounds(level_str).width) / 2,
                    screen.getViewportHeight() - fsize - pad - (hh - lbh) / 2);
            //draw attack
            String attack_str = Integer.toString(target_unit.getAttack());
            switch (target_unit.getAttackType()) {
                case Unit.ATTACK_PHYSICAL:
                    FontRenderer.setTextColor(ResourceManager.getPhysicalAttackColor());
                    break;
                case Unit.ATTACK_MAGICAL:
                    FontRenderer.setTextColor(ResourceManager.getMagicalAttackColor());
                    break;
            }
            FontRenderer.drawText(batch, attack_str,
                    screen.getViewportWidth() + pad + hw + (fsize - hw - FontRenderer.getTextFont().getBounds(attack_str).width) / 2,
                    screen.getViewportHeight() - fsize - pad - hh - (hh - lbh) / 2);
            FontRenderer.setTextColor(Color.WHITE);
            //draw physical defence
            String pdefence_str = Integer.toString(target_unit.getPhysicalDefence());
            FontRenderer.drawText(batch, pdefence_str,
                    screen.getViewportWidth() + pad + hw + (fsize - hw - FontRenderer.getTextFont().getBounds(pdefence_str).width) / 2,
                    screen.getViewportHeight() - fsize - pad - hh * 2 - (hh - lbh) / 2);
            //draw magical defence
            String mdefence_str = Integer.toString(target_unit.getMagicalDefence());
            FontRenderer.drawText(batch, mdefence_str,
                    screen.getViewportWidth() + pad + hw + (fsize - hw - FontRenderer.getTextFont().getBounds(mdefence_str).width) / 2,
                    screen.getViewportHeight() - fsize - pad - hh * 3 - (hh - lbh) / 2);
            //draw health points
            String hp_str = target_unit.getCurrentHp() + "/" + target_unit.getMaxHp();
            FontRenderer.setTextColor(Color.GREEN);
            FontRenderer.drawText(batch, hp_str,
                    screen.getViewportWidth() + pad + FontRenderer.getTextFont().getBounds(STR_HP).width,
                    screen.getViewportHeight() - fsize - pad - hh * 4 - ts / 4);
            //draw experience
            String exp_str = target_unit.getLevelUpExperience() > 0 ?
                    target_unit.getCurrentExperience() + "/" + target_unit.getLevelUpExperience() : "-/-";
            FontRenderer.setTextColor(Color.CYAN);
            FontRenderer.drawText(batch, exp_str,
                    screen.getViewportWidth() + pad + FontRenderer.getTextFont().getBounds(STR_EXP).width,
                    screen.getViewportHeight() - fsize - pad - hh * 4 - lbh - ts / 2);
            FontRenderer.setTextColor(Color.WHITE);
        }
        batch.draw(ResourceManager.getBattleHudIcon(3),
                screen.getViewportWidth() + pad, screen.getViewportHeight() - fsize - pad - hh, hw, hh);
        batch.draw(ResourceManager.getBattleHudIcon(0),
                screen.getViewportWidth() + pad, screen.getViewportHeight() - fsize - pad - hh * 2, hw, hh);
        batch.draw(ResourceManager.getBattleHudIcon(1),
                screen.getViewportWidth() + pad, screen.getViewportHeight() - fsize - pad - hh * 3, hw, hh);
        batch.draw(ResourceManager.getBattleHudIcon(2),
                screen.getViewportWidth() + pad, screen.getViewportHeight() - fsize - pad - hh * 4, hw, hh);
        FontRenderer.drawText(batch, STR_HP,
                screen.getViewportWidth() + pad, screen.getViewportHeight() - fsize - pad - hh * 4 - ts / 4);
        FontRenderer.drawText(batch, STR_EXP,
                screen.getViewportWidth() + pad, screen.getViewportHeight() - fsize - pad - hh * 4 - lbh - ts / 2);

    }

}