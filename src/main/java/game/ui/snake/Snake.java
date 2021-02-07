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
 * 蛇
 */

public class Snake extends Unit implements SnakeEffect {

    private final game.ui.Canvas canvas;
    @Getter private final LinkedList<Body> bodies = new LinkedList<>();
    private final int maxLen;

    @Getter private Dir dir;
    @Getter @Setter private SnakeState state = SnakeState.NORMAL;
    @Getter @Setter private int speed = 500;

    private volatile boolean stop = false;

    public Snake(Canvas canvas, Dir dir, int initLen, int maxLen, int x, int y) {
        if(initLen < 2) {
            initLen = 2;
        }
        this.canvas = canvas;
        this.dir = dir;
        this.maxLen = maxLen;

        Body head = new Body(this);
        head.setPosition(x, y);
        bodies.add(head);
        for(int i = 1; i < initLen; i++) {
            this.addTail();
        }
    }

    public void move() {
        Body head = getHead();
        Body tail = bodies.removeLast();

        // 找到下一个位置
        Position nextHead;
        try {
            nextHead = head.getPosition().nextHead(this.dir, this.canvas.getCols(), this.canvas.getRows());
        } catch (BeyondBoundaryException e) {
            this.setState(SnakeState.DIE);
            return;
        }

        //先拿出下一个格子的物品，等下要判断这个物品碰撞效果
        Optional<Unit> unit = canvas.getUnit(nextHead);

        // 移动
        if(this.stop) {
           this.stop = false;
        } else {
            tail.setPosition(nextHead);
            this.bodies.addFirst(tail);
        }

        // 判断碰撞影响
        unit.ifPresent(u-> {
            if(u instanceof SnakeEffect) {
                ((SnakeEffect) u).whenCollision(this);
            }
        });

        if(bodies.size() >= maxLen) {
            this.state = SnakeState.FULL;
        }

    }

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
        this.stop = stop;
    }

    private void addTail() {
        Body next = new Body(this);
        try {
            next.setPosition(getTail().getPosition().nextTail(dir, this.canvas.getCols(), this.canvas.getRows()));
        } catch (BeyondBoundaryException e) {
            this.setState(SnakeState.DIE);
            JOptionPane.showMessageDialog(null, "添加尾巴时碰到了边界，很奇怪！需要检查,游戏结束");
            return;
        }
        bodies.add(next);
    }

    public Body getHead() {
        return bodies.getFirst();
    }

    public Body getTail() {
        return bodies.getLast();
    }

    @Override
    public Color drawColor() {
        return Color.BLACK;
    }

    @Override
    public boolean collision(Position position) {
        return this.bodies.stream().anyMatch(body -> body.collision(position));
    }

    @Override
    public void whenCollision(Snake snake) {
        snake.setState(SnakeState.DIE);
    }

    @Override
    public Position getPosition() {
        return getHead().getPosition();
    }

    @Override
    public void draw(Graphics g, Dimension minUnit) {
        Color c = g.getColor();
        new ArrayList<>(bodies).forEach(body -> body.draw(g, minUnit));
        g.setColor(c);
    }
}
