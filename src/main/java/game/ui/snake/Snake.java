package game.ui.snake;

import game.Dir;
import game.Position;
import game.exception.BeyondBoundaryException;
import game.ui.Canvas;
import game.ui.SnakeEffect;
import game.ui.Unit;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

/**
 * 蛇，不需要继承最小单位，只需要继承IDrawable（可会知物接口就行了，因为蛇不是最小单位，但是可绘制物品）
 */

public class Snake extends Unit implements SnakeEffect {

    //持有场景画布
    private final game.ui.Canvas canvas;
    //蛇的身体集合,因为蛇是由身体组成的
    @Getter private final LinkedList<Body> bodies = new LinkedList<>();
    //最大长度
    private final int maxLen;
    //方向
    @Getter private Dir dir;
    //蛇的状态（三种状态NORMAL, DIE, FULL;）
    @Getter @Setter private SnakeState state = SnakeState.NORMAL;
    //蛇运行速度
    @Getter @Setter private int speed = 500;
    /**
     * volatile关键字的主要作用是使变量在多线程间可见
     * 停止的标记
     */
    private volatile boolean stop = false;

    /**
     *
     * @param canvas    场景
     * @param dir       方向
     * @param initLen   初始化身的大小
     * @param maxLen    蛇的最大长度
     * @param x         x坐标
     * @param y         y坐标
     */
    public Snake(Canvas canvas, Dir dir, int initLen, int maxLen, int x, int y) {
        //初始长度不足2位，默认赋值长度为2
        if(initLen < 2) {
            initLen = 2;
        }
        this.canvas = canvas;
        this.dir = dir;
        this.maxLen = maxLen;

        //创建蛇身体对象
        Body head = new Body(this);
        //设置身体的位置
        head.setPosition(x, y);
        //将身体添加到列表的末尾位置
        bodies.add(head);
        //遍历蛇的身体
        for(int i = 1; i < initLen; i++) {
            //将身体添加到尾部
            this.addTail();
        }
    }

    //移动
    public void move() {
        //获取蛇头部
        Body head = getHead();
        //获取蛇尾部
        Body tail = bodies.removeLast();

        // 定义下一个位置
        Position nextHead;
        try {
            // 根据方向、场景画布的大小找到下一个位置
            nextHead = head.getPosition().nextHead(this.dir, this.canvas.getCols(), this.canvas.getRows());
        } catch (BeyondBoundaryException e) {
            //如果出现异常，将蛇的状态设置为DIE,游戏结束
            this.setState(SnakeState.DIE);
            return;
        }

        //先拿出下一个格子的物品，等下要判断这个物品碰撞效果
        Optional<Unit> unit = canvas.getUnit(nextHead);

        // 判断碰撞影响
        unit.ifPresent(u-> {
            if(u instanceof SnakeEffect) {
                ((SnakeEffect) u).whenCollision(this);
            }
        });

        // 如果stop状态为true
        if(this.stop) {
            // 将状态设置为false，防止在边界出现物品还继续往前走，设置为false后，手动调整方向，再继续走，不然没有时间改变方向会撞墙
           this.stop = false;
           //将删掉的尾巴加上，不然不会涨身体，因为添加头部，删除尾部等于没有长身体
           this.bodies.addLast(tail);
        } else {
            //下面两部为真正的移动
            //设置尾部的位置
            tail.setPosition(nextHead);
            //将尾巴添加到列表的第一个位置
            this.bodies.addFirst(tail);
        }



        //如果身体的长度到指定的长度，将蛇的状态设置为FULL,就是通关
        if(bodies.size() >= maxLen) {
            this.state = SnakeState.FULL;
        }

    }

    //保证走的方向只能是一个方向，方向相反就return，不会重新赋值
    public void setDir(Dir dir) {
        if(this.dir == Dir.UP && dir == Dir.DOWN) {
            return;
        }
        if(this.dir == Dir.DOWN && dir == Dir.UP) {
            return;
        }
        if(this.dir == Dir.LEFT && dir == Dir.RIGHT) {
            return;
        }
        if(this.dir == Dir.RIGHT && dir == Dir.LEFT) {
            return;
        }


        this.dir = dir;
    }

    public void stop() {
        this.stop = true;
    }

    private void addTail() {
        //创建蛇下一个身体对象（物品）
        Body next = new Body(this);
        try {
            //设置物品出现的位置
            next.setPosition(getTail().getPosition().nextTail(dir, this.canvas.getCols(), this.canvas.getRows()));
        } catch (BeyondBoundaryException e) {
            this.setState(SnakeState.DIE);
            JOptionPane.showMessageDialog(null, "添加尾巴时碰到了边界，很奇怪！需要检查,游戏结束");
            return;
        }
        //将物品添加到蛇身体尾部
        bodies.add(next);
    }

    //获取身体头部
    public Body getHead() {
        return bodies.getFirst();
    }
    //获取身体尾部
    public Body getTail() {
        return bodies.getLast();
    }
    //绘制物品的颜色
    @Override
    public Color drawColor() {
        return Color.BLACK;
    }

    // 判断整个蛇是否与传进来的坐标发生碰撞
    @Override
    public boolean collision(Position position) {
        //从身体集合中取出每个元素去匹配，看看传进来的坐标是否与蛇身体的任何一个位置发生碰撞
        return this.bodies.stream().anyMatch(body -> body.collision(position));
    }

    //蛇与蛇身体发生碰撞后，设置蛇的状态
    @Override
    public void whenCollision(Snake snake) {
        snake.setState(SnakeState.DIE);
    }
    //获取蛇头的位置
    @Override
    public Position getPosition() {
        return getHead().getPosition();
    }

    //绘制蛇身
    @Override
    public void draw(Graphics g, Dimension minUnit) {
        Color c = g.getColor();
        new ArrayList<>(bodies).forEach(body -> body.draw(g, minUnit));
        g.setColor(c);
    }
}
