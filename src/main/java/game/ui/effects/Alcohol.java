package game.ui.effects;

import game.Game;
import game.ui.SnakeEffect;
import game.ui.Unit;
import game.ui.snake.Snake;

import java.awt.*;

public class Alcohol extends Unit implements SnakeEffect {
    @Override
    public void whenCollision(Snake snake) {
        System.out.println("减速");
        snake.setSpeed(snake.getSpeed() - 30);
        Game.getInstance().getCanvas().removeDrawable(this);
    }

    @Override
    public Color drawColor() {
        return Color.BLUE;
    }
}
