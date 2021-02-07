package game.ui;

import game.Position;

import java.awt.*;

/**
 * 最小单元，地图，蛇，果子都是最小单元构成
 */
public abstract class Unit implements IDrawable {
    protected Position position;

    public void setPosition(int x, int y) {
        this.position = new Position(x, y);
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public int getX() {
        return getPosition().getX();
    }

    public int getY() {
        return getPosition().getY();
    }

    public void draw(Graphics g, Dimension minUnit) {
        Color c = g.getColor();

        g.setColor(drawColor());

        g.fillRect(getX() * minUnit.width, getY() * minUnit.height, minUnit.width, minUnit.height);//绘制实心矩形

        g.setColor(c);
    }

    public abstract Color drawColor();

    public boolean collision(Position position) {
        return this.position.equals(position);
    }
}
