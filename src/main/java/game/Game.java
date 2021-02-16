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
        //关闭窗口
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //不用frame自带的布局
        frame.setLayout(null);
        //设置大小
        frame.setSize(canvas.getWidth() + 50, canvas.getHeight() + 100);
        //设置窗口相对于指定组件的位置(窗口在屏幕的中间出现)
        frame.setLocationRelativeTo(null);
        //添加场景画布
        frame.add(canvas);
        //设置场景（Jpanel出现的位置）
        canvas.setLocation(10, 10);
        //添加食物
        addFruit();
        //窗体增加键盘监听事件,会调用父类监听器的 KeyListener   public void keyPressed(KeyEvent e);
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
        //刷新时间(当前时间)
        long refreshTime = System.currentTimeMillis();
        //如果是正常状态
        while(!snake.getState().done()) {
            try {
                //设置最大的速度，如果速度小于20就取最大速度为20
                int time = Math.max(20, 800 - snake.getSpeed());
                //设置最小速度，最小速度超过1500就取最小速度1500
                time = Math.min(time, 1500);
                //设置frame的title
                frame.setTitle("贪吃蛇  当前速度:" + time);
                //设置睡眠时间，也就蛇运行的当前速度（下一回合）
                Thread.sleep(time);
            } catch (InterruptedException ignore) {}
            //如果方向不为空，设置蛇的运行方向
            if(null != preDir) {

                snake.setDir(preDir);
            }
            //根据方向移动位置
            snake.move();
            //重新绘制图片，内部会调用update方法
            canvas.repaint();

            //每隔10秒钟刷新一个影响碰撞的物品
            if(System.currentTimeMillis() - refreshTime > 10000) {
                addEffects();
                //重新将刷新时间置为当前时间
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
        //随机获取坐标，如果这个坐标存在
        randomPosition().ifPresent(position -> {
            // 随机获取一个影响物品的的字节码对象
            Class<?> clazz = CollectionKit.randomOne(effects);
            try {
                //使用反射创建实例
                Unit effect = (Unit) clazz.newInstance();
                //设置随机物品的位置
                effect.setPosition(position);
                //将随机物品添加到可绘制物品集合
                canvas.addDrawable(effect);
            } catch (InstantiationException | IllegalAccessException ignore) {}
        });

    }
    //添加食物
    public void addFruit() {
        //随机获取位置，如果存在
        randomPosition().ifPresent(position -> {
            //创建食物对象
            Fruit f = new Fruit();
            //设置食物出现的位置
            f.setPosition(position);
            //将物品添加到可绘制物品集合中
            canvas.addDrawable(f);
        });

    }

    /**
     * 随机获取一个没有物品的坐标
     */
    public Optional<Position> randomPosition() {
        //创建set集合将List<Position>放进去
        Set<Position> all = new HashSet<>(allPos);
        //如果集合不为空
        while(!all.isEmpty()) {
            //从位置集合中随机获取一个位置
            Position position = CollectionKit.randomOne(all);
            //获取位置后，将该位置重集合中移除
            all.remove(position);


            //如果随机的坐标没有物品，则返回这个坐标
            Optional<Unit> unit = canvas.getUnit(position);
            //如果物品不存在，就返回这个位置
            if(!unit.isPresent()) {
                //返回这个坐标用Optional包装下
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
     * @param snakeLength 蛇的大小
     */
    private Game(Dimension minUnitSize, int rows, int cols, int snakeLength) {
        frame = new JFrame();
        this.canvas = new Canvas(minUnitSize, rows, cols);
        this.snake = new Snake(this.canvas, Dir.RIGHT, snakeLength, rows * cols / 2, snakeLength +1, 0);
        //将可绘制的物品添加到场景中可绘制物品集合
        canvas.addDrawable(snake);

        //遍历将所有的位置添加到集合中，相当于是整个地图上的位置（整个面积）
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                this.allPos.add(new Position(c, r));
            }
        }
    }

    /**
     * 初始化创建一个Game实例类对象
     * @param minUnitSize   最小单位
     * @param rows  地图行
     * @param cols  地图列
     * @param snakeLength  蛇的大小
     */
    public static void init(Dimension minUnitSize, int rows, int cols, int snakeLength) {
        instance = new Game(minUnitSize, rows, cols, snakeLength);
    }
    //获取游戏实例对象
    public static Game getInstance() {
        return instance;
    }
}
