package game.ui;

import game.Position;

import java.awt.*;

/**
 * 可绘制物体
 */
public interface IDrawable {
    Position getPosition();
    void draw(Graphics g, Dimension minUnit);
}
