package game.ui;

import game.ui.snake.Snake;

/**
 * 与蛇发生接触后会发生效果的物品
 */
public interface SnakeEffect {

    /**
     * 当与蛇身发生碰撞
     * @param snake 蛇
     */
    void whenCollision(Snake snake);

}
