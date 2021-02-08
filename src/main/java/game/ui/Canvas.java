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
 * 场景
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
        this.offScreenImage = createImage(width, height);

        this.setSize(width, height);
        this.setPreferredSize(new Dimension(width, height));
        this.setMinimumSize(new Dimension(width, height));
        this.setBackground(Color.WHITE);
    }

    public void addDrawable(IDrawable drawable) {
        this.drawables.add(drawable);
    }

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
        Graphics graphics = this.offScreenImage.getGraphics();

        Color color = graphics.getColor();//获取之前的颜色
        paint(graphics);
        graphics.setColor(color);//重置之前的颜色

        //释放画笔
        graphics.dispose();

        // 绘制图片到画布上
        g.drawImage(this.offScreenImage, 0, 0, null);

//        Color c = g.getColor();
//
//        paint(g);
//
//        g.setColor(c);
    }

    //调用画笔的方法绘制所有的图片
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        List<IDrawable> draws = new ArrayList<>(this.drawables);
        draws.forEach(draw -> draw.draw(g, this.unitSize));
    }

    //根据位置获取位置中的物品，可能有也可能没有
    public Optional<Unit> getUnit(Position position) {
        List<IDrawable> draws = new ArrayList<>(this.drawables);
        return draws.stream()
                .filter(drawable -> drawable instanceof Unit)
                .map(drawable -> (Unit) drawable)
                .filter(unit -> unit.collision(position))
                //从流中获取第一个元素
                .findFirst();
    }
}
