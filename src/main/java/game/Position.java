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
     * @return 下一个头部未知
     */
    public Position nextHead(Dir dir, int cols, int rows) throws BeyondBoundaryException {
        return next(dir, -1, cols, rows);
    }

    /**
     * 根据方向获取下一个尾部位置
     * @param dir 方向
     * @return 下一个尾部未知
     */
    public Position nextTail(Dir dir, int cols, int rows) throws BeyondBoundaryException {
        return next(dir, 1, cols, rows);
    }

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
