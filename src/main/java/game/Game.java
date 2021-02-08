package game;

import com.chuang.tauceti.tools.basic.collection.CollectionKit;
import game.ui.Canvas;
import game.ui.Unit;
import game.ui.effects.Alcohol;
import game.ui.effects.Doping;
import game.ui.effects.Fruit;
import game.ui.effects.Poison;
import game.ui.snake.Snake;
import game.ui.snake.SnakeState;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.*;

/**
 * 游戏类，用于控制和协调整个游戏流程
 */
public class Game extends KeyAdapter implements Runnable {
    //贪吃蛇
    @Getter private final Snake snake;
    //画布场景
    @Getter private final Canvas canvas;
    //窗体容器
    private final JFrame frame;
    //位置集合
    private final List<Position> allPos = new ArrayList<>();
    //影响事件集合
    private final List<Class<?>> effects = Arrays.asList(Alcohol.class, Doping.class, Poison.class);
    //方向
    private Dir preDir;

    public void showUI() {
        //设置容器出现的位置
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //不用frame自带的布局
        frame.setLayout(null);
        //设置大小
        frame.setSize(canvas.getWidth() + 50, canvas.getHeight() + 100);
        //设置窗口相对于指定组件的位置
        frame.setLocationRelativeTo(null);
        //添加场景画布
        frame.add(canvas);
        //设置位置
        canvas.setLocation(10, 10);
        //添加食物
        addFruit();
        //设置监听器
        frame.addKeyListener(this);
        //显示窗口
        frame.setVisible(true);
    }
    //开始线程任务
    public void start() {
        new Thread(this).start();
    }


    //要执行的任务
    @Override
    public void run() {
        //刷新时间
        long refreshTime = System.currentTimeMillis();
        //todo 如果是正常状态
        while(!snake.getState().done()) {
            try {
                int time = Math.max(20, 800 - snake.getSpeed());
                time = Math.min(time, 1500);
                frame.setTitle("贪吃蛇  当前速度:" + time);
                Thread.sleep(time);
            } catch (InterruptedException ignore) {}
            if(null != preDir) {
                snake.setDir(preDir);
            }
            //根据方向移动位置
            snake.move();
            //重新绘制图片，内部会调用update方法
            canvas.repaint();

            if(System.currentTimeMillis() - refreshTime > 10000) {
                addEffects();
                refreshTime = System.currentTimeMillis();
            }
        }

        if(snake.getState() == SnakeState.FULL) {
            JOptionPane.showMessageDialog(null, "很厉害，通关了");
            return;
        }
        if(snake.getState() == SnakeState.DIE) {
            JOptionPane.showMessageDialog(null, "小蛇已死, 游戏结束");
        }
    }
    //添加影响物品
    public void addEffects() {
        randomPosition().ifPresent(position -> {
            Class<?> clazz = CollectionKit.randomOne(effects);
            try {
                Unit effect = (Unit) clazz.newInstance();
                effect.setPosition(position);
                canvas.addDrawable(effect);
            } catch (InstantiationException | IllegalAccessException ignore) {}
        });

    }
    //添加食物
    public void addFruit() {
        randomPosition().ifPresent(position -> {
            Fruit f = new Fruit();
            f.setPosition(position);
            canvas.addDrawable(f);
        });

    }
   //随机获取位置
    public Optional<Position> randomPosition() {

        Set<Position> all = new HashSet<>(allPos);
        while(!all.isEmpty()) {

            Position position = CollectionKit.randomOne(all);
            all.remove(position);
            //如果随机的坐标没有物品，则返回这个坐标
            Optional<Unit> unit = canvas.getUnit(position);
            if(!unit.isPresent()) {
                return Optional.of(position);
            }
        }

        return Optional.empty();
    }


    /**
     * 按键事件
     */
    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        switch (e.getKeyCode()) {
            case 37:
                this.preDir = Dir.LEFT;
                break;
            case 38:
                this.preDir = Dir.UP;
                break;
            case 39:
                this.preDir = Dir.RIGHT;
                break;
            case 40:
                this.preDir = Dir.DOWN;
                break;
        }
    }

    private static Game instance;
    /**
     * 初始化游戏
     * @param minUnitSize 最小单元的大小
     * @param rows 地图行
     * @param cols 地图列
     */
    private Game(Dimension minUnitSize, int rows, int cols, int snakeLength) {
        frame = new JFrame();
        this.canvas = new Canvas(minUnitSize, rows, cols);
        this.snake = new Snake(this.canvas, Dir.RIGHT, snakeLength, rows * cols / 2, snakeLength + 1, 0);
        canvas.addDrawable(snake);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                this.allPos.add(new Position(c, r));
            }
        }
    }

    public static void init(Dimension minUnitSize, int rows, int cols, int snakeLength) {
        instance = new Game(minUnitSize, rows, cols, snakeLength);
    }

    public static Game getInstance() {
        return instance;
    }
}
