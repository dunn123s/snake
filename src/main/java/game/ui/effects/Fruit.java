package game.ui.effects;

import game.Game;
import game.ui.SnakeEffect;
import game.ui.Unit;
import game.ui.snake.Body;
import game.ui.snake.Snake;

import java.awt.*;

/**
 * 要绘制出来长身体的物品
 */
public class Fruit extends Unit implements SnakeEffect {
    @Override
    public Color drawColor() {
        return Color.RED;
    }


    @Override
    public void whenCollision(Snake snake) {
        System.out.println("长身体");
        Body body = new Body(snake);
        body.setPosition(this.position);

        snake.getBodies().addFirst(body);
        snake.stop();

        Game.getInstance().getCanvas().removeDrawable(this);
        Game.getInstance().addFruit();
    }
}
