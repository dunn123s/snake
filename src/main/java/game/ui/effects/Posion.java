package game.ui.effects;

import game.Game;
import game.ui.SnakeEffect;
import game.ui.Unit;
import game.ui.snake.Snake;
import game.ui.snake.SnakeState;

import java.awt.*;

/**
 * 要绘制出来碰到会毒死的物品
 */
public class Posion extends Unit implements SnakeEffect {
    @Override
    public void whenCollision(Snake snake) {
        System.out.println("毒死");
        //射的状态设置为DIE
        snake.setState(SnakeState.DIE);
        //该物品使用后要清理掉该物品
        Game.getInstance().getCanvas().removeDrawable(this);
    }

    // 要绘制物品的颜色
    @Override
    public Color drawColor() {
        return Color.MAGENTA;
    }
}
