package game.ui;

import game.Position;

import java.awt.*;

/**
 * 最小单元，地图，蛇，果子都是最小单元构成
 */
public abstract class Unit implements IDrawable {
    //要绘制物品的位置
    protected Position position;

    //设置物品位置
    public void setPosition(int x, int y) {
        this.position = new Position(x, y);
    }

    //设置物品位置
    public void setPosition(Position position) {
        this.position = position;
    }

    //获取位置
    public Position getPosition() {
        return position;
    }

    //获取物品位置x坐标
    public int getX() {
        return getPosition().getX();
    }

    //获取物品位置Y坐标
    public int getY() {
        return getPosition().getY();
    }

    /**
     * 使用画笔绘制物品
     * @param   g        画笔
     * @param minUnit   要绘制的物品
     */
    public void draw(Graphics g, Dimension minUnit) {
        //获取当前画笔的颜色
        Color c = g.getColor();

        //设置画笔颜色
        g.setColor(drawColor());
        //根据坐标与要绘制物品的规格来绘制物品
        g.fillRect(getX() * minUnit.width, getY() * minUnit.height, minUnit.width, minUnit.height);//绘制实心矩形
        // 恢复画笔之前的颜色
        g.setColor(c);
    }
    // 获取要绘画的物品颜色
    public abstract Color drawColor();

    //判断是否发生碰撞，如果传进来的位置与绘制物品的位置一致，说明发生碰撞
    public boolean collision(Position position) {
        return this.position.equals(position);
    }
}
