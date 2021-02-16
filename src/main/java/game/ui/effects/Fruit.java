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
        //设置身体出现的位置
        body.setPosition(this.position);
        //添加到身体集合中
        snake.getBodies().addFirst(body);
        //吃东西后设置不移动，不然在边界如果继续运动会撞墙
        snake.stop();
        //将使用使用过的食物移除
        Game.getInstance().getCanvas().removeDrawable(this);
        //随机位置添加一个食物
        Game.getInstance().addFruit();
    }
}
