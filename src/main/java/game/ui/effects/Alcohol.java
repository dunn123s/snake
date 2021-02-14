package game.ui.effects;

import game.Game;
import game.ui.SnakeEffect;
import game.ui.Unit;
import game.ui.snake.Snake;

import java.awt.*;

/**
 * 要绘制出来减速的物品，
 */
public class Alcohol extends Unit implements SnakeEffect {
    @Override
    public void whenCollision(Snake snake) {
        System.out.println("减速");
        //碰到一次减速30
        snake.setSpeed(snake.getSpeed() - 30);
        //该物品使用后要清除掉该物品
        Game.getInstance().getCanvas().removeDrawable(this);
    }

    // 要绘制物品的颜色
    @Override
    public Color drawColor() {
        return Color.BLUE;
    }
}
