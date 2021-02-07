package game.ui.effects;

import game.Game;
import game.ui.SnakeEffect;
import game.ui.Unit;
import game.ui.snake.Snake;
import game.ui.snake.SnakeState;

import java.awt.*;

public class Poison extends Unit implements SnakeEffect {
    @Override
    public void whenCollision(Snake snake) {
        System.out.println("毒死");
        snake.setState(SnakeState.DIE);
        Game.getInstance().getCanvas().removeDrawable(this);
    }

    @Override
    public Color drawColor() {
        return Color.MAGENTA;
    }
}
