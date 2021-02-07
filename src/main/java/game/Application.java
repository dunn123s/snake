package game;

import java.awt.*;

public class Application {
    public static void main(String[] args) {
        Game.init(new Dimension(15, 15), 50, 50, 3);
        Game.getInstance().showUI();
        Game.getInstance().start();
    }
}
