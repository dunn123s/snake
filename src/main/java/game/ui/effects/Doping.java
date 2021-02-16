package game.ui.effects;

import game.Game;
import game.ui.SnakeEffect;
import game.ui.Unit;
import game.ui.snake.Snake;

import java.awt.*;

/**
 * 要绘制出来加速的物品
 */
public class Doping extends Unit implements SnakeEffect {
    @Override
    public void whenCollision(Snake snake) {
        System.out.println("加速");
        //碰到一次加速30
        snake.setSpeed(snake.getSpeed() + 30);
        //该物品使用后要清除掉该物品
        Game.getInstance().getCanvas().removeDrawable(this);
    }
    //获取要绘制物品的颜色
    @Override
    public Color drawColor() {
        return Color.CYAN;
    }
}
