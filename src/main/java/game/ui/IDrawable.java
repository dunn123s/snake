package game.ui;

import game.Position;

import java.awt.*;

/**
 * 可绘制物体
 */
public interface IDrawable {
    //获取位置
    Position getPosition();
    //使用画笔绘制物品
    void draw(Graphics g, Dimension minUnit);
}
