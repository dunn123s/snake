package game.ui.snake;

import game.ui.SnakeEffect;
import game.ui.Unit;

import java.awt.*;

/**
 * 蛇的身体
 */
public class Body extends Unit implements SnakeEffect {

    //持有蛇本身，要知道是哪条蛇
    private final Snake owner;

    //构造函数初始化蛇身
    public Body(Snake owner) {
        this.owner = owner;
    }

    //要绘制蛇身体的颜色
    @Override
    public Color drawColor() {
        return owner.drawColor();
    }

    //当与蛇身发生碰撞后产生的效果
    @Override
    public void whenCollision(Snake snake) {
        owner.setState(SnakeState.DIE);
    }
}
