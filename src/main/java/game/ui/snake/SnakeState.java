package game.ui.snake;

public enum  SnakeState {
    NORMAL, DIE, FULL;

    public boolean done() {
        return this != NORMAL;
    }
}
