package game;

import game.exception.BeyondBoundaryException;

import java.util.Objects;

/**
 * 位置
 */
public class Position {

    private int x;

    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * 根据方向获取下一个头部位置
     * @param dir 方向
     * @param cols 地图行
     * @param rows 地图列
     * @return 下一个头部未知
     */
    public Position nextHead(Dir dir, int cols, int rows) throws BeyondBoundaryException {
        return next(dir, -1, cols, rows);
    }

    /**
     * 根据方向获取下一个尾部位置
     * @param dir 方向
     * @return 下一个尾部位置
     */
    public Position nextTail(Dir dir, int cols, int rows) throws BeyondBoundaryException {
        return next(dir, 1, cols, rows);
    }

    /**
     *
     * @param dir 方向
     * @param reverse 反转（坐标轴是x，y都是从0开始，如果y当前位置是10，那么往前走的下一个坐标就9的位置，需要-1）
     * @param cols 列
     * @param rows 行
     * @return
     * @throws BeyondBoundaryException
     */
    private Position next(Dir dir, int reverse, int cols, int rows) throws BeyondBoundaryException {
        Position position;
        switch (dir) {
            case UP:
                position = new Position(x, y + reverse);
                break;
            case DOWN:
                position = new Position(x, y - reverse);
                break;
            case LEFT:
                position = new Position(x + reverse, y);
                break;
            case RIGHT:
                position = new Position(x - reverse, y);
                break;
            default:
                throw new RuntimeException(dir + "方向无法识别");
        }
        //如果x坐标大于场景的x最大坐标或者小于零(x坐标所对应的点应为列的坐标，所以要对应cols)；y坐标大于场景的y坐标或者小于零,说明越界，需抛异常
        if(position.getX() >= cols || position.getX() < 0 ||
            position.getY() >= rows || position.getY() < 0) {
            throw new BeyondBoundaryException();
        }
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
