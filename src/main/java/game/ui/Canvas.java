package game.ui;

import game.Position;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

/**
 * 场景,因为场景里面要装东西，所有要继承JPanel
 */
public class Canvas extends JPanel {

    // 单元大小
    private final Dimension unitSize;
    // 缓冲图片
    private final Image offScreenImage;
    // x,y坐标
    @Getter private final int rows, cols;
    // 可绘制物集合
    private final LinkedHashSet<IDrawable> drawables = new LinkedHashSet<>();

    //构造函数
    public Canvas(Dimension unitSize, int rows, int clos) {
        this.unitSize = unitSize;
        this.rows = rows;
        this.cols = clos;
        int width = unitSize.width * clos;
        int height = unitSize.height * rows;
        //初始化图片与Jpanel的规格一样大
        this.offScreenImage = createImage(width, height);

        //设置Jpanel宽高
        this.setSize(width, height);
        //设置Jpanel在父容器中的大小
        this.setPreferredSize(new Dimension(width, height));
        //设置Jpanel的最小大小
        this.setMinimumSize(new Dimension(width, height));
        //设置场景的背景颜色
        this.setBackground(Color.WHITE);
        }

    /**
     * 将可绘制的物品添加到可绘制物品集合
      * @param drawable 可绘制物品接口
     */
    public void addDrawable(IDrawable drawable) {
        this.drawables.add(drawable);
    }
    /**
     * 将可绘制的物品从可绘制物品集合中移除
     * @param drawable 可绘制物品接口
     */
    public void removeDrawable(IDrawable drawable) {
        this.drawables.remove(drawable);
    }

    /**
     * 默认的update会直接调用paint，而默认的pain会先情况画布，然后再进行绘制。这样会导致界面上先出现空白，再出现图像（看上去会闪烁）。
     * 因此，我们重写update，每次调用绘制之前，先把图片的画笔传递过去。让其在图片上绘画。图片的绘制过程不会体现在UI上，因此绘制过程看不到闪烁。
     * 当图片绘制好之后，再直接把图片通过画笔直接整个覆盖到UI上。避免了UI先擦除再绘制导致的闪烁。
     */
    @Override
    public void update(Graphics g) {
        //根据图片获取画笔（图片和jpenl的大小是一样大的）
        Graphics graphics = this.offScreenImage.getGraphics();
        //获取图片画笔颜色
        Color color = graphics.getColor();
        //将物品绘制到图片上
        paint(graphics);
        //重置之前的颜色
        graphics.setColor(color);

        //释放画笔资源
        graphics.dispose();

        //获取Jpenl画笔颜色（也是ui的颜色）
        Color c = g.getColor();
        //将图片绘制在Jpenl上
        g.drawImage(this.offScreenImage, 0, 0, null);
        //重置jpenl之前的颜色
        g.setColor(c);
    }

    //调用画笔的方法绘制所有的物品
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        List<IDrawable> draws = new ArrayList<>(this.drawables);
        //绘制出所有最小单元
        draws.forEach(draw -> draw.draw(g, this.unitSize));
    }

    /**
     * 根据位置获取发生碰撞的位置中的物品，可能有也可能没有
     * @param position  坐标位置
     * @return 位置中的物品
     */
    public Optional<Unit> getUnit(Position position) {
        // 创建list集合存放可绘制物品集合
        List<IDrawable> draws = new ArrayList<>(this.drawables);
        //转换成流，过滤后从流中获取第一个元素
        return draws.stream()
                //过滤所有最小单元的物品
                .filter(drawable -> drawable instanceof Unit)
                //收集所有最小单元格物品
                .map(drawable -> (Unit) drawable)
                //todo 过滤只发生碰撞的物品
                .filter(unit -> unit.collision(position))
                //从流中获取第一个元素
                .findFirst();
    }
}
