package game.ui.snake;

import game.ui.SnakeEffect;
import game.ui.Unit;

import java.awt.*;

/**
 * 蛇的身体
 */
public class Body extends Unit implements SnakeEffect {

    private final Snake owner;

    public Body(Snake owner) {
        this.owner = owner;
    }

    @Override
    public Color drawColor() {
        return owner.drawColor();
    }

    @Override
    public void whenCollision(Snake snake) {
        owner.setState(SnakeState.DIE);
    }
}
